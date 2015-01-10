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
	
	private static HashMap<String, ArrayList<Date>> map;
	
	public static HashMap<String, ArrayList<Date>> getMap() { 
		if (map == null) setMap();
		return map;
	}
	
	
	private static void setMap() {
		try {
			map = readEarningsDatesCSV(StockConst.EARNINGS_DATES_FILENAME);
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
	public static HashMap<String, ArrayList<Date>> readEarningsDatesCSV(String filename) throws Exception {
		HashMap<String, ArrayList<Date>> earningsDatesMap = new HashMap<String, ArrayList<Date>>();
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line;
		while ((line = br.readLine()) != null) {
			String data[] = line.split(",");
			String symbol = data[0];
			ArrayList<Date> dates = new ArrayList<Date>();
			for (int i = 1; i < data.length; i++) {
				Date date = StockUtil.parseDate(data[i]);
				if (date != null) {
					dates.add(date);
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
		HashMap<String, ArrayList<Date>> earningsDatesMapZach = readEarningsDatesCSV(StockConst.EARNINGS_DATES_ZACH_FILENAME);
		HashMap<String, ArrayList<Date>> earningsDatesMapStreetInsider = readEarningsDatesCSV(StockConst.EARNINGS_DATES_STREET_INSIDER_FILENAME);
		ArrayList<String> symbolsStreetInsider = new ArrayList<String>(earningsDatesMapStreetInsider.keySet());
		Collections.sort(symbolsStreetInsider);
		for (String symbolStreetInsider : symbolsStreetInsider) {
			ArrayList<Date> datesZach = earningsDatesMapZach.get(symbolStreetInsider);
			ArrayList<Date> datesStreetInsider = earningsDatesMapStreetInsider.get(symbolStreetInsider);
			if (datesZach == null) {
				sfw.writeLine(symbolStreetInsider + " has no data for Zach.");
				continue;
			}
			for (Date dateStreetInsider : datesStreetInsider) {
				if (!datesZach.contains(dateStreetInsider) && (datesZach.get(0).before(dateStreetInsider))) {
					Date closeDateZach = getCloseDate(dateStreetInsider, datesZach, 2);
					if (closeDateZach != null) {
						sfw.writeLine(symbolStreetInsider + "'s earning date " + StockUtil.formatDate(dateStreetInsider)
								+ " is close to Zach's " + StockUtil.formatDate(closeDateZach) + ".");
					}
					else {
						sfw.writeLine(symbolStreetInsider + "'s earning date " + StockUtil.formatDate(dateStreetInsider)
								+ " is not in Zach and the date is after Zach's first earning date " + StockUtil.formatDate(datesZach.get(0)) + ".");
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
	private static Date getCloseDate(Date inputDate, ArrayList<Date> dates, int difference) {
		for (Date date : dates) {
			if (StockUtil.isCloseDates(inputDate, date, difference)) return date;
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
		ArrayList<Date> dates = map.get(symbol);
		for (int index = 0; index < dates.size(); index ++){
			long diff = dates.get(index).getTime() - date.getTime();
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
	public static void parseTheStreet() {
		//TODO:
		//1. Read each HTMl file.
		//2. The earnings date is hidden in the comments of the HTML file. Use regular expression to retrive those data.
	}
	
}

