package test;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.TreeMap;

import org.joda.time.LocalDate;

import intraday.IntraDayLowHighIntervalMap;
import intraday.IntraDayPriceVolumeMap;
import intraday.IntraDayReaderGoogle;
import intraday.IntraDayReaderYahoo;
import intraday.IntraDayVolumeDistribution;
import intraday.IntraDaystockCandleList;
import intraday.MultiDaysCandleList;
import stock.DayTradingDistribution;
import stock.StockAPI;
import stock.StockCandle;
import stock.StockConst;
import stock.StockEnum.StockCandleClass;
import stock.StockMarketCap;
import stock.StockTurnoverRateDistribution;
import util.StockUtil;

public class AnalysisIntraDayTest {
	public static void main(String args[]) throws Exception {
//		testPrintDailyVolume();
//		testPrintDailyPrice();
//		testReadIntraDaystockCandleList();
//		testIntraDayVolumeDistribution();
//		testIntraDayHighLowInterval();
		testDayTradingDistribution();		
//		testTurnoverRate();
//		testIntraDayLowHighIntervalMap();
//		testIntraDayPriceVolumeMap();
	}

	
	private static void testPrintDailyVolume() throws Exception {
//		IntraDayAnalysisGoogle.printDailyVolumeForSingleStock();
		IntraDayReaderYahoo.printDailyVolumeForSingleStock();
	}
	
	private static void testPrintDailyPrice() throws Exception {
		IntraDayReaderYahoo.printDailyPriceForSingleStock();
	}
	

	
	private static void testIntraDayVolumeDistribution() {
		System.out.println(IntraDayVolumeDistribution.getVolumeRate(0));
	}
	
	private static void testAnalyzeSimplePriceModelGoogle() throws Exception {
		IntraDayReaderGoogle.analyzeSimplePriceModel();
	}
	
	private static void testIntraDayHighLowInterval() throws Exception {
		File directory = new File(StockConst.INTRADAY_DIRECTORY_PATH_GOOGLE);
//		int intervalLow[] = new int[391];
//		int intervalHigh[] = new int[391];
		int intervalWhiteLongHigh[] = new int[391];
		int intervalWhiteLongLow[] = new int[391];
		int intervalBlackLongHigh[] = new int[391];
		int intervalBlackLongLow[] = new int[391];
		int intervalUpperLongerHigh[] = new int[391];
		int intervalUpperLongerLow[] = new int[391];
		int intervalLowerLongerHigh[] = new int[391];
		int intervalLowerLongerLow[] = new int[391];
		int dailyCandleCount = 0;
		int highCount = 0;
		int lowCount = 0;
		int highAfterLowCount = 0;
		int highBeforeLowCount = 0;
		for (File file : directory.listFiles()) {
		
			String symbol = StockUtil.getSymbolFromFile(file);
			if (!StockMarketCap.isLargeMarketCap(symbol)) continue;
//			if (!symbol.equals("GOOG")) continue;
			MultiDaysCandleList mdstockCandleList = StockAPI.getIntraDaystockCandleListGoogle(file);
			dailyCandleCount += mdstockCandleList.size(); 
			for (int i = 0; i < mdstockCandleList.size(); i++) {
				IntraDaystockCandleList idstockCandleList = mdstockCandleList.get(i);
				Timestamp ts = idstockCandleList.getTimestamp();
				StockCandleClass candleClass = idstockCandleList.getCandleClass();
				switch (candleClass) {
				case WHITE_LONG:
//					countInterval(intervalWhiteLongHigh, idstockCandleList.getHighIntervals());
//					countInterval(intervalWhiteLongLow, idstockCandleList.getLowIntervals());
					
					ArrayList<Integer> highIntervals = idstockCandleList.getHighIntervals();
					ArrayList<Integer> lowIntervals = idstockCandleList.getLowIntervals();
					countInterval(intervalWhiteLongHigh, highIntervals);
					countInterval(intervalWhiteLongLow, lowIntervals);
					for (int j = 0; j < highIntervals.size(); j++) {
						for (int k = 0; k < lowIntervals.size(); k++) {
							if (highIntervals.get(j) > lowIntervals.get(k)) {
								highAfterLowCount++;
							}
							if (highIntervals.get(j) < lowIntervals.get(k)) {
								highBeforeLowCount++;
							}
						}						
					}
					break;
				case BLACK_LONG:
					countInterval(intervalBlackLongHigh, idstockCandleList.getHighIntervals());
					countInterval(intervalBlackLongLow, idstockCandleList.getLowIntervals());
					break;
				case UPPER_LONGER:
					countInterval(intervalUpperLongerHigh, idstockCandleList.getHighIntervals());
					countInterval(intervalUpperLongerLow, idstockCandleList.getLowIntervals());
					break;
				case LOWER_LONGER:
					countInterval(intervalLowerLongerHigh, idstockCandleList.getHighIntervals());
					countInterval(intervalLowerLongerLow, idstockCandleList.getLowIntervals());
					break;					
				}
//				intervalHigh[idstockCandleList.getHighInterval()]++;
//				intervalLow[idstockCandleList.getLowInterval()]++;
//				System.out.println(ts + " " + new Timestamp(ts.getTime() + idstockCandleList.getHighInterval() * 60000) + " " + idstockCandleList.getHighInterval() + " " + idstockCandleList.getHigh());
//				System.out.println(ts + " " + new Timestamp(ts.getTime() + idstockCandleList.getLowInterval() * 60000) + " " + idstockCandleList.getLowInterval() + " " + idstockCandleList.getLow());
				highCount += idstockCandleList.getHighIntervals().size();
				lowCount += idstockCandleList.getLowIntervals().size();
			}
			
		}
//		for (int i = 0; i < intervalWhiteLongHigh.length; i++) {
//			System.out.println(i + " " + intervalWhiteLongHigh[i]);
//		}
//		
//		for (int i = 0; i < intervalWhiteLongLow.length; i++) {
//			System.out.println(i + " " + intervalWhiteLongLow[i]);
//		}
		
		System.out.println("High Before Low for white long: " + highBeforeLowCount);
		System.out.println("High After Low for white long: " + highAfterLowCount);
		
		
//		for (int i = 0; i < intervalBlackLongHigh.length; i++) {
//			System.out.println(i + " " + intervalBlackLongHigh[i]);
//		}
//		
//		for (int i = 0; i < intervalBlackLongLow.length; i++) {
//			System.out.println(i + " " + intervalBlackLongLow[i]);
//		}
		
//		for (int i = 0; i < intervalUpperLongerHigh.length; i++) {
//			System.out.println(i + " " + intervalUpperLongerHigh[i]);
//		}
//		
//		for (int i = 0; i < intervalUpperLongerLow.length; i++) {
//			System.out.println(i + " " + intervalUpperLongerLow[i]);
//		}
//		
//		for (int i = 0; i < intervalLowerLongerHigh.length; i++) {
//			System.out.println(i + " " + intervalLowerLongerHigh[i]);
//		}
//		
//		for (int i = 0; i < intervalLowerLongerLow.length; i++) {
//			System.out.println(i + " " + intervalLowerLongerLow[i]);
//		}
		System.out.println("Total intra day candles: " + dailyCandleCount);
		System.out.println("Number of times that candles reach high: " + highCount);
		System.out.println("Number of times that candles reach low: " + lowCount);
	}
	
	private static void countInterval(int[] intervalCount, ArrayList<Integer> intervals) {
		for (int i = 0; i < intervals.size(); i++) {
			int interval = intervals.get(i);
			intervalCount[interval]++;			
		}
		
	}
	
	public static void testDayTradingDistribution() {
		double[] distribution = DayTradingDistribution.getDayTradingDistribution();
		for (int i = 0; i < distribution.length; i++) {
			System.out.println(distribution[i]);
		}
	}
	
	public static void testTurnoverRate() {
		double[] distribution = StockTurnoverRateDistribution.getDistribution();
		for (int i = 0; i < distribution.length; i++) {
			System.out.println(distribution[i]);
		}
	}

	public static void testIntraDayLowHighIntervalMap() throws Exception {
//		IntraDayLowHighIntervalMap.writeInterval();
		System.out.println(IntraDayLowHighIntervalMap.getLowInterval(StockCandleClass.WHITE_LONG));
		System.out.println(IntraDayLowHighIntervalMap.getHighInterval(StockCandleClass.WHITE_LONG));
		
	}
	
	public static void testIntraDayPriceVolumeMap() {
		StockCandle stockCandle = new StockCandle(new LocalDate(), 95.0, 105.0, 90.0, 110.0, 1000000);
		System.out.println("Candle class: " + stockCandle.getCandleClass());
		TreeMap<Integer, Long> map = new TreeMap<Integer, Long>(IntraDayPriceVolumeMap.getMap(stockCandle));
		for (int price : map.keySet()) {
			System.out.println(price + " " + map.get(price));
		}
	}
}



