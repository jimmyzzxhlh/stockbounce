package test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import org.joda.time.LocalDate;

import util.StockUtil;
import download.StockDownload;

public class DownloadStocksTest {
	
	private static final String SYMBOL = "BITA";
	private static final String START_DATE = "01/01/2005";
	private static final String END_DATE = "12/31/2014";
	
	public static void main(String args[]) throws Exception {
//		downloadSingleStock();
//		downloadShtocks();
//		downloadOutstandingSharesCSV();
//		downloadPreviousCloseCSV();
//		downloadIntraDayStocksFromGoogle();
//		downloadIntraDayStocksFromYahoo();
//		downloadCompanyLists();
		downloadURL();
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
	
	private static void downloadIntraDayStocksFromYahoo() {
		try {
			System.out.println("Enter date for the file name (e.g. 20141226), nothing then use today.");
			Scanner reader = new Scanner(System.in);
			String dateString = reader.nextLine().trim();
			if (dateString.length() <= 0) {
				StockDownload.downloadIntraDayStocksFromYahoo();
			}
			else {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
				Date date = formatter.parse(dateString);
				StockDownload.downloadIntraDayStocksFromYahoo(date);
			}
			reader.close();

			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		try {
			System.out.println("Press any key to continue...");
			System.in.read();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
			
		
	}
	
	private static void downloadCompanyLists() throws Exception {
		StockDownload.downloadCompanyLists();
	}
	
	private static void downloadURL() {
		StockUtil.downloadURL("http://biz.yahoo.com/research/earncal/20141215.html", "D:\\zzx\\Stock\\tempDownload.txt");
	}
	
	private static void downloadEarningsDate() throws Exception {

	}
}
