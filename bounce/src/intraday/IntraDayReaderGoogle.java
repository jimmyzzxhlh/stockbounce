package intraday;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import stock.StockConst;
import stock.StockMarketCap;
import util.StockUtil;

/**
 * @deprecated
 * This class is pretty much depracated as the Google data doesn't seem to be too reliable.
 * @author jimmyzzxhlh-Dell
 *
 */
public class IntraDayReaderGoogle {
	
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
	 * Print out the daily volume for one single stock.
	 * Use this to test whether the sum of the intraday volume is correct.
	 * @throws Exception
	 */
	public static void printDailyVolumeForSingleStock() throws Exception {
		String symbol = "GOOG";
		File file = new File(StockConst.INTRADAY_DIRECTORY_PATH_GOOGLE + symbol + ".txt");
		MultiDaysStockCandleArray mdStockCandleArray = getIntraDayStockCandleArray(file);
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
	 * Get a intra day stock candle from the input line.
	 * It has the one of the following formats:
	 * a1415629800,12.11,12.11,12.11,12.11,4845
     * 30,12.2,12.2,12.18,12.18,300
	 * @param line
	 * @return
	 */
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
	
	/**
	 * Return an array of an array of intraday stock candles.
	 * A intraday data file downloaded from Google API contains at most 15 days.
	 * Each day contains at most 391 intraday stock candles (from 8:30 AM to 3:00 PM, inclusively).
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static MultiDaysStockCandleArray getIntraDayStockCandleArray(File file) throws Exception {
		//Create an object of multi-days stock candle array.
		String symbol = StockUtil.getSymbolFromFile(file);
		MultiDaysStockCandleArray mdStockCandleArray = new MultiDaysStockCandleArray(symbol);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		//Skip the first few lines until we hit the first day that needs to be processed.
		while ((line = br.readLine()) != null) {
			if (line.startsWith("a")) break;
		}
		
		while (line != null) {
			IntraDayStockCandleArray idStockCandleArray = new IntraDayStockCandleArray();
			//Process the first intraday stock candle, which has the timestamp.
			Timestamp ts = getTimestamp(line);
			idStockCandleArray.setTimeStamp(ts);
			idStockCandleArray.setSymbol(symbol);
			IntraDayStockCandle idStockCandle = null;
			idStockCandle = getIntraDayStockCandle(line);
			if (idStockCandle != null) {
				idStockCandleArray.add(idStockCandle);
			}
			else {
				System.out.println(symbol + " has invalid interval: " + line);
				break;
			}
			//Process the rest of the intraday stock candles.
			while ((line = br.readLine()) != null) {
				//If we get to the next day then quit.
				if (line.startsWith("a")) {						
					break;					
				}
				idStockCandle = getIntraDayStockCandle(line);
				if (idStockCandle != null) {
					idStockCandleArray.add(idStockCandle);
				}
				else {
					System.out.println(symbol + " has invalid interval: " + line);
					continue;
				}					
			}
			//Set open, close, high, low attributes for the current day.
			idStockCandleArray.setOpen();
			idStockCandleArray.setClose();
			idStockCandleArray.setHigh();
			idStockCandleArray.setLow();
			mdStockCandleArray.add(idStockCandleArray);
		}
		return mdStockCandleArray;
	}
	
	/**
	 * Analyze the simplest intraday model.
	 * Given open, high, low, close, we select the average of the four price.
	 * Then we assume that everybody is at this price and given the volume we will
	 * know how much capital (money) is traded.
	 * This can be compared to the intraday price/volume and we can get the amount of
	 * error for the total capital.
	 * Our new price model should perform better than this simplest model, hopefully.
	 * The simplest model turns out to be pretty good. Most of the symbols have an error
	 * percentage of < 1% for the total capital.
	 */
	public static void analyzeSimplePriceModel() throws Exception {
		File directory = new File(StockConst.INTRADAY_DIRECTORY_PATH_GOOGLE);
		for (File file : directory.listFiles()) {
			String symbol = StockUtil.getSymbolFromFile(file);
//			if (!StockMarketCap.isLargeMarketCap(symbol)) continue;
			MultiDaysStockCandleArray mdStockCandleArray = getIntraDayStockCandleArray(file);
			double estimatedTotalCapital = 0;
			double realTotalCapital = 0;
			for (int i = 0; i < mdStockCandleArray.size(); i++) {
				IntraDayStockCandleArray idStockCandleArray = mdStockCandleArray.get(i);
				double averagePrice = (idStockCandleArray.getOpen() + idStockCandleArray.getClose() + idStockCandleArray.getHigh() + idStockCandleArray.getLow()) * 0.25;
				estimatedTotalCapital += averagePrice * idStockCandleArray.getVolume();
				realTotalCapital += idStockCandleArray.getTotalCapital();				
			}
			double errorTotalCapital = (estimatedTotalCapital - realTotalCapital) / realTotalCapital * 100;
			System.out.println(symbol + " " + StockUtil.getRoundDecimals(errorTotalCapital, 5) + "%");
		}
	}

	
	
//	/**
//	 * Not useful for now.
//	 * @throws Exception
//	 */
//	public static void analyzePrice() throws Exception {
//		File directory = new File(StockConst.INTRADAY_DIRECTORY_PATH);
//		File[] directoryList = directory.listFiles();
//		double percentageFromLow = 0;
//		double distanceWhite[] = new double[391];
//		double distanceBlack[] = new double[391];
//		int intervalCount[] = new int[391];
//		int intervalCountLowWhite[] = new int[391];
//		int intervalCountLowBlack[] = new int[391];
//		for (File file : directoryList) {
//			BufferedReader br = new BufferedReader(new FileReader(file));
//			String symbol = StockUtil.getSymbolFromFile(file);
////			if (!StockMarketCap.isLargeMarketCap(symbol)) continue;
//			System.out.println(symbol);
////			if (!symbol.equals("GOOG")) continue;
//			String line;
//			//Skip the first few lines until we hit the data that needs to be processed.
//			while ((line = br.readLine()) != null) {
//				if (line.startsWith("a")) break;
//			}
//			
//			while (line != null) {
//				ArrayList<IntraDayStockCandle> idStockCandleArray = new ArrayList<IntraDayStockCandle>();
//				double open,close,high,low;
//				open = close = high = low = 0;
//				//Process the first stock candle
//				Timestamp ts = getTimestamp(line);
//				IntraDayStockCandle idStockCandle = null;
//				idStockCandle = getIntraDayStockCandle(line);
//				if (idStockCandle != null) {
//					idStockCandleArray.add(idStockCandle);
//					//Initialize open/high/low.
//					open = idStockCandle.getOpen();
//					high = idStockCandle.getHigh();
//					low = idStockCandle.getLow();
//					
//				}
//				else {
//					System.out.println(symbol + " has invalid interval: " + line);
//					break;
//				}
//				while ((line = br.readLine()) != null) {
//					if (line.startsWith("a")) {						
//						break;					
//					}
//					idStockCandle = getIntraDayStockCandle(line);
//					if (idStockCandle != null) {
//						idStockCandleArray.add(idStockCandle);
//						if (idStockCandle.getHigh() > high) high = idStockCandle.getHigh();
//						if (idStockCandle.getLow() < low) low = idStockCandle.getLow();
//					}
//					else {
//						System.out.println(symbol + " has invalid interval: " + line);
//						continue;
//					}					
//				}
//				close = idStockCandle.getClose();  //This line may throw out null error if the interval is invalid.
////				System.out.println(ts + ": " + "Open: " + StockUtil.getRoundTwoDecimals(open) + ", High: " + StockUtil.getRoundTwoDecimals(high) + ", Low: " + StockUtil.getRoundTwoDecimals(low) + ", Close: " + StockUtil.getRoundTwoDecimals(close));
//				double totalLength = high - low;
//				
//				//Sanity check
//				if (totalLength <= 0) {
//					if (totalLength < 0) {
//						System.err.println(symbol + " has invalid low / high on " + ts + " (" + ts.getTime() + ")");
//					}
//					continue;
//				}
//				
//				for (int i = 0; i < idStockCandleArray.size(); i++) {
//					idStockCandle = idStockCandleArray.get(i);
//					intervalCount[idStockCandle.getInterval()]++;
//					if (Math.abs(idStockCandle.getLow() - low) < 0.01) {
//						if (close > open) {
//							intervalCountLowWhite[idStockCandle.getInterval()]++;
//						}
//						else {
//							intervalCountLowBlack[idStockCandle.getInterval()]++;
//						}
//					}
////					double percentageFromLow = (idStockCandle.getMidPrice() - low) * 1.0 / totalLength;					
//					
////					if (close > open) {
////						distanceWhite[idStockCandle.getInterval()] += percentageFromLow;	
////					}
////					else if (close < open) {
////						distanceBlack[idStockCandle.getInterval()] += percentageFromLow;
////					}
//					
////					double percentageFromOpen = (idStockCandle.getMidPrice() - open) * 1.0 / totalLength;
////					double percentageFromOpen = (idStockCandle.getMidPrice() - open) * 1.0 / open;
////					if ((close > open) && ((open - low) / open > 0.03)) {
////						distanceWhite[idStockCandle.getInterval()] += percentageFromOpen;						
////					}
////					else if (close < open) {
////						distanceBlack[idStockCandle.getInterval()] += percentageFromOpen;
////					}
//				}
//			}
//		}
//		for (int i = 0; i < intervalCountLowWhite.length; i++) {
//			System.out.println(i + " " + intervalCountLowWhite[i]);
//		}
//		for (int i = 0; i < intervalCountLowBlack.length; i++) {
//			System.out.println(i + " " + intervalCountLowBlack[i]);
//		}
////		for (int i = 0; i < distanceWhite.length; i++) {
////			System.out.println(i + " " + distanceWhite[i] / intervalCount[i]);
////		}
//		
////		for (int i = 0; i < distanceBlack.length; i++) {
////			System.out.println(i + " " + distanceBlack[i] / intervalCount[i]);
////		}
//		
////		for (int i = 0; i < intervalCount.length; i++) {
////			System.out.println(i + " " + intervalCount[i]);
////		}
//	}
}

