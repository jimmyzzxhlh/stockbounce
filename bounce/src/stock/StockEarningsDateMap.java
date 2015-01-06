package stock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.StockUtil;

/**
 * Read the text file which contains the earnings date.
 * @author Dongyue Xue
 *
 */
public class StockEarningsDateMap {
	
	private StockEarningsDateMap() {
		
	}
	
	private static HashMap<String, ArrayList<Date>> map;
	
	public static HashMap<String, ArrayList<Date>> getMap() { 
//		if (map == null) setMap();
		return map;
	}
	
	
//	private static void setMap() {
//		String filename = ;
//		try{
//			File file = new File(filename);
//			if (!file.exists()){
//				setup();
//				return;
//			}
//			map = new HashMap<String, ArrayList<Date>>();
//			BufferedReader br = new BufferedReader(new FileReader(file));		
//			String line;
//			while ((line = br.readLine()) != null) {
//				String[] elements = line.split(",");
//				String symbol = elements[0];
//				ArrayList<Date> dates = new ArrayList<Date>();
//				for (int index = 1; index < elements.length; index ++){
//					DateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
//					Date date = format.parse(elements[index]);
//					dates.add(date);
//				}
//				map.put(symbol, dates);
//			}
//			br.close();
//		}
//		catch(Exception ex){
//			ex.getMessage();
//		}
//
//	}
	
	/**
	 * Parse earnings data from street insider.
	 * Notice that street insider has older data but its data might not be exactly correct.
	 * Use Zach's data if available. If not then use street insider's.
	 * @throws Exception
	 */
	public static void parseStreetInsider() throws Exception {
		//Find a string with the following format (e.g. 1/2/14)
		//1. One or two digits first. (Month)
		//2. Follow by a slash.
		//3. Follow by one or two digits. (Day)
		//4. Follow by a slash.
		//5. Follow by two digits. (Year)
		//6. Follow by a non-digit.
		String regEx = "[\\d]{1,2}[/]{1}[\\d]{1,2}[/]{1}[\\d]{2}[\\D]";
		Pattern pattern = Pattern.compile(regEx);
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy");
		
		ArrayList<String> symbolList = StockAPI.getSymbolList();
		StockFileWriter sfw = new StockFileWriter(StockConst.EARNINGS_DATES_STREET_INSIDER);
		for (String symbol : symbolList) {
			sfw.write(symbol + ",");
			String filename = StockConst.EARNINGS_DATES_DIRECTORY_PATH_STREET_INSIDER + symbol + ".html";
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			ArrayList<Date> dates = new ArrayList<Date>();
			String line;
			while ((line = br.readLine()) != null) {
				Matcher matcher = pattern.matcher(line);
				if (!matcher.find()) continue;
				//Cut the last character as it should be a non-digit.
				String dateString = matcher.group();
				dateString = dateString.substring(0, dateString.length() - 1);
				Date date = format.parse(dateString);
				//If the date is already in the list then do not add it again. (The last earnings date can appear twice)
				if (dates.contains(date)) continue;
				//Find the word "Details" in the last grid.
				boolean detailsFound = false;
				while (!line.contains("</tr>")) {
					line = br.readLine();
					if (line == null) break;  //Should probably not happen.
					if (line.contains("Details")) {
						detailsFound = true;
						break;
					}
				}
				if (detailsFound) {
					dates.add(date);
				}
			}
			for (Date date : dates) {
				sfw.write(StockUtil.formatDate(date) + ",");
			}
			sfw.newLine();
			br.close();
		}
		
	}
	
	/**
	 * Parse earnings data from Zach.
	 * @throws Exception
	 */
	public static void parseZach() throws Exception {
		File directory = new File(StockConst.EARNINGS_DATES_DIRECTORY_PATH_ZACH);
		HashMap<String, ArrayList<Date>> earningsDateMap = new HashMap<String, ArrayList<Date>>();
		for (File file : directory.listFiles()) {
			//Get the date from the file name.
			Date date = StockUtil.parseDate(StockUtil.getFilenameWithoutExtension(file.getName()));
			BufferedReader br = new BufferedReader(new FileReader(file));
			//Skip the first line which contains the header.
			String line = br.readLine(); 
			while ((line = br.readLine()) != null) {
				String data[] = line.split("\t");
//				System.out.println(Arrays.toString(data));
				String symbol = data[0];
				ArrayList<Date> dates;
				if (earningsDateMap.containsKey(symbol)) {
					dates = earningsDateMap.get(symbol);
					dates.add(date);
				}
				else {
					dates = new ArrayList<Date>();
					dates.add(date);
					earningsDateMap.put(symbol, dates);
				}				
			}
			br.close();
		}
		ArrayList<String> symbols = new ArrayList<String>(earningsDateMap.keySet());
		Collections.sort(symbols);
		StockFileWriter sfw = new StockFileWriter(StockConst.EARNINGS_DATES_ZACH);
		for (String symbol : symbols) {
			sfw.write(symbol + ",");
			for (Date date : earningsDateMap.get(symbol)) {
				sfw.write(StockUtil.formatDate(date) + ",");
			}
			sfw.newLine();
		}
		sfw.close();
	}
	
	
	/**
	 * Check whether there is an earning date that is right after the current date. 
	 * @param symbol
	 * @param date
	 * @return
	 */
	public static boolean isCloseToEarningsDate(String symbol, Date date) {
//		if (map == null) setMap();
		if (!map.containsKey(symbol)){
			return false;
		}
		ArrayList<Date> dates = map.get(symbol);
		for (int index = 0; index < dates.size(); index ++){
			long diff = dates.get(index).getTime() - date.getTime();
			if ((diff > 0) && (diff / (24 * 60 * 60 * 1000) < StockConst.CLOSE_TO_EARNING_DAYS)){
				return true;
			}
		}
		return false;
	}
	
	
}
