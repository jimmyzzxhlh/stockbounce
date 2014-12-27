package intraday;

import java.util.HashMap;

public class IntraDayPriceVolumeMap {
	
	private IntraDayStockCandleArray idStockCandleArray;
	
	public IntraDayPriceVolumeMap() {
		//Initialize intraday stock candle array.
	}
	
	private HashMap<Integer, Long> map; //Map between price and volume. Price has been multiplied by 100 and rounded.
	
	public HashMap<Integer, Long> getMap() {
		//Singleton
		if (map == null) {
			setMap();
		}
		return map;
	}
	
	public void setMap() {
		
	}
}
