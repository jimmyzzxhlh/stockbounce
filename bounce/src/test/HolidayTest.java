package test;

import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import de.jollyday.Holiday;
import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;

public class HolidayTest {
	public static void main(String args[]) {
		HolidayManager m = HolidayManager.getInstance(HolidayCalendar.UNITED_STATES);
//		DateTime startDate = new DateTime(2000, 1, 1, 0, 0, 0, 0);
//		DateTime endDate = new DateTime(2014, 12, 31, 0, 0, 0, 0);
		DateTime startDate = DateTime.parse("2000-01-01");
		DateTime endDate = DateTime.parse("2014-12-31");
		Interval interval = new Interval(startDate, endDate);
		Set<Holiday> holidays = m.getHolidays(interval);
		
		for (Holiday holiday : holidays) {
			System.out.println(holiday.getDate().toString());
		}
	}
}
