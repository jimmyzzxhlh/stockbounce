package test;

import java.sql.Timestamp;
import java.util.Date;

public class TimestampTest {
	public static void main(String args[]) {
		Timestamp ts = new Timestamp(1417012477 * 1000L);  //Notice that we must have the "L" besides 1000 so that it is returning a long value!
		System.out.println(ts);
		Date d = (Date) ts;
		System.out.println(d);
	}
}
