package test;

import indicator.StockAverageCostIndicator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import stock.StockAPI;
import stock.StockCandleArray;
import stock.StockEnum.StockCandleDataType;
import util.StockUtil;

public class AverageCostIndicatorTest {
	private static final String symbol = "YHOO";
	
	public static void main(String args[]) {
//		testAverageCostIndicator();
		testAverageCostIndicatorReverseUp();
	}
	
	public static void testAverageCostIndicator() {
		StockAverageCostIndicator indicator = new StockAverageCostIndicator(symbol);
		StockCandleArray stockCandleArray = indicator.getStockCandleArray();

		for (int i = 0; i < indicator.size(); i++) {
			double close = stockCandleArray.getClose(i);
			double averageCost = indicator.getAverageCost(i);
			double difference = close - averageCost; 
			System.out.println(StockUtil.formatDate(stockCandleArray.getDate(i)) + " " + StockUtil.getRoundTwoDecimals(close) +
					" " + StockUtil.getRoundTwoDecimals(averageCost) + " " + StockUtil.getRoundTwoDecimals(difference) +
					" " + StockUtil.getRoundTwoDecimals(difference * 100.0 / close) + "%");
		}
	}
	
	public static void testAverageCostIndicatorReverseUp() {
		ArrayList<String> symbolList = StockAPI.getSymbolList();
		int dateCount = 0;
		for (String symbol : symbolList) {
			if (!symbol.equals("YHOO")) continue;
			System.out.println(symbol);
			StockAverageCostIndicator indicator = new StockAverageCostIndicator(symbol);
			ArrayList<Integer> reverseUpDatesIndex = indicator.getReverseUpDatesIndex();
			StockCandleArray stockCandleArray = indicator.getStockCandleArray();
			for (int index : reverseUpDatesIndex) {
				Date reverseUpDate = stockCandleArray.getDate(index);
				dateCount++;
				System.out.print(StockUtil.formatDate(reverseUpDate));
				int start = Math.max(0, index - 15);
//				int end = Math.min(stockCandleArray.size() - 1, index + 10);
				int end = index;
				boolean localMaxFound = false;
				int localMaxIndex = 0;
				for (int j = start; j <= end; j++) {
					if (stockCandleArray.isLocalMax(j, 10, StockCandleDataType.OPENCLOSEMAX)) {
						localMaxFound = true;
						localMaxIndex = j;
						break;
					}					
				}
				if (localMaxFound) {
					System.out.print(" (Local max found on " + StockUtil.formatDate(stockCandleArray.getDate(localMaxIndex)) + ")");
					if (stockCandleArray.getClose(index) > stockCandleArray.getClose(localMaxIndex)) {
						System.out.print(" (Close > local max close)");
					}
				}
				System.out.println();
				
//				System.out.println(dateFormat.format(stockCandleArray.getDate(index)));
//				double close = stockCandleArray.getClose(index);
//				double maxProfit = stockCandleArray.getMaxProfit(index, 20);
//				double maxLoss = stockCandleArray.getMaxLoss(index, 20);
//				if (Math.abs(maxLoss) > Math.abs(maxProfit)) {
//					System.out.println(StockUtil.formatDate(reverseUpDate) + " " + symbol + " " + StockUtil.getRoundTwoDecimals(maxProfit) + " " + StockUtil.getRoundTwoDecimals(maxLoss));
//				}
			}
			
		}
		
	}
	
}

