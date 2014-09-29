package test;

import java.text.DecimalFormat;

import stock.StockCandle;
import stock.StockCandleArray;
import util.StockIndicator;
import yahoo.YahooParser;

public class IndicatorTest {
	
	private final static String FILENAME = "D:\\zzx\\Stock\\CSV\\JD.csv";
	private final static int MAX_CANDLE = 500;
	private final static DecimalFormat df = new DecimalFormat("0.00");
	
	public static void main(String args[]) {
//		testSimpleMovingAverageFakeData();
		testRSI();
//		testExponentialMovingAverage();
	}
	
	private static void testSimpleMovingAverageFakeData() {
		StockCandleArray stockCandleArray = new StockCandleArray();
		for (int i = 0; i < 100; i++) {
			StockCandle stockCandle = new StockCandle();
			stockCandle.close = i;
			stockCandleArray.add(stockCandle);
		}
		double[] movingAverage = StockIndicator.getSimpleMovingAverage(stockCandleArray, 10);
		for (int i = 0; i < 100; i++) {
			System.out.println(i + ": " + movingAverage[i]);
		}
	}
	
//	private static void testSimpleMovingAverageRealData() {
//		final String filename = "D:\\zzx\\Stock\\CSV\\CAMT.csv";
//		final int maxCandle = 200;
//		StockCandleArray stockCandleArray = YahooParser.readCSVFile(filename, maxCandle);
//				
//	}
	
	//Not working yet.
	private static void testRSI() {		
		int period = 14;
		StockCandleArray stockCandleArray = YahooParser.readCSVFile(FILENAME, MAX_CANDLE);
		double[] rsi = StockIndicator.getRSI(stockCandleArray, period);
		for (int i = 0; i < stockCandleArray.size(); i++) {
			System.out.println(stockCandleArray.getDate(i) + ": " + df.format(rsi[i]));
		}
	}
	
	private static void testExponentialMovingAverage() {
		StockCandleArray stockCandleArray = YahooParser.readCSVFile(FILENAME, MAX_CANDLE);
		int period = 20;
		double[] ema = StockIndicator.getExponentialMovingAverage(stockCandleArray, period);
		for (int i = 0; i < stockCandleArray.size(); i++) {
			System.out.println(stockCandleArray.getDate(i) + ": " + df.format(ema[i]));
		}
	}
}
