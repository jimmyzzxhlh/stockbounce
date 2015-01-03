package stock;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import stock.StockEnum.Exchange;
import util.StockUtil;

public class StockMarketCap {

	private static HashMap<String, Double> map;
	
	private StockMarketCap() {
		
	}
	
	
	/**
	 * Get market capitalization. The definition is
	 * Market Capitalization = Outstanding shares * current close price (should it be average?)
	 * @param symbol Symbol of the stock
	 * @param stockCandleArray Stock Candle Array. Only use the last close price for now.
	 * @return A double indicating market capitalization
	 */
	public static double getMarketCapFromSharesOutstanding(String symbol, StockCandleArray stockCandleArray) {
		if (stockCandleArray.size() <= 0) return 0;
		return getMarketCapFromSharesOutstanding(symbol, stockCandleArray.getClose(stockCandleArray.size() - 1));		
	}
	
	public static double getMarketCapFromSharesOutstanding(String symbol, double currentClose) {
		HashMap<String, Long> sharesOutstandingMap = StockAPI.getSharesOutstandingMap();
		long sharesOutstanding = sharesOutstandingMap.get(symbol);
		return currentClose * sharesOutstanding; 		
	}
	
	
	/**
	 * This is deprecated. To get the current market capitalization, we can simply read from
	 * the company list file.
	 */
//	public static double getMarketCap(String symbol) {
//		HashMap<String, Long> sharesOutstandingMap = StockAPI.getSharesOutstandingMap();
//		if (!sharesOutstandingMap.containsKey(symbol)) return 0;
//		long sharesOutstanding = sharesOutstandingMap.get(symbol).longValue();
//		HashMap<String, Double> previousCloseMap = StockPreviousCloseMap.getMap();
//		if (!previousCloseMap.containsKey(symbol)) return 0;
//		double previousClose = previousCloseMap.get(symbol).doubleValue();
//		return previousClose * sharesOutstanding;
//	}
	
	/**
	 * Get market capitalization given a symbol.
	 * Read the data from company list files.
	 * Notice that:
	 * 1. If the market capitalization is 0, then the symbol listed in the company list file is probably an invalid one
	 * or cannot be traded at all.
	 * 2. We will use this function to check in general whether the market cap is large / mid / small.
	 * If you need to use the last close price and shares outstanding to compute the market capitalization, use
	 * the other functions instead.
	 * @param symbol
	 * @return
	 */
	public static double getMarketCap(String symbol) {
		if (map == null) {		
			setMap();
		}
		return map.get(symbol);
	}
	
	/**
	 * Set mapping between symbol and the market capitalization for all stock exchanges.
	 */
	private static void setMap() {
		map = new HashMap<String, Double>();
		setMap(Exchange.NASDAQ);
		setMap(Exchange.NYSE);		
	}
	
	/**
	 * Set mapping between symbol and the market capitalization for one stock exchange.
	 */
	private static void setMap(Exchange exchange) {
		String filename = StockExchange.getCompanyListFilename(exchange);
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line;
			int lineNumber = 0;
			while ((line = br.readLine()) != null) {
				lineNumber++;
				//Ignore the first line as it is a header.
				if (lineNumber == 1) continue;
				//Split the data
				String data[] = StockUtil.splitCSVLine(line);
				//Check market capitalization. If not greater than 0 then ignore the symbol.
				double marketCap = Double.parseDouble(data[3]);
				if (marketCap <= 0) continue;
				//Symbol is the first piece of the comma delimited string
				String symbol = data[0];
				//Remove quotes.
				map.put(symbol, marketCap);
			}
			br.close();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}	
	}

	/**
	 * Get symbol list from the mapping.
	 * In this way we will filter off symbols that do not have a market capitalization so the 
	 * data is cleaner.
	 */
	public static ArrayList<String> getSymbolList() {
		if (map == null) {
			setMap();
		}
		ArrayList<String> symbolList = new ArrayList<String>(map.keySet()); 
		Collections.sort(symbolList);
		return symbolList;
	}
	
	
	/**
	 * Return true if the symbol is a large market capitalization stock.
	 * This is computed based on the last close price and the shares outstanding.
	 * @param symbol
	 * @param stockCandleArray
	 * @return
	 */
	public static boolean isLargeMarketCapFromSharesOutstanding(String symbol, StockCandleArray stockCandleArray) {
		if (stockCandleArray.size() <= 0) return false;
		return isLargeMarketCapFromSharesOutstanding(symbol, stockCandleArray.getClose(stockCandleArray.size() - 1));
	}
	
	public static boolean isLargeMarketCapFromSharesOutstanding(String symbol, double currentClose) {
		double marketCap = getMarketCapFromSharesOutstanding(symbol, currentClose);
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
	public static boolean isMiddleMarketCapFromSharesOutstanding(String symbol, StockCandleArray stockCandleArray) {
		if (stockCandleArray.size() <= 0) return false;
		return isMiddleMarketCapFromSharesOutstanding(symbol, stockCandleArray.getClose(stockCandleArray.size() - 1));
	}
	
	public static boolean isMiddleMarketCapFromSharesOutstanding(String symbol, double currentClose) {
		double marketCap = getMarketCapFromSharesOutstanding(symbol, currentClose);
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
	public static boolean isSmallMarketCapFromSharesOutstanding(String symbol, StockCandleArray stockCandleArray) {
		if (stockCandleArray.size() <= 0) return false;
		return isSmallMarketCapFromSharesOutstanding(symbol, stockCandleArray.getClose(stockCandleArray.size() - 1));
	}
	
	public static boolean isSmallMarketCapFromSharesOutstanding(String symbol, double currentClose) {
		double marketCap = getMarketCapFromSharesOutstanding(symbol, currentClose);
		if ((marketCap >= StockConst.SMALL_MARKET_CAP_MIN) && (marketCap <= StockConst.SMALL_MARKET_CAP_MAX)) return true;
		return false;
	}
	
	public static boolean isSmallMarketCap(String symbol) {
		double marketCap = getMarketCap(symbol);
		if ((marketCap >= StockConst.SMALL_MARKET_CAP_MIN) && (marketCap <= StockConst.SMALL_MARKET_CAP_MAX)) return true;
		return false;
	}
	
}
