package util;

import stock.StockCandleArray;

/**
 * Static class for computing indicators. 
 *
 */
public class StockIndicator {
	public static double[] getSimpleMovingAverage(StockCandleArray stockCandleArray, int period) {
		double[] movingAverage = new double[stockCandleArray.size()];
		double sum = 0;
		for (int i = 0; i < stockCandleArray.size(); i++) {
			sum += stockCandleArray.getClose(i);
			if (i < period - 1) continue;
			if (i >= period) sum -= stockCandleArray.getClose(i - period);
			movingAverage[i] = sum / (period * 1.0);				
		}
		return movingAverage;	
	}
	
	/**
	 * See the following definition for exponential moving average.
	 * http://www.incrediblecharts.com/indicators/exponential_moving_average.php 
	 * @param stockCandleArray
	 * @param period
	 * @return
	 */
	public static double[] getExponentialMovingAverage(StockCandleArray stockCandleArray, int period) {
		double[] ema = new double[stockCandleArray.size()];
		double emaPercent = 2.0 / (period + 1);
		double oneMinusEMAPercent = 1 - emaPercent;
		if (ema.length <= 0) return ema;
		ema[0] = stockCandleArray.getClose(0);		
		for (int i = 1; i < stockCandleArray.size(); i++) {
			ema[i] = stockCandleArray.getClose(i) * emaPercent + ema[i - 1] * oneMinusEMAPercent;
		}		
		return ema;
	}
	
	public static double[] getRSI(StockCandleArray stockCandleArray, int period) {
		double[] rsi = new double[stockCandleArray.size()];
		double sumUp = 0;
		double sumDown = 0;
		for (int i = 1; i < stockCandleArray.size(); i++) {
			double closeNow = stockCandleArray.getClose(i);
			double closePrevious = stockCandleArray.getClose(i - 1);
			double closeDiff = Math.abs(closeNow - closePrevious);
			if (closeNow > closePrevious) { 
				sumUp += closeDiff;			
			}
			else { 
				sumDown += closeDiff;
			}
			if (i < period - 1) continue;
			if (i >= period) {
				closeNow = stockCandleArray.getClose(i - period);
				closePrevious = stockCandleArray.getClose(i - period);
				closeDiff = Math.abs(closeNow - closePrevious);
				if (closeNow > closePrevious) {
					sumUp -= closeDiff;
				}
				else {
					sumDown -= closeDiff;
				}
			}
			if (sumDown == 0) rsi[i] = 100;
			else rsi[i] = 100 - 100 / (1 + sumUp / sumDown * 1.0);
		}
		return rsi;
	}
	
	

}
