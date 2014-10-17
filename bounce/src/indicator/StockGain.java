package indicator;

import stock.StockCandleArray;

public class StockGain {
	
	/**
	 * NOT USED.
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
	
	/**
	 * NOT USED.
	 * @param stockCandleArray
	 * @param index
	 * @param period
	 * @return
	 */
	public static double getLongGainEMA(StockCandleArray stockCandleArray, int index, int period) {
		return getGainEMA(stockCandleArray, index, period);
	}
	
	/**
	 * NOT USED.
	 * @param stockCandleArray
	 * @param index
	 * @param period
	 * @return
	 */
	public static double getShortGainEMA(StockCandleArray stockCandleArray, int index, int period) {
		return -getGainEMA(stockCandleArray, index, period);
	}
	
	/**
	 * Calculate percentage stock gains, assuming stock is bought at the next day's opening price.
	 * @param stockCandleArray
	 * @param period
	 * @return array of stock gains, with the last <period> entries being 0.
	 */
	public static double[] getStockGain(StockCandleArray stockCandleArray, int period) {
		double[] stockGains = new double[stockCandleArray.size()];
		if (stockCandleArray.size() <= period) return stockGains;
		
		//Right now we are using the exponential moving average coefficients to calculate the expected gain value.
		//May need to adjust the coefficient in the future. 
		double[] coef = StockGain.getExponentialMovingAverageCoefficient(period);
		
		//Calculate gains
		for (int i = 0; i < stockCandleArray.size() - period; i++) {
			for (int j = 0; j < period; j++) {
				stockGains[i] += stockCandleArray.getClose(i + j + 1) * coef[j];
			}	
			//convert to percentage gain. Notice that we start with the open price of the next day.
			stockGains[i] = (stockGains[i] / stockCandleArray.getOpen(i + 1) - 1) * 100.0; 
		}

		return stockGains;
	}
	
	/**
	 * Get an array of exponential moving average coefficients.
	 * This can be used by the gain evaluation function and the coefficients can be adjusted in the future.
	 * @param period
	 * @return
	 */
	public static double[] getExponentialMovingAverageCoefficient(int period) {
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
}
