package test;

import java.util.ArrayList;
import java.util.Collections;
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
//		testMarketCap();
//		testGetAllSymbolList();
		testGetChinaSymbolList();
	}
	
	private static void testSharesOutstanding() {
		HashMap<String, Long> sharesOutstanding = StockAPI.getSharesOutstandingMap();
		ArrayList<String> symbols = new ArrayList(sharesOutstanding.keySet());
		Collections.sort(symbols);
		for (String symbol : symbols) {
			System.out.println(symbol + " " + sharesOutstanding.get(symbol));
		}
		
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
	
	private static void testGetAllSymbolList() {
		ArrayList<String> symbolList = StockAPI.getAllUSSymbolList();
		for (int i = 0; i < symbolList.size(); i++) {
			System.out.println(symbolList.get(i));
		}
	}
	
	private static void testGetChinaSymbolList() {
//		System.out.println("Shanghai:");
//		ArrayList<String> symbolList = StockAPI.getSSESymbolList();
//		for (int i = 0; i < symbolList.size(); i++) {
//			System.out.println(symbolList.get(i));
//		}
		System.out.println("Shen Zhen:");
		ArrayList<String> symbolList = StockAPI.getSZSESymbolList();
		for (int i = 0; i < symbolList.size(); i++) {
			System.out.println(symbolList.get(i));
		}
		
	}
}
