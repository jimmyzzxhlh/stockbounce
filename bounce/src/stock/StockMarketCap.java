package stock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import util.StockUtil;
import yahoo.YahooParser;

public class StockMarketCap {
	
	public static void downloadOutstandingSharesCSV() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < StockConst.LARGE_MARKET_CAP_SYMBOLS.length; i++) {
			String symbol = StockConst.LARGE_MARKET_CAP_SYMBOLS[i];
			sb.append(symbol);
			if (i < StockConst.LARGE_MARKET_CAP_SYMBOLS.length - 1) sb.append("+");
		}
		String urlString = "http://finance.yahoo.com/d/quotes.csv?s=" + sb.toString() + "&f=sj2";
		StockUtil.downloadURL(urlString, StockConst.MARKET_CAP_FILENAME);
		
	}
	
	public static HashMap<String, Long> getSharesOutstandingMap() throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(StockConst.MARKET_CAP_FILENAME));
		String line;
		HashMap<String, Long> sharesOutstandingMap = new HashMap<String, Long>();
		while ((line = br.readLine()) != null) {
			String[] lineArray = line.split(" ");
			for (int i = 0; i < lineArray.length; i++) lineArray[i].trim();
			String symbol = lineArray[0].substring(1, lineArray[0].length() - 2);
			String sharesOutStandingStr = lineArray[lineArray.length - 1];
			sharesOutStandingStr = sharesOutStandingStr.replace(",", "");
			sharesOutstandingMap.put(symbol, Long.parseLong(sharesOutStandingStr));
		}
		br.close();
		return sharesOutstandingMap;
		
	}
	
	public static double[] getTurnoverRateDistribution() {
		File directory = new File(StockConst.STOCK_CSV_DIRECTORY_PATH);
		File[] directoryList = directory.listFiles();
		if (directoryList == null) return null;
		HashMap<String, Integer> largeMarketCapMap = null;
		HashMap<String, Long> sharesOutstandingMap = null;
		try {
			sharesOutstandingMap = getSharesOutstandingMap();
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		int[] turnoverRateDistributionCount = new int[1000];
		int totalCandles = 0;
		for (File csvFile : directoryList) {
			String symbol = StockUtil.getSymbolFromFile(csvFile);
			if (!StockUtil.isLargeMarketCap(symbol, largeMarketCapMap)) continue; 
			StockCandleArray stockCandleArray = YahooParser.readCSVFile(csvFile, 0);
			long sharesOutstanding = sharesOutstandingMap.get(symbol).longValue();
			totalCandles += stockCandleArray.size();
			for (int i = 0; i < stockCandleArray.size(); i++) {
				int volume = stockCandleArray.getVolume(i);
				double rate = volume * 1000.0;
				rate = rate / sharesOutstanding;
				turnoverRateDistributionCount[(int) Math.round(rate)]++; 
			}		
		}
		double[] turnoverRateDistribution = new double[1000];
		for (int i = 0; i < turnoverRateDistributionCount.length; i++) {
			turnoverRateDistribution[i] = turnoverRateDistributionCount[i] * 1.0 / totalCandles;
		}
		return turnoverRateDistribution;
	}
}
