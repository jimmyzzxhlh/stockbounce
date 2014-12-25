package intraday;

import java.sql.Timestamp;
import java.util.ArrayList;

import stock.StockConst;
import stock.StockEnum.StockIntraDayClass;

/**
 * Class that represents all the candles in a day.
 * @author jimmyzzxhlh-Dell
 *
 */
public class IntraDayStockCandleArray {
	private static final double NAN = -1e10;
	private static final int NANINT = -1;
	private ArrayList<IntraDayStockCandle> stockCandleArray = null;
	private String symbol;
	private double open = NAN;
	private double close = NAN;
	private double high = NAN;
	private double low = NAN;
	private long volume = NANINT;
	private ArrayList<Integer> highIntervals = null;
	private ArrayList<Integer> lowIntervals = null;
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
			if ((high == NAN) || (currentHigh > high)) {
				high = currentHigh;
			}
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
			if ((low == NAN) || (currentLow < low)) {
				low = currentLow;							
			}
		}
	}
	
	public long getVolume() {
		if (volume > 0) return volume;
		if (!isStockCandleArrayValid()) return NANINT;
		setVolume();
		return volume;
	}
	
	public void setVolume() {
		if (!isStockCandleArrayValid()) return;
		volume = 0;
		for (int i = 0; i < stockCandleArray.size(); i++) {
			long currentVolume = stockCandleArray.get(i).getVolume();
			volume += currentVolume;
		}
	}
	
	public ArrayList<Integer> getHighIntervals() {
		if (highIntervals == null) {
			setHighIntervals();
		}
		return highIntervals;
	}
	
	public void setHighIntervals() {
		if (high == NAN) setHigh();
		highIntervals = new ArrayList<Integer>();
		for (int i = 0; i < stockCandleArray.size(); i++) {
			IntraDayStockCandle idStockCandle = stockCandleArray.get(i);
			if (Math.abs(idStockCandle.getHigh() - high) < 0.01) {
				highIntervals.add(idStockCandle.getInterval());				
			}
		}		
	}
	
	public ArrayList<Integer> getLowIntervals() {
		if (lowIntervals == null) {
			setLowIntervals();
		}
		return lowIntervals;		
	}
	
	public void setLowIntervals() {
		if (low == NAN) setLow();
		lowIntervals = new ArrayList<Integer>();
		for (int i = 0; i < stockCandleArray.size(); i++) {
			IntraDayStockCandle idStockCandle = stockCandleArray.get(i);
			if (Math.abs(idStockCandle.getLow() - low) < 0.01) {
				lowIntervals.add(idStockCandle.getInterval());				
			}
		}		
	}
	
	
	public double getUpperShadowLength() {
		if (hasNANPrice()) {
			setPrice();
		}
		if (isWhite()) {
			return high - close;
		}
		return high - open;
	}
	
	public double getLowerShadowLength() {
		if (hasNANPrice()) {
			setPrice();
		}
		if (isWhite()) {
			return open - low;
		}
		return close - low;		
	}
	
	public boolean isUpperShadowLonger() {
		return (getUpperShadowLength() >= getLowerShadowLength());
	}
	
	public boolean isWhite() {
		return (close > open);
	}
	
	public boolean isBlack() {
		return (close < open);
	}
	
	public boolean hasNANPrice() {
		if ((open == NAN) || (close == NAN) || (high == NAN) || (low == NAN)) return true;
		return false;
	}
	
	public void setPrice() {
		setOpen();
		setClose();
		setHigh();
		setLow();
	}
	
	public double getBodyLength() {
		return Math.abs(close - open);
	}
	
	public StockIntraDayClass getIntraDayClass() {
		if (hasNANPrice()) {
			setPrice();
		}
		double bodyLength = getBodyLength();
		if (isWhite()) {
			if (bodyLength / open >= StockConst.LONG_DAY_PERCENTAGE) return StockIntraDayClass.WHITE_LONG;
		}
		if (isBlack()) {
			if (bodyLength / open >= StockConst.LONG_DAY_PERCENTAGE) return StockIntraDayClass.BLACK_LONG;
		}
		if (isUpperShadowLonger())
			return StockIntraDayClass.UPPER_LONGER;
		return StockIntraDayClass.LOWER_LONGER;
	}
	
	public double getTotalCapital() {
		double totalCapital = 0;
		for (int i = 0; i < stockCandleArray.size(); i++) {
			IntraDayStockCandle idStockCandle = stockCandleArray.get(i);
			double averagePrice = idStockCandle.getAveragePrice();
			totalCapital += averagePrice * idStockCandle.getVolume();			
		}
		return totalCapital;
	}
	
	
}
