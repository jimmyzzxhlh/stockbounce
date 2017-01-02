package test;

import java.io.File;
import java.util.List;

import stock.EarningsDate;
import stock.EarningsDatesMap;
import stock.StockConst;

public class EarningsDatesTest {

	public static void main(String[] args) throws Exception {
//		testOne();
//		renameEarningsDatesHTML();
//		testParseStreetInsiderHTML();
//		testParseZach();
//		testCompareZachStreetInsider();
//		testParseTheStreet();
		testGetMap();
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


	
	public static void testParseTheStreet() throws Exception {
		EarningsDatesMap.parseTheStreet();
	}
	
	public static void testGetMap() throws Exception{
		List<EarningsDate> dates = EarningsDatesMap.getEarningsDates("AAPL");
		for (int index = 0; index < dates.size(); index ++){
			System.out.println(dates.get(index).getDate());
			System.out.println(dates.get(index).getEstimate());
			System.out.println(dates.get(index).getType());
			System.out.println(dates.get(index).getReported());
			System.out.println(dates.get(index).getSurprise());
		}
	}
}
