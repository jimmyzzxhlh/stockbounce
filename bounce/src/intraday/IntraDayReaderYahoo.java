package intraday;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import stock.StockConst;
import stock.StockEnum.Exchange;
import stock.ExchangeUtil;
import util.StockUtil;

public class IntraDayReaderYahoo {
	
	/**
	 * Return timestamp from a line in the intraday data.
	 * e.g.
	 * 1415629800,48.0800,48.1600,48.0400,48.1182,174600
	 * This will return the timestamp for 1415629800, which is 2014-11-10 08:30:00.0
	 * @param line
	 * @return
	 */
	public static Timestamp getTimestamp(String line) {
		String data[] = line.split(",");
		Timestamp ts;
		try {
			ts = new Timestamp(Long.parseLong(data[0]) * 1000L);
		}
		catch (Exception e) {
			System.err.println("Timestamp invalid for: " + line);
			return null;
		}
		return ts;
	}
	

	public static int getIntervalFromTimestamp(Timestamp ts) {
		if (ts == null) return -1;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(ts);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		return (hour - 8) * 60 + (minute - 30);		
	}
	
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
	 * Get an intra day stock candle from the input line.
	 * It has the following format:
	 * 1419258600,21.6400,21.6400,21.6400,21.6400,900
	 * @param line
	 * @return
	 */
	private static IntraDayCandle getIntraDayStockCandle(String line) {
		String data[] = line.split(",");
		Timestamp ts;
		if (data.length <= 5) {
			System.err.println("Data fields should be 6, but it is " + data.length + ": " + line);
			return null;
		}
		
		ts = getTimestamp(line);
		int interval = getIntervalFromTimestamp(ts);
		//Not checking the interval as the interval is different between US and China.
//		if (!isValidInterval(interval)) {
//			System.err.println("Interval invalid for: " + line);
//			return null;
//		}
		IntraDayCandle idStockCandle = new IntraDayCandle();
		idStockCandle.setInterval(interval);
		idStockCandle.setClose(Double.parseDouble(data[1]));
		idStockCandle.setHigh(Double.parseDouble(data[2]));
		idStockCandle.setLow(Double.parseDouble(data[3]));
		idStockCandle.setOpen(Double.parseDouble(data[4]));
		idStockCandle.setVolume(Integer.parseInt(data[5]));
		return idStockCandle;		
	}
	
	/**
	 * Return an array of an array of intraday stock candles.
	 * Notice that this function will be deprecated in the future as we wil combine all the intraday data
	 * together into one file to improve the read performance.
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static MultiDaysCandleList getMultipleDaysstockCandleList(Exchange exchange, String symbol) throws Exception {
		//Redirect to a different function if we already have the combined data.
		if (StockConst.INTRADAY_IS_DATA_COMBINED) {
			return getMultipleDaysstockCandleListCombined(exchange, symbol);
		}		
		//Create an object of multi-days stock candle array.
		MultiDaysCandleList mdstockCandleList = new MultiDaysCandleList(symbol);
		//Read from a folder
		File directory = new File(Exchange.getIntraDayDirectory(exchange, symbol));
		for (File file : directory.listFiles()) {
			System.out.println(file.getAbsolutePath());
			IntraDaystockCandleList idstockCandleList = getIntraDaystockCandleList(symbol, file);
			mdstockCandleList.add(idstockCandleList);
			
		}
		//Sort by dates.
		mdstockCandleList.sortByDate();
		return mdstockCandleList;
	}
	
	private static MultiDaysCandleList getMultipleDaysstockCandleListCombined(Exchange exchange, String symbol) throws Exception {
		//Create an object of multi-days stock candle array.
		MultiDaysCandleList mdstockCandleList = new MultiDaysCandleList(symbol);
		//Read from a particular file
		File file = new File(Exchange.getIntraDayMergedFilePath(exchange, symbol));
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = br.readLine();
		
		while (line != null) {
			IntraDaystockCandleList idstockCandleList = new IntraDaystockCandleList();
			//Start with processing the date.
			//Date, time zone, gmt offset have a "#" at the beginning
			Date date = StockUtil.parseDate(line.substring(1));   			
			line = br.readLine();
			String timeZone = line.substring(1);
			line = br.readLine();
			int gmtOffset = Integer.parseInt(line.substring(1));
			idstockCandleList.setDate(date);
			idstockCandleList.setTimeZone(timeZone);
			idstockCandleList.setGmtOffset(gmtOffset);
			idstockCandleList.setSymbol(symbol);
			line = br.readLine();
			if (line == null) {
				System.err.println("No intraday data is found for " + symbol + " on " + StockUtil.formatDate(date));
				break;
			}
			//Store the first time stamp as the general timestamp for the intraday stock candle array
			idstockCandleList.setTimeStamp(getTimestamp(line));			
			//Process the data			
			while (line != null) {
				IntraDayCandle idStockCandle = getIntraDayStockCandle(line);
				if (idStockCandle != null) {
					idstockCandleList.add(idStockCandle);
				}
				line = br.readLine();
			}
			//Set open, close, high, low attributes for the current day.
			idstockCandleList.setOpen();
			idstockCandleList.setClose();
			idstockCandleList.setHigh();
			idstockCandleList.setLow();
			//Add the intraday stock candle array to multiple one
			mdstockCandleList.add(idstockCandleList);			
		}
		br.close();
		return mdstockCandleList;
	}
	
	
	/**
	 * Get an intraday stock candle array from a single uncombined file (i.e. only contain 1 day data) 
	 * @param symbol
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static IntraDaystockCandleList getIntraDaystockCandleList(String symbol, File file) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		//Skip the first few lines until we hit the first timestamp that needs to be processed.
		//The first line is below the "volume" line.
		while ((line = br.readLine()) != null) {
			if (line.startsWith("volume")) break;
		}
		line = br.readLine();
		if (line == null) {
			System.err.println("No intraday data is found for " + symbol + " " + file.getName());
			br.close();
			throw new Exception();		
			
		}
		IntraDaystockCandleList idstockCandleList = new IntraDaystockCandleList();
		Date date = StockUtil.parseDate(StockUtil.getFilenameWithoutExtension(file.getName()));
		idstockCandleList.setDate(date);
		//Store the first timestamp in idstockCandleList. Usually it should be 8:30 AM in the morning.
		idstockCandleList.setTimeStamp(getTimestamp(line));
		idstockCandleList.setSymbol(symbol);
		while (line != null) {
			//Process the first intraday stock candle, which has the timestamp.
			IntraDayCandle idStockCandle = getIntraDayStockCandle(line);
			if (idStockCandle != null) {
				idstockCandleList.add(idStockCandle);
			}			
			else {
				br.close();
				throw new Exception();
			}
			line = br.readLine();
		}
		//Set open, close, high, low attributes for the current day.
		idstockCandleList.setOpen();
		idstockCandleList.setClose();
		idstockCandleList.setHigh();
		idstockCandleList.setLow();
		br.close();
		return idstockCandleList;
	}
	
	public static IntraDaystockCandleList getIntraDaystockCandleList(Exchange exchange, String symbol, String dateString) throws Exception {
		String filepath = Exchange.getIntraDayDirectory(exchange, symbol)  + dateString + ".txt";
		File file = new File(filepath);	
		if (!file.exists()) {
			System.err.println("Intra day file " + dateString + ".txt does not exist for " + symbol);
			throw new Exception();
		}
		
		return getIntraDaystockCandleList(symbol, file);
	}
	/*******************************Below are testing functions for the intraday reader********************************/
	/**
	 * Print out the daily volume for one single stock.
	 * Use this to test whether the sum of the intraday volume is correct.
	 * @throws Exception
	 */
	public static void printDailyVolumeForSingleStock() throws Exception {
		String symbol = "GOOG";
		MultiDaysCandleList mdstockCandleList = getMultipleDaysstockCandleList(Exchange.NASDAQ, symbol);
		for (int i = 0; i < mdstockCandleList.size(); i++) {
			long volumeDay = 0;
			IntraDaystockCandleList idstockCandleList = mdstockCandleList.get(i);
			for (int j = 0; j < idstockCandleList.size(); j++) {
				volumeDay += idstockCandleList.get(j).getVolume();
			}
			System.out.println(idstockCandleList.getTimestamp() + " " + volumeDay);
		}
	}
	
	/**
	 * Print out the daily price for one single stock.
	 * Use this to test the ability of reading intraday data from Yahoo API.
	 * @throws Exception
	 */
	public static void printDailyPriceForSingleStock() throws Exception {
		String symbol = "GOOG";
		MultiDaysCandleList mdstockCandleList = getMultipleDaysstockCandleList(Exchange.NASDAQ, symbol);
		System.out.println("Date Time Open High Low Close");
		for (int i = 0; i < mdstockCandleList.size(); i++) {
			IntraDaystockCandleList idstockCandleList = mdstockCandleList.get(i);
			System.out.println(idstockCandleList.getTimestamp() + " " + StockUtil.getRoundTwoDecimals(idstockCandleList.getOpen()) + " " 
				  +	StockUtil.getRoundTwoDecimals(idstockCandleList.getHigh()) + " " + StockUtil.getRoundTwoDecimals(idstockCandleList.getLow()) + " "
				  + StockUtil.getRoundTwoDecimals(idstockCandleList.getClose()));
			
		}
	}
}
