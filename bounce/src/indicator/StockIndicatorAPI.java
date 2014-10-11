package indicator;

import stock.StockCandleArray;

/**
 * Static class for computing indicators. 
 *
 */
public class StockIndicatorAPI {
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
		
		//Why is this 2 / (period + 1)? Because if you think about the last data in the array,
		//if it needs to have twice the weight as the weight for the other data, then the total weight
		//will be period + 1 and the weight for the last data is 2.
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
	 * Get an array of exponential moving average coefficients.
	 * This can be used by the gain evaluation function and the coefficients can be adjusted in the future.
	 * @param period
	 * @return
	 */
	public static double[] getExponentiaoMovingAverageCoefficient(int period) {
		if (period < 1) return null; 
		double[] emaCoefficient = new double[period + 1];
		double emaPercent = 2.0 / (period + 1);
		double[] emaOneMinusPercentExp = new double[period + 1];
		double oneMinusEMAPercent = 1 - emaPercent;
		
		//Compute the exponential of 1 - emaPercent
		emaOneMinusPercentExp[0] = 1;
		for (int i = 1; i <= period; i++) {
			emaOneMinusPercentExp[i] = emaOneMinusPercentExp[i - 1] * oneMinusEMAPercent;
		}
		
		//The first coefficient can be really large.
		//We are building a virtual coefficient at the beginning and then distribute it into the later coefficients.
		//P[0] = (1 - alpha)^N
		//P[1] = (1 - alpha)^(N - 1) * alpha
		//-> P[i] = (1 - alpha)^(N - i) * alpha (N > 1, i=1..N)
		//P[N] = alpha
		//P[0] + P[1] + ... + P[N] = 1
		//Distribute the coefficient P[0] based on scale so we will get
		//P[i]' = P[0] * {P[i] / [1 - (1 - alpha)^N]} + P[i]
		//      = P[i] / [(1 - (1 - alpha)^N]
		//Here alpha = 2 / (period + 1)
		for (int j = 0; j < period; j++) {
			int i = j + 1;
			emaCoefficient[j] = (emaOneMinusPercentExp[period - i] * emaPercent) / (1 - emaOneMinusPercentExp[period]);			
		}
		return emaCoefficient;
		
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
