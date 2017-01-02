package intraday;

import org.joda.time.DateTime;

import stock.StockCandle;

/**
 * Class that represents a candle during a certain period during one day (e.g. 1 minute candle).
 * @author jimmyzzxhlh-Dell
 *
 */
public class IntraDayCandle extends StockCandle {
	/**
	 * An interval is the index of the number of minutes from market open.
	 * For US, the market opens from 9:30 and ends at 16:00 in eastern time, so altogether there are 390 minutes.
	 * Therefore the interval is an integer between 0 and 390.
	 */
	private int interval = 0;  
	
	public int getInterval() { return interval; }
	
	public void setInterval() {
		//The market opens from 14:30 and closes at 21:00 in UTC time.
		interval = (instant.getHourOfDay() - 14) * 60 + (instant.getMinuteOfHour() - 30);
	}
	
	/**
	 * Notice that the instant should be in UTC time. 
	 */
	public IntraDayCandle(DateTime instant, double open, double close, double high, double low, long volume) {
		super(instant, open, close, high, low, volume);
		setInterval();
	}
	
	public IntraDayCandle copy() {
		IntraDayCandle candle = new IntraDayCandle(
				this.instant,
				this.open,
				this.close,
				this.high,
				this.low,
				this.volume);
		return candle;
	}
	
	public double getAveragePriceIgnoreHighLow() {
		return (open + close) * 0.5;
	}
	
	public double getAveragePrice() {
		return (open + close + high + low) * 0.25; 
	}
	
	public String toString() {
		return super.toString() + String.format(", interval=%d", interval);
	}

	
}
