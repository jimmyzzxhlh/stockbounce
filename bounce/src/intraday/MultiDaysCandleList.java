package intraday;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import stock.Company;

/**
 * This class contains multiple-days intraday data for one symbol.
 * @author jimmyzzxhlh-Dell
 *
 */
public class MultiDaysCandleList {

	private List<IntraDayCandleList> mdCandleList;
	private Company company;
	
	public MultiDaysCandleList(Company company) {
		this.company = company;
		mdCandleList = new ArrayList<>();
	}
	
	public void destroy() {
		if (mdCandleList != null) {
			for (IntraDayCandleList idCandleList : mdCandleList) {
				idCandleList.destroy();
			}
			mdCandleList.clear();
			mdCandleList = null;
		}
		company = null;
	}
	
	public void sortByDate() {
		Collections.sort(mdCandleList);
	}
	
	public IntraDayCandleList get(int index) {
		return mdCandleList.get(index);
	}
	
	public int size() {
		return mdCandleList.size();
	}
	
	/**
	 * Given a date, find the corresponding intraday candle array if we have the intraday data for that date.
	 * Use binary search.
	 * @param date An input date for finding out intraday data.
	 * @return An intraday stock candle array object if found, otherwise null.
	 */
	public IntraDayCandleList get(DateTime date) {
		int start = 0;
		int maxEnd = mdCandleList.size() - 1;
		int end = maxEnd;
		int mid = -1;
		boolean found = false;
		while ((!found) && (start <= end)) {
			mid = (start + end) / 2;
			DateTime midDate = mdCandleList.get(mid).getDate();
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
		if (!found) return null;
		return mdCandleList.get(mid);
	}
	
	
	
}
