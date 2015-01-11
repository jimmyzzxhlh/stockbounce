package test;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import stock.StockConst;
import stock.StockEarningDate;
import stock.StockEarningsDatesMap;

public class StockEarningsDatesTest {

	public static void main(String[] args) throws Exception {
//		testOne();
//		renameEarningsDatesHTML();
//		testParseStreetInsiderHTML();
//		testParseZach();
//		testCompareZachStreetInsider();
		testParseTheStreet();
		testGetMap();
	}

	
	public static void testOne() {
		String string= "10/15/14";
		DateFormat format = new SimpleDateFormat("MM/dd/yy", Locale.ENGLISH);
		Date date = new Date();
		try {
			date = format.parse(string);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(StockEarningsDatesMap.isCloseToEarningsDate("MSFT", date));
	}
	
	public static void renameEarningsDatesHTML() { 
		File directory = new File(StockConst.EARNINGS_DATES_DIRECTORY_PATH_STREET_INSIDER);
		for (File file : directory.listFiles()) {
			String data[] = file.getName().split("_");
			String symbol = data[0];
			String newFilename = StockConst.EARNINGS_DATES_DIRECTORY_PATH_STREET_INSIDER + symbol + ".html";
			file.renameTo(new File(newFilename));
		}		
	}
	
	public static void testParseStreetInsiderHTML() throws Exception {
		StockEarningsDatesMap.parseStreetInsider();
	}
	
	public static void testParseZach() throws Exception {
		StockEarningsDatesMap.parseZach();
	}
	
	public static void testCompareZachStreetInsider() throws Exception {
		StockEarningsDatesMap.compareZachStreetInsider();
	}
	
	public static void testParseTheStreet() throws Exception {
		StockEarningsDatesMap.parseTheStreet();
	}
	
	public static void testGetMap() throws Exception{
		HashMap<String, ArrayList<StockEarningDate>> map = StockEarningsDatesMap.getMap();
		ArrayList<StockEarningDate> dates = map.get("AAPL");
		for (int index = 0; index < dates.size(); index ++){
			System.out.println(dates.get(index).getDate());
			System.out.println(dates.get(index).getEstimate());
			System.out.println(dates.get(index).getType());
			System.out.println(dates.get(index).getReported());
			System.out.println(dates.get(index).getSurprise());
		}
	}
}
