package test;

import download.StockDownload;

public class DownloadStocksTest {
	
	private static final String SYMBOL = "ZNGA";
	private static final String START_DATE = "01/01/2012";
	private static final String END_DATE = "11/28/2014";
	
	public static void main(String args[]) throws Exception {
//		downloadSingleStock();
//		downloadStocks();
//		downloadOutstandingSharesCSV();
//		downloadPreviousCloseCSV();
//		downloadIntraDayStocksFromGoogle();
		downloadIntraDayStocksFromYahoo();
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
	
	private static void downloadPreviousCloseCSV() {
		StockDownload.downloadPreviousCloseCSV();
	}
	
	private static void downloadIntraDayStocksFromGoogle() throws Exception {
		StockDownload.downloadIntraDayStocksFromGoogle();
	}

	private static void downloadIntraDayStockFromYahoo() throws Exception {
		StockDownload.downloadIntraDayStockFromYahoo("GOOG");
	}
	
	private static void downloadIntraDayStocksFromYahoo() throws Exception {
		StockDownload.downloadIntraDayStocksFromYahoo();
	}
}
