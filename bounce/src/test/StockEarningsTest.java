package test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import stock.StockEarningsDateMap;

public class StockEarningsTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String string= "10/15/14";
		DateFormat format = new SimpleDateFormat("MM/dd/yy", Locale.ENGLISH);
		Date date = new Date();
		try {
			date = format.parse(string);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(StockEarningsDateMap.isCloseToEarningsDate("MSFT", date));
	}

}
