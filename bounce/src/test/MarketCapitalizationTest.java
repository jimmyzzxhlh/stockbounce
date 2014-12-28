package test;

import java.util.HashMap;

import stock.StockAPI;
import stock.StockMarketCap;
import stock.StockPreviousCloseMap;
import stock.StockTurnoverRateDistribution;

public class MarketCapitalizationTest {
	
	
	private static final double[] turnoverRateDistribution = null;

	public static void main(String args[]) throws Exception {
//		testSharesOutstanding();
//		StockTurnoverRateDistribution.writeTurnoverRateDistribution();
//		getTurnoverRateDistribution();
//		testPreviousClose();
		testMarketCap();
		
	}
	
	private static void testSharesOutstanding() {
		HashMap<String, Long> sharesOutstanding = StockAPI.getSharesOutstandingMap();
		System.out.println(sharesOutstanding.get("TRUE").longValue());	
		
	}
		
	
	private static void getTurnoverRateDistribution() {
		double[] distribution = StockTurnoverRateDistribution.getDistribution();
		for (int i = 0; i < distribution.length; i++) {
			System.out.println(i + " " + distribution[i]);
		}
	}

	private static void testPreviousClose() {
		HashMap<String, Double> previousCloseMap = StockPreviousCloseMap.getMap();
		System.out.println(previousCloseMap.get("TRUE").doubleValue());
	}
	
	private static void testMarketCap() {
		System.out.println(StockMarketCap.getMarketCap("ZNGA"));
	}
	
	private static void printMarketCap() {
		
	}
}
