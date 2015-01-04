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

	private static final String DEFAULT_START_DATE = "1/1/2006";
	private static final String TEMPORARY_MARKET_CAP_FILENAME = "D:\\zzx\\Stock\\MarketCap_Temp.csv";
	private static final String TEMPORARY_PREVIOUS_CLOSE_FILENAME = "D:\\zzx\\Stock\\PreviousClose_Temp.csv"; 
	private final static int MAX_RETRY = 5;
	private String startDate; 
	private String endDate;
	

	/**
	 * startDate and endDate are strings of the form "MM/dd/yyyy"
	 * Download csv files from startDate to endDate. Input null, null to use the default dates. 
	 * startDate is 4/1/2010 by default. endDate is today by default.
	 */
	public StockDownload(String startDate, String endDate){		
		this.startDate = startDate;
		this.endDate = endDate;
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
	 * @param symbol
	 * @throws Exception
	 */
	public void downloadStock(String symbol) throws Exception {
		String fileStock = StockConst.STOCK_CSV_DIRECTORY_PATH + symbol + ".csv";
		
		File file = new File(fileStock);
		if (!file.exists()) {
			file.createNewFile();
		}
		
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date fromDate = dateFormat.parse(startDate);
		Date toDate;
		if (endDate == null) {
			toDate = new Date();  //Get current date
		}
		else {
			toDate = dateFormat.parse(endDate);
		}
				
		Calendar cal = Calendar.getInstance();
		cal.setTime(fromDate);
		String startMonth = Integer.toString(cal.get(Calendar.MONTH));
        String startDay = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
        String startYear = Integer.toString(cal.get(Calendar.YEAR));
        cal.setTime(toDate);
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
		try {
			StockUtil.downloadURL(urlString, filename);
			Thread.sleep(1000);
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * Static function for downloading outstanding shares data from Yahoo.
	 * Example:
	 * http://finance.yahoo.com/d/quotes.csv?s=AAPL+GOOG+YHOO&f=sj2
	 */
	public static void downloadOutstandingSharesCSV() {
		//Delete the file first so that the existing content is wiped out.
		File f = new File(StockConst.SHARES_OUTSTANDING_FILENAME);
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
				String urlString = "http://finance.yahoo.com/d/quotes.csv?s=" + sb.toString() + "&f=sj2";
				sb = new StringBuilder();
				count = 0;
				StockUtil.downloadURL(urlString, TEMPORARY_MARKET_CAP_FILENAME);
				//Merge the temporary file to the ultimate output file.
				mergeFile(TEMPORARY_MARKET_CAP_FILENAME, StockConst.SHARES_OUTSTANDING_FILENAME);
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
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
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
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
				Thread.sleep(sleepTime * 1000);
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
	public static boolean downloadIntraDayStockFromYahoo(String symbol, Date today) throws Exception {
		//Get today's date
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String directory = StockConst.INTRADAY_DIRECTORY_PATH_YAHOO + symbol + "\\";
		File directoryFile = new File(directory);
		if (!directoryFile.exists()) {
			directoryFile.mkdirs();
		}
		String fileStock = directory + df.format(today) + ".txt";
//		String fileStock = directory + "20141222.txt";
		File file = new File(fileStock);
		if (!file.exists()) {
			file.createNewFile();
		}
		else {
			System.out.println(symbol + " already downloaded, ignored.");
			return false; //Google prevents us from downloading very aggressively.
		}
//		System.out.println(fileStock);
		String siteAddress = "http://chartapi.finance.yahoo.com/instrument/1.0/" + symbol + "/chartdata;type=quote;range=1d/csv";
        URL site = new URL(siteAddress);
        ReadableByteChannel rbc = Channels.newChannel(site.openStream());
        FileOutputStream fos = new FileOutputStream(file);
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		fos.close();
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
				Thread.sleep(sleepTime * 500);
			}
			index++;
		}	
	}
	
	public static void downloadEarningsDate() {
		
	}
	
	
}
