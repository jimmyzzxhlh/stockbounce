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
		if (ema.length <= 0) return null;
		ema[0] = stockCandleArray.getClose(0);		
		for (int i = 1; i < stockCandleArray.size(); i++) {
			ema[i] = stockCandleArray.getClose(i) * emaPercent + ema[i - 1] * oneMinusEMAPercent;
		}		
		return ema;
	}
	
	public static double[] getExponentialMovingAverage(double[] inputArray, int period) {
		double[] ema = new double[inputArray.length];
		double emaPercent = 2.0 / (period + 1);
		double oneMinusEMAPercent = 1 - emaPercent;
		if (ema.length <= 0) return null;
		ema[0] = inputArray[0];
		for (int i = 1; i < inputArray.length; i++) {
			ema[i] = inputArray[i] * emaPercent + ema[i - 1] * oneMinusEMAPercent;
		}		
		return ema;
	}
	
	/**
	 * See the following definition for RSI.
	 * http://stackoverflow.com/questions/22195412/calculate-macd-and-rsi-in-grails
	 * @param stockCandleArray
	 * @param period
	 * @return
	 */
	public static double[] getRSI(StockCandleArray stockCandleArray, int period) {
		double[] rsi = new double[stockCandleArray.size()];
		if (stockCandleArray.size() <= period) return rsi;
		double upAverage = 0;
		double downAverage = 0;
		
		//Initialize the average.
		for (int i = 1; i <= period; i++) {
			double closeDiff = stockCandleArray.getClose(i) - stockCandleArray.getClose(i - 1);
			if (closeDiff > 0) {
				upAverage += closeDiff;
			}
			else {
				downAverage += (-closeDiff);
			}
		}
		
		upAverage = upAverage * 1.0 / period;
		downAverage = downAverage * 1.0 / period;
		
		rsi[period] = 100 - 100.0 / (1 + upAverage / downAverage);
		
		for (int i = period + 1; i < stockCandleArray.size(); i++) {
			double closeDiff = stockCandleArray.getClose(i) - stockCandleArray.getClose(i - 1);
			double upIncrement = 0;
			double downIncrement = 0;
			if (closeDiff > 0) {
				upIncrement = closeDiff;
				downIncrement = 0;
			}
			else {
				upIncrement = 0;
				downIncrement = -closeDiff;
			}
			upAverage = (upAverage * (period - 1) + upIncrement) / (period * 1.0);
			downAverage = (downAverage * (period - 1) + downIncrement) / (period * 1.0);
			rsi[i] = 100 - 100.0 / (1 + upAverage / downAverage);
		}
		return rsi;
	}
	
	

}
