package test;

import download.StockData;

public class YahooDownloadChart {
	
	private static final String SYMBOL = "JD";
	private static final String START_DATE = "05/22/2014";
	private static final String END_DATE = "09/27/2014";
	
	public static void main(String args[]) throws Exception {
		StockData stockData = new StockData(START_DATE, END_DATE);
		stockData.downloadStock(SYMBOL);
	}
}
