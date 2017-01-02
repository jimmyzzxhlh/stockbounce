package test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import stock.OutstandingShares;
import stock.StockAPI;
import stock.StockMarketCap;
import stock.StockTurnoverRateDistribution;

public class MarketCapitalizationTest {
	
	
	private static final double[] turnoverRateDistribution = null;

	public static void main(String args[]) throws Exception {
//		testoutstandingShares();
//		StockTurnoverRateDistribution.writeTurnoverRateDistribution();
//		getTurnoverRateDistribution();
//		testPreviousClose();
//		testMarketCap();
//		testGetAllSymbolList();
		testGetChinaSymbolList();
	}
	
	private static void testoutstandingShares() {
		List<String> symbolList = StockAPI.getAllUSSymbolList();
		Collections.sort(symbolList);
		for (String symbol : symbolList) {
			System.out.println(symbol + " " + OutstandingShares.getOutstandingShares(symbol));
		}
		
	}
		
	
	private static void getTurnoverRateDistribution() {
		double[] distribution = StockTurnoverRateDistribution.getDistribution();
		for (int i = 0; i < distribution.length; i++) {
			System.out.println(i + " " + distribution[i]);
		}
	}

	
	private static void testMarketCap() {
		System.out.println(StockMarketCap.getMarketCap("ZNGA"));
	}
	
	private static void printMarketCap() {
		
	}
	
	private static void testGetAllSymbolList() {
		List<String> symbolList = StockAPI.getAllUSSymbolList();
		for (int i = 0; i < symbolList.size(); i++) {
			System.out.println(symbolList.get(i));
		}
	}
	
	private static void testGetChinaSymbolList() {
//		System.out.println("Shanghai:");
//		List<String> symbolList = StockAPI.getSSESymbolList();
//		for (int i = 0; i < symbolList.size(); i++) {
//			System.out.println(symbolList.get(i));
//		}
		System.out.println("Shen Zhen:");
		List<String> symbolList = StockAPI.getSZSESymbolList();
		for (int i = 0; i < symbolList.size(); i++) {
			System.out.println(symbolList.get(i));
		}
		
	}
}
