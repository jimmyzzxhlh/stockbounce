package test;

import indicator.StockAverageCostIndicator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

import stock.StockAPI;
import stock.StockCandleArray;
import stock.StockEnum.StockCandleDataType;
import util.StockUtil;

public class AverageCostIndicatorTest {
	private static final String TEST_SYMBOL = "CAMT";
	
	public static void main(String args[]) throws Exception {
		testAverageCostIndicator();
//		testAverageCostIndicatorReverseUp();
	}
	
	public static void testAverageCostIndicator() throws Exception {
		StockAverageCostIndicator indicator = new StockAverageCostIndicator(TEST_SYMBOL);
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
	
	public static void testAverageCostIndicatorReverseUp() throws Exception {
		ArrayList<String> symbolList = StockAPI.getUSSymbolList();
		int dateCount = 0;
		for (String symbol : symbolList) {
			if (!symbol.equals(TEST_SYMBOL)) continue;
			System.out.println(symbol);
			StockAverageCostIndicator indicator = new StockAverageCostIndicator(symbol);
			ArrayList<Integer> reverseUpDatesIndex = indicator.getReverseUpDatesIndex();
			StockCandleArray stockCandleArray = indicator.getStockCandleArray();
			for (int index : reverseUpDatesIndex) {
				Date reverseUpDate = stockCandleArray.getDate(index);
				dateCount++;
//				System.out.println(StockUtil.formatDate(reverseUpDate));
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
				//If local max is found and the current close is not higher than the local max,
				//then it means we have not broken through yet. So this is a false signal.
				if (localMaxFound && (stockCandleArray.getClose(index) <= stockCandleArray.getClose(localMaxIndex))) continue;
				int holdPeriod = 20;
				//Boundary check
				if (index + holdPeriod + 1 >= stockCandleArray.size()) continue;
				double openPrice = stockCandleArray.getOpen(index + 1);
				double minClose = Double.MAX_VALUE;
				double maxClose = Double.MIN_VALUE;
				int minCloseIndex = -1;
				int maxCloseIndex = -1;
				for (int j = index + 1; j <= index + 1 + holdPeriod; j++) {
					double close = stockCandleArray.getClose(j);
					if (close < minClose) {
						minCloseIndex = j;
						minClose = close;
					}
					if (close > maxClose) {
						maxCloseIndex = j;
						maxClose = close;
					}
				}
				//Use the following criteria if you only want to analyze losses and ignore profits
//				if (Math.abs(maxClose - openPrice) > Math.abs(openPrice - minClose)) continue; 
				System.out.print("Open position on " + StockUtil.formatDate(stockCandleArray.getDate(index + 1)) + " at " + StockUtil.getRoundTwoDecimals(openPrice) + ". ");
				if (minClose >= openPrice) {
					System.out.print("No loss in " + holdPeriod + " days. ");
					System.out.print("Max profit on " + StockUtil.formatDate(stockCandleArray.getDate(maxCloseIndex)) + " at " + StockUtil.getRoundTwoDecimals(maxClose) + ". ");
					System.out.println("Amount: " + StockUtil.getRoundTwoDecimals(maxClose - openPrice) + ". Per: " + StockUtil.getRoundTwoDecimals((maxClose - openPrice) * 100.0 / openPrice) + "%");
				}
				else {
					System.out.print("Max loss on " + StockUtil.formatDate(stockCandleArray.getDate(minCloseIndex)) + " at " + StockUtil.getRoundTwoDecimals(minClose) + ". ");
					System.out.print("Amount: " + StockUtil.getRoundTwoDecimals(minClose - openPrice) + ". Per: " + StockUtil.getRoundTwoDecimals((minClose - openPrice) * 100.0 / openPrice) + "%. ");
					System.out.print("Max profit on " + StockUtil.formatDate(stockCandleArray.getDate(maxCloseIndex)) + " at " + StockUtil.getRoundTwoDecimals(maxClose) + ". ");
					System.out.println("Amount: " + StockUtil.getRoundTwoDecimals(maxClose - openPrice) + ". Per: " + StockUtil.getRoundTwoDecimals((maxClose - openPrice) * 100.0 / openPrice) + "%");
				}
				
//				if (localMaxFound) {
//					System.out.print(" (Local max found on " + StockUtil.formatDate(stockCandleArray.getDate(localMaxIndex)) + ")");
//					if (stockCandleArray.getClose(index) > stockCandleArray.getClose(localMaxIndex)) {
//						System.out.print(" (Close > local max close)");
//					}
//				}
//				System.out.println();
				
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

