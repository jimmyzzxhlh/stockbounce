package indicator;

import stock.CandleList;

/**
 * Static class for computing indicators. 
 * Notice that:
 * 1. Unless specified otherwise, the first period - 1 data points will not have indicator value computed,
 * because we cannot do that (for obvious reasons).
 * 2. For each day, we know the stock price of the current day and then we compute the indicator, so
 * this means when using the indicator, we need to evaluate the next day instead of current day!
 * 3. Every indicator API here needs to return a value within 0 to 100 (in rare cases if the maximum value is greater
 * than 100, such as %b from bollinger band, that might be fine, but need further verification).
 * 4. For values that cannot be computed, especially the first few days, we will return the NAN value defined
 * in StockIndicatorConst. Any feature vector with NAN value should not be trained or tested.
 * 
 */
public class StockIndicatorAPI {
	
	/**
	 * Get simple moving average from a stock candle array.
	 * @param stockCandleList
	 * @param period
	 * @return An array that lists simple moving average of each data point. 
	 */
	public static double[] getSimpleMovingAverage(CandleList stockCandleList, int period) {
		double[] movingAverage = new double[stockCandleList.size()];
		double sum = 0;
		for (int i = 0; i < stockCandleList.size(); i++) {
			sum += stockCandleList.getClose(i);
			if (i < period - 1) {
				movingAverage[i] = StockIndicatorConst.NAN;
				continue;
			}
			if (i >= period) sum -= stockCandleList.getClose(i - period);
			movingAverage[i] = sum / (period * 1.0);				
		}
		return movingAverage;	
	}
	
	/**
	 * See the following definition for exponential moving average.
	 * http://www.incrediblecharts.com/indicators/exponential_moving_average.php 
	 * @param stockCandleList
	 * @param period
	 * @return An array that lists exponential moving average of each data point. 
	 */
	public static double[] getExponentialMovingAverage(CandleList stockCandleList, int period) {
		double[] ema = new double[stockCandleList.size()];
		
		//Why is this 2 / (period + 1)? Because if you think about the last data in the array,
		//if it needs to have twice the weight as the weight for the other data, then the total weight
		//will be period + 1 and the weight for the last data is 2.
		double emaPercent = 2.0 / (period + 1);
		double oneMinusEMAPercent = 1 - emaPercent;
		if (ema.length <= 0) return null;
		ema[0] = stockCandleList.getClose(0);		
		for (int i = 1; i < stockCandleList.size(); i++) {
			ema[i] = stockCandleList.getClose(i) * emaPercent + ema[i - 1] * oneMinusEMAPercent;
		}		
		return ema;
	}
	
	/**
	 * See the following definition for exponential moving average.
	 * http://www.incrediblecharts.com/indicators/exponential_moving_average.php 
	 * @param inputArray
	 * @param period
	 * @return An array that lists exponential moving average of each data point. 
	 */
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
	 * @param stockCandleList
	 * @param period
	 * @return An array that lists RSI of each data point. 
	 */
	public static double[] getRSI(CandleList stockCandleList, int period) {
		double[] rsi = new double[stockCandleList.size()];
		if (stockCandleList.size() <= period) {
			for (int i = 0; i < stockCandleList.size(); i++) {
				rsi[i] = StockIndicatorConst.NAN;
			}
			return rsi;		
		}
		double upAverage = 0;
		double downAverage = 0;
		
		//Initialize the first period days to NAN
		for (int i = 0; i < period; i++) {
			rsi[i] = StockIndicatorConst.NAN;
		}
		
		//Initialize the average.
		for (int i = 1; i <= period; i++) {
			double closeDiff = stockCandleList.getClose(i) - stockCandleList.getClose(i - 1);
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
		
		for (int i = period + 1; i < stockCandleList.size(); i++) {
			double closeDiff = stockCandleList.getClose(i) - stockCandleList.getClose(i - 1);
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
	
	/**
	 * Given data point i, compute the standard deviation of the close price from i - period + 1 to i.
	 * @param stockCandleList
	 * @param period
	 * @return An array that lists the standard deviation of each data point. 
	 */
	public static double[] getStandardDeviation(CandleList stockCandleList, int period) {
		double[] sdArray = new double[stockCandleList.size()];
		if (stockCandleList.size() < period) {
			for (int i = 0; i < stockCandleList.size(); i++) {
				sdArray[i] = StockIndicatorConst.NAN;
			}
			return sdArray;
		}
		
		for (int i = 0; i < period - 1; i++) {
			sdArray[i] = StockIndicatorConst.NAN;
		}
		
		for (int i = period - 1; i < stockCandleList.size(); i++) {
			double average = 0;
			int start = i - period + 1;
			int end = i;
			for (int j = start; j <= end; j++) {
				average += stockCandleList.getClose(j);
			}
			average /= (period * 1.0);
			double sd = 0;
			for (int j = start; j <= end; j++) {
				sd += (stockCandleList.getClose(j) - average) * (stockCandleList.getClose(j) - average); 
			}
			sd = Math.sqrt(sd / period);
			sdArray[i] = sd;
		}
		return sdArray;
	}
	
	/**
	 * See the following wiki for the definition of bollinger bands.
	 * http://zh.wikipedia.org/wiki/%E5%B8%83%E6%9E%97%E5%B8%A6
	 * @param stockCandleList
	 * @param period
	 * @param k By default k is set to 2. According to normal distribution, about 95% of the data will fall into the range of 
	 *          average +- 2 * standard deviation. 
	 * @return bollingerBands[0] = upperBB : Middle BB + k * (standard deviation within the period)
	 *         bollingerBands[1] = middleBB : Simple moving average within the period. 
	 *         bollingerBands[2] = lowerBB : Middle BB - k * (standard deviation within the period)
	 */
	public static double[][] getBollingerBands(CandleList stockCandleList, int period, int k) {
		double[][] bollingerBands = new double[3][stockCandleList.size()];
		if (stockCandleList.size() < period) {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < stockCandleList.size(); j++) {
					bollingerBands[i][j] = StockIndicatorConst.NAN;
				}
			}
			return bollingerBands;
		}
		
		double[] movingAverage = getSimpleMovingAverage(stockCandleList, period);
		double[] standardDeviation = getStandardDeviation(stockCandleList, period);
		for (int i = 0; i < period - 1; i++) {
			bollingerBands[0][i] = bollingerBands[1][i] = bollingerBands[2][i] = StockIndicatorConst.NAN;
		}
		for (int i = period - 1; i < stockCandleList.size(); i++) {
			bollingerBands[1][i] = movingAverage[i];                                 //middle
			bollingerBands[0][i] = bollingerBands[1][i] + k * standardDeviation[i];  //upper
			bollingerBands[2][i] = bollingerBands[1][i] - k * standardDeviation[i];  //lower
		}
		return bollingerBands;
	}
	
	/**
	 * Return the %b indicator from the bollinger bands. The return value is normalized for percentage (i.e. * 100).
	 * %b = (Close Price - Lower BB) / (Upper BB - Lower BB)
	 * Range of the value: Can be either positive or negative, but most of the time it is around -100 to 100.
	 * If stock price goes up and close price is greater than the upper BB, then %b > 1. If stock price goes down
	 * and close price is less than the lower BB, then %b < 0. There is no restricted range for %b.
	 * See the following wiki for the definition of bollinger bands.
	 * http://zh.wikipedia.org/wiki/%E5%B8%83%E6%9E%97%E5%B8%A6
	 * @param stockCandleList
	 * @param period
	 * @param k
	 * @return An array that lists the %b indicator.
	 */
	public static double[] getBollingerBandsPercentB(CandleList stockCandleList, int period, int k) {
		double[] percentB = new double[stockCandleList.size()];
		if (stockCandleList.size() < period) {
			for (int i = 0; i < stockCandleList.size(); i++) {
				percentB[i] = StockIndicatorConst.NAN;
			}
			return percentB;
		}
		
		for (int i = 0; i < period - 1; i++) {
			percentB[i] = StockIndicatorConst.NAN;
		}
		double[][] bollingerBands = getBollingerBands(stockCandleList, period, k);
		for (int i = period - 1; i < stockCandleList.size(); i++) {
			percentB[i] = (stockCandleList.getClose(i) - bollingerBands[2][i]) * 100.0 / (bollingerBands[0][i] - bollingerBands[2][i]);
		}
		return percentB;
	}
	
	/**
	 * Return the bandwidth from the bollinger bands. The return value is normalized for percentage (i.e. * 100)
	 * Range of the value: >= 0, it is possible that the percentage can be >= 200, but rare.
	 * Bandwidth = (Upper BB - Lower BB) / Middle BB
	 * See the following wiki for the definition of bollinger bands.
	 * http://zh.wikipedia.org/wiki/%E5%B8%83%E6%9E%97%E5%B8%A6
	 * @param stockCandleList
	 * @param period
	 * @param k
	 * @return An array that lists the bandwidth indicator.
	 */
	public static double[] getBollingerBandsBandwidth(CandleList stockCandleList, int period, int k) {
		double[] bandwidth = new double[stockCandleList.size()];
		if (stockCandleList.size() < period) {
			for (int i = 0; i < stockCandleList.size(); i++) {
				bandwidth[i] = StockIndicatorConst.NAN;
			}
			return bandwidth;
		}
		
		for (int i = 0; i < period - 1; i++) {
			bandwidth[i] = StockIndicatorConst.NAN;
		}
		double[][] bollingerBands = getBollingerBands(stockCandleList, period, k);
		for (int i = period - 1; i < stockCandleList.size(); i++) {
			bandwidth[i] = (bollingerBands[0][i] - bollingerBands[2][i]) * 100.0 / bollingerBands[1][i];
		}
		return bandwidth;
	}
	
	/**
	 * See the following wiki for the definition of MACD.
	 * http://en.wikipedia.org/wiki/MACD
	 * For trading, when the divergence turns from negative to positive or positive to negative, it may be
	 * indicating a new trend has begun.
	 * @param stockCandleList
	 * @param shortPeriod By default it is 12 days (2 weeks in the past that Saturday was still a workday)
	 * @param longPeriod By default it is 26 days (1 month in the past)
	 * @param macdAveragePeriod By default it is 9 days
	 * @return An array that lists the MACD components.
	 *   macd[0][i] (MACD)       = EMA of short period (12 days) - EMA of long period (26 days)
	 *   macd[1][i] (signal)     = macd[0]'s EMA (9 days)
	 *   macd[2][i] (divergence) = (MACD - signal)
	 * 
	 */
	public static double[][] getMACD(CandleList stockCandleList, int shortPeriod, int longPeriod, int macdAveragePeriod) {
		double[] emaShort = getExponentialMovingAverage(stockCandleList, shortPeriod);
		double[] emaLong = getExponentialMovingAverage(stockCandleList, longPeriod);
		double[] emaDiff = new double[stockCandleList.size()];
		for (int i = 0; i < stockCandleList.size(); i++) {
			emaDiff[i] = emaShort[i] - emaLong[i];
		}
		
		double[] macdSignal = getExponentialMovingAverage(emaDiff, macdAveragePeriod);
		
		double[][] macd = new double[3][stockCandleList.size()];
		for (int i = 0; i < stockCandleList.size(); i++) {
			macd[0][i] = emaDiff[i];
			macd[1][i] = macdSignal[i];
			macd[2][i] = emaDiff[i] - macdSignal[i];
		}
		
		return macd;		
	}
	
	public static double[] getMACDNormalized(CandleList stockCandleList, int shortPeriod, int longPeriod, int macdAveragePeriod) {
//		double[] emaLong = getExponentialMovingAverage(stockCandleList, longPeriod);
		double[][] macd = getMACD(stockCandleList, shortPeriod, longPeriod, macdAveragePeriod);
		//The min/max period is temporarily set as long period. This can be adjusted if needed.
		double[] macdMin = getMin(macd[2], longPeriod);
		double[] macdMax = getMax(macd[2], longPeriod);
		double[] macdNormalized = new double[stockCandleList.size()];
		
		for (int i = 0; i < stockCandleList.size(); i++) {
			if (macdMax[i] != macdMin[i]) {
				macdNormalized[i] = (macd[2][i] - macdMin[i]) * 100.0 / (macdMax[i] - macdMin[i]);
			}
		}
		return macdNormalized;
		
	}
	
	/**
	 * Return minimum value of an array within a period.
	 * @param inputArray
	 * @param period
	 * @return
	 */
	public static double[] getMin(double[] inputArray, int period) {
		double[] min = new double[inputArray.length];
		for (int i = 0; i < min.length; i++) {
			min[i] = 1e10;
		}
		for (int i = 0; i < inputArray.length; i++) {
			int start = i - period + 1;
			if (start < 0) start = 0;
			for (int j = start; j <= i; j++) {
				if (min[i] > inputArray[j]) min[i] = inputArray[j];
			}
		}
		return min;
	}
	
	public static double[] getMax(double[] inputArray, int period) {
		double[] max = new double[inputArray.length];
		for (int i = 0; i < max.length; i++) {
			max[i] = -1e10;
		}
		for (int i = 0; i < inputArray.length; i++) {
			int start = i - period + 1;
			if (start < 0) start = 0;
			for (int j = start; j <= i; j++) {
				if (max[i] < inputArray[j]) max[i] = inputArray[j];
			}
		}
		return max;
	}

	/**
	 * Return the distance in percentage from the current close price to the EMA line. 
	 * @param stockCandleList
	 * @param period
	 * @return
	 */
	public static double[] getExponentialMovingAverageDistance(CandleList stockCandleList, int period) {
		double[] ema = getExponentialMovingAverage(stockCandleList, period);
		double[] emaDistance = new double[stockCandleList.size()];
		for (int i = 0; i < stockCandleList.size(); i++) {
			emaDistance[i] = (stockCandleList.getClose(i) - ema[i]) * 100.0 / ema[i]; 
		}
		return emaDistance;		
	}
	
	/**
	 * Return an array of volume from stock candle array.
	 * @param stockCandleList
	 * @return
	 */
	public static long[] getVolume(CandleList stockCandleList) {
		long[] volume = new long[stockCandleList.size()];
		for (int i = 0; i < stockCandleList.size(); i++) {
			volume[i] = stockCandleList.getVolume(i);
		}
		return volume;
	}

}
