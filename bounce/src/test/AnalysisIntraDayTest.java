package test;

import intraday.IntraDayAnalysisGoogle;
import intraday.IntraDayAnalysisYahoo;
import intraday.IntraDayStockCandle;
import intraday.IntraDayStockCandleArray;
import intraday.IntraDayVolumeDistribution;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Timestamp;
import java.util.ArrayList;

import stock.StockConst;
import stock.StockMarketCap;
import stock.StockEnum.StockIntraDayClass;
import util.StockUtil;

public class AnalysisIntraDayTest {
	public static void main(String args[]) throws Exception {
//		testPrintDailyVolume();
//		testPrintDailyPrice();
//		testReadIntraDayStockCandleArray();
//		testIntraDayVolumeDistribution();
//		testIntraDayHighLowInterval();
		testAnalyzeSimplePriceModelGoogle();
	}
	
	private static void testReadIntraDayStockGoogle() throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(StockConst.INTRADAY_DIRECTORY_PATH_GOOGLE + "GGACU.txt"));
		String line;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
		br.close();
	}

	
	private static void testPrintDailyVolume() throws Exception {
//		IntraDayAnalysisGoogle.printDailyVolumeForSingleStock();
		IntraDayAnalysisYahoo.printDailyVolumeForSingleStock();
	}
	
	private static void testPrintDailyPrice() throws Exception {
		IntraDayAnalysisYahoo.printDailyPriceForSingleStock();
	}
	
	
	private static void testReadIntraDayStockCandleArrayGoogle() throws Exception {
		String symbol = "GOOG";
		File file = new File(StockConst.INTRADAY_DIRECTORY_PATH_GOOGLE + symbol + ".txt");
		ArrayList<IntraDayStockCandleArray> mdStockCandleArray = IntraDayAnalysisGoogle.getIntraDayStockCandleArray(file);
		for (int i = 0; i < mdStockCandleArray.size(); i++) {
			IntraDayStockCandleArray idStockCandleArray = mdStockCandleArray.get(i);
			System.out.println(idStockCandleArray.getTimestamp());
			for (int j = 0; j < idStockCandleArray.size(); j++) {
				IntraDayStockCandle idStockCandle = idStockCandleArray.get(j);
				System.out.print(idStockCandle.getInterval() + ": ");
				System.out.print(StockUtil.getRoundTwoDecimals(idStockCandle.close) + " ");
				System.out.print(StockUtil.getRoundTwoDecimals(idStockCandle.high) + " ");
				System.out.print(StockUtil.getRoundTwoDecimals(idStockCandle.low) + " ");
				System.out.print(StockUtil.getRoundTwoDecimals(idStockCandle.open) + " ");
				System.out.print(idStockCandle.volume + " ");
				System.out.println();
				
			}
		}
	}
	
	private static void testIntraDayVolumeDistribution() {
		System.out.println(IntraDayVolumeDistribution.getVolumeRate(0));
	}
	
	private static void testAnalyzeSimplePriceModelGoogle() throws Exception {
		IntraDayAnalysisGoogle.analyzeSimplePriceModel();
	}
	
	private static void testIntraDayHighLowInterval() throws Exception {
		File directory = new File(StockConst.INTRADAY_DIRECTORY_PATH_GOOGLE);
		File[] directoryList = directory.listFiles();
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
		for (File file : directoryList) {
		
			String symbol = StockUtil.getSymbolFromFile(file);
			if (!StockMarketCap.isLargeMarketCap(symbol)) continue;
//			if (!symbol.equals("GOOG")) continue;
			ArrayList<IntraDayStockCandleArray> mdStockCandleArray = IntraDayAnalysisGoogle.getIntraDayStockCandleArray(file);
			dailyCandleCount += mdStockCandleArray.size(); 
			for (int i = 0; i < mdStockCandleArray.size(); i++) {
				IntraDayStockCandleArray idStockCandleArray = mdStockCandleArray.get(i);
				Timestamp ts = idStockCandleArray.getTimestamp();
				StockIntraDayClass intraDayClass = idStockCandleArray.getIntraDayClass();
				switch (intraDayClass) {
				case WHITE_LONG:
//					countInterval(intervalWhiteLongHigh, idStockCandleArray.getHighIntervals());
//					countInterval(intervalWhiteLongLow, idStockCandleArray.getLowIntervals());
					
					ArrayList<Integer> highIntervals = idStockCandleArray.getHighIntervals();
					ArrayList<Integer> lowIntervals = idStockCandleArray.getLowIntervals();
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
					countInterval(intervalBlackLongHigh, idStockCandleArray.getHighIntervals());
					countInterval(intervalBlackLongLow, idStockCandleArray.getLowIntervals());
					break;
				case UPPER_LONGER:
					countInterval(intervalUpperLongerHigh, idStockCandleArray.getHighIntervals());
					countInterval(intervalUpperLongerLow, idStockCandleArray.getLowIntervals());
					break;
				case LOWER_LONGER:
					countInterval(intervalLowerLongerHigh, idStockCandleArray.getHighIntervals());
					countInterval(intervalLowerLongerLow, idStockCandleArray.getLowIntervals());
					break;					
				}
//				intervalHigh[idStockCandleArray.getHighInterval()]++;
//				intervalLow[idStockCandleArray.getLowInterval()]++;
//				System.out.println(ts + " " + new Timestamp(ts.getTime() + idStockCandleArray.getHighInterval() * 60000) + " " + idStockCandleArray.getHighInterval() + " " + idStockCandleArray.getHigh());
//				System.out.println(ts + " " + new Timestamp(ts.getTime() + idStockCandleArray.getLowInterval() * 60000) + " " + idStockCandleArray.getLowInterval() + " " + idStockCandleArray.getLow());
				highCount += idStockCandleArray.getHighIntervals().size();
				lowCount += idStockCandleArray.getLowIntervals().size();
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
	
}



