package intraday;

import java.util.HashMap;

import stock.StockCandle;
import stock.StockEnum.StockCandleClass;

/**
 * Given a single stock candle with the following four prices and the daily volume:
 * Low, High, Open, Close.
 * Estimate the detailed price and the volume for the candle.
 * For example, if we have low = 98, High = 102, Open = 99, Close = 101, Volume = 10000
 * Then we can possibly have the following mapping:
 * (9800, 1500)
 * (9900, 2000)
 * (10000, 3000)
 * (10100, 2000)
 * (10200, 1500)
 * Notice that the price is multiplied by 100 and rounded.
 * To estimate the number of shares for a possible price, we do the following:
 * 1. Identify the candle class. For example, the candle can be a white long.
 * 2. Get the average interval where low and high appears. This is changed based on candle class.
 * 3. If the interval for low appears before the interval for high, then we assume that the price movement is
 * Open -> Low -> High -> Close
 * On the other hand, if the interval for high appears before the interval for low, then we assume that the price movement is
 * Open -> High -> Low -> Close
 * 4. Assume that the movement is linear between intervals, we can then get a price array for all the intervals.
 * 5. Dot product the price array with the volume distribution and create a mapping. 
 * @author Dongyue Xue
 *
 */
public class IntraDayPriceVolumeMap {
	
	public static HashMap<Integer, Long> getMap(StockCandle stockCandle) {
		HashMap<Integer, Long> map = new HashMap<Integer, Long>(); 
		StockCandleClass candleClass = stockCandle.getCandleClass();
		int lowInterval = IntraDayLowHighIntervalMap.getLowInterval(candleClass);
		int highInterval = IntraDayLowHighIntervalMap.getHighInterval(candleClass);
		int[] priceArray = new int[391];
		double open = stockCandle.getOpen() * 100;
		double close = stockCandle.getClose() * 100;
		double high = stockCandle.getHigh() * 100;
		double low = stockCandle.getLow() * 100;
		double volume = stockCandle.getVolume();
		for (int interval = 0; interval < priceArray.length; interval++) {
			//Open -> Low -> High -> Close
			if (lowInterval <= highInterval) {
				if (interval <= lowInterval) {
//					if (lowInterval == 0) {
//						priceArray[interval] = (int) Math.round(open);
//					}
//					else {
//						priceArray[interval] = (int) Math.round(open + (low - open) / lowInterval * interval) ;
//					}
					priceArray[interval] = (int) Math.round(open + (low - open) / lowInterval * interval) ;
					continue;
				}
				if (interval >= highInterval) {
//					if (highInterval == 390) {
//						priceArray[interval] = (int) Math.round(close);
//					}
//					else {
//						priceArray[interval] = (int) Math.round(high + (close - high) / (390 - highInterval) * (interval - highInterval));
//					}
					priceArray[interval] = (int) Math.round(high + (close - high) / (390 - highInterval) * (interval - highInterval));
					continue;
				}
				//otherwise, lowInterval < interval < highInterval
				priceArray[interval] = (int) Math.round(low + (high - low) / (highInterval - lowInterval) * (interval - lowInterval));
			}
			//Open -> High -> Low -> Close
			else { 
				if (interval <= highInterval) {
//					if (highInterval == 0) {
//						priceArray[interval] = (int) Math.round(open);
//					}
//					else {
//						priceArray[interval] = (int) Math.round(open + (high - open) / highInterval * interval);
//					}
					priceArray[interval] = (int) Math.round(open + (high - open) / highInterval * interval);
					continue;
				}
				if (interval >= lowInterval){
//					if (lowInterval == 390) {
//						priceArray[interval] = (int) Math.round(close);
//					}
//					else {
//						priceArray[interval] = (int) Math.round(low + (close - low) / (390 - lowInterval) * (interval - lowInterval));
//					}
					priceArray[interval] = (int) Math.round(low + (close - low) / (390 - lowInterval) * (interval - lowInterval));
					continue;
				}
				//otherwise, highInterval < interval < lowInterval
				priceArray[interval] = (int) Math.round(high + (low - high) / (lowInterval - highInterval) * (interval - highInterval));
			}
		}
		double[] volumeDistribution = IntraDayVolumeDistribution.getDistribution();
		for (int interval = 0; interval < priceArray.length; interval ++) {
			if (!map.containsKey(priceArray[interval])) {
				map.put(priceArray[interval], (long) (volumeDistribution[interval] * volume));
			}
			else{
				map.put(priceArray[interval], (long) (map.get(priceArray[interval]) + volumeDistribution[interval] * volume));
			}
		}

		return map;
	}
	
	// Given intraday candles, get price-volume map
	// For each intraday candle: calculate the average of high,low,open, and close; add the volume to the map
	public static HashMap<Integer, Long> getMap(IntraDayStockCandleArray intraDayCandleArray) {
		HashMap<Integer, Long> map = new HashMap<Integer, Long>(); 
		for (int i = 0; i < intraDayCandleArray.size(); i++){
			IntraDayStockCandle intraDayCandle = intraDayCandleArray.get(i);
			//Actual price * 100
			int price = (int)Math.round((intraDayCandle.high +intraDayCandle.low +intraDayCandle.open + intraDayCandle.close) / 4 * 100);
			if (!map.containsKey(price)){
				map.put(price, intraDayCandle.volume);
			}
			else{
				map.put(price, map.get(price) + intraDayCandle.volume);
			}
			
		}
		
		return map;
	}
	

}
