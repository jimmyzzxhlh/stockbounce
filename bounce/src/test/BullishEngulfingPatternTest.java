package test;

import pattern.StockPattern;

public class BullishEngulfingPatternTest extends PatternTest {
	
	public static void main(String args[]) throws Exception {
		BullishEngulfingPatternTest test = new BullishEngulfingPatternTest();
		test.testChart();
	}

	@Override
	public boolean hasPattern(StockPattern stockPattern, int index) {
		return stockPattern.isBullishEngulfing(index);
	}
}
