package intraday;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import stock.StockConst;
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
	private static IntraDayStockCandle getIntraDayStockCandle(String line) {
		String data[] = line.split(",");
		Timestamp ts;
		if (data.length <= 5) {
			System.err.println("Data fields should be 6, but it is " + data.length + ": " + line);
		}
		
		ts = getTimestamp(line);
		int interval = getIntervalFromTimestamp(ts);
		if (!isValidInterval(interval)) {
			System.err.println("Interval invalid for: " + line);
			return null;
		}
		IntraDayStockCandle idStockCandle = new IntraDayStockCandle();
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
	 * A intraday data file downloaded from Google API contains at most 15 days.
	 * Each day contains at most 391 intraday stock candles (from 8:30 AM to 3:00 PM, inclusively).
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static MultiDaysStockCandleArray getMultipleDaysStockCandleArray(String symbol) throws Exception {
		//Create an object of multi-days stock candle array.
		MultiDaysStockCandleArray mdStockCandleArray = new MultiDaysStockCandleArray(symbol);
		//Read from a folder
		File directory = new File(StockConst.INTRADAY_DIRECTORY_PATH_YAHOO + symbol + "\\");
		for (File file : directory.listFiles()) {
			System.out.println(file.getAbsolutePath());
			IntraDayStockCandleArray idStockCandleArray = getIntraDayStockCandleArray(symbol, file);
			mdStockCandleArray.add(idStockCandleArray);
			
		}
		//Sort by dates.
		mdStockCandleArray.sortByDate();
		return mdStockCandleArray;
	}
	
	public static IntraDayStockCandleArray getIntraDayStockCandleArray(String symbol, File file) throws Exception {
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
			return null;
		}
		IntraDayStockCandleArray idStockCandleArray = new IntraDayStockCandleArray();
		Date date = StockUtil.parseDate(StockUtil.getFilenameWithoutExtension(file.getName()));
		idStockCandleArray.setDate(date);
		//Store the first timestamp in idStockCandleArray. Usually it should be 8:30 AM in the morning.
		idStockCandleArray.setTimeStamp(getTimestamp(line));
		idStockCandleArray.setSymbol(symbol);
		while (line != null) {
			//Process the first intraday stock candle, which has the timestamp.
			IntraDayStockCandle idStockCandle = getIntraDayStockCandle(line);
			if (idStockCandle != null) {
				idStockCandleArray.add(idStockCandle);
			}			
			line = br.readLine();
		}
		//Set open, close, high, low attributes for the current day.
		idStockCandleArray.setOpen();
		idStockCandleArray.setClose();
		idStockCandleArray.setHigh();
		idStockCandleArray.setLow();
		br.close();
		return idStockCandleArray;
	}
	
	public static IntraDayStockCandleArray getIntraDayStockCandleArray(String symbol, String dateString) throws Exception {
		File file = new File(StockConst.INTRADAY_DIRECTORY_PATH_YAHOO + symbol + "\\" + dateString + ".txt");
		if (!file.exists()) {
			System.err.println("There is no data for " + symbol + " on " + dateString);
			return null;
		}
		return getIntraDayStockCandleArray(symbol, file);
	}
	/*******************************Below are testing functions for the intraday reader********************************/
	/**
	 * Print out the daily volume for one single stock.
	 * Use this to test whether the sum of the intraday volume is correct.
	 * @throws Exception
	 */
	public static void printDailyVolumeForSingleStock() throws Exception {
		String symbol = "GOOG";
		MultiDaysStockCandleArray mdStockCandleArray = getMultipleDaysStockCandleArray(symbol);
		for (int i = 0; i < mdStockCandleArray.size(); i++) {
			long volumeDay = 0;
			IntraDayStockCandleArray idStockCandleArray = mdStockCandleArray.get(i);
			for (int j = 0; j < idStockCandleArray.size(); j++) {
				volumeDay += idStockCandleArray.get(j).getVolume();
			}
			System.out.println(idStockCandleArray.getTimestamp() + " " + volumeDay);
		}
	}
	
	/**
	 * Print out the daily price for one single stock.
	 * Use this to test the ability of reading intraday data from Yahoo API.
	 * @throws Exception
	 */
	public static void printDailyPriceForSingleStock() throws Exception {
		String symbol = "GOOG";
		MultiDaysStockCandleArray mdStockCandleArray = getMultipleDaysStockCandleArray(symbol);
		System.out.println("Date Time Open High Low Close");
		for (int i = 0; i < mdStockCandleArray.size(); i++) {
			IntraDayStockCandleArray idStockCandleArray = mdStockCandleArray.get(i);
			System.out.println(idStockCandleArray.getTimestamp() + " " + StockUtil.getRoundTwoDecimals(idStockCandleArray.getOpen()) + " " 
				  +	StockUtil.getRoundTwoDecimals(idStockCandleArray.getHigh()) + " " + StockUtil.getRoundTwoDecimals(idStockCandleArray.getLow()) + " "
				  + StockUtil.getRoundTwoDecimals(idStockCandleArray.getClose()));
			
		}
	}
}
