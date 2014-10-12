package test;

import indicator.StockGain;
import indicator.StockIndicatorAPI;

import java.text.DecimalFormat;

import stock.StockCandle;
import stock.StockCandleArray;
import yahoo.YahooParser;

public class IndicatorTest {
	
	private final static String FILENAME = "D:\\zzx\\Stock\\CSV\\ZIPR.csv";
	private final static int MAX_CANDLE = 500;
	private final static DecimalFormat df = new DecimalFormat("0.00");
	
	public static void main(String args[]) {
//		testSimpleMovingAverageFakeData();
		testRSI();
//		testExponentialMovingAverage();
//		testStandardDeviation();
//		testBollingerBands();
//		testMACD();
//		testEMACoefficient();
	}
	
	private static void testSimpleMovingAverageFakeData() {
		StockCandleArray stockCandleArray = new StockCandleArray();
		for (int i = 0; i < 100; i++) {
			StockCandle stockCandle = new StockCandle();
			stockCandle.close = i;
			stockCandleArray.add(stockCandle);
		}
		double[] movingAverage = StockIndicatorAPI.getSimpleMovingAverage(stockCandleArray, 10);
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
	
	private static void testRSI() {		
		int period = 14;
		StockCandleArray stockCandleArray = YahooParser.readCSVFile(FILENAME, MAX_CANDLE);
		double[] rsi = StockIndicatorAPI.getRSI(stockCandleArray, period);
		for (int i = 0; i < stockCandleArray.size(); i++) {
			System.out.println(stockCandleArray.getDate(i) + ": " + df.format(rsi[i]));
		}
	}
	
	private static void testExponentialMovingAverage() {
		StockCandleArray stockCandleArray = YahooParser.readCSVFile(FILENAME, MAX_CANDLE);
		int period = 20;
		double[] ema = StockIndicatorAPI.getExponentialMovingAverage(stockCandleArray, period);
		for (int i = 0; i < stockCandleArray.size(); i++) {
			System.out.println(stockCandleArray.getDate(i) + ": " + df.format(ema[i]));
		}
	}
	
	private static void testStandardDeviation() {
		StockCandleArray stockCandleArray = new StockCandleArray();
		stockCandleArray.add(createStockCandle(2));
		stockCandleArray.add(createStockCandle(4));
		stockCandleArray.add(createStockCandle(4));
		stockCandleArray.add(createStockCandle(4));
		stockCandleArray.add(createStockCandle(5));
		stockCandleArray.add(createStockCandle(5));
		stockCandleArray.add(createStockCandle(7));
		stockCandleArray.add(createStockCandle(9));
		double[] sd = StockIndicatorAPI.getStandardDeviation(stockCandleArray, 8);
		for (int i = 0; i < sd.length; i++) {
			System.out.println(i + ": " + sd[i]);
		}
	}
	
	private static StockCandle createStockCandle(double data) {
		StockCandle stockCandle = new StockCandle();
		stockCandle.close = data;
		return stockCandle;
	}
	
	public static void testBollingerBands() {
		StockCandleArray stockCandleArray = YahooParser.readCSVFile(FILENAME, MAX_CANDLE);
		int period = 20;
		int k = 2;
		double[][] bb = StockIndicatorAPI.getBollingerBands(stockCandleArray, period, k);
		for (int i = 0; i < stockCandleArray.size(); i++) {
			System.out.println(stockCandleArray.getDate(i) + ": " + df.format(bb[0][i]) + " " + df.format(bb[2][i]));
		}
	}
	
	public static void testMACD() {
		StockCandleArray stockCandleArray = YahooParser.readCSVFile(FILENAME, MAX_CANDLE);
		int shortPeriod = 12;
		int longPeriod = 26;
		int macdAveragePeriod = 9;
		double[][] macd = StockIndicatorAPI.getMACD(stockCandleArray, shortPeriod, longPeriod, macdAveragePeriod);
		for (int i = 0; i < stockCandleArray.size(); i++) {
			System.out.println(stockCandleArray.getDate(i) + ": " + df.format(macd[0][i]) + " " + df.format(macd[1][i]) + " " + df.format(macd[2][i]));
		}
	}
	
	public static void testEMACoefficient() {
		int period = 20;
		double sum = 0;
		double[] emaCoefficient = StockGain.getExponentialMovingAverageCoefficient(period);
		for (int i = 0; i < period; i++) {
			sum += emaCoefficient[i];
			System.out.println(emaCoefficient[i]);
			
		}
//		System.out.println(emaCoefficient[0]);
//		System.out.println(emaCoefficient[1]);
//		System.out.println(emaCoefficient[period - 1]);
		System.out.println("sum: " + sum);
		
	}
}
