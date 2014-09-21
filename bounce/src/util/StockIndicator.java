package util;

import java.util.ArrayList;

import stock.StockCandle;
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
			else {
				if (i >= period) sum -= stockCandleArray.getClose(i - period);
				movingAverage[i] = sum / (period * 1.0);				
			}
		}
		return movingAverage;		
		
	}
	
	

}
