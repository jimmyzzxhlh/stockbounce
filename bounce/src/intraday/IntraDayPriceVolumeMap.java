package intraday;

import java.util.HashMap;

import stock.StockEnum.StockIntraDayClass;

public class IntraDayPriceVolumeMap {
	public IntraDayPriceVolumeMap() {
	}
	
	public static HashMap<Integer, Long> getMap(IntraDayStockCandleArray idStockCandleArray) {
		HashMap<Integer, Long> map = null; //Map between price and volume. Price has been multiplied by 100 and rounded.
		StockIntraDayClass intraDayClass = idStockCandleArray.getIntraDayClass();
		int lowInterval = IntraDayGetLowHigh.getLowInterval(intraDayClass);
		int highInterval = IntraDayGetLowHigh.getHighInterval(intraDayClass);
		int[] priceArray = new int[391];
		double open = idStockCandleArray.getOpen() * 100;
		double close = idStockCandleArray.getClose() * 100;
		double high = idStockCandleArray.getHigh() * 100;
		double low = idStockCandleArray.getLow() * 100;
		double volume = idStockCandleArray.getVolume();
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
			if (map.containsKey(priceArray[interval])){
				map.replace(priceArray[interval], (long) (map.get(priceArray[interval]) + volumeDistribution[interval] * volume));
			}
			else{
				map.put(priceArray[interval], (long) (volumeDistribution[interval] * volume));
			}
		}

		return map;
	}

}
