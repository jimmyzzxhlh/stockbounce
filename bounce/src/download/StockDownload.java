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
import java.util.Collections;
import java.util.Date;
import java.util.Random;

import stock.StockConst;
import util.StockUtil;

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
		
		ArrayList<String> symbolList = getSymbolList();
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
	 * Example:
	 * 
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
	
	/**
	 * Static function for getting a list of symbols from the company list CSV file.
	 * @return
	 */
	public static ArrayList<String> getSymbolList() {
		ArrayList<String> symbolList = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(StockConst.COMPANY_LIST_NASDAQ_FILENAME));
			String line;
			int lineNumber = 0;
			while ((line = br.readLine()) != null) {
				lineNumber++;
				//Ignore the first line as it is a header.
				if (lineNumber == 1) continue;
				//Symbol is the first piece of the comma delimited string
				String symbol = line.split(StockConst.COMMA_DELIMITER)[0];
				//Remove quotes.
				symbol = symbol.split(StockConst.QUOTE_DELIMITER)[1].trim();
				symbolList.add(symbol);
			}
			br.close();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Collections.sort(symbolList);
		return symbolList;
	}
	
	/**
	 * Static function for downloading outstanding shares
	 * Example:
	 * http://finance.yahoo.com/d/quotes.csv?s=AAPL+GOOG+YHOO&f=sj2
	 */
	public static void downloadOutstandingSharesCSV() {
		//Delete the file first so that the existing content is wiped out.
		File f = new File(StockConst.SHARES_OUTSTANDING_FILENAME);
		f.delete();
		StringBuilder sb = new StringBuilder();
		ArrayList<String> symbolList = getSymbolList();
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
		ArrayList<String> symbolList = getSymbolList();
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
	public static boolean downloadIntraDayStock(String symbol) throws Exception {
		String fileStock = StockConst.INTRADAY_DIRECTORY_PATH + symbol + ".txt";
		
		File file = new File(fileStock);
		if (!file.exists()) {
			file.createNewFile();
		}
		else {
			System.out.println(symbol + " already downloaded, ignored.");
			return false; //Google prevents us from downloading very aggressively.
		}
		String siteAddress = "https://www.google.com/finance/getprices?i=" + StockConst.INTRADAY_DOWNLOAD_INTERVAL + "&p=" + StockConst.INTRADAY_DOWNLOAD_PERIOD + "d&f=d,o,h,l,c,v&q=" + symbol;
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
	public static void downloadIntraDayStocks() throws Exception {
		StockUtil.createNewDirectory(StockConst.INTRADAY_DIRECTORY_PATH);
		ArrayList<String> symbolList = getSymbolList();
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
			boolean downloaded = downloadIntraDayStock(symbol);
			if (downloaded) {
				int sleepTime = random.nextInt(30 - 15 + 1) + 15;
				Thread.sleep(sleepTime * 1000);
			}
			index++;
		}
		
	}
}
