package intraday;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.joda.time.DateTime;

import stock.CandleList;
import stock.StockConst;
import stock.StockEnum.StockCandleClass;

/**
 * Class that represents all the candles in a day.
 * @author jimmyzzxhlh-Dell
 *
 */
public class IntraDayCandleList extends CandleList implements Comparable<IntraDayCandleList>  {
	
	private static Logger log = Logger.getLogger(IntraDayCandleList.class.getName());
	private static final double NAN = -1e10;
	private static final int NANINT = -1;
	private List<Integer> highIntervals = null;   //Intervals that contain the high price
	private List<Integer> lowIntervals = null;    //Intervals that contain the low price
	private DateTime date;

	public IntraDayCandleList(CandleList list) {
		super(list);		
	}
	
	/**
	 * Destroy the object for garbage collection.
	 */
	public void destroy() {
		super.destroy();
		highIntervals.clear();
		lowIntervals.clear();
		highIntervals = lowIntervals = null;
		date = null;
	}
	
	public DateTime getDate() {             
		return date;
	}
	
	public void setDate(DateTime date) {
		this.date = date;
	}

	/**
	 * Override the get function in the parent class to easily get an IntraDayCandle object.
	 */
	@Override
	public IntraDayCandle get(int index) {
		return (IntraDayCandle)(super.get(index));
	}
	
	public List<Integer> getHighIntervals() {
		if (highIntervals == null) setHighIntervals();
		return highIntervals;
	}
	
	private void setHighIntervals() {
		if (size() == 0) return;
		highIntervals = new ArrayList<>();
		for (int i = 0; i < size(); i++) {
			IntraDayCandle idCandle = get(i);
			if (Math.abs(idCandle.getHigh() - high) < 0.01) {
				highIntervals.add(idCandle.getInterval());				
			}
		}		
	}
	
	public List<Integer> getLowIntervals() {
		if (lowIntervals == null) setLowIntervals();
		return lowIntervals;		
	}
	
	private void setLowIntervals() {
		if (size() == 0) return;
		lowIntervals = new ArrayList<>();
		for (int i = 0; i < size(); i++) {
			IntraDayCandle idStockCandle = get(i);
			if (Math.abs(idStockCandle.getLow() - low) < 0.01) {
				lowIntervals.add(idStockCandle.getInterval());				
			}
		}		
	}
	
	public StockCandleClass getCandleClass() {
//		if (hasNANPrice()) {
//			setPrice();
//		}
		double bodyLength = getClose() - getOpen();
		if (bodyLength / getOpen() >= StockConst.LONG_DAY_PERCENTAGE) {
			return isWhite() ? StockCandleClass.WHITE_LONG : StockCandleClass.BLACK_LONG;
		}
		return isUpperShadowLonger() ? StockCandleClass.UPPER_LONGER : StockCandleClass.LOWER_LONGER;
	}
	
	public double getTotalCapital() {
		double totalCapital = 0;
		for (int i = 0; i < size(); i++) {
			IntraDayCandle idStockCandle = get(i);			
			double averagePrice = idStockCandle.getAveragePrice();
			totalCapital += averagePrice * idStockCandle.getVolume();			
		}
		return totalCapital;
	}

	/**
	 * Function for comparing to another intraday stock candle array.
	 * This allows us to sort a multiple days intraday stock candle array object.
	 * @param idstockCandleList
	 * @return
	 */
	public int compareTo(IntraDayCandleList idstockCandleList) {
		return date.compareTo(idstockCandleList.getDate());
	}
	
	
}
