package intraday;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * This class contains multiple-days intraday data for one symbol.
 * @author jimmyzzxhlh-Dell
 *
 */
public class MultipleDaysStockCandleArray {

	private ArrayList<IntraDayStockCandleArray> mdStockCandleArray;
	
	private String symbol;
	
	public MultipleDaysStockCandleArray(String symbol) {
		this.symbol = symbol;
		mdStockCandleArray = new ArrayList<IntraDayStockCandleArray>();
	}
	
	public void destroy() {
		if (mdStockCandleArray != null) {
			for (IntraDayStockCandleArray idStockCandleArray : mdStockCandleArray) {
				idStockCandleArray.destroy();
			}
			mdStockCandleArray.clear();
			mdStockCandleArray = null;
		}
		symbol = null;
	}
	
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	public String getSymbol() {
		return symbol;
	}
	
	public void sortByDate() {
		Collections.sort(mdStockCandleArray);
	}
	
	public IntraDayStockCandleArray get(int index) {
		return mdStockCandleArray.get(index);
	}
	
	public void add(IntraDayStockCandleArray idStockCandleArray) {
		mdStockCandleArray.add(idStockCandleArray);
	}
	
	public int size() {
		return mdStockCandleArray.size();
	}
	
	/**
	 * Given a date, find the corresponding intraday candle array if we have the intraday data for that date.
	 * Use binary search.
	 * @param date An input date for finding out intraday data.
	 * @return An intraday stock candle array object if found, otherwise null.
	 */
	public IntraDayStockCandleArray get(Date date) {
		int start = 0;
		int maxEnd = mdStockCandleArray.size() - 1;
		int end = maxEnd;
		int mid = -1;
		boolean found = false;
		while ((!found) && (start <= end)) {
			mid = (start + end) / 2;
			Date midDate = mdStockCandleArray.get(mid).getDate();
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
		if (!found) return null;
		return mdStockCandleArray.get(mid);
	}
	
	
	
}
