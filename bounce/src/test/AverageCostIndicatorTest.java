package test;

import indicator.StockAverageCostIndicator;
import stock.StockCandleArray;
import stock.StockConst;

public class AverageCostIndicatorTest {
	private static final String filename = StockConst.STOCK_CSV_DIRECTORY_PATH + "JD.csv";
	
	public static void main(String args[]) {
		testAverageCostIndicator();
	}
	
	public static void testAverageCostIndicator() {
		StockAverageCostIndicator indicator = new StockAverageCostIndicator(filename);
		StockCandleArray stockCandleArray = indicator.getStockCandleArray();
		for (int i = 0; i < indicator.size(); i++) {
			System.out.println(stockCandleArray.getDate(i) + " " + indicator.getAverageCost(i) / 100);
		}
	}
}
