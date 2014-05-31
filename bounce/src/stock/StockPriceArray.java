package stock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import stock.StockEnum.StockPriceDataType;

/**
 * Main class for a list of stock prices.
 * Algorithm implementation will also be here.
 * Glossary:
 * White Candle - Close > Open
 * Black Candle - Close < Open 
 */
public class StockPriceArray {
	private ArrayList<StockPrice> stockPriceArray;
	private static final double TREND_UP_SLOPE = 0.5;
	private static final double TREND_DOWN_SLOPE = -0.5;
	private static final double MIN_UPPER_SHADOW_LENGTH = 15;
	private static final double MAX_BODY_LENGTH = 3;
	
	
	public ArrayList<StockPrice> getStockPriceArray() {
		return stockPriceArray;
	}

	public void setStockPriceArray(ArrayList<StockPrice> stockPriceArray) {
		this.stockPriceArray = stockPriceArray;
	}

	
	public StockPriceArray() {
		stockPriceArray = new ArrayList<StockPrice>(); 
	}
	
	public static void normalizeStockPrice(ArrayList<StockPrice> stockPriceArray, int maxHeight) {
		StockPrice stockPrice;
		double max = 0;
		double min = 1e10;
		double scale = 1;
		for (int i = 0; i < stockPriceArray.size(); i++) {
			stockPrice = stockPriceArray.get(i);
			if (stockPrice.low < min) {
				min = stockPrice.low;
			}
			if (stockPrice.high > max) {
				max = stockPrice.high;
			}
		}
		scale = maxHeight / (max - min);
		for (int i = 0; i < stockPriceArray.size(); i++) {
			stockPrice = stockPriceArray.get(i);
			stockPrice.open = (stockPrice.open - min) * scale;
			stockPrice.close = (stockPrice.close - min) * scale;
			stockPrice.high = (stockPrice.high - min) * scale;
			stockPrice.low = (stockPrice.low - min) * scale;
			
		}		
	}
	
	public void sortByDate() {
		Collections.sort(stockPriceArray, new Comparator<StockPrice>() {
			public int compare(StockPrice a, StockPrice b) {
				if (a.date.before(b.date)) return -1;
				return 1;
			}
		});
	}
	
	public boolean IsTrendUp(int start, int end, StockPriceDataType dataPoint) {
		SimpleLinearRegression slr = new SimpleLinearRegression();
		double slope;
		for (int i = start; i < end; i++) {
			if (dataPoint == StockPriceDataType.OPEN) slr.data.add(stockPriceArray.get(i).getOpen());
			else if (dataPoint == StockPriceDataType.CLOSE) slr.data.add(stockPriceArray.get(i).getClose());
			else if (dataPoint == StockPriceDataType.HIGH) slr.data.add(stockPriceArray.get(i).getHigh());
			else if (dataPoint == StockPriceDataType.LOW) slr.data.add(stockPriceArray.get(i).getLow());			
		}
		
		slope = slr.getSlope();
		if (slope >= TREND_UP_SLOPE) return true;
		else return false;
	}
	
	public boolean IsTrendDown(int start, int end, StockPriceDataType dataPoint) {
		SimpleLinearRegression slr = new SimpleLinearRegression();
		double slope;
		for (int i = start; i < end; i++) {
			if (dataPoint == StockPriceDataType.OPEN) slr.data.add(stockPriceArray.get(i).getOpen());
			else if (dataPoint == StockPriceDataType.CLOSE) slr.data.add(stockPriceArray.get(i).getClose());
			else if (dataPoint == StockPriceDataType.HIGH) slr.data.add(stockPriceArray.get(i).getHigh());
			else if (dataPoint == StockPriceDataType.LOW) slr.data.add(stockPriceArray.get(i).getLow());			
		}
		
		slope = slr.getSlope();
		if (slope <= TREND_UP_SLOPE) return true;
		else return false;
	}
	
	public ArrayList<StockPrice> combineCandles(int candleDays, boolean overrideOriginalData) {
		ArrayList<StockPrice> combinedStockPriceArray = new ArrayList<StockPrice>();
		StockPrice combinedStockPrice;
		StockPrice currentStockPrice;
		int i = 0; 
		
		while (i < stockPriceArray.size()) {
			combinedStockPrice = new StockPrice();
			for (int j = i; j < i + candleDays; j++) {
				if (j >= stockPriceArray.size()) break;
				currentStockPrice = stockPriceArray.get(j);
				if (j == i) {
					combinedStockPrice.setDate(currentStockPrice.getDate());
					combinedStockPrice.setOpen(currentStockPrice.getOpen());					
				}
				combinedStockPrice.setClose(currentStockPrice.getClose());
				combinedStockPrice.setLowOverride(currentStockPrice.getLow());
				combinedStockPrice.setHighOverride(currentStockPrice.getHigh());
				combinedStockPrice.setVolume(combinedStockPrice.getVolume() + currentStockPrice.getVolume());				
			}
			i += candleDays;			
		}	
		
		if (overrideOriginalData) {
			stockPriceArray = combinedStockPriceArray;
		}
		
		return combinedStockPriceArray;
		
	}

	
	
	
//	public boolean HasLongUpperShadow(int position) {
//		if ((position < 0) || (position >= stockPriceArray.size())) return false;
//		StockPrice stockPrice;
//		double bodyLength;
//		double upperShadowLength;
//		stockPrice = stockPriceArray.get(position);
//		
//		bodyLength = Math.abs(stockPrice.close - stockPrice.open);
//		upperShadowLength = stockPrice.high - Math.max(stockPrice.open, stockPrice.close);
//		//Shouldn't happen
//		if (upperShadowLength < 0) upperShadowLength = 0;
//		 
//		
//		//if (upperShadowLength > LONG_UPPER_SHADOW_LENGTH)
//		return false;
//	}
}
			
		
	