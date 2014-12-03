package analysis;

import stock.StockCandle;

public class IntraDayStockCandle extends StockCandle {
	private int interval = 0;

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}
	
	public double getMidPrice() {
		return (open + close) * 0.5;
	}
	
	/**
	 * 
	 * @param dayHigh
	 * @param dayLow
	 * @return
	 */
	public double getPercentageFromLow(double dayHigh, double dayLow) {
		if (dayHigh < dayLow) return 0;  //Should not happen.
		double dayBodyLength = dayHigh - dayLow;
		return (getMidPrice() - dayLow) / dayBodyLength;
	}
	
}
