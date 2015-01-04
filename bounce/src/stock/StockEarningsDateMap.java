package stock;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Read the text file which contains the earnings date.
 * @author jimmyzzxhlh-Dell
 *
 */
public class StockEarningsDateMap {
	
	private StockEarningsDateMap() {
		
	}
	
	private static HashMap<String, ArrayList<Date>> map;
	
	public static HashMap<String, ArrayList<Date>> getMap() { 
		if (map == null) setMap();
		return map;
	}
	
	private static void setMap() {
		map = new HashMap<String, ArrayList<Date>>();
		//TODO
	}
	
	/**
	 * Check whether there is an earning date that is right after the current date. 
	 * @param symbol
	 * @param date
	 * @return
	 */
	public static boolean isCloseToEarningsDate(String symbol, Date date) {
		return false;
		//TODO
	}
	
}
