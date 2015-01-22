package download;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import stock.StockAPI;
import stock.StockConst;
import stock.StockEnum.Exchange;
import stock.StockExchange;
import util.StockUtil;

/**
 * Class for downloading stock data from source such as Yahoo / Google.
 * @author jimmyzzxhlh-Dell
 *
 */
public class StockDownload {

	private static final Date DEFAULT_START_DATE = StockUtil.parseDate("20060101");
	private static final String TEMPORARY_MARKET_CAP_FILENAME = "D:\\zzx\\Stock\\MarketCap_Temp.csv";
	private static final String TEMPORARY_PREVIOUS_CLOSE_FILENAME = "D:\\zzx\\Stock\\PreviousClose_Temp.csv"; 
	private final static int MAX_RETRY = 5;
	private Date startDate; 
	private Date endDate;
	

	/**
	 * startDate and endDate are strings of the form "MM/dd/yyyy"
	 * Download csv files from startDate to endDate. Input null, null to use the default dates. 
	 * startDate is 4/1/2010 by default. endDate is today by default.
	 */
	public StockDownload(String startDateStr, String endDateStr){
		if (startDateStr == null) {
			startDate = DEFAULT_START_DATE;
		}
		else {
			startDate = StockUtil.parseDate(startDateStr);
		}

		if (endDateStr == null) {
			endDate = new Date();
		}
		else {
			endDate = StockUtil.parseDate(endDateStr);
		}
	}	
	

	public StockDownload(Date startDate, Date endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
		if (startDate == null) {
			startDate = DEFAULT_START_DATE;
		}
		if (endDate == null) {
			endDate = new Date();
		}
		
	}
	
	public StockDownload() {
		
	}
	
	/**
	 * Download all stocks from yahoo API.
	 * @throws Exception
	 */
	public void downloadStocks() throws Exception {
		StockUtil.createNewDirectory(StockConst.STOCK_CSV_DIRECTORY_PATH);
		if (this.startDate == null)
			this.startDate = DEFAULT_START_DATE;
		
		ArrayList<String> symbolList = StockAPI.getSymbolList();
		int retry = 0;
		int index = 0;
		while (index < symbolList.size()) {
			String symbol = symbolList.get(index);
			if (retry > 0) {
				System.out.println(symbol + "Retry: " + retry);
			}
			else {
				System.out.println(symbol);
			}
			try {
				downloadStock(symbol);
			}
			catch (Exception e) {
				e.printStackTrace();
				if (e instanceof IOException) {
					retry++;
					if (retry < MAX_RETRY) {
						Thread.sleep(300);
						continue;					
					}
					
				}					
			}
			index++;
			retry = 0;
			Thread.sleep(300);
			
		}
		
	}
	
	/**
	 * Download a single stock.
	 * Example: Download Yahoo stock from 1/2/2014 to 12/23/2014.
	 * Notice that month range is 0 to 11.
	 * http://ichart.finance.yahoo.com/table.csv?s=YHOO&d=11&e=23&f=2014&g=d&a=0&b=2&c=2014&ignore=.csv
	 * http://ichart.finance.yahoo.com/table.csv?s=YHOO&d=0&e=28&f=2010&g=d&a=3&b=12&c=1996&ignore=.csv
	 * s = TICKER
	 * a = fromMonth-1
     * b = fromDay (two digits)
	 * c = fromYear
     * d = toMonth-1
     * e = toDay (two digits)
     * f = toYear
     * g = d for day, m for month, y for yearly
	 * @param symbol
	 * @throws Exception
	 */
	public void downloadStock(String symbol) throws Exception {
		String fileStock = StockConst.STOCK_CSV_DIRECTORY_PATH + symbol + ".csv";
		
		File file = new File(fileStock);
		if (!file.exists()) {
			file.createNewFile();
		}
		
				
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		String startMonth = Integer.toString(cal.get(Calendar.MONTH));
        String startDay = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
        String startYear = Integer.toString(cal.get(Calendar.YEAR));
        cal.setTime(endDate);
		String endMonth = Integer.toString(cal.get(Calendar.MONTH));
        String endDay = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
        String endYear = Integer.toString(cal.get(Calendar.YEAR));
        String siteAddr = "http://ichart.finance.yahoo.com/table.csv?s="+symbol+"&d="+endMonth+"&e="+endDay+"&f="+endYear+"&g=d&a="+startMonth+"&b="+startDay+"&c="+startYear+"&ignore=.csv";
		System.out.println(siteAddr);
        URL site = new URL(siteAddr);
        ReadableByteChannel rbc = Channels.newChannel(site.openStream());
		FileOutputStream fos = new FileOutputStream(file);
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		fos.close();
   
	}
	
	public static void downloadCompanyLists() {
		downloadCompanyList(Exchange.NASDAQ);
		downloadCompanyList(Exchange.NYSE);
	}
	
	private static void downloadCompanyList(Exchange exchange) {
		String urlString = StockExchange.getDownloadCompanyListURL(exchange);
		String filename = StockExchange.getCompanyListFilename(exchange);
		StockUtil.downloadURL(urlString, filename);
		StockUtil.sleepThread(1000);
	}
	
	/**
	 * Static function for downloading outstanding shares data from Yahoo.
	 * Example:
	 * http://finance.yahoo.com/d/quotes.csv?s=AAPL+GOOG+YHOO&f=sj2
	 */
	public static void downloadOutstandingSharesCSV() {
		//Delete the file first so that the existing content is wiped out.
		File f = new File(StockConst.SHARES_OUTSTANDING_FILENAME);
		if (f.exists()) f.delete();
		StringBuilder sb = new StringBuilder();
		//Get all the symbols from company file.
		ArrayList<String> symbolList = StockAPI.getAllSymbolList();
		int count = 0;
		for (int i = 0; i < symbolList.size(); i++) {
			count++;
			if (count > 1) sb.append("+");
			String symbol = symbolList.get(i);
			sb.append(symbol);
			//Download CSV for every 50 symbols. Otherwise, if there are too many symbols then there will be 
			//URL error.
			if ((count % 50 == 0) || (i == symbolList.size() - 1)) {
				System.out.println("Downloading from " + symbolList.get(i - 49) + " to " + symbol);
				String urlString = "http://finance.yahoo.com/d/quotes.csv?s=" + sb.toString() + "&f=sj2";
				sb = new StringBuilder();
				count = 0;
				StockUtil.downloadURL(urlString, TEMPORARY_MARKET_CAP_FILENAME);
				//Merge the temporary file to the ultimate output file.
				mergeFile(TEMPORARY_MARKET_CAP_FILENAME, StockConst.SHARES_OUTSTANDING_FILENAME);
				StockUtil.sleepThread(300);
			}
		}
				
	}
	
	/**
	 * Static function for downloading previous close price, used with outstanding shares CSV to compute
	 * the market capitalization if an instance of {@StockCandleArray} is not given.
	 * Example:
	 * http://finance.yahoo.com/d/quotes.csv?s=AAPL+GOOG+YHOO&f=sp
	 */
	public static void downloadPreviousCloseCSV() {
		//Delete the file first so that the existing content is wiped out.
		File f = new File(StockConst.PREVIOUS_CLOSE_FILENAME);
		f.delete();
		StringBuilder sb = new StringBuilder();
		ArrayList<String> symbolList = StockAPI.getSymbolList();
		int count = 0;
		for (int i = 0; i < symbolList.size(); i++) {
			count++;
			if (count > 1) sb.append("+");
			String symbol = symbolList.get(i);
			sb.append(symbol);
			//Download CSV for every 50 symbols. Otherwise, if there are too many symbols then there will be 
			//URL error.
			if (count % 50 == 0) {
				System.out.println("Downloading from " + symbolList.get(i - 49) + " to " + symbol);
				String urlString = "http://finance.yahoo.com/d/quotes.csv?s=" + sb.toString() + "&f=sp";
				sb = new StringBuilder();
				count = 0;
				StockUtil.downloadURL(urlString, TEMPORARY_PREVIOUS_CLOSE_FILENAME);
				//Merge the temporary file to the ultimate output file.
				mergeFile(TEMPORARY_PREVIOUS_CLOSE_FILENAME, StockConst.PREVIOUS_CLOSE_FILENAME);
				StockUtil.sleepThread(300);
			}
		}
				
	}
	
	/**
	 * Merge two files and delete the file that is being merged.
	 * @param subFilename
	 * @param mainFilename
	 */
	public static void mergeFile(String subFilename, String mainFilename) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(subFilename));
			BufferedWriter bw = new BufferedWriter(new FileWriter(mainFilename, true));
			String line;
			while ((line = br.readLine()) != null) {
				bw.write(line);
				bw.newLine();
			}
			br.close();
			bw.close();
			File subFile = new File(subFilename);
			subFile.delete();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Download intraday data from Google API. 
	 * Example:
	 * https://www.google.com/finance/getprices?i=60&p=30d&f=d,o,h,l,c,v&q=TRUE
	 * @param symbol
	 * @return True if a request is sent out to Google API. False if the file is already downloaded.
	 * Used to prevent from downloading again.
	 * @throws Exception
	 */
	public static boolean downloadIntraDayStockFromGoogle(String symbol) throws Exception {
		String fileStock = StockConst.INTRADAY_DIRECTORY_PATH_GOOGLE + symbol + ".txt";
		
		File file = new File(fileStock);
		if (!file.exists()) {
			file.createNewFile();
		}
		else {
			System.out.println(symbol + " already downloaded, ignored.");
			return false; //Google prevents us from downloading very aggressively.
		}
		String siteAddress = "https://www.google.com/finance/getprices?i=" + StockConst.INTRADAY_DOWNLOAD_INTERVAL_GOOGLE + "&p=" + StockConst.INTRADAY_DOWNLOAD_PERIOD_GOOGLE + "d&f=d,o,h,l,c,v&q=" + symbol;
        URL site = new URL(siteAddress);
        ReadableByteChannel rbc = Channels.newChannel(site.openStream());
        FileOutputStream fos = new FileOutputStream(file);
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		fos.close();
		return true;
	}
	
	/**
	 * Download all intraday stocks from Google API.
	 * Google does not allow us to download very aggressively. So sleep 
	 * 
	 * @throws Exception
	 */
	public static void downloadIntraDayStocksFromGoogle() throws Exception {
		StockUtil.createNewDirectory(StockConst.INTRADAY_DIRECTORY_PATH_GOOGLE);
		ArrayList<String> symbolList = StockAPI.getSymbolList();
		int retry = 0;
		int index = 0;
		Random random = new Random();
		while (index < symbolList.size()) {
			String symbol = symbolList.get(index);
			if (retry > 0) {
				System.out.println(symbol + "Retry: " + retry);
			}
			else {
				System.out.println(symbol);
			}
			boolean downloaded = downloadIntraDayStockFromGoogle(symbol);
			if (downloaded) {
				int sleepTime = random.nextInt(30 - 15 + 1) + 15;
				StockUtil.sleepThread(sleepTime * 1000);
			}
			index++;
		}		
	}
	
	/**
	 * Download Intraday data for one single stock from Yahoo. The filename is defauted as today's date, i.e. yyyyMMdd.txt
	 * @param symbol
	 * @return
	 * @throws Exception
	 */
	public static boolean downloadIntraDayStockFromYahoo(String symbol) throws Exception {
		Date today = new Date();
		return downloadIntraDayStockFromYahoo(symbol, today);
	}
	
	/**
	 * Download Intraday data for one single stock from Yahoo.
	 * @param symbol
	 * @param today
	 * @return
	 * @throws Exception
	 */
	public static boolean downloadIntraDayStockFromYahoo(String symbol, Date today) {
		//Get today's date
		String directory = StockConst.INTRADAY_DIRECTORY_PATH_YAHOO + symbol + "\\";
		File directoryFile = new File(directory);
		if (!directoryFile.exists()) {
			directoryFile.mkdirs();
		}
		String fileStock = directory + StockUtil.formatDate(today) + ".txt";
//		String fileStock = directory + "20141222.txt";
		File file = new File(fileStock);
		if (!file.exists()) {
			try {
				file.createNewFile();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			System.out.println(symbol + " already downloaded, ignored.");
			return false;
		}
//		System.out.println(fileStock);
		String siteAddress = "http://chartapi.finance.yahoo.com/instrument/1.0/" + symbol + "/chartdata;type=quote;range=1d/csv";
		boolean success = false;
		int retry = 0;
		while (!success && (retry < MAX_RETRY)) {
			try {
				URL site = new URL(siteAddress);
				ReadableByteChannel rbc = Channels.newChannel(site.openStream());
				FileOutputStream fos = new FileOutputStream(file);
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
				fos.close();
				success = true;
			}
			catch (Exception e) {
				e.printStackTrace();
				retry++;
				System.out.println("Retry " + symbol + " " + retry);
				StockUtil.sleepThread(1000);
			}
		}
		return true;

	}
	
	/**
	 * Download Intraday data for all stocks from Yahoo. The filename is defauted as today's date, i.e. yyyyMMdd.txt
	 * @throws Exception
	 */
	public static void downloadIntraDayStocksFromYahoo() throws Exception {
		Date today = new Date();
		downloadIntraDayStocksFromYahoo(today);
	}
	
	/**
	 * Download Intraday data for all stocks from Yahoo.
	 * @param date
	 * @throws Exception
	 */
	public static void downloadIntraDayStocksFromYahoo(Date date) throws Exception {
		StockUtil.createNewDirectory(StockConst.INTRADAY_DIRECTORY_PATH_YAHOO);
		ArrayList<String> symbolList = StockAPI.getSymbolList();
		int retry = 0;
		int index = 0;
		Random random = new Random();
		while (index < symbolList.size()) {
			String symbol = symbolList.get(index);
			if (retry > 0) {
				System.out.println(symbol + "Retry: " + retry);
			}
			else {
				System.out.println(symbol);
			}
			boolean downloaded = downloadIntraDayStockFromYahoo(symbol, date);
			if (downloaded) {
//				int sleepTime = random.nextInt(30 - 15 + 1) + 15;
				int sleepTime = 1;
				StockUtil.sleepThread(sleepTime * 500);
			}
			index++;
		}	
	}
	
	/**
	 * Download earnings date data from Zach.
	 */
	public void downloadEarningsDatesFromZach() {
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd");
		//Parse start date.
		LocalDate startLocalDate = new LocalDate(startDate);
		//Parse end date.
		LocalDate endLocalDate = new LocalDate(endDate);
		
		StockUtil.createNewDirectory(StockConst.EARNINGS_DATES_DIRECTORY_PATH_ZACH);
		for (LocalDate localDate = endLocalDate; !localDate.isBefore(startLocalDate); localDate = localDate.minusDays(1)) {
			System.out.println(localDate.toString(formatter));
			String urlString = "http://www.zacks.com/research/earnings/earning_export.php?timestamp=" + (localDate.toDate().getTime() / 1000L) + "&tab_id=1";
//			System.out.println(urlString);
			String filename = StockConst.EARNINGS_DATES_DIRECTORY_PATH_ZACH + localDate.toString(formatter) + ".txt";
			if (StockUtil.fileExists(filename)) continue;
			StockUtil.downloadHTMLURL(urlString, filename);
			StockUtil.sleepThread(300);
		}
	}
	
	private static void downloadEarningsDatesFromStreetInsider(String symbol, String filename) {
		String urlString = "http://www.streetinsider.com/ec_earnings.php?q=" + symbol;
		StockUtil.downloadHTMLURL(urlString, filename);
		StockUtil.sleepThread(300);
	}
	
	public static void downloadEarningsDatesFromStreetInsider() {
		ArrayList<String> symbols = StockAPI.getAllSymbolList();
		for (String symbol : symbols) {
			String filename = StockConst.EARNINGS_DATES_DIRECTORY_PATH_STREET_INSIDER + symbol + ".html";
			if (StockUtil.fileExists(filename)) {
				System.out.println(symbol + " exists, skipped.");
				continue;			
			}
			if (symbol.contains("/")) {
				System.out.println(symbol + " has slash, skipped.");
				continue;			
			}
			System.out.println(symbol + " downloading...");
			downloadEarningsDatesFromStreetInsider(symbol, filename);
		}
	}
	
	public static void downloadEarningsDatesFromTheStreet() {
		ArrayList<String> symbols = StockAPI.getSymbolList();
		StockUtil.createNewDirectory(StockConst.EARNINGS_DATES_DIRECTORY_PATH_THE_STREEET);
		for (String symbol : symbols) {
			String filename = StockConst.EARNINGS_DATES_DIRECTORY_PATH_THE_STREEET + symbol + ".html";
			if (StockUtil.fileExists(filename)) {
				System.out.println(symbol + " exists, skipped.");
				continue;
			}
			System.out.print(symbol + " downloading...");
			downloadEarningsDatesFromTheStreet(symbol, filename);
			System.out.println("Done");
		}
	}
	
	private static void downloadEarningsDatesFromTheStreet(String symbol, String filename) {
		String urlString = "http://zacks.thestreet.com/CompanyView.php";
		String urlParameters = "ticker=" + symbol;
		StockUtil.downloadHTMLURLWithPost(urlString, filename, urlParameters);
		StockUtil.sleepThread(300);
	}
}
