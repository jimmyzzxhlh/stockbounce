package pattern;

import java.util.ArrayList;

import stock.StockPrice;

public class StockPattern {
	//=============Constant Definition=============
	//The constant name should follow the below naming convention:
	//<Function Name>_<Constant Name>
	private static final double WHITE_LONG_DAY_MIN_BODY_LENGTH = 10;
	
	private static final double BLACK_LONG_DAY_MIN_BODY_LENGTH = 10;
	
	private ArrayList<StockPrice> stockPriceArray;
	
	public ArrayList<StockPrice> getStockPriceArray() {
		return stockPriceArray;
	}

	public void setStockPriceArray(ArrayList<StockPrice> stockPriceArray) {
		this.stockPriceArray = stockPriceArray;
	}

	public StockPattern() {
		
	}
	
	/**
	 * Constructor. The stock price array should always be normalized!
	 * Call static function StockPriceArray.normalizeStockPrice for normalization.
	 * @param stockPriceArray
	 */
	public StockPattern(ArrayList<StockPrice> stockPriceArray) {
		this.stockPriceArray = stockPriceArray;
	}
	
	
	/**
	 * Return true if the current candle is a white long day.
	 * Definition:
	 * 1. Close > Open.
	 * 2. Body length >= WHITE_LONG_DAY_MIN_BODY_LENGTH
	 * @param index The subscript in the stock price array.
	 * @return True if the current candle is a white long day.
	 */
	public boolean whiteLongDay(int index) {
		double bodyLength = stockPriceArray.get(index).close - stockPriceArray.get(index).open;
		if (bodyLength >= WHITE_LONG_DAY_MIN_BODY_LENGTH) return true;
		return false;		
	}
	
	/**
	 * Return true if the current candle is a black long day.
	 * Definition:
	 * 1. Close < Open.
	 * 2. Body length >= BLACK_LONG_DAY_MIN_BODY_LENGTH
	 * @param index The subscript in the stock price array.
	 * @return True if the current candle is a black long day.
	 */
	public boolean blackLongDay(int index) {
		double bodyLength = stockPriceArray.get(index).open - stockPriceArray.get(index).close;
		if (bodyLength >= BLACK_LONG_DAY_MIN_BODY_LENGTH) return true;
		return false;
	}
	
	
}
