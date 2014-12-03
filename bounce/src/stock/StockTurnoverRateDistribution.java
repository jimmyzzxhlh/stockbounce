package stock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import util.StockUtil;
import yahoo.YahooParser;

public class StockTurnoverRateDistribution {
	private static double[] distribution = null;
	
	private StockTurnoverRateDistribution() {
		
	}
	
	public static double[] getDistribution() { 
		if (distribution == null) setDistribution();
		return distribution;
	}
	
	public static double getProbability(int turnoverRate) {
		return getDistribution()[turnoverRate];
	}
	
	private static void setDistribution() {
		distribution = new double[StockConst.TURNOVER_RATE_DISTRIBUTION_ARRAY_LENGTH];
		try {
			BufferedReader br = new BufferedReader(new FileReader(StockConst.TURNOVER_RATE_DISTRIBUTION_FILENAME));
			String line;
			int index = 0;
			while ((line = br.readLine()) != null) {
				distribution[index] = Double.parseDouble(line);
				index++;
			}
			br.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Write turnover rate distribution to a CSV file.
	 * Each line represents the probability that the turnover rate can appear.
	 * Notice that we are only dealing with large market capitalization stock right now.
	 */
	public static void writeTurnoverRateDistribution() {
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
			//Right now we are only dealing with large market capitalization stock.
			if (!StockMarketCap.isLargeMarketCap(symbol, stockCandleArray)) continue;
			totalCandles += stockCandleArray.size();
			for (int i = 0; i < stockCandleArray.size(); i++) {
				long volume = stockCandleArray.getVolume(i);
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
