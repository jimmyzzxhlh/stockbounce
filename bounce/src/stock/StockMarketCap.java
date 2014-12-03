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
		if (stockCandleArray.size() <= 0) return 0;
		return getMarketCap(symbol, stockCandleArray.getClose(stockCandleArray.size() - 1));		
	}
	
	public static double getMarketCap(String symbol, double currentClose) {
		HashMap<String, Long> sharesOutstandingMap = StockSharesOutstandingMap.getMap();
		long sharesOutstanding = sharesOutstandingMap.get(symbol);
		return currentClose * sharesOutstanding; 		
	}
	
	/**
	 * Similar function for getting market capitalization. It reads the shares outstanding
	 * and the previous close price from CSV.
	 * @param symbol
	 * @return
	 */
	public static double getMarketCap(String symbol) {
		HashMap<String, Long> sharesOutstandingMap = StockSharesOutstandingMap.getMap();
		if (!sharesOutstandingMap.containsKey(symbol)) return 0;
		long sharesOutstanding = sharesOutstandingMap.get(symbol).longValue();
		HashMap<String, Double> previousCloseMap = StockPreviousCloseMap.getMap();
		if (!previousCloseMap.containsKey(symbol)) return 0;
		double previousClose = previousCloseMap.get(symbol).doubleValue();
		return previousClose * sharesOutstanding;
	}
	
	/**
	 * Return true if the symbol is a large market capitalization stock.
	 * @param symbol
	 * @param stockCandleArray
	 * @return
	 */
	public static boolean isLargeMarketCap(String symbol, StockCandleArray stockCandleArray) {
		if (stockCandleArray.size() <= 0) return false;
		return isLargeMarketCap(symbol, stockCandleArray.getClose(stockCandleArray.size() - 1));
	}
	
	public static boolean isLargeMarketCap(String symbol, double currentClose) {
		double marketCap = getMarketCap(symbol, currentClose);
		if (marketCap >= StockConst.LARGE_MARKET_CAP_MIN) return true;
		return false;
	}
	
	public static boolean isLargeMarketCap(String symbol) {
		double marketCap = getMarketCap(symbol);
		if (marketCap >= StockConst.LARGE_MARKET_CAP_MIN) return true;
		return false;
	}
	
	/**
	 * Return true if the symbol is a middle market capitalization stock.
	 * @param symbol
	 * @param stockCandleArray
	 * @return
	 */
	public static boolean isMiddleMarketCap(String symbol, StockCandleArray stockCandleArray) {
		if (stockCandleArray.size() <= 0) return false;
		return isMiddleMarketCap(symbol, stockCandleArray.getClose(stockCandleArray.size() - 1));
	}
	
	public static boolean isMiddleMarketCap(String symbol, double currentClose) {
		double marketCap = getMarketCap(symbol, currentClose);
		if ((marketCap >= StockConst.MIDDLE_MARKET_CAP_MIN) && (marketCap <= StockConst.MIDDLE_MARKET_CAP_MAX)) return true;
		return false;
	}
	
	public static boolean isMiddleMarketCap(String symbol) {
		double marketCap = getMarketCap(symbol);
		if ((marketCap >= StockConst.MIDDLE_MARKET_CAP_MIN) && (marketCap <= StockConst.MIDDLE_MARKET_CAP_MAX)) return true;
		return false;
	}
	
	/**
	 * Return true if the symbol is a small market capitalization stock.
	 * @param symbol
	 * @param stockCandleArray
	 * @return
	 */
	public static boolean isSmallMarketCap(String symbol, StockCandleArray stockCandleArray) {
		if (stockCandleArray.size() <= 0) return false;
		return isSmallMarketCap(symbol, stockCandleArray.getClose(stockCandleArray.size() - 1));
	}
	
	public static boolean isSmallMarketCap(String symbol, double currentClose) {
		double marketCap = getMarketCap(symbol, currentClose);
		if ((marketCap >= StockConst.SMALL_MARKET_CAP_MIN) && (marketCap <= StockConst.SMALL_MARKET_CAP_MAX)) return true;
		return false;
	}
	
	public static boolean isSmallMarketCap(String symbol) {
		double marketCap = getMarketCap(symbol);
		if ((marketCap >= StockConst.SMALL_MARKET_CAP_MIN) && (marketCap <= StockConst.SMALL_MARKET_CAP_MAX)) return true;
		return false;
	}
	
}
