package stock;

import java.util.Date;

public class StockCandle {
	
	public double open = 0;
	public double close = 0;
	public double high = 0;
	public double low = 0;
	public int volume = 0;
	public Date date;

	
	public StockCandle() {
			
	}
	
	public StockCandle(Date date, double open, double close, double high, double low, int volume) {
		this.date = date;
		this.open = open;
		this.close = close;
		this.high = high;
		this.low = low;
		this.volume = volume;
	}
	
	@Override
	public String toString() {
		return "Date: " + this.date + " Open: " + this.open + " Close: " + this.close + " High: " + this.high + " Low: " + this.low + " Volume: " + this.volume;
	}
	
	public double getOpen() {
		return open;
	}

	public void setOpen(double open) {
		this.open = open;
	}

	public double getClose() {
		return close;
	}

	public void setClose(double close) {
		this.close = close;
	}

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public void setLowOverride(double low) {
		if ((this.low == 0) || (this.low > low)) {
			this.low = low;
		}
	}
	
	public void setHighOverride(double high) {
		if ((this.high == 0) || (this.high < high)) {
			this.high = high;
		}
	}
	
	public double getBodyLength() {
		return Math.abs(close - open);
	}
	
	public double getTotalLength() {
		return high - low;
	}

	public double getUpperShadowLength() {
		if (close > open) return high - close;
		return high - open;
	}
	
	public double getLowerShadowLength() {
		if (close > open) return open - low;
		return close - low;
	}
	
	/**
	 * Return the gap between two adjacent stock prices.
	 * Use open / close price to compute the gap.
	 * Gap above example (��������):
	 * 
	 *    ��
	 *    ��
	 * 
	 * |
	 * ��
	 * ��
	 * ��
	 * ��
	 * |
	 * 
	 * Here the gap length will be 3.
	 * @param lastStockCandle Last stock price object.
	 * @param currentStockCandle Current stock price object.
	 * @return
	 */
	public static double getGapLength(StockCandle lastStockCandle, StockCandle currentStockCandle) {
		if (lastStockCandle.close > lastStockCandle.open) {
			if (currentStockCandle.open > lastStockCandle.close) return currentStockCandle.open - lastStockCandle.close;
		}
		else {
			if (currentStockCandle.open < lastStockCandle.close) return lastStockCandle.close - currentStockCandle.open;
		}
		return 0;
	}
}

