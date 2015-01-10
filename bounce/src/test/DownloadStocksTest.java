package test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import org.joda.time.LocalDate;

import util.StockUtil;
import download.StockDownload;

public class DownloadStocksTest {
	
	private static final String SYMBOL = "CAMT";
	private static final String START_DATE = "20050101";
	private static final String END_DATE = "20150108";
	
	public static void main(String args[]) throws Exception {
		downloadSingleStock();
//		downloadStocks();
//		downloadOutstandingSharesCSV();
//		downloadPreviousCloseCSV();
//		downloadIntraDayStocksFromGoogle();
//		downloadIntraDayStocksFromYahoo();
//		downloadCompanyLists();
//		downloadEarningsDatesFromZach();
//		downloadDailyTask();
//		downloadHTMLURL();
//		downloadEarningsDatesFromStreetInsider();
//		downloadHTMLURLWithPostTest();
//		downloadEarningsDatesFromTheStreet();
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
				Date date = StockUtil.parseDate(dateString);
				StockDownload.downloadIntraDayStocksFromYahoo(date);
			}
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private static void downloadDailyTask() { 
		System.out.println("1. Download Intra Day Data from Yahoo...");
		downloadIntraDayStocksFromYahoo();
		System.out.println("2. Download Earnings Date from Zach.");
		downloadEarningsDateForToday();
		StockUtil.pressAnyKeyToContinue();
	}
	
	private static void downloadHTMLURL() {
		StockUtil.downloadHTMLURL("http://www.streetinsider.com/ec_earnings.php?q=VPFG", "D:\\zzx\\Stock\\EarningsDatesStreetInsider\\VPFG.html");
	}
	
	private static void downloadEarningsDatesFromZach() throws Exception {
		StockDownload stockDownload = new StockDownload(START_DATE, END_DATE);
		stockDownload.downloadEarningsDatesFromZach();
		StockUtil.pressAnyKeyToContinue();
	}
	
	private static void downloadEarningsDateForToday() {
		Date today = new Date();
		StockDownload stockDownload = new StockDownload(today, today);
		stockDownload.downloadEarningsDatesFromZach();
	}
	
	private static void downloadEarningsDatesFromStreetInsider() {
		StockDownload.downloadEarningsDatesFromStreetInsider();
	}
	
	private static void downloadHTMLURLWithPostTest() {
		String urlString = "http://zacks.thestreet.com/CompanyView.php";
		String filename = "D:\\zzx\\Stock\\testPost.html";
		String urlParameters = "ticker=MITT^A";
		StockUtil.downloadHTMLURLWithPost(urlString, filename, urlParameters);
	}
	
	private static void downloadEarningsDatesFromTheStreet() {
		StockDownload.downloadEarningsDatesFromTheStreet();
	}
}
