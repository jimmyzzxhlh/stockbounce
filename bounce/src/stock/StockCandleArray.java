package stock;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import stock.StockEnum.StockCandleDataType;

/**
 * Main class for a list of stock candles.
 * Algorithm implementation will also be here.
 */
public class StockCandleArray {
	private ArrayList<StockCandle> stockCandleArray;
	private String symbol;
	
	
	public StockCandleArray(StockCandleArray inputStockCandleArray) {
		this.stockCandleArray = new ArrayList<StockCandle>();
		for (int i = 0; i < inputStockCandleArray.getStockCandleArray().size(); i++) {
			StockCandle stockCandle = new StockCandle(inputStockCandleArray.getStockCandleArray().get(i));
			this.stockCandleArray.add(stockCandle);
		}		
		this.symbol = inputStockCandleArray.symbol;
	}
	
	public ArrayList<StockCandle> getStockCandleArray() {
		return stockCandleArray;
	}

	public void setStockCandleArray(ArrayList<StockCandle> stockCandleArray) {
		this.stockCandleArray = stockCandleArray;
	}

	public String getSymbol() {
		return symbol;
	}
	
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	public StockCandleArray() {
		stockCandleArray = new ArrayList<StockCandle>(); 
	}
	
	public int size() {
		return stockCandleArray.size();
	}
	
	public StockCandle get(int index) {
		return stockCandleArray.get(index);
	}
	
	public void add(StockCandle stockCandle) {
		stockCandleArray.add(stockCandle);
	}
	
	public void normalizeStockCandle(double maxForNormalization) {
		normalizeStockCandle(maxForNormalization, 0, stockCandleArray.size());
	}
	
	public void normalizeStockCandle(double maxForNormalization, int start, int end) {
		StockCandle stockCandle;
		double max = 0;
		double min = 1e10;
		double scale = 1;
		if ((start < 0) || (end >= stockCandleArray.size())) return;
		for (int i = start; i <= end; i++) {
			stockCandle = stockCandleArray.get(i);
			if (stockCandle.low < min) {
				min = stockCandle.low;
			}
			if (stockCandle.high > max) {
				max = stockCandle.high;
			}
		}
		scale = maxForNormalization / (max - min);
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

	
	/**
	 * Get the maximum stock price during a time range, defined by the current day (index)
	 * and the number of days to look forward.
	 * @param index The subscript in the stock candle array that represents the current day.
	 * @param days Number of days to look forward.
	 * @param dataType Type of data to look at (open, close, etc.).
	 * @return See description. If nothing can be returned, then return 0.
	 */
	public double getMaxStockPrice(int index, int days, StockCandleDataType dataType) {
		if (index + days - 1 >= stockCandleArray.size()) return 0;
		double result = 0;
		for (int i = index; i < index + days; i++) {
			double currentPrice = stockCandleArray.get(i).getStockPrice(dataType);
			if (currentPrice > result) result = currentPrice;
		}
		return result;		
	}
	
	public static String formatPrice(double price) {
		DecimalFormat decimalFormat = new DecimalFormat("#.00");
		return decimalFormat.format(price);
	}
	
	/**
	 * Get the minimum stock price during a time range, defined by the current day (index)
	 * and the number of days to look forward.
	 * @param index The subscript in the stock candle array that represents the current day.
	 * @param days Number of days to look forward.
	 * @param dataType Type of data to look at (open, close, etc.).
	 * @return See description. If nothing can be returned, then return 0.
	 */
	public double getMinStockPrice(int index, int days, StockCandleDataType dataType) {
		if (index + days - 1 >= stockCandleArray.size()) return 0;
		double result = 0;
		for (int i = index; i < index + days; i++) {
			double currentPrice = stockCandleArray.get(i).getStockPrice(dataType);
			if ((result == 0) || (currentPrice < result)) result = currentPrice;
		}
		return result;		
	}
	

}
			
		
	