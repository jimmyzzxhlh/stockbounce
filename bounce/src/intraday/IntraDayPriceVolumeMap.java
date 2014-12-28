package intraday;

import java.util.HashMap;

import stock.StockCandle;
import stock.StockEnum.StockIntraDayClass;

public class IntraDayPriceVolumeMap {
	public IntraDayPriceVolumeMap() {
	}
	
	public static HashMap<Integer, Long> getMap(StockCandle idStockCandle) {
		HashMap<Integer, Long> map = new HashMap<Integer, Long>(); //Map between price and volume. Price has been multiplied by 100 and rounded.
		StockIntraDayClass intraDayClass = idStockCandle.getIntraDayClass();
		int lowInterval = IntraDayGetLowHigh.getLowInterval(intraDayClass);
		int highInterval = IntraDayGetLowHigh.getHighInterval(intraDayClass);
		int[] priceArray = new int[391];
		double open = idStockCandle.getOpen() * 100;
		double close = idStockCandle.getClose() * 100;
		double high = idStockCandle.getHigh() * 100;
		double low = idStockCandle.getLow() * 100;
		double volume = idStockCandle.getVolume();
		for (int interval = 0; interval < priceArray.length; interval ++){
			if (lowInterval <= highInterval){
				if (interval <=lowInterval){
					if (lowInterval == 0){
						priceArray[interval] = (int) Math.round(open);
					}
					else priceArray[interval] = (int) Math.round(open + (low - open) / lowInterval * interval) ;
					continue;
				}
				if (interval >= highInterval){
					if (highInterval == 390){
						priceArray[interval] = (int) Math.round(close);
					}
					else priceArray[interval] = (int) Math.round(high + (close - high) / (390 - highInterval) * (interval-highInterval));
					continue;
				}
				//otherwise, lowInterval < interval < highInterval
					priceArray[interval] = (int) Math.round(low + (high - low) / (highInterval-lowInterval) * (interval-lowInterval));
			}
			
			else //i.e., highInterval < lowInterval
			{
				if (interval <=highInterval){
					if (highInterval == 0){
						priceArray[interval] = (int) Math.round(open);
					}
					else priceArray[interval] = (int) Math.round(open + (high - open) / highInterval * interval) ;
					continue;
				}
				if (interval >= lowInterval){
					if (lowInterval == 390){
						priceArray[interval] = (int) Math.round(close);
					}
					else priceArray[interval] = (int) Math.round(low + (close - low) / (390 - lowInterval) * (interval-lowInterval));
					continue;
				}
				//otherwise, highInterval < interval < lowInterval
					priceArray[interval] = (int) Math.round(high + (low - high) / (lowInterval-highInterval) * (interval-highInterval));
			}
		}
		double[] volumeDistribution = IntraDayVolumeDistribution.getDistribution();
		for (int interval = 0; interval < priceArray.length; interval ++){
			map.put(priceArray[interval], (long) (map.get(priceArray[interval]) + volumeDistribution[interval] * volume));
//			if (map.containsKey(priceArray[interval])){
//				map.put(priceArray[interval], (long) (map.get(priceArray[interval]) + volumeDistribution[interval] * volume));
//			}
//			else{
//				map.put(priceArray[interval], (long) (volumeDistribution[interval] * volume));
//			}
		}

		return map;
	}

}
