package stock;

import intraday.IntraDayAnalysisGoogle;
import intraday.IntraDayAnalysisYahoo;
import intraday.IntraDayVolumeDistribution;
import intraday.MultiDaysStockCandleArray;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import yahoo.YahooParser;

/**
 * This is a class for all common API functions so that we can use one single place to refer all APIs. 
 * @author jimmyzzxhlh-Dell
 *
 */
public class StockAPI {
	
	/**
	 * Get a list of symbols. Use this function for getting symbols that have market capitalization and
	 * outstanding shares data.
	 * @return Array list of string that contains symbols.
	 */
	public static ArrayList<String> getUSSymbolList() {
		return StockSymbolList.getUSSymbolList();
	}
	
	/**
	 * Get all the symbols from the company list file. Use this function ONLY when downloading outstanding shares CSV.
	 * @return
	 */
	public static ArrayList<String> getAllUSSymbolList() {
		return StockSymbolList.getAllUSSymbolList();
	}
	
	
	public static ArrayList<String> getSSESymbolList() {
		return StockSymbolList.getSSESymbolList();
	}
	
	public static ArrayList<String> getSZSESymbolList() {
		return StockSymbolList.getSZSESymbolList();
		
	}
	
	/**
	 * Get intraday data from Google. Will be deprecated in the future.
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static MultiDaysStockCandleArray getIntraDayStockCandleArrayGoogle(File file) throws Exception {
		return IntraDayAnalysisGoogle.getIntraDayStockCandleArray(file);
	}
	
	/**
	 * Get intraday data from Yahoo. Return an array list of intraday stock candle array.
	 * An intraday stock candle array contains at most 390 candles which represent the trend in one single day.
	 * @param symbol Symbol of the stock
	 * @return See description
	 * @throws Exception
	 */
	public static MultiDaysStockCandleArray getIntraDayStockCandleArrayYahoo(String symbol) throws Exception {
		return IntraDayAnalysisYahoo.getMultipleDaysStockCandleArray(symbol);
	}
	
	/**
	 * Get intraday volume distribution based on intervals. The length of the array is 390.
	 * Normally, the following timing has the largest volume:
	 * 0 (Before 8:30 AM) - After hour trading between the close time of the previous day and the open time of the curfrent day.
	 * 210 (1:00 PM) - 12:00 PM at eastern time which is lunch time.
	 * 390 (3:00 PM) - Market close time. 
	 * The distribution is percentage based. Sum of the distribution is 1.
	 * @return
	 */
	public static double[] getVolumeDistribution() {
		return IntraDayVolumeDistribution.getDistribution();
	}
	
	/**
	 * Get shares outstanding map for each symbol.
	 * Used to calculate turnover rate as well as market capitalization.
	 * @return
	 */
	public static HashMap<String, Long> getSharesOutstandingMap() {
		return StockSharesOutstandingMap.getMap();
	}

	/**
	 * Get a daily stock candle array from Yahoo data.
	 * @param symbol Symbol of the stock
	 * @return A stock candle array represents daily stock candles.
	 */
	public static StockCandleArray getStockCandleArrayYahoo(String symbol) {
		return YahooParser.readCSVFile(symbol);
	}
	
	/**
	 * Get a daily stock candle array from Yahoo data.
	 * @param file
	 * @return
	 */
	public static StockCandleArray getStockCandleArrayYahoo(File file) {
		return YahooParser.readCSVFile(file);
	}

}
