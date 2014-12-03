package test;

import java.io.BufferedReader;
import java.io.FileReader;

import stock.StockConst;
import analysis.IntraDayAnalysis;
import download.StockDownload;

public class YahooDownloadStocksTest {
	
	private static final String SYMBOL = "ZNGA";
	private static final String START_DATE = "01/01/2012";
	private static final String END_DATE = "11/28/2014";
	
	public static void main(String args[]) throws Exception {
//		downloadSingleStock();
//		downloadStocks();
//		downloadOutstandingSharesCSV();
		downloadPreviousCloseCSV();
//		downloadIntraDayStocks();
//		testAnalysisIntraDayStocks();
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
	
	private static void downloadIntraDayStocks() throws Exception {
		StockDownload.downloadIntraDayStocks();
	}
	
	private static void testReadIntraDayStock() throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(StockConst.STOCK_INTRADAY_DIRECTORY_PATH + "GGACU.txt"));
		String line;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
		br.close();
	}
	
	private static void testAnalysisIntraDayStocks() throws Exception {
		IntraDayAnalysis.analyzeVolume();
		
	}
	

	
	
}
