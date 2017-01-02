package stock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import stock.StockEnum.EarningsTimeType;
import util.StockFileWriter;
import util.StockUtil;

/**
 * Read the text file which contains the earnings date.
 * @author Dongyue Xue
 *
 */
public class EarningsDatesMap {
	
	/**
	 * Singleton
	 */
	private EarningsDatesMap() {}
	
	private static HashMap<String, List<EarningsDate>> map;
	
	public static List<EarningsDate> getEarningsDates(String symbol) {
		if (map == null) setMap();
		return map.getOrDefault(symbol, null);
	}
	
//	/**
//	 * Get next earnings date object. The earnings date should be on or after the current date.
//	 * @param symbol
//	 * @param date
//	 * @return
//	 */
//	public StockEarningsDate getNextEarningsDate(String symbol, Date date) {
//		if (!map.containsKey(symbol)) return null;
//		ArrayList<StockEarningsDate> earningsDates = map.get(symbol);
//		for (StockEarningsDate stockEarningsDate : earningsDates) {
//			//Find the next earnings date on or after the current date.
//			if (!stockEarningsDate.getDate().before(date)) {
//				return stockEarningsDate;			
//			}
//		}
//		return null;
//	}
	
	private static void setMap() {
		try {
			readEarningsDatesCSV(StockConst.EARNINGS_DATES_THE_STREET_FILENAME);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Read earnings date CSV from a file.
	 * Each earnings date date array list within a symbol is sorted.
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	private static void readEarningsDatesCSV(String filename) throws Exception {
		map = new HashMap<String, List<EarningsDate>>();
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line;
		while ((line = br.readLine()) != null) {
			String data[] = line.split(",");
			String symbol = data[0];
			System.out.println(symbol);
			List<EarningsDate> dates = new ArrayList<EarningsDate>();	
			for (int i = 1; i < data.length; i++) {
				Date date = null;
				double estimate = -1;
				EarningsTimeType type = null;
				double reported = -1;
				String properties[] = data[i].split(";");
				for (int j = 0; j < properties.length; j++){
					switch(j){
						case 0: date = StockUtil.parseDate(properties[0]);
						case 1: estimate = Double.parseDouble(properties[1]);
						case 2: type = StockUtil.getEnumFromString(EarningsTimeType.class, properties[2]);
						case 3: reported = Double.parseDouble(properties[3]);
					}
				}
				if (date != null) {
					if (type != null){
						dates.add(new EarningsDate(symbol, date, estimate, type, reported));
					}
					else{
						dates.add(new EarningsDate(symbol, date));
					}
				}
			}
			Collections.sort(dates);
			map.put(symbol, dates);
		}
		br.close();		
	}
	

	/**
	 * Parse the earnings date data from the street.
	 * 
	 */
	public static void parseTheStreet() throws Exception {
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
		
		List<String> symbolList = StockAPI.getUSSymbolList();
		StockFileWriter sfw = new StockFileWriter(StockConst.EARNINGS_DATES_THE_STREET_FILENAME);
		for (String symbol : symbolList) {
			sfw.write(symbol + ",");
			String filename = StockConst.EARNINGS_DATES_DIRECTORY_PATH_THE_STREEET + symbol + ".html";
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			//ArrayList<Date> dates = new ArrayList<Date>();
			List<EarningsDate> dates = new ArrayList<EarningsDate>();
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
					dates.add(new EarningsDate(symbol, date));
					break;
				}
				//[2] => <Stock symbol>
				if ((line = br.readLine()) == null) {
					dates.add(new EarningsDate(symbol, date));
					break;
				}
				//[3] => <Stock earning month>
				if ((line = br.readLine()) == null) {
					dates.add(new EarningsDate(symbol, date));
					break;
				}
				//[4] => <estimate>
				if ((line = br.readLine()) == null) {
					dates.add(new EarningsDate(symbol, date));
					break;
				}
				double estimate = Double.parseDouble(line.replace(" ", "").substring(5));
				//[5] => <AMC, NONE, BTO>
				if ((line = br.readLine()) == null) {
					dates.add(new EarningsDate(symbol, date, estimate, EarningsTimeType.NONE, 999));
					break;
				}
				EarningsTimeType type = StockUtil.getEnumFromString(EarningsTimeType.class, line.replace(" ", "").substring(5));
				//[6] => <reported>
				if ((line = br.readLine()) == null) {
					dates.add(new EarningsDate(symbol, date, estimate, type,999));
					break;
				}
				double reported = Double.parseDouble(line.replace(" ", "").substring(5));
				dates.add(new EarningsDate(symbol, date, estimate, type, reported));
			}
			Collections.sort(dates);
			for (int index = 0; index < dates.size(); index ++){
				EarningsDate stockEarningsDate = dates.get(index);
				sfw.write(StockUtil.formatDate(stockEarningsDate.getDate()) + ";" + stockEarningsDate.getEstimate() + ";" + stockEarningsDate.getType() + ";" + stockEarningsDate.getReported() + ",");
			}
			sfw.newLine();
			br.close();
		}
		sfw.close();
	}



	
}

