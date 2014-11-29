package test;

import download.StockDownload;

public class YahooDownloadStocks {
	
	private static final String SYMBOL = "VIPS";
	private static final String START_DATE = "01/01/2012";
	private static final String END_DATE = "11/07/2014";
	
	public static void main(String args[]) throws Exception {
//		downloadStocks();
		downloadOutstandingSharesCSV();
	}
	
	private static void downloadSingleStock() throws Exception {
		StockDownload stockDownload = new StockDownload(START_DATE, END_DATE);
		stockDownload.downloadStock(SYMBOL);
	}
	
	private static void downloadStocks() throws Exception {
		StockDownload stockDownload = new StockDownload();
		stockDownload.downloadStocks();
	}
	
	private static void downloadOutstandingSharesCSV() {
		StockDownload.downloadOutstandingSharesCSV();
	}
}
