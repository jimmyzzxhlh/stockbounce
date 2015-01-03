package test;

import indicator.StockAverageCostIndicator;

import java.text.SimpleDateFormat;

import stock.StockCandleArray;
import util.StockUtil;

public class AverageCostIndicatorTest {
	private static final String symbol = "JD";
	
	public static void main(String args[]) {
		testAverageCostIndicator();
	}
	
	public static void testAverageCostIndicator() {
		StockAverageCostIndicator indicator = new StockAverageCostIndicator(symbol);
		StockCandleArray stockCandleArray = indicator.getStockCandleArray();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		for (int i = 0; i < indicator.size(); i++) {
			double close = stockCandleArray.getClose(i);
			double averageCost = indicator.getAverageCost(i) / 100;
			double difference = close - averageCost; 
			System.out.println(dateFormat.format(stockCandleArray.getDate(i)) + " " + StockUtil.getRoundTwoDecimals(close) +
					" " + StockUtil.getRoundTwoDecimals(averageCost) + " " + StockUtil.getRoundTwoDecimals(difference));
		}
	}
}
