package stock;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import org.joda.time.LocalDate;

import stock.StockEnum.StockCandleDataType;

/**
 * Main class for a list of stock candles.
 * Algorithm implementation will also be here.
 */
public class StockCandleArray {
	private ArrayList<StockCandle> stockCandleArray;
	private String symbol;
	private long sharesOutstanding;
	private LocalDate startLocalDate;
	private LocalDate endLocalDate;
	
	public StockCandleArray(StockCandleArray inputStockCandleArray) {
		this.stockCandleArray = new ArrayList<StockCandle>();
		this.symbol = inputStockCandleArray.symbol;
		this.sharesOutstanding = inputStockCandleArray.sharesOutstanding;
		for (int i = 0; i < inputStockCandleArray.getStockCandleArray().size(); i++) {
			StockCandle stockCandle = new StockCandle(inputStockCandleArray.getStockCandleArray().get(i));
			this.stockCandleArray.add(stockCandle);
		}		
		
	}
	
	public void destroy() {
		if (stockCandleArray != null) {
			for (StockCandle stockCandle : stockCandleArray) {
				stockCandle.destroy();
			}
			stockCandleArray.clear();
			stockCandleArray = null;
		}
		symbol = null;
		startLocalDate = null;
		endLocalDate = null;
	}
	
	public ArrayList<StockCandle> getStockCandleArray() {
		return stockCandleArray;
	}

	public double getHigh(int index) {
		return stockCandleArray.get(index).high;
	}
	
	public double getLow(int index) {
		return stockCandleArray.get(index).low;
	}
	
	
	public double getOpen(int index) {
		return stockCandleArray.get(index).open;
	}
	
	public double getClose(int index) {
		return stockCandleArray.get(index).close;
	}
	
	public double getClose(Date date) {
		int dateIndex = this.getDateIndex(date);
		return getClose(dateIndex);
	}
	
	public long getVolume(int index) {
		return stockCandleArray.get(index).volume;
	}
	
	public Date getDate(int index) {
		return stockCandleArray.get(index).date;
	}
	
	public void setStockCandleArray(ArrayList<StockCandle> stockCandleArray) {
		this.stockCandleArray = stockCandleArray;
	}
	
	public double getTurnoverRate(int index) {
		return stockCandleArray.get(index).getTurnoverRate();
	}
	
	public void setTurnoverRate(int index) {
		stockCandleArray.get(index).setTurnoverRate();
	}

	public String getSymbol() {
		return symbol;
	}
	
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	public long getSharesOutstanding() {
		return sharesOutstanding;
	}

	public void setSharesOutstanding(long sharesOutstanding) {
		this.sharesOutstanding = sharesOutstanding;
	}
	
	/**
	 * This function is called after we have already set the stock candle array appropriately.
	 * Set the first date and the last date in Joda LocalDate format.
	 */
	public void setLocalDates() {
		startLocalDate =  new LocalDate(getDate(0));
		endLocalDate = new LocalDate(getDate(size() - 1));		
	}
	
	public LocalDate getStartLocalDate() {
		return startLocalDate;
	}
	
	public LocalDate getEndLocalDate() {
		return endLocalDate;
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
		normalizeStockCandle(maxForNormalization, 0, stockCandleArray.size() - 1);
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
	
	public void normalizeStockCandle(double maxForNormalization, double min, double max, int start, int end) {
		double scale = maxForNormalization / (max - min);
		for (int i = 0; i < stockCandleArray.size(); i++) {
			StockCandle stockCandle = stockCandleArray.get(i);
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
	
	public double getMaxProfit(int index, int days, StockCandleDataType dataType) {
		double currentPrice = stockCandleArray.get(index).getStockPrice(dataType);
		double maxPrice = getMaxStockPrice(index, days, dataType);
		return maxPrice - currentPrice;
	}
	
	public double getMaxProfit(int index, int days) {
		return getMaxProfit(index, days, StockCandleDataType.CLOSE);
	}
	
	public double getMaxLoss(int index, int days, StockCandleDataType dataType) {
		double currentPrice = stockCandleArray.get(index).getStockPrice(dataType);
		double minClosePrice = getMinStockPrice(index, days, dataType);
		return minClosePrice - currentPrice;
	}
	
	public double getMaxLoss(int index, int days) {
		return getMaxLoss(index, days, StockCandleDataType.CLOSE);
	}

	/**
	 * Use binary search to get the index in the stock candle array given a date.
	 * @param date Input date
	 * @param getNearestIndex True if we either return the exact date index or return the nearest date index (so we will always
	 *        return a value > 0). False if we can return -1. 
	 * @return
	 */
	public int getDateIndex(Date date, boolean getNearestIndex) {
		int start = 0;
		int maxEnd = stockCandleArray.size() - 1;
		int end = maxEnd;
		int mid = -1;
		boolean found = false;
		while ((!found) && (start <= end)) {
			mid = (start + end) / 2;
			Date midDate = stockCandleArray.get(mid).getDate();
			if (date.before(midDate)) {
				end = mid - 1;
			}
			else if (date.after(midDate)) {
				start = mid + 1;
			}
			else {
				found = true;
			}
		}
		if (!found) {
			int dateIndex = -1;
			//If we need to get the nearest index from the given date
			if (getNearestIndex) {
				dateIndex = mid;
				if (dateIndex < 0) dateIndex = 0;
				if (dateIndex > maxEnd) dateIndex = maxEnd;				
			}
			//Should not happen if the date passed in is a valid date.
			return dateIndex;
		}
		return mid;
	}
	
	public int getDateIndex(Date date) {
		return getDateIndex(date, false);
	}
	
	public boolean hasDate(Date date) {
		return (getDateIndex(date, false) >= 0);
	}
	
	public boolean hasDate(LocalDate localDate) {
		Date date = localDate.toDate();
		return hasDate(date);
	}
	
	public boolean isLocalMax(int index, int period, StockCandleDataType dataType) {
		int start = Math.max(index - period, 0);
		int end = Math.min(index + period, stockCandleArray.size() - 1);
		double price = stockCandleArray.get(index).getStockPrice(dataType);
		for (int i = start; i <= end; i++) {
			if (i == index) continue;
			if (stockCandleArray.get(i).getStockPrice(dataType) > price) return false; 
		}
		return true;
	}
	
	//TODO
	public boolean isLocalMin(int index, int period, StockCandleDataType dataType) {
		return false;
	}
	
	public long getMaxVolume(int startIndex, int endIndex) {
		long maxVolume = -1;
		for (int i = startIndex; i <= endIndex; i++) {
			long currentVolume = this.getVolume(i);
			if (currentVolume > maxVolume) {
				maxVolume = currentVolume;
			}
		}
		return maxVolume;
	}
	
	public long getMinVolume(int startIndex, int endIndex) {
		long minVolume = Long.MAX_VALUE;
		for (int i = startIndex; i <= endIndex; i++) {
			long currentVolume = this.getVolume(i);
			if (currentVolume < minVolume) {
				minVolume = currentVolume;
			}
		}
		return minVolume;
	}
}
			
		
	