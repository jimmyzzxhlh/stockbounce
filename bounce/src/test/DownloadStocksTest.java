package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;

import download.StockDataMerge;
import download.StockDownload;
import indicator.StockAverageCostIndicator;
import intraday.IntraDayReaderYahoo;
import intraday.IntraDayStockCandleArray;
import stock.StockAPI;
import stock.StockConst;
import stock.StockEnum.Country;
import stock.StockEnum.Exchange;
import util.StockFileWriter;
import util.StockUtil;

public class DownloadStocksTest {
	
	private static final String SYMBOL = "BABA";
	private static final String START_DATE = "20050101";
	private static final String END_DATE = "20150411";
	
	public static void main(String args[]) throws Exception {
//		downloadSingleStock();
//		downloadStocks();
//		downloadOutstandingSharesCSV();
//		downloadPreviousCloseCSV();
//		downloadIntraDayStocksFromGoogle();
//		downloadIntraDayStocksFromYahoo(Country.US);
//		downloadIntraDayStocksFromYahoo(Country.CHINA);
//		downloadCompanyLists();
//		downloadEarningsDatesFromZach();
		downloadDailyTask();
//		downloadHTMLURL();
//		downloadEarningsDatesFromStreetInsider();
//		downloadHTMLURLWithPostTest();
//		downloadEarningsDatesFromTheStreet();
//		cleanUpBadDataTwo();
//		extractIntraDayFromMultipleDays();
//		downloadCompanyListsFromSSE();
//		analyzeIndicator();
		
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

	
	private static void downloadIntraDayStocksFromYahoo(Country country) throws Exception {
		Scanner reader = new Scanner(System.in);
		System.out.println(country.name() + ": Enter date for the file name (e.g. 20141226). Enter 1 to use today. Enter nothing to skip downloading.");
		String inputString;
		String dateString = null;
		while (dateString == null) {
			inputString = reader.nextLine().trim();
			if (inputString.length() <= 0) break;
			if (inputString.equals("1")) {
				dateString = StockUtil.formatDate(new Date());
				break;
			}
			else {
				Date date = StockUtil.parseDate(dateString);
				if (date != null) break;
			}				
		}
		System.out.println(country.name() + ": Enter sleep time, nothing then use the default sleep time 300.");
		String sleepTimeString = reader.nextLine().trim();
		int sleepTime = 0;
		if (sleepTimeString.length() <= 0) {
			sleepTime = StockConst.INTRADAY_DEFAULT_SLEEP_TIME;
		}
		else {
			sleepTime = Integer.parseInt(sleepTimeString);
		}
		switch (country) {
		case US:
			//Setting a default US exchange (any US exchange is OK here).
			StockDownload.downloadIntraDayStocksFromYahoo(Exchange.NASDAQ, dateString, sleepTime);
			System.out.println("Verifying data...");
			StockDownload.verifyIntraDayDataFromYahoo(Exchange.NASDAQ, dateString);
			break;
		case CHINA:
			StockDownload.downloadIntraDayStocksFromYahoo(Exchange.SSE, dateString, sleepTime);
			System.out.println("Verifying data...");
			StockDownload.verifyIntraDayDataFromYahoo(Exchange.SSE, dateString);
			StockDownload.downloadIntraDayStocksFromYahoo(Exchange.SZSE, dateString, sleepTime);
			System.out.println("Verifying data...");
			StockDownload.verifyIntraDayDataFromYahoo(Exchange.SZSE, dateString);
			break;
		}
				
	}
	
	private static void downloadDailyTask() throws Exception { 
		Scanner reader = new Scanner(System.in);
		String inputString;
		int taskNumber = 0;
		taskNumber++;
		System.out.println(taskNumber + ". Download US company lists...");
		System.out.println("Enter 1 to update the company lists, otherwise you can enter nothing.");
		inputString = reader.nextLine().trim();
		if (inputString.equals("1")) {
			downloadCompanyLists();
		}
		else {
			System.out.println("Use existing company lists...");
		}
		taskNumber++;
		System.out.println(taskNumber + ". Download SSE company lists...");
		System.out.println("Enter 1 to update the company lists, otherwise you can enter nothing.");
		inputString = reader.nextLine().trim();
		if (inputString.equals("1")) {
			downloadCompanyListsFromSSE();
		}
		else {
			System.out.println("Use existing SSE company lists...");
		}
		taskNumber++;
		System.out.println(taskNumber + ". Download outstanding shares data from Yahoo...");
		System.out.println("Enter 1 to update outstanding shares, otherwise you can enter nothing.");
		inputString = reader.nextLine().trim();
		if (inputString.equals("1")) {
			downloadOutstandingSharesCSV();
		}
		else {
			System.out.println("Use existing outstanding shares data...");
		}
		taskNumber++;
		System.out.println(taskNumber + ". Download US Intra day data from Yahoo...");
		downloadIntraDayStocksFromYahoo(Country.US);
		taskNumber++;
		System.out.println(taskNumber + ". Download China Intra day data from Yahoo...");
		downloadIntraDayStocksFromYahoo(Country.CHINA);
		StockUtil.pressAnyKeyToContinue();
		reader.close();
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
	
	private static void downloadCompanyLists() {
		StockDownload.downloadCompanyLists();
	}
	
	/**
	 * Clean up invalid data downloaded.
	 * Delete symbol folders that do not have outstanding shares data.
	 * 
	 */
	private static void cleanUpBadDataOne() {
		String filename = "20150217.txt";
		File directory = new File(StockConst.INTRADAY_DIRECTORY_PATH_YAHOO);
		ArrayList<String> symbolList = StockAPI.getUSSymbolList();
		ArrayList<String> allSymbolList = StockAPI.getAllUSSymbolList();
		for (File subDirectory : directory.listFiles()) {
			String symbol = subDirectory.getName();
			boolean delete = false;
			//Delete only if the symbol does not have outstanding shares data.
			if (!symbolList.contains(symbol) && allSymbolList.contains(symbol)) {
				File[] files = subDirectory.listFiles();
				if (files.length > 1) continue;
				for (File file : subDirectory.listFiles()) {
					if (file.getName().equals(filename)) {
						delete = true;
						file.delete();
						System.out.println(symbol + "\\" + file.getName() + " deleted.");
					}
				}
			}
			if (delete) {
				subDirectory.delete();
				System.out.println(symbol + " folder deleted.");
			}
		}
	}
	
	/**
	 * Clean up bad data. 
	 * This function needs to be modified every time. Reuse it.
	 * @throws Exception
	 */
	private static void cleanUpBadDataTwo() throws Exception {
		String filename = "20150217.txt";
		File directory = new File(StockConst.INTRADAY_DIRECTORY_PATH_YAHOO);
		for (File subDirectory : directory.listFiles()) {
			String symbol = subDirectory.getName();
			File file = new File(subDirectory.getAbsolutePath() + "\\" + filename);
			if (!file.exists()) continue;
			if (file.length() > 1) continue;
			System.out.println(symbol);
			file.delete();
			
		}
	}
	
	/**
	 * Extract intraday data from multiple days data (e.g. 2d/csv).
	 * The data will have less accuracy though...
	 * @throws Exception
	 */
	private static void extractIntraDayFromMultipleDays() throws Exception {
		String filename = "20150317.txt";
//		File directory = new File(StockConst.INTRADAY_DIRECTORY_PATH_YAHOO);
//		File directory = new File(StockConst.INTRADAY_DIRECTORY_SSE_PATH);
		File directory = new File(StockConst.INTRADAY_DIRECTORY_SZSE_PATH);
		String outputRootDirectoryString = "D:\\zzx\\Stock\\IntraDayRepair\\";
		StockUtil.createNewDirectory(outputRootDirectoryString);
		for (File subDirectory : directory.listFiles()) {
			String symbol = subDirectory.getName();
			
//			if (!symbol.equals("600005")) continue;
			String outputDirectoryString = outputRootDirectoryString + symbol + "\\";
			StockUtil.createNewDirectory(outputDirectoryString);

			String outputFilename = outputDirectoryString + filename;

			for (File file : subDirectory.listFiles()) {
				if (!file.getName().equals(filename)) continue;
				
				StockFileWriter sfw = new StockFileWriter(outputFilename);
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line;
				String[] data;
				int rangeStartOne = 0;
				int rangeEndOne = 0;
				boolean firstLine = true;
				while ((line = br.readLine()) != null) {
					
					if (line.startsWith("range:20150317")) {
						data = StockUtil.splitCSVLine(line);
						rangeStartOne = Integer.parseInt(data[1]);
						rangeEndOne = Integer.parseInt(data[2]);
						break;
					}
					else if (line.startsWith("range:")) {
						continue;
					}
					else {
//						System.out.println(line);
						if (firstLine) {
							line = line.replace("2d/csv", "1d/csv");
							firstLine = false;
						}
						sfw.writeLine(line);
					}
				}
				if ((rangeStartOne == 0) || (rangeEndOne == 0)) {
					sfw.close();
					sfw.getFile().delete();
//					outputFolderDirectory.delete();
					
					System.out.println(symbol + " does not need repair.");
					break;
				}
				System.out.println(symbol + "\\" + file.getName() + " processing...");
				//Skipping all range lines.
				while (line.startsWith("range:")) {
					line = br.readLine();
				}
				//Skipping timestamp line. This can be output from the range.
//				System.out.println("Timestamp:" + rangeStartOne + "," + rangeEndOne);
				sfw.writeLine("Timestamp:" + rangeStartOne + "," + rangeEndOne);
				//Processing label line.
				line = br.readLine();
//				System.out.print(line.substring(0, 7));
				sfw.write(line.substring(0, 7));
				line = line.substring(7);
				data = StockUtil.splitCSVLine(line);
				for (int i = 0; i < data.length; i++) {
					String tsStr = data[i];
					int ts = Integer.parseInt(tsStr);
					if ((ts >= rangeStartOne) && (ts <= rangeEndOne)) {
						if (i > 0) {
//							System.out.print(",");
							sfw.write(",");
						}
//						System.out.print(tsStr);
						sfw.write(tsStr);
					}
				}
//				System.out.println();
				sfw.newLine();
				line = br.readLine();
//				System.out.println(line);
				sfw.writeLine(line);
				while ((line = br.readLine()) != null) {
					if (line.startsWith("volume")) break;
				}
				ArrayList<Integer> tsArray = new ArrayList<Integer>();
				ArrayList<Double> closeArray = new ArrayList<Double>();
				ArrayList<Double> highArray = new ArrayList<Double>();
				ArrayList<Double> lowArray = new ArrayList<Double>();
				ArrayList<Double> openArray = new ArrayList<Double>();
				ArrayList<Long> volumeArray = new ArrayList<Long>();
				while ((line = br.readLine()) != null) {
					data = StockUtil.splitCSVLine(line);
					int ts = Integer.parseInt(data[0]);
					if ((ts >= rangeStartOne) && (ts <= rangeEndOne)) {
						double close = Double.parseDouble(data[1]);
						double high = Double.parseDouble(data[2]);
						double low = Double.parseDouble(data[3]);
						double open = Double.parseDouble(data[4]);
						long volume = Long.parseLong(data[5]);
						tsArray.add(ts);
						closeArray.add(close);
						highArray.add(high);
						lowArray.add(low);
						openArray.add(open);
						volumeArray.add(volume);
					}
				}
//				System.out.println("close:" + Collections.min(closeArray) + "," + Collections.max(closeArray));
//				System.out.println("high:" + Collections.min(highArray) + "," + Collections.max(highArray));
//				System.out.println("low:" + Collections.min(lowArray) + "," + Collections.max(lowArray));
//				System.out.println("open:" + Collections.min(openArray) + "," + Collections.max(openArray));
//				System.out.println("volume:" + Collections.min(volumeArray) + "," + Collections.max(volumeArray));
				sfw.writeLine("close:" + Collections.min(closeArray) + "," + Collections.max(closeArray));
				sfw.writeLine("high:" + Collections.min(highArray) + "," + Collections.max(highArray));
				sfw.writeLine("low:" + Collections.min(lowArray) + "," + Collections.max(lowArray));
				sfw.writeLine("open:" + Collections.min(openArray) + "," + Collections.max(openArray));
				sfw.writeLine("volume:" + Collections.min(volumeArray) + "," + Collections.max(volumeArray));
				
				for (int i = 0; i < tsArray.size(); i++) {
//					System.out.println(tsArray.get(i) + "," + closeArray.get(i) + "," + highArray.get(i) + "," + lowArray.get(i) + "," + openArray.get(i) + "," + volumeArray.get(i));
					sfw.writeLine(tsArray.get(i) + "," + closeArray.get(i) + "," + highArray.get(i) + "," + lowArray.get(i) + "," + openArray.get(i) + "," + volumeArray.get(i));
				}
				br.close();
				sfw.close();
			}
		}
		
		File outputRootDirectory = new File(outputRootDirectoryString);
		for (File subDirectory : outputRootDirectory.listFiles()) {
			if (subDirectory.listFiles().length == 0) {
				String symbol = subDirectory.getName();
				System.out.println(symbol + " folder deleting...");
				subDirectory.delete();
			}
		}
	}
	
	private static void downloadCompanyListsFromSSE() {
		try {
			StockDownload.downloadSSECompanyList();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void analyzeIndicator() throws Exception {
		StockAverageCostIndicator.analyzeIndicator();
	}
	

	

}