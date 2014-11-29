package test;

import java.io.File;
import java.util.HashMap;

import stock.StockCandleArray;
import stock.StockConst;
import stock.StockFileWriter;
import stock.StockSharesOutstandingMap;
import util.StockUtil;
import yahoo.YahooParser;

public class MarketCapitalizationTest {
	
	
	private static final double[] turnoverRateDistribution = null;

	public static void main(String args[]) throws Exception {
//		testSharesOutstanding();
		writeTurnoverRateDistribution();
	}
	
	private static void testSharesOutstanding() {
		HashMap<String, Long> sharesOutstanding = StockSharesOutstandingMap.getMap();
		System.out.println(sharesOutstanding.get("TRUE").longValue());	
		
	}
	
	private static void writeTurnoverRateDistribution() {
		File directory = new File(StockConst.STOCK_CSV_DIRECTORY_PATH);
		File[] directoryList = directory.listFiles();
		if (directoryList == null) {
			System.err.println("Cannot read files from directory: " + StockConst.STOCK_CSV_DIRECTORY_PATH);
			return;		
		}
		HashMap<String, Long> sharesOutstandingMap = null;
		sharesOutstandingMap = StockSharesOutstandingMap.getMap();
		if (sharesOutstandingMap == null) {
			System.err.println("Cannot get the shares outstanding map.");
			return;		
		}
		//Count how many times that the turnover rate appears.
		int[] turnoverRateDistributionCount = new int[1001];
		int totalCandles = 0;
		for (File csvFile : directoryList) {
			String symbol = StockUtil.getSymbolFromFile(csvFile);
			long sharesOutstanding = 0;
			if (sharesOutstandingMap.containsKey(symbol)) {
				sharesOutstanding = sharesOutstandingMap.get(symbol).longValue();
			}
			if (sharesOutstanding <= 0) {
				System.out.println(symbol + " does not have outstanding shares data, ignored.");
				continue;
			}
			StockCandleArray stockCandleArray;
			try {
				stockCandleArray = YahooParser.readCSVFile(csvFile);
			}
			catch (Exception e) {
				e.printStackTrace();
				System.err.println(csvFile.getName());
				return;
			}
			totalCandles += stockCandleArray.size();
			for (int i = 0; i < stockCandleArray.size(); i++) {
				int volume = stockCandleArray.getVolume(i);
				double rate = volume * 1000.0;
				rate = rate / sharesOutstanding;
				if (rate > 1000) rate = 1000;
				turnoverRateDistributionCount[(int) Math.round(rate)]++; 
			}		
		}
		
		try {
			StockFileWriter sfw = new StockFileWriter(StockConst.TURNOVER_RATE_DISTRIBUTION_FILENAME);
			for (int i = 0; i < turnoverRateDistributionCount.length; i++) {
				sfw.writeLine(turnoverRateDistributionCount[i] * 1.0 / totalCandles);
			}				
			sfw.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
