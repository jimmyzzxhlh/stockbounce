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
public class StockEarningsDatesMap {
	
	private StockEarningsDatesMap() {
		
	}
	
	private static HashMap<String, ArrayList<StockEarningDate>> map;
	
	public static HashMap<String, ArrayList<StockEarningDate>> getMap() { 
		if (map == null) setMap();
		return map;
	}
	
	
	private static void setMap() {
		try {
			map = readEarningsDatesCSV(StockConst.EARNINGS_DATES_THE_STREET_FILENAME);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	
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
		StockFileWriter sfw = new StockFileWriter(StockConst.EARNINGS_DATES_STREET_INSIDER_FILENAME);
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
	 * It turns out that Zach does not have accurate earnings date either.
	 * @throws Exception
	 */
	public static void parseZach() throws Exception {
		File directory = new File(StockConst.EARNINGS_DATES_DIRECTORY_PATH_ZACH);
		HashMap<String, ArrayList<Date>> earningsDatesMap = new HashMap<String, ArrayList<Date>>();
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
				if (earningsDatesMap.containsKey(symbol)) {
					dates = earningsDatesMap.get(symbol);
					dates.add(date);
				}
				else {
					dates = new ArrayList<Date>();
					dates.add(date);
					earningsDatesMap.put(symbol, dates);
				}				
			}
			br.close();
		}
		ArrayList<String> symbols = new ArrayList<String>(earningsDatesMap.keySet());
		Collections.sort(symbols);
		StockFileWriter sfw = new StockFileWriter(StockConst.EARNINGS_DATES_ZACH_FILENAME);
		for (String symbol : symbols) {
			sfw.write(symbol + ",");
			for (Date date : earningsDatesMap.get(symbol)) {
				sfw.write(StockUtil.formatDate(date) + ",");
			}
			sfw.newLine();
		}
		sfw.close();
	}
	
	/**
	 * Read earnings date CSV from a file.
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public static HashMap<String, ArrayList<StockEarningDate>> readEarningsDatesCSV(String filename) throws Exception {
		HashMap<String, ArrayList<StockEarningDate>> earningsDatesMap = new HashMap<String, ArrayList<StockEarningDate>>();
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line;
		while ((line = br.readLine()) != null) {
			String data[] = line.split(",");
			String symbol = data[0];
			ArrayList<StockEarningDate> dates = new ArrayList<StockEarningDate>();	
			for (int i = 1; i < data.length; i++) {
				Date date = null;
				double estimate = -1;
				String type = null;
				double reported = -1;
				String properties[] = data[i].split(";");
				for (int j = 0; j < properties.length; j++){
					switch(j){
						case 0: date = StockUtil.parseDate(properties[0]);
						case 1: estimate = Double.parseDouble(properties[1]);
						case 2: type = properties[2];
						case 3: reported = Double.parseDouble(properties[3]);
					}
				}
				if (date != null) {
					if (type!=null){
						dates.add(new StockEarningDate(symbol, date, estimate, type, reported));
					}
					else{
						dates.add(new StockEarningDate(symbol, date));
					}
				}
			}
			earningsDatesMap.put(symbol, dates);
		}
		br.close();
		return earningsDatesMap;
	}
	
	/**
	 * Compare the earnings date for Zach VS street insider.
	 * It looks like street insider has much more accurate data, but it could possibly miss some earnings date.
	 * So we are not going to use any of them.
	 * @throws Exception
	 */
	public static void compareZachStreetInsider() throws Exception {
		StockFileWriter sfw = new StockFileWriter("D:\\zzx\\Stock\\CompareZachStreetInsider.txt");
		HashMap<String, ArrayList<StockEarningDate>> earningsDatesMapZach = readEarningsDatesCSV(StockConst.EARNINGS_DATES_ZACH_FILENAME);
		HashMap<String, ArrayList<StockEarningDate>> earningsDatesMapStreetInsider = readEarningsDatesCSV(StockConst.EARNINGS_DATES_STREET_INSIDER_FILENAME);
		ArrayList<String> symbolsStreetInsider = new ArrayList<String>(earningsDatesMapStreetInsider.keySet());
		Collections.sort(symbolsStreetInsider);
		for (String symbolStreetInsider : symbolsStreetInsider) {
			ArrayList<StockEarningDate> datesZach = earningsDatesMapZach.get(symbolStreetInsider);
			ArrayList<StockEarningDate> datesStreetInsider = earningsDatesMapStreetInsider.get(symbolStreetInsider);
			if (datesZach == null) {
				sfw.writeLine(symbolStreetInsider + " has no data for Zach.");
				continue;
			}
			for (StockEarningDate dateStreetInsider : datesStreetInsider) {
				if (!datesZach.contains(dateStreetInsider) && (datesZach.get(0).getDate().before(dateStreetInsider.getDate()))) {
					Date closeDateZach = getCloseDate(dateStreetInsider, datesZach, 2);
					if (closeDateZach != null) {
						sfw.writeLine(symbolStreetInsider + "'s earning date " + StockUtil.formatDate(dateStreetInsider.getDate())
								+ " is close to Zach's " + StockUtil.formatDate(closeDateZach) + ".");
					}
					else {
						sfw.writeLine(symbolStreetInsider + "'s earning date " + StockUtil.formatDate(dateStreetInsider.getDate())
								+ " is not in Zach and the date is after Zach's first earning date " + StockUtil.formatDate(datesZach.get(0).getDate()) + ".");
					}
				}
			}
		}
		sfw.close();
	}
	
	/**
	 * Find a date in a date list that is close enough to the input date.
	 * @param inputDate
	 * @param dates
	 * @param difference
	 * @return
	 */
	private static Date getCloseDate(StockEarningDate inputDate, ArrayList<StockEarningDate> dates, int difference) {
		for (StockEarningDate date : dates) {
			if (StockUtil.isCloseDates(inputDate.getDate(), date.getDate(), difference)) return date.getDate();
		}
		return null;
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
		ArrayList<StockEarningDate> dates = map.get(symbol);
		for (int index = 0; index < dates.size(); index ++){
			long diff = dates.get(index).getDate().getTime() - date.getTime();
			if ((diff > 0) && (diff / (24 * 60 * 60 * 1000) < StockConst.CLOSE_TO_EARNING_DAYS)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Parse the earnings date data from the street.
	 * 
	 */
	public static void parseTheStreet() throws Exception {
		//TODO:
		//1. Read each HTMl file.
		//2. The earnings date is hidden in the comments of the HTML file. Use regular expression to retrieve those data.
		//Find a string with the following format (e.g. 1/2/14)
		//1. One or two digits first. (Month)
		//2. Follow by a slash.
		//3. Follow by one or two digits. (Day)
		//4. Follow by a slash.
		//5. Follow by two digits. (Year)
		//6. Follow by a non-digit.
		String regEx = "[\\[]{1}[0]{1}[\\]]{1}\\s[=]{1}[>]{1}\\s[\\d]{6}";
		Pattern pattern = Pattern.compile(regEx);
		SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd");
		
		ArrayList<String> symbolList = StockAPI.getSymbolList();
		StockFileWriter sfw = new StockFileWriter(StockConst.EARNINGS_DATES_THE_STREET_FILENAME);
		for (String symbol : symbolList) {
			sfw.write(symbol + ",");
			String filename = StockConst.EARNINGS_DATES_DIRECTORY_PATH_THE_STREEET + symbol + ".html";
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			//ArrayList<Date> dates = new ArrayList<Date>();
			ArrayList<StockEarningDate> dates = new ArrayList<StockEarningDate>();
			String line;
			while ((line = br.readLine()) != null) {
				Matcher matcher = pattern.matcher(line);
				if (!matcher.find()) continue;
				String dateString = matcher.group();
				dateString = dateString.substring(7);
				dateString = dateString.substring(0, 2) + "/" + dateString.substring(2, 4) + "/" + dateString.substring(4);
				Date date = format.parse(dateString);
				//[1] => <Stock name>
				if ((line = br.readLine()) == null) {
					dates.add(new StockEarningDate(symbol, date));
					break;
				}
				//[2] => <Stock symbol>
				if ((line = br.readLine()) == null) {
					dates.add(new StockEarningDate(symbol, date));
					break;
				}
				//[3] => <Stock earning month>
				if ((line = br.readLine()) == null) {
					dates.add(new StockEarningDate(symbol, date));
					break;
				}
				//[4] => <estimate>
				if ((line = br.readLine()) == null) {
					dates.add(new StockEarningDate(symbol, date));
					break;
				}
				String estimate = line.replace(" ", "").substring(5);
				//[5] => <AMC, NONE, BTO>
				if ((line = br.readLine()) == null) {
					dates.add(new StockEarningDate(symbol, date,Double.parseDouble(estimate),"NONE",999));
					break;
				}
				String type = line.replace(" ", "").substring(5);
				//[6] => <reported>
				if ((line = br.readLine()) == null) {
					dates.add(new StockEarningDate(symbol, date,Double.parseDouble(estimate),type,999));
					break;
				}
				String reported = line.replace(" ", "").substring(5);
				dates.add(new StockEarningDate(symbol, date,Double.parseDouble(estimate),type,Double.parseDouble(reported)));
			}
			Collections.sort(dates);
			for (int index = 0; index < dates.size(); index ++){
				StockEarningDate stockEarningDate = dates.get(index);
				sfw.write(StockUtil.formatDate(stockEarningDate.getDate()) + ";" + stockEarningDate.getEstimate() + ";" + stockEarningDate.getType() + ";" + stockEarningDate.getReported() + ",");
			}
			sfw.newLine();
			br.close();
		}
		sfw.close();
	}
	
}

