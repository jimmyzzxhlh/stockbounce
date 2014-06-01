package stock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Main class for a list of stock candles.
 * Algorithm implementation will also be here.
 */
public class StockCandleArray {
	private ArrayList<StockCandle> stockCandleArray;
	private static final double TREND_UP_SLOPE = 0.5;
	private static final double TREND_DOWN_SLOPE = -0.5;
	private static final double MIN_UPPER_SHADOW_LENGTH = 15;
	private static final double MAX_BODY_LENGTH = 3;
	
	
	public ArrayList<StockCandle> getStockCandleArray() {
		return stockCandleArray;
	}

	public void setStockCandleArray(ArrayList<StockCandle> stockCandleArray) {
		this.stockCandleArray = stockCandleArray;
	}

	
	public StockCandleArray() {
		stockCandleArray = new ArrayList<StockCandle>(); 
	}
	
	public static void normalizeStockCandle(ArrayList<StockCandle> stockCandleArray, int maxHeight) {
		StockCandle stockCandle;
		double max = 0;
		double min = 1e10;
		double scale = 1;
		for (int i = 0; i < stockCandleArray.size(); i++) {
			stockCandle = stockCandleArray.get(i);
			if (stockCandle.low < min) {
				min = stockCandle.low;
			}
			if (stockCandle.high > max) {
				max = stockCandle.high;
			}
		}
		scale = maxHeight / (max - min);
		for (int i = 0; i < stockCandleArray.size(); i++) {
			stockCandle = stockCandleArray.get(i);
			stockCandle.open = (stockCandle.open - min) * scale;
			stockCandle.close = (stockCandle.close - min) * scale;
			stockCandle.high = (stockCandle.high - min) * scale;
			stockCandle.low = (stockCandle.low - min) * scale;
			
		}		
	}
	
	public void sortByDate() {
		Collections.sort(stockCandleArray, new Comparator<StockCandle>() {
			public int compare(StockCandle a, StockCandle b) {
				if (a.date.before(b.date)) return -1;
				return 1;
			}
		});
	}
	
	
	public ArrayList<StockCandle> combineCandles(int candleDays, boolean overrideOriginalData) {
		ArrayList<StockCandle> combinedStockCandleArray = new ArrayList<StockCandle>();
		StockCandle combinedStockCandle;
		StockCandle currentStockCandle;
		int i = 0; 
		
		while (i < stockCandleArray.size()) {
			combinedStockCandle = new StockCandle();
			for (int j = i; j < i + candleDays; j++) {
				if (j >= stockCandleArray.size()) break;
				currentStockCandle = stockCandleArray.get(j);
				if (j == i) {
					combinedStockCandle.setDate(currentStockCandle.getDate());
					combinedStockCandle.setOpen(currentStockCandle.getOpen());					
				}
				combinedStockCandle.setClose(currentStockCandle.getClose());
				combinedStockCandle.setLowOverride(currentStockCandle.getLow());
				combinedStockCandle.setHighOverride(currentStockCandle.getHigh());
				combinedStockCandle.setVolume(combinedStockCandle.getVolume() + currentStockCandle.getVolume());				
			}
			i += candleDays;			
		}	
		
		if (overrideOriginalData) {
			stockCandleArray = combinedStockCandleArray;
		}
		
		return combinedStockCandleArray;
		
	}

	
	
	
//	public boolean HasLongUpperShadow(int position) {
//		if ((position < 0) || (position >= stockCandleArray.size())) return false;
//		StockCandle stockCandle;
//		double bodyLength;
//		double upperShadowLength;
//		stockCandle = stockCandleArray.get(position);
//		
//		bodyLength = Math.abs(stockCandle.close - stockCandle.open);
//		upperShadowLength = stockCandle.high - Math.max(stockCandle.open, stockCandle.close);
//		//Shouldn't happen
//		if (upperShadowLength < 0) upperShadowLength = 0;
//		 
//		
//		//if (upperShadowLength > LONG_UPPER_SHADOW_LENGTH)
//		return false;
//	}
}
			
		
	