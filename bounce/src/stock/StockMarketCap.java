package stock;

import java.io.File;
import java.util.HashMap;

import util.StockUtil;
import yahoo.YahooParser;

public class StockMarketCap {

	
	public static double[] getTurnoverRateDistribution() {
		File directory = new File(StockConst.STOCK_CSV_DIRECTORY_PATH);
		File[] directoryList = directory.listFiles();
		if (directoryList == null) return null;
		HashMap<String, Integer> largeMarketCapMap = null;
		HashMap<String, Long> sharesOutstandingMap = null;
		sharesOutstandingMap = StockSharesOutstandingMap.getMap();
		if (sharesOutstandingMap == null) return null;
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
