package test;

import download.StockData;

public class YahooDownloadChart {
	
	private static final String SYMBOL = "VIPS";
	private static final String START_DATE = "01/01/2012";
	private static final String END_DATE = "11/07/2014";
	
	public static void main(String args[]) throws Exception {
		StockData stockData = new StockData(START_DATE, END_DATE);
		stockData.downloadStock(SYMBOL);
	}
}
