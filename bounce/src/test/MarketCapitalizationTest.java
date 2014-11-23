package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import stock.StockCandleArray;
import stock.StockConst;
import stock.StockMarketCap;
import util.StockUtil;
import yahoo.YahooParser;

public class MarketCapitalizationTest {
	
	
	public static void main(String args[]) throws Exception {
//		downloadOutstandingSharesCSV();
//		HashMap<String, Long> sharesOutstanding = getSharesOutstandingMap();
//		System.out.println(sharesOutstanding.get("AAPL").longValue());
		double[] turnoverRateDistribution = StockMarketCap.getTurnoverRateDistribution();
		for (int i = 0; i < turnoverRateDistribution.length; i++) {
			System.out.println(turnoverRateDistribution[i]);
			
		}
	}
	

}
