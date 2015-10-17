package test;

import intraday.IntraDayReaderYahoo;

public class IntraDayPriceVolumeTest {

	public static void main(String[] args) throws Exception {
//		testPrintDailyVolume();
		testPrintDailyPrice();
		
	}
	

	private static void testPrintDailyVolume() throws Exception {
		IntraDayReaderYahoo.printDailyVolumeForSingleStock();
	}
	
	private static void testPrintDailyPrice() throws Exception {
		IntraDayReaderYahoo.printDailyPriceForSingleStock();
	}
}
