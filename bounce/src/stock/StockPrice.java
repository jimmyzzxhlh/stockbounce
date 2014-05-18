package stock;

import java.util.Date;

public class StockPrice {
	public double open;
	public double close;
	public double high;
	public double low;
	public int volume;
	public Date date;
	
	public StockPrice() {
		
	}
	
	public StockPrice(Date date, double open, double close, double high, double low, int volume) {
		this.date = date;
		this.open = open;
		this.close = close;
		this.high = high;
		this.low = low;
		this.volume = volume;
	}
	
	public String toString() {
		return "Date: " + this.date + " Open: " + this.open + " Close: " + this.close + " High: " + this.high + " Low: " + this.low + " Volume: " + this.volume;
	}
}
