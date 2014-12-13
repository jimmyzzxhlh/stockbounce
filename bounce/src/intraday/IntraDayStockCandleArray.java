package intraday;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Class that represents all the candles in a day.
 * @author jimmyzzxhlh-Dell
 *
 */
public class IntraDayStockCandleArray {
	private static final double NAN = -1e10;
	private ArrayList<IntraDayStockCandle> stockCandleArray = null;
	private String symbol;
	private double open = NAN;
	private double close = NAN;
	private double high = NAN;
	private double low = NAN;
	private Timestamp ts;
	
	public IntraDayStockCandleArray() {
		stockCandleArray = new ArrayList<IntraDayStockCandle>();
	}
	
	public IntraDayStockCandle get(int index) {
		return stockCandleArray.get(index);
	}
	
	public void add(IntraDayStockCandle intraDayStockCandle) {
		stockCandleArray.add(intraDayStockCandle);		
	}
	
	public int size() {
		return stockCandleArray.size();
	}
	
	public boolean isStockCandleArrayValid() {
		if (stockCandleArray == null) return false;
		if (stockCandleArray.size() < 1) return false;
		return true;
	}
	
	public String getSymbol() {
		return symbol;
	}
	
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	public Timestamp getTimestamp() {
		return ts;
	}
	
	public void setTimeStamp(Timestamp ts) {
		this.ts = ts;
	}
	
	public double getOpen() {
		if (open > 0) return open;
		if (!isStockCandleArrayValid()) return NAN;
		setOpen();
		return open;
	}
	
	public void setOpen() {
		if (!isStockCandleArrayValid()) return;
		open = stockCandleArray.get(0).getOpen(); 
	}
	
	public double getClose() {
		if (close > 0) return close;
		if (!isStockCandleArrayValid()) return NAN;
		setClose();
		return close;
	}
	
	public void setClose() {
		if (!isStockCandleArrayValid()) return;
		close = stockCandleArray.get(stockCandleArray.size() - 1).getClose();
	}
	
	public double getHigh() {
		if (high > 0) return high;
		if (!isStockCandleArrayValid()) return NAN;
		setHigh();
		return high;
	}
	
	public void setHigh() {
		if (!isStockCandleArrayValid()) return;
		high = NAN;
		for (int i = 0; i < stockCandleArray.size(); i++) {
			double currentHigh = stockCandleArray.get(i).getHigh();
			if ((high == NAN) || (currentHigh > high)) high = currentHigh;			
		}
	}
	
	public double getLow() {
		if (low > 0) return low;
		if (!isStockCandleArrayValid()) return NAN;
		setLow();
		return low;
	}
	
	public void setLow() {
		if (!isStockCandleArrayValid()) return;
		low = NAN;
		for (int i = 0; i < stockCandleArray.size(); i++) {
			double currentLow = stockCandleArray.get(i).getLow();
			if ((low == NAN) || (currentLow < low)) low = currentLow;			
		}
	}
	
	
	
	
	
}
