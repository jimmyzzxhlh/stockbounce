package test;

import indicator.StockAverageCostIndicator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import stock.StockAPI;
import stock.StockCandleArray;
import util.StockUtil;

public class AverageCostIndicatorTest {
	private static final String symbol = "YHOO";
	
	public static void main(String args[]) {
//		testAverageCostIndicator();
		testAverageCostIndicatorReverseUp();
	}
	
	public static void testAverageCostIndicator() {
		StockAverageCostIndicator indicator = new StockAverageCostIndicator(symbol);
		ArrayList<Date> reverseUpDates = indicator.getReverseUpDates();
		StockCandleArray stockCandleArray = indicator.getStockCandleArray();
		for (int i = 0; i < reverseUpDates.size(); i++) {
			System.out.println(StockUtil.formatDate(reverseUpDates.get(i)));
		}
//		

//		for (int i = 0; i < indicator.size(); i++) {
//			double close = stockCandleArray.getClose(i);
//			double averageCost = indicator.getAverageCost(i);
//			double difference = close - averageCost; 
//			System.out.println(dateFormat.format(stockCandleArray.getDate(i)) + " " + StockUtil.getRoundTwoDecimals(close) +
//					" " + StockUtil.getRoundTwoDecimals(averageCost) + " " + StockUtil.getRoundTwoDecimals(difference) +
//					" " + StockUtil.getRoundTwoDecimals(difference * 100.0 / close) + "%");
//		}
	}
	
	public static void testAverageCostIndicatorReverseUp() {
		ArrayList<String> symbolList = StockAPI.getSymbolList();
		int dateCount = 0;
		for (String symbol : symbolList) {
//			if (!symbol.equals("YHOO")) continue;
			System.out.println(symbol);
			StockAverageCostIndicator indicator = new StockAverageCostIndicator(symbol);
			ArrayList<Date> reverseUpDates = indicator.getReverseUpDates();
			StockCandleArray stockCandleArray = indicator.getStockCandleArray();
			for (Date reverseUpDate : reverseUpDates) {
				dateCount++;
//				System.out.println(dateFormat.format(reverseUpDate));
				int index = stockCandleArray.getDateIndex(reverseUpDate);
				
//				System.out.println(dateFormat.format(stockCandleArray.getDate(index)));
				double close = stockCandleArray.getClose(index);
				double maxProfit = stockCandleArray.getMaxProfit(index, 20);
				double maxLoss = stockCandleArray.getMaxLoss(index, 20);
				if (Math.abs(maxLoss) > Math.abs(maxProfit)) {
					System.out.println(StockUtil.formatDate(reverseUpDate) + " " + symbol + " " + StockUtil.getRoundTwoDecimals(maxProfit) + " " + StockUtil.getRoundTwoDecimals(maxLoss));
				}
			}
			
		}
		
	}
	
}
