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
	public static boolean returnAllSymbols = false;  //Set to true if we do not want to ignore symbols that have market capitalization
	                                                 //equal to 0 or outstanding shares equal to 0. Use this flag to download oustanding shares CSV.
	private StockMarketCap() {}
	
	
	/**
	 * Get market capitalization. The definition is
	 * Market Capitalization = Outstanding shares * current close price (should it be average?)
	 * @param symbol Symbol of the stock
	 * @param stockCandleList Stock Candle Array. Only use the last close price for now.
	 * @return A double indicating market capitalization
	 */
	public static double getMarketCapFromOutstandingShares(String symbol, CandleList stockCandleList) {
		if (stockCandleList.size() <= 0) return 0;
		return getMarketCapFromOutstandingShares(symbol, stockCandleList.getClose(stockCandleList.size() - 1));		
	}
	
	public static double getMarketCapFromOutstandingShares(String symbol, double currentClose) {
		return currentClose * OutstandingShares.getOutstandingShares(symbol); 		
	}
	
	
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
		try {
			setMap(Exchange.NASDAQ);
			setMap(Exchange.NYSE);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Set mapping between symbol and the market capitalization for one stock exchange.
	 */
	private static void setMap(Exchange exchange) throws Exception {
		String filename = ExchangeUtil.getCompanyListFilename(exchange);
		
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line;
		int lineNumber = 0;
		while ((line = br.readLine()) != null) {
			lineNumber++;
			//Ignore the first line as it is a header.
			if (lineNumber == 1) continue;
			//Split the data
			String data[] = StockUtil.splitCSVLine(line);
			String symbol = data[0];
			String marketCapStr = data[3];
			double marketCap = -1;
			//Check market capitalization. If not greater than 0 then ignore the symbol.
			//Do the check only when returnAllSymbols = false.
			if (!returnAllSymbols) {					
				if (marketCapStr.equals("n/a")) continue;
				//If there is no shares outstanding at all then ignore.
				if (OutstandingShares.getOutstandingShares(symbol) <= 0) { 
//					System.out.println(symbol + " does not have shares outstanding data.");
					continue;					
				}
				if (marketCapStr.startsWith("$")) marketCapStr = marketCapStr.substring(1);
				int coefficient = 1;
				if (marketCapStr.endsWith("K")) {
					coefficient = 1000;	
					marketCapStr = marketCapStr.substring(0, marketCapStr.length() - 1);
				}
				else if (marketCapStr.endsWith("M")) {
					coefficient = 1000000;
					marketCapStr = marketCapStr.substring(0, marketCapStr.length() - 1);					
				}
				else if (marketCapStr.endsWith("B")) {
					coefficient = 1000000000;
					marketCapStr = marketCapStr.substring(0, marketCapStr.length() - 1);
				}
				marketCap = Double.parseDouble(marketCapStr) * coefficient;
				
			}
			if (marketCap > 0) {
				map.put(symbol, marketCap);
			}
		}
		br.close();
	}


	
	/**
	 * Return true if the symbol is a large market capitalization stock.
	 * This is computed based on the last close price and the shares outstanding.
	 * @param symbol
	 * @param stockCandleList
	 * @return
	 */
	public static boolean isLargeMarketCapFromoutstandingShares(String symbol, CandleList stockCandleList) {
		if (stockCandleList.size() <= 0) return false;
		return isLargeMarketCapFromoutstandingShares(symbol, stockCandleList.getClose(stockCandleList.size() - 1));
	}
	
	public static boolean isLargeMarketCapFromoutstandingShares(String symbol, double currentClose) {
		double marketCap = getMarketCapFromOutstandingShares(symbol, currentClose);
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
	 * @param stockCandleList
	 * @return
	 */
	public static boolean isMiddleMarketCapFromoutstandingShares(String symbol, CandleList stockCandleList) {
		if (stockCandleList.size() <= 0) return false;
		return isMiddleMarketCapFromoutstandingShares(symbol, stockCandleList.getClose(stockCandleList.size() - 1));
	}
	
	public static boolean isMiddleMarketCapFromoutstandingShares(String symbol, double currentClose) {
		double marketCap = getMarketCapFromOutstandingShares(symbol, currentClose);
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
	 * @param stockCandleList
	 * @return
	 */
	public static boolean isSmallMarketCapFromoutstandingShares(String symbol, CandleList stockCandleList) {
		if (stockCandleList.size() <= 0) return false;
		return isSmallMarketCapFromoutstandingShares(symbol, stockCandleList.getClose(stockCandleList.size() - 1));
	}
	
	public static boolean isSmallMarketCapFromoutstandingShares(String symbol, double currentClose) {
		double marketCap = getMarketCapFromOutstandingShares(symbol, currentClose);
		if ((marketCap >= StockConst.SMALL_MARKET_CAP_MIN) && (marketCap <= StockConst.SMALL_MARKET_CAP_MAX)) return true;
		return false;
	}
	
	public static boolean isSmallMarketCap(String symbol) {
		double marketCap = getMarketCap(symbol);
		if ((marketCap >= StockConst.SMALL_MARKET_CAP_MIN) && (marketCap <= StockConst.SMALL_MARKET_CAP_MAX)) return true;
		return false;
	}
	
}
