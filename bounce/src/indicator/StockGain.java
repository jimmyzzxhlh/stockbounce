package indicator;

import stock.StockCandleArray;

public class StockGain {
	
	/**
	 * Get exponential moving average of the gain at the end of a certain period amount of time.
	 * The gain is represented by percentage.
	 * This can be used as an evaluation function of the gain in our machine learning system.
	 * We assume that we will enter a long position at the open price of the start day.
	 * If we need the gain of the short position, then we just need to return the negative number
	 * of the gain for the long position.
	 * @param stockCandleArray
	 * @param index Start day 
	 * @param period Number of days evaluated from the start day (including the start day).
	 * @return
	 */
	private static double getGainEMA(StockCandleArray stockCandleArray, int index, int period) {
//		if (period < 1) return 0;
//		double[] gainCoefficient = new double[period];
//		for (int i = index; i < index + period; i++) {
//			gain[i - index] = (stockCandleArray.getClose(i) - open) / open;
//		}
//		double[] gainEMA = StockIndicator.getExponentialMovingAverage(gain, period);
//		return gainEMA[period - 1];
		return 0;
	}
	
	public static double getLongGainEMA(StockCandleArray stockCandleArray, int index, int period) {
		return getGainEMA(stockCandleArray, index, period);
	}
	
	public static double getShortGainEMA(StockCandleArray stockCandleArray, int index, int period) {
		return -getGainEMA(stockCandleArray, index, period);
	}
}
