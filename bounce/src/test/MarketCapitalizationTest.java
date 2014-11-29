package test;

import java.util.HashMap;

import stock.StockMarketCap;
import stock.StockSharesOutstandingMap;

public class MarketCapitalizationTest {
	
	
	public static void main(String args[]) throws Exception {
		testSharesOutstanding();
	}
	
	private static void testSharesOutstanding() {
		HashMap<String, Long> sharesOutstanding = StockSharesOutstandingMap.getMap();
		System.out.println(sharesOutstanding.get("TRUE").longValue());	
		
	}
	
	private static void testTurnoverRateDistribution() {
		double[] turnoverRateDistribution = StockMarketCap.getTurnoverRateDistribution();
		for (int i = 0; i < turnoverRateDistribution.length; i++) {
			System.out.println(turnoverRateDistribution[i]);
			
		}
	}

}
