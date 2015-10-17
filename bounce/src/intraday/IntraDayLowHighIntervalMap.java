package intraday;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import stock.StockAPI;
import stock.StockConst;
import stock.StockEnum.StockCandleClass;
import util.StockFileWriter;
import util.StockUtil;

/**
 * Return a mapping between candle classification and the average interval where low/high appears.
 * The interval is between 0 - 390.
 * Right now there are just a few basic candle classes (long white, long black, etc.)
 * The mapping file has the following format:
 * <Stock Candle Class>,<Sum of intervals for low>,<Count of intervals for low>
 * <Stock Candle Class>,<Sum of intervals for high>,<Count of intervals for high>
 * This is a singleton class to boost the efficiency. 
 * @author Dongyue Xue
 *
 */
public class IntraDayLowHighIntervalMap {

	private IntraDayLowHighIntervalMap() {
		
	}
	
	private static HashMap<StockCandleClass, Integer> lowIntervalMap;
	private static HashMap<StockCandleClass, Integer> highIntervalMap;
	
	/**
	 * Get the average interval where low appears for a specific candle class. 
	 * @param candleClass
	 * @return
	 */
	public static int getLowInterval(StockCandleClass candleClass) {
		if ((lowIntervalMap == null) || (highIntervalMap == null)) {
			setIntervalMap();
		}
		return lowIntervalMap.get(candleClass);
	}
	
	/**
	 * Get the average interval where high appears for a specific candle class. 
	 * @param candleClass
	 * @return
	 */
	public static int getHighInterval(StockCandleClass candleClass) {
		if ((lowIntervalMap == null) || (highIntervalMap == null)) {
			setIntervalMap();
		}
		return highIntervalMap.get(candleClass);
	}
	
	
	/**
	 * Reading from file, add a stock candle class to a map.
	 * @param br
	 * @param intervalMap
	 * @throws Exception
	 */
	private static void addStockCandleClassToMap(BufferedReader br, HashMap<StockCandleClass, Integer> intervalMap) throws Exception { 
		String line = br.readLine();
		String data[] = line.split(",");
		StockCandleClass candleClass = StockUtil.getEnumFromString(StockCandleClass.class, data[0]);
		long intervalTotal = Long.parseLong(data[1]);
		long intervalCount = Long.parseLong(data[2]);
		long interval = intervalTotal / intervalCount;
		intervalMap.put(candleClass, (int) interval);
	}
	
	/**
	 * Set low / high interval for specific candle class.
	 * @param candleClass
	 * @throws Exception
	 */
	private static void setIntervalMap() {
		lowIntervalMap = new HashMap<StockCandleClass, Integer>();
		highIntervalMap = new HashMap<StockCandleClass, Integer>();
		try {
			File file = new File(StockConst.INTRADAY_LOW_HIGH_INTERVAL_FILENAME);
			if (!file.exists()) {
				writeInterval();
			}
			BufferedReader br = new BufferedReader(new FileReader(file));
			//Candle class here is not useful.
			for (StockCandleClass candleClass : StockCandleClass.values()) {
				addStockCandleClassToMap(br, lowIntervalMap);
				addStockCandleClassToMap(br, highIntervalMap);
			}
			br.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	/**
	 * Write the sum of intervals / count of intervals for low and high.
	 * @throws Exception
	 */
	public static void writeInterval() throws Exception {
		File directory = new File(StockConst.INTRADAY_DIRECTORY_PATH_GOOGLE);
		
		HashMap<StockCandleClass, Long> lowIntervalTotalMap = new HashMap<StockCandleClass, Long>();
		HashMap<StockCandleClass, Long> lowIntervalCountMap = new HashMap<StockCandleClass, Long>();
		HashMap<StockCandleClass, Long> highIntervalTotalMap = new HashMap<StockCandleClass, Long>();
		HashMap<StockCandleClass, Long> highIntervalCountMap = new HashMap<StockCandleClass, Long>();
		
		for (File file : directory.listFiles()) {
//			if (!StockMarketCap.isLargeMarketCap(symbol)) continue;
			MultiDaysStockCandleArray mdStockCandleArray = StockAPI.getIntraDayStockCandleArrayGoogle(file);
			for (int i = 0; i < mdStockCandleArray.size(); i++) {
				IntraDayStockCandleArray idStockCandleArray = mdStockCandleArray.get(i);
				StockCandleClass candleClass = idStockCandleArray.getCandleClass();
				long lowIntervalTotal = getValueFromStockCandleClassMap(lowIntervalTotalMap, candleClass);
				long lowIntervalCount = getValueFromStockCandleClassMap(lowIntervalCountMap, candleClass);
				long highIntervalTotal = getValueFromStockCandleClassMap(highIntervalTotalMap, candleClass);
				long highIntervalCount = getValueFromStockCandleClassMap(highIntervalCountMap, candleClass);
				
				lowIntervalCount += idStockCandleArray.getLowIntervals().size();
				for (int index = 0; index < idStockCandleArray.getLowIntervals().size(); index++){
					lowIntervalTotal += idStockCandleArray.getLowIntervals().get(index);					
				}
				
				highIntervalCount += idStockCandleArray.getHighIntervals().size();
				for (int index = 0; index < idStockCandleArray.getHighIntervals().size(); index++){
					highIntervalTotal += idStockCandleArray.getHighIntervals().get(index);					
				}
				
				lowIntervalTotalMap.put(candleClass, lowIntervalTotal);
				lowIntervalCountMap.put(candleClass, lowIntervalCount);
				highIntervalTotalMap.put(candleClass, highIntervalTotal);
				highIntervalCountMap.put(candleClass, highIntervalCount);
				
			}
		}
		
		StockFileWriter sfw = new StockFileWriter(StockConst.INTRADAY_LOW_HIGH_INTERVAL_FILENAME);
		
		for (StockCandleClass candleClass : StockCandleClass.values()) {
			long lowIntervalTotal = getValueFromStockCandleClassMap(lowIntervalTotalMap, candleClass);
			long lowIntervalCount = getValueFromStockCandleClassMap(lowIntervalCountMap, candleClass);
			long highIntervalTotal = getValueFromStockCandleClassMap(highIntervalTotalMap, candleClass);
			long highIntervalCount = getValueFromStockCandleClassMap(highIntervalCountMap, candleClass);
			sfw.writeLine(candleClass.toString() + "," + lowIntervalTotal + "," + lowIntervalCount);
			sfw.writeLine(candleClass.toString() + "," + highIntervalTotal + "," + highIntervalCount);
		}
		sfw.close();
	}
	
	/**
	 * Get a value from the mapping. Check whether the mapping exists the stock candle class key first.
	 * @param map
	 * @param candleClass
	 * @return
	 */
	private static long getValueFromStockCandleClassMap(HashMap<StockCandleClass, Long> map, StockCandleClass candleClass) {
		if (map.containsKey(candleClass)) {
			return map.get(candleClass);
		}
		return 0;
	}
	
	

}
