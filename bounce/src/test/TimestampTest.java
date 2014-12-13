package test;

import intraday.IntraDayAnalysis;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * Test the timestamp from the intraday data.
 * The timestamp is a MySQL timestamp.
 * @author jimmyzzxhlh-Dell
 *
 */
public class TimestampTest {
	public static void main(String args[]) {
		testTimestampOne();
	}
	
	private static void testTimestampOne() {
		Timestamp ts = new Timestamp(1418331600 * 1000L);  //Notice that we must have the "L" besides 1000 so that it is returning a long value!
		System.out.println(ts);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(ts);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		System.out.println(hour);
		System.out.println(minute);
		System.out.println((hour - 8) * 60 + (minute - 30));		
		
	}
	
	private static void testTimestampTwo() {
		String line = "a1415629800,12.11,12.11,12.11,12.11,484";
		System.out.println(IntraDayAnalysis.getTimestamp(line));
	}
}
