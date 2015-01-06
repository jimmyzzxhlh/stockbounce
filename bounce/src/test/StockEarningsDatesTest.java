package test;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import stock.StockConst;
import stock.StockEarningsDateMap;

public class StockEarningsDatesTest {

	public static void main(String[] args) throws Exception {
//		testOne();
//		renameEarningsDatesHTML();
//		testParseStreetInsiderHTML();
		testParseZach();
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
		System.out.println(StockEarningsDateMap.isCloseToEarningsDate("MSFT", date));
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
		StockEarningsDateMap.parseStreetInsider();
	}
	
	public static void testParseZach() throws Exception {
		StockEarningsDateMap.parseZach();
	}
}
