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
	
	public static double[] getStandardDeviation(StockCandleArray stockCandleArray, int period) {
		double[] sdArray = new double[stockCandleArray.size()];
		for (int i = period - 1; i < stockCandleArray.size(); i++) {
			double average = 0;
			int start = i - period + 1;
			int end = i;
			for (int j = start; j <= end; j++) {
				average += stockCandleArray.getClose(j);
			}
			average /= (period * 1.0);
			double sd = 0;
			for (int j = start; j <= end; j++) {
				sd += (stockCandleArray.getClose(j) - average) * (stockCandleArray.getClose(j) - average); 
			}
			sd = Math.sqrt(sd / period);
			sdArray[i] = sd;
		}
		return sdArray;
	}
	/**
	 * http://zh.wikipedia.org/wiki/%E5%B8%83%E6%9E%97%E5%B8%A6
	 * @param stockCandleArray
	 * @param period
	 * @param k
	 * @return bollingerBands[0] = upperBB
	 *         bollingerBands[1] = middleBB
	 *         bollingerBands[2] = lowerBB
	 */
	public static double[][] getBollingerBands(StockCandleArray stockCandleArray, int period, int k) {
		double[] movingAverage = getSimpleMovingAverage(stockCandleArray, period);
		double[] standardDeviation = getStandardDeviation(stockCandleArray, period);
		double[][] bollingerBands = new double[3][stockCandleArray.size()];
		for (int i = period - 1; i < stockCandleArray.size(); i++) {
			bollingerBands[1][i] = movingAverage[i];                                 //middle
			bollingerBands[0][i] = bollingerBands[1][i] + k * standardDeviation[i];  //upper
			bollingerBands[2][i] = bollingerBands[1][i] - k * standardDeviation[i];  //lower
		}
		return bollingerBands;
	}
	
	public static double[] getPercentBFromBollingerBands(StockCandleArray stockCandleArray, int period, int k) {
		double[][] bollingerBands = getBollingerBands(stockCandleArray, period, k);
		double[] percentB = new double[stockCandleArray.size()];
		for (int i = period - 1; i < stockCandleArray.size(); i++) {
			percentB[i] = (stockCandleArray.getClose(i) - bollingerBands[2][i]) / (bollingerBands[0][i] - bollingerBands[2][i]);
		}
		return percentB;
	}
	
	public static double[] getBandwidthFromBollingerBands(StockCandleArray stockCandleArray, int period, int k) {
		double[][] bollingerBands = getBollingerBands(stockCandleArray, period, k);
		double[] bandwidth = new double[stockCandleArray.size()];
		for (int i = period - 1; i < stockCandleArray.size(); i++) {
			bandwidth[i] = (bollingerBands[0][i] - bollingerBands[2][i]) / bollingerBands[1][i];
		}
		return bandwidth;
	}
	
	/**
	 * http://en.wikipedia.org/wiki/MACD
	 * @param stockCandleArray
	 * @param shortPeriod
	 * @param longPeriod
	 * @param macdAveragePeriod
	 * @return
	 *   macd[0][i] (MACD)       = EMA of short period (12 days) - EMA of long period (26 days)
	 *   macd[1][i] (signal)     = macd[0]'s EMA (9 days)
	 *   macd[2][i] (divergence) = (MACD - signal)
	 */
	public static double[][] getMACD(StockCandleArray stockCandleArray, int shortPeriod, int longPeriod, int macdAveragePeriod) {
		double[] emaShort = getExponentialMovingAverage(stockCandleArray, shortPeriod);
		double[] emaLong = getExponentialMovingAverage(stockCandleArray, longPeriod);
		double[] emaDiff = new double[stockCandleArray.size()];
		for (int i = 0; i < stockCandleArray.size(); i++) {
			emaDiff[i] = emaShort[i] - emaLong[i];
		}
		
		double[] macdSignal = getExponentialMovingAverage(emaDiff, macdAveragePeriod);
		
		double[][] macd = new double[3][stockCandleArray.size()];
		for (int i = 0; i < stockCandleArray.size(); i++) {
			macd[0][i] = emaDiff[i];
			macd[1][i] = macdSignal[i];
			macd[2][i] = emaDiff[i] - macdSignal[i];
		}
		
		return macd;
		
	}

}