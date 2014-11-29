package stock;

import java.util.HashMap;

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
	
	public static boolean isLargeMarketCap(String symbol, StockCandleArray stockCandleArray) {
		return isLargeMarketCap(symbol, stockCandleArray.getClose(stockCandleArray.size() - 1));
	}
	
	
	public static boolean isLargeMarketCap(String symbol, double currentClose) {
		double marketCap = getMarketCap(symbol, currentClose);
		if (marketCap >= StockConst.LARGE_MARKET_CAP_MIN) return true;
		return false;
	}
	
}
