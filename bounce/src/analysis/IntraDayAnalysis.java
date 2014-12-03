package analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Timestamp;
import java.util.Calendar;

import stock.StockConst;
import util.StockUtil;

public class IntraDayAnalysis {
	
	/**
	 * Return timestamp from a line in the intraday data.
	 * e.g.
	 * a1415629800,12.11,12.11,12.11,12.11,484
	 * This will return the timestamp for 1415629800, which is 2014-11-10 08:30:00.0
	 * @param line
	 * @return
	 */
	public static Timestamp getTimestamp(String line) {
		String data[] = line.split(",");
		if (!data[0].startsWith("a")) return null;
		Timestamp ts = new Timestamp(Long.parseLong(data[0].substring(1, data[0].length())) * 1000L);
		return ts;
	}
	
	/**
	 * Analyze the volume distribution for each minute between the market open and close (8:30 AM to 3:00 PM CST).
	 * e.g.
	 * a1415629800,12.11,12.11,12.11,12.11,4845
     * 30,12.2,12.2,12.18,12.18,300
     * means
     * At 2014-11-10 08:30:00.0, there are 4845 volume.
     * At 2014-11-10 09:00:00.0, there are 300 volume.
     * @throws Exception
	 */

	
	/**
	 * Check whether the interval is valid or not.
	 * If the interval is between 8:30 AM and 3:00 PM then it is valid.
	 * @param interval
	 * @return
	 */
	private static boolean isValidInterval(int interval) {
		if ((interval < 0) || (interval > 390)) return false;
		return true;
	}
	
	/**
	 * Analyze the distribution of the volume.
	 * @throws Exception
	 */
	public static void analyzeVolume() throws Exception {
		File directory = new File(StockConst.STOCK_INTRADAY_DIRECTORY_PATH);
		File[] directoryList = directory.listFiles();
		long volumes[] = new long[391];
		for (File file : directoryList) {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String symbol = StockUtil.getSymbolFromFile(file);
			
			System.out.println(symbol);
//			if (!symbol.equals("ZNGA")) continue;
			String line;
			//Skip the first few lines until we hit the data that needs to be processed.
			while ((line = br.readLine()) != null) {
				if (line.startsWith("a")) break;
			}			
			
			while (line != null) {
				long volumeDay = 0;
				IntraDayStockCandle idStockCandle = null;
				Timestamp ts = getTimestamp(line);
				idStockCandle = getIntraDayStockCandle(line);
				if (idStockCandle != null) {
					volumes[idStockCandle.getInterval()] += idStockCandle.getVolume();
					volumeDay += idStockCandle.getVolume();
				}
				else {
					System.out.println(symbol + " has invalid interval: " + line);
				}
				
				//Process each interval under the current date.
				int intervalCount = 0;
				while ((line = br.readLine()) != null) {
					if (line.startsWith("a")) break;
					intervalCount++;
					idStockCandle = getIntraDayStockCandle(line);
					if (idStockCandle != null) {
						volumes[idStockCandle.getInterval()] += idStockCandle.getVolume();
						volumeDay += idStockCandle.getVolume();
					}
					else {
						System.out.println(symbol + " has invalid interval: " + line);
						continue;
					}
				}				
//				if (symbol.equals("ZNGA")) {
//					System.out.println(symbol + " " + ts + " has " + volumeDay + " volume.");
//				}
//				System.out.println(symbol + " " + ts + " has " + intervalCount + " lines of data.");				
			}		
		}
		for (int i = 0; i < volumes.length; i++) {
			System.out.println(i + " " + volumes[i]);
		}
	}
	
	private static IntraDayStockCandle getIntraDayStockCandle(String line) {
		String data[] = line.split(",");
		int interval = -1;
		if (data[0].startsWith("a")) {
			//Process the line that starts with "a" which is the beginning of the day.
			Timestamp ts = getTimestamp(line);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(ts);
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int minute = calendar.get(Calendar.MINUTE);
			interval = (hour - 8) * 60 + (minute - 30);						
		}
		else {
			interval = Integer.parseInt(data[0]);			
		}
		if (!isValidInterval(interval)) return null;
		IntraDayStockCandle idStockCandle = new IntraDayStockCandle();
		idStockCandle.setInterval(interval);
		idStockCandle.setClose(Double.parseDouble(data[1]));
		idStockCandle.setHigh(Double.parseDouble(data[2]));
		idStockCandle.setLow(Double.parseDouble(data[3]));
		idStockCandle.setOpen(Double.parseDouble(data[4]));
		idStockCandle.setVolume(Integer.parseInt(data[5]));
		return idStockCandle;		
	}
	
	public static void analyzePrice() throws Exception {
		File directory = new File(StockConst.STOCK_INTRADAY_DIRECTORY_PATH);
		File[] directoryList = directory.listFiles();
		long volumes[] = new long[391];
		for (File file : directoryList) {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String symbol = StockUtil.getSymbolFromFile(file);
			
//			System.out.println(symbol);
//			if (!symbol.equals("AAME")) continue;
			String line;
			//Skip the first few lines until we hit the data that needs to be processed.
			while ((line = br.readLine()) != null) {
				if (line.startsWith("a")) break;
			}
			
			long volumeAfterHours = 0;
			
			while (line != null) {
				int interval = 0;
				long volume = 0;
				long volumeDay = 0;
				Timestamp ts = getTimestamp(line);
				
				//Process the line that starts with "a" which is the beginning of the day.
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(ts);
				int hour = calendar.get(Calendar.HOUR_OF_DAY);
				int minute = calendar.get(Calendar.MINUTE);
				interval = (hour - 8) * 60 + (minute - 30);
				
				//Should always have a valid interval (between 8:30 AM and 3:00 PM).
				if (isValidInterval(interval)) {  
					String data[] = line.split(",");
					volume = Long.parseLong(data[5]);
					volumes[interval] += volume;
				}
				else {
					System.out.println(symbol + " has invalid interval " + interval + ": " + line);
				}
				volumeDay += volume;
				//Process each interval under the current date.
				int intervalCount = 0;
				while ((line = br.readLine()) != null) {
					if (line.startsWith("a")) break;
					intervalCount++;
					String data[] = line.split(",");
					interval = Integer.parseInt(data[0]);
					if (isValidInterval(interval)) {
						volume = Long.parseLong(data[5]);
						volumes[interval] += volume;
						volumeDay += volume;
					}
					else {
						System.out.println(symbol + " has invalid interval " + interval + ": " + line);
						continue;
					}
					if (interval == 390) {
						volumeAfterHours += volume;
					}
				}				
				if (symbol.equals("ZNGA")) {
					System.out.println(symbol + " " + ts + " has " + volumeDay + " volume.");
				}
//				System.out.println(symbol + " " + ts + " has " + intervalCount + " lines of data.");				
			}
			
//			System.out.println(symbol + " has after hour volume " + volumeAfterHours);
		}
		for (int i = 0; i < volumes.length; i++) {
			System.out.println(i + " " + volumes[i]);
		}
	}
}

