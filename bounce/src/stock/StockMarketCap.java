package stock;

import java.io.File;
import java.util.HashMap;

import util.StockUtil;
import yahoo.YahooParser;

public class StockMarketCap {

	/**
	 * Get market capitalization. The definition is
	 * Market Capitalization = Outstanding shares * current close price (should it be average?)
	 * @param symbol Symbol of the stock
	 * @param stockCandleArray Stock Candle Array. Only use the last close price for now.
	 * @return A double indicating market capitalization
	 */
	public static double getMarketCap(String symbol, StockCandleArray stockCandleArray) {
		return getMarketCap(symbol, stockCandleArray.getClose(stockCandleArray.size() - 1));		
	}
	
	public static double getMarketCap(String symbol, double currentClose) {
		HashMap<String, Long> sharesOutstandingMap = StockSharesOutstandingMap.getMap();
		long sharesOutstanding = sharesOutstandingMap.get(symbol);
		return currentClose * sharesOutstanding; 		
	}
	
	
	
	
}
