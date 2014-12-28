package test;

import indicator.StockAverageCostIndicator;
import stock.StockConst;

public class AverageCostIndicatorTest {
	private static final String filename = StockConst.STOCK_CSV_DIRECTORY_PATH + "JD.csv";
	
	public static void main(String args[]) {
		testAverageCostIndicator();
	}
	
	public static void testAverageCostIndicator() {
		StockAverageCostIndicator indicator = new StockAverageCostIndicator(filename);
		for (int i = 0; i < indicator.size(); i++) {
			System.out.println(indicator.getAverageCost(i));
		}
	}
}
