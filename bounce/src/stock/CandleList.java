package stock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import stock.StockEnum.StockCandleDataType;

/**
 * Class for a list of stock candles.  
 */
public class CandleList {
	protected List<StockCandle> candleList;
	protected Company company;
	protected long volume;
	protected double low = Double.MAX_VALUE;
	protected double high = Double.MIN_VALUE;
	
	public CandleList(Company company) {
		this.company = company;
		candleList = new ArrayList<>();
	}
	
	/**
	 * Clone a stock candle list.
	 * @param inputstockCandleList
	 */
	public CandleList(CandleList candleList) {
		this.candleList = new ArrayList<>();
		this.company = new Company(company);
		this.volume = candleList.volume;
		this.low = candleList.low;
		this.high = candleList.high;
		for (int i = 0; i < candleList.size(); i++) {
			StockCandle stockCandle = candleList.get(i).copy();
			this.candleList.add(stockCandle);
		}				
	}
	
	public void destroy() {
		if (candleList != null) {
			candleList.clear();
			candleList = null;
		}		
		company = null;
	}

	public Company getCompany()          { return company; }
	public String getSymbol()            { return company.getSymbol(); }
	public long getOutstandingShares()   { return company.getOutstandingShares(); }
	
	public double getHigh(int index)  { return candleList.get(index).high;   }
	public double getLow(int index)   { return candleList.get(index).low;    }
	public double getOpen(int index)  { return candleList.get(index).open;   }
	public double getClose(int index) { return candleList.get(index).close;  }
	public long getVolume(int index)  { return candleList.get(index).volume; }
	public DateTime getInstant(int index) { return candleList.get(index).getInstant(); }
	
	public double getClose(DateTime date) { return getClose(getDateIndex(date)); }
	public double getHigh()           { return high;   }
	public double getLow()            { return low;    }
	public long getVolume()           { return volume; }
	public double getOpen()  { return candleList.get(0).getOpen();  }
	public double getClose() { return candleList.get(size() - 1).getClose(); }
	
	public int size() {
		return candleList.size();
	}
	
	public StockCandle get(int index) {
		return candleList.get(index);
	}
	
	public void add(StockCandle stockCandle) {
		candleList.add(stockCandle);
		volume += stockCandle.volume;
		low = Double.min(low, stockCandle.low);
		high = Double.max(high, stockCandle.high);
	}
	
	public double getBodyLength() {
		return isWhite() ? getClose() - getOpen() : getOpen() - getClose();
	}
	
	public double getUpperShadowLength() {
		return isWhite() ? high - getClose() : high - getOpen();		
	}
	
	public double getLowerShadowLength() {
		return isWhite() ? getOpen() - low : getClose() - low;		
	}
	
	public boolean isUpperShadowLonger() {
		return (getUpperShadowLength() >= getLowerShadowLength());
	}
	
	public boolean isWhite() {
		return (getClose() > getOpen());
	}
	
	public boolean isBlack() {
		return (getClose() < getOpen());
	}
	
	/**
	 * Normalize the candles so that they are restricted to a certain range.
	 * This is mainly for drawing the candles.
	 * @param maxForNormalization
	 */
	public void normalizeStockCandle(double maxForNormalization) {
		normalizeStockCandle(maxForNormalization, 0, candleList.size() - 1);
	}
	
	public void normalizeStockCandle(double maxForNormalization, int start, int end) {
		StockCandle candle;
		double max = 0;
		double min = Double.MAX_VALUE;
		double scale = 1;
		if ((start < 0) || (end >= candleList.size())) return;
		for (int i = start; i <= end; i++) {
			candle = candleList.get(i);
			if (candle.low < min) {
				min = candle.low;
			}
			if (candle.high > max) {
				max = candle.high;
			}
		}
		scale = maxForNormalization / (max - min);
		for (int i = 0; i < candleList.size(); i++) {
			candle = candleList.get(i);
			candle.open = (candle.open - min) * scale;
			candle.close = (candle.close - min) * scale;
			candle.high = (candle.high - min) * scale;
			candle.low = (candle.low - min) * scale;			
		}		
	}
	
	public void normalizeStockCandle(double maxForNormalization, double min, double max, int start, int end) {
		double scale = maxForNormalization / (max - min);
		for (int i = 0; i < candleList.size(); i++) {
			StockCandle candle = candleList.get(i);
			candle.open = (candle.open - min) * scale;
			candle.close = (candle.close - min) * scale;
			candle.high = (candle.high - min) * scale;
			candle.low = (candle.low - min) * scale;			
		}	
	}
	
	/**
	 * Sort the candles by their dates.
	 */
	public void sortByDate() {
		Collections.sort(candleList, (a, b) ->
		{
			if (a.instant.isBefore(b.instant)) return -1;
			return 1;
		}
		);
		
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
		if (index + days - 1 >= candleList.size()) return 0;
		double result = 0;
		for (int i = index; i < index + days; i++) {
			double currentPrice = candleList.get(i).getStockPrice(dataType);
			if (currentPrice > result) result = currentPrice;
		}
		return result;		
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
		if (index + days - 1 >= candleList.size()) return 0;
		double result = 0;
		for (int i = index; i < index + days; i++) {
			double currentPrice = candleList.get(i).getStockPrice(dataType);
			if ((result == 0) || (currentPrice < result)) result = currentPrice;
		}
		return result;		
	}
	
	/**
	 * Use binary search to get the index in the stock candle array given a date.
	 * TODO: Can we use map instead of binary search?
	 * @param date Input date
	 * @param getNearestIndex True if we either return the exact date index or return the nearest date index (so we will always
	 *        return a value > 0). False if we can return -1. 
	 * @return
	 */
	public int getDateIndex(DateTime date, boolean getNearestIndex) {
		int start = 0;
		int maxEnd = candleList.size() - 1;
		int end = maxEnd;
		int mid = -1;
		boolean found = false;
		while ((!found) && (start <= end)) {
			mid = (start + end) / 2;
			DateTime midDate = candleList.get(mid).getInstant();
			if (date.isBefore(midDate)) {
				end = mid - 1;
			}
			else if (date.isAfter(midDate)) {
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
	
	public int getDateIndex(DateTime date) {
		return getDateIndex(date, false);
	}
	
	public boolean hasDate(DateTime date) {
		return (getDateIndex(date, false) >= 0);
	}
	
}
			
		
	