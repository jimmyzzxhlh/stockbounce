package test;

import stock.StockCandle;
import stock.StockCandleArray;
import util.StockIndicator;

public class IndicatorTest {
	
	public static void main(String args[]) {
		testSimpleMovingAverageFakeData();
	}
	
	private static void testSimpleMovingAverageFakeData() {
		StockCandleArray stockCandleArray = new StockCandleArray();
		for (int i = 0; i < 100; i++) {
			StockCandle stockCandle = new StockCandle();
			stockCandle.close = i;
			stockCandleArray.add(stockCandle);
		}
		double[] movingAverage = StockIndicator.getSimpleMovingAverage(stockCandleArray, 10);
		for (int i = 0; i < 100; i++) {
			System.out.println(i + ": " + movingAverage[i]);
		}
	}
	
	private static void testSimpleMovingAverageRealData() {
		
	}
}
