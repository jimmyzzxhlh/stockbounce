package test;

import java.text.DecimalFormat;

import stock.StockCandle;
import stock.StockCandleArray;
import stock.StockConst;
import util.StockUtil;
import yahoo.YahooParser;

public class ResistLevelVolumeTest {
	private static final String CSV_FILENAME = StockConst.STOCK_CSV_DIRECTORY_PATH + "CAMT.csv";
	private static final int MAX_CANDLE = 10000;
	private static final int INTERVAL_DENOMINATOR = 15;
	
	public static void main(String args[]) throws Exception {
		ResistLevelVolumeTest mainProgram = new ResistLevelVolumeTest();
		mainProgram.testChart();
	}
	
	public void testChart() throws Exception {
		StockCandleArray stockCandleArray = YahooParser.readCSVFile(CSV_FILENAME, MAX_CANDLE);
		double maxPrice = 0;
		double minPrice = 1e10;
		
		for (int i = 0; i < stockCandleArray.size(); i++) {
			//Get maximum and minimum price
			StockCandle stockCandle = stockCandleArray.get(i);
			if (stockCandle.high > maxPrice) maxPrice = stockCandle.high;
			if (stockCandle.low < minPrice) minPrice = stockCandle.low;
		}

		double intervalRange = StockUtil.getRoundTwoDecimals(maxPrice / INTERVAL_DENOMINATOR);
		System.out.println(intervalRange);
		int intervalNum = StockUtil.getIntervalNum(minPrice, maxPrice, intervalRange);
		
		int[] intervalVolumeArray = new int[intervalNum];
		double[] intervalPriceArray = new double[intervalNum];
		double price = StockUtil.getRoundTwoDecimals((int)(minPrice * 1.0 / intervalRange) * intervalRange);
		
		for (int i = 0; i < intervalNum; i++) {
			intervalPriceArray[i] = price;
			price += intervalRange;
		}
		//for (int i = 0; i < intervalNum; i++) System.out.print(intervalPriceArray[i] + " ");
		
		for (int i = stockCandleArray.size() - 1; i >= 0; i--) {
			StockCandle stockCandle = stockCandleArray.get(i);
			double high = stockCandle.high;
			double low = stockCandle.low;
			int volumeSplit = (int)(stockCandle.volume * 1.0 / StockUtil.getIntervalNum(low, high, intervalRange));
			for (int j = 0; j < intervalPriceArray.length; j++) {
				if (intervalPriceArray[j] < low) continue;
				if (intervalPriceArray[j] > high) break;
				intervalVolumeArray[j] += volumeSplit;
			}
		}
		
		for (int i = 0; i < intervalNum - 1; i++) {
			for (int j = i + 1; j < intervalNum; j++) {
				if (intervalVolumeArray[i] < intervalVolumeArray[j]) {
					int tempInt = intervalVolumeArray[i];
					intervalVolumeArray[i] = intervalVolumeArray[j];
					intervalVolumeArray[j] = tempInt;
					double tempDouble = intervalPriceArray[i];
					intervalPriceArray[i] = intervalPriceArray[j];
					intervalPriceArray[j] = tempDouble;
				}
			}
		}
		
		DecimalFormat df = new DecimalFormat("0.00");
		for (int i = 0; i < intervalNum; i++) {
			System.out.println(df.format(intervalPriceArray[i]) +  ": " + intervalVolumeArray[i]);
		}
	}

}
