package test;

import intraday.IntraDayAnalysisYahoo;

public class IntraDayPriceVolumeTest {

	public static void main(String[] args) throws Exception {
//		testPrintDailyVolume();
		testPrintDailyPrice();
		
	}
	

	private static void testPrintDailyVolume() throws Exception {
		IntraDayAnalysisYahoo.printDailyVolumeForSingleStock();
	}
	
	private static void testPrintDailyPrice() throws Exception {
		IntraDayAnalysisYahoo.printDailyPriceForSingleStock();
	}
}
