package test;

import indicator.StockAverageCostIndicator;
import stock.StockCandleArray;
import stock.StockConst;

public class AverageCostIndicatorTest {
	private static final String symbol = "YHOO";
	
	public static void main(String args[]) {
		testAverageCostIndicator();
	}
	
	public static void testAverageCostIndicator() {
		StockAverageCostIndicator indicator = new StockAverageCostIndicator(symbol);
		StockCandleArray stockCandleArray = indicator.getStockCandleArray();
		for (int i = 0; i < indicator.size(); i++) {
			System.out.println(stockCandleArray.getDate(i) + " " + indicator.getAverageCost(i) / 100);
		}
	}
}
