package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import pattern.StockPattern;
import stock.StockCandle;
import stock.StockCandleArray;
import stock.StockEnum.StockCandleDataType;
import util.StockUtil;
import yahoo.YahooParser;

/**
 * Main abstract class for testing the pattern. 
 * @author jimmyzzxhlh
 */
public abstract class PatternTest {
	private static final String CSV_DIRECTORY_PATH = "D:\\zzx\\Stock\\CSV\\";
	private static final String OUTPUT_DIRECTORY_PATH = "D:\\zzx\\Stock\\";
	private static final int STOCK_CANDLE_ARRAY_NORMALIZE_DAYS = 250;
	private static final double STOCK_CANDLE_NORMALIZE_MAX = 500;
	private static final int TEST_DAYS = 250;
	private static final int MIN_VOLUME = 20000;
	
	public void testChart() throws Exception {
		File directory = new File(CSV_DIRECTORY_PATH);
		File[] directoryList = directory.listFiles();
		File outputFile = new File(OUTPUT_DIRECTORY_PATH + "engulfing.csv");
		StockCandleArray stockCandleArray, originalStockCandleArray;
		if (directoryList == null) return;
		outputFile.createNewFile();
		FileWriter fw = new FileWriter(outputFile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		//Write the first header line in the CSV file.		
		bw.write("Symbol" + ",");
		bw.write("Date" + ",");
		bw.write("Current Close" + ",");
		bw.write("Next Open" + ",");
		for (int days = 5; days <= 15; days+=5) {
			bw.write(days + "d Max Increase %" + ",");
			bw.write(days + "d Min Decrease %" + ",");
		}
		bw.newLine();
		
		for (File csvFile : directoryList) {
			//Initialize parser and parse each line of the stock data.
			System.out.println(csvFile.getName());
			stockCandleArray = YahooParser.readCSVFile(csvFile, 0);
			//Store a copy of the stock candle array at the beginning so that each time the stock
			//candle array can be messed up.
			originalStockCandleArray = new StockCandleArray(stockCandleArray);
			for (int i = stockCandleArray.size() - 1; i > stockCandleArray.size() - TEST_DAYS; i--) {
				if (i < 0) break;
				int start = i - STOCK_CANDLE_ARRAY_NORMALIZE_DAYS + 1;
				int end = i;
				if (start < 0) start = 0;
				if (stockCandleArray.get(i).volume < MIN_VOLUME) continue;
				//Normalize the stock array. This is very important as normalization will help eliminate
				//the affect of absolute price itself.
				stockCandleArray.normalizeStockCandle(STOCK_CANDLE_NORMALIZE_MAX, start, end);
				//Check whether any pattern occurs.
				checkPattern(bw, end, stockCandleArray, originalStockCandleArray);
				//Restore the stock candle array 
				stockCandleArray = new StockCandleArray(originalStockCandleArray);
			}			
		}
		bw.close();
		fw.close();
	}
	
	/**
	 * Check whether a stock candle array has specified patterns. If yes, output the position where
	 * we enter the market, and output the positions for next several days.
	 * @param bw Buffered writer for output
	 * @param index The current index considered in the stock candle array.
	 * @param stockCandleArray Stock candle array (normalized).
	 * @param originalStockCandleArray Original stock candle array that has not been normalized yet. For output use.
	 * @throws Exception
	 */
	private void checkPattern(BufferedWriter bw, int index, StockCandleArray stockCandleArray, StockCandleArray originalStockCandleArray) throws Exception {
		StockPattern stockPattern = new StockPattern(stockCandleArray.getStockCandleArray());
		stockPattern.setSymbol(stockCandleArray.getSymbol());
		YahooDrawPattern drawPattern = new YahooDrawPattern();
		if (index >= originalStockCandleArray.size() - 1) return;
		double nextOpen = originalStockCandleArray.get(index + 1).open;
		
		if (hasPattern(stockPattern, index)) {
			bw.write(stockPattern.getSymbol() + ",");
			bw.write(stockPattern.getDate(index) + ",");
			//Write current close price
			bw.write(StockCandleArray.formatPrice(originalStockCandleArray.get(index).close) + ",");
			//Write next day's open price if exists
			if (index < originalStockCandleArray.size() - 1) {
				bw.write(StockCandleArray.formatPrice(nextOpen) + ",");
			}
			else {
				bw.write(0 + ",");
			}
			
			//Write prices for the next several days.
			for (int days = 5; days <= 15; days+=5) {
				double maxIncreaseRate = StockUtil.changeRate(nextOpen, originalStockCandleArray.getMaxStockPrice(index, days, StockCandleDataType.HIGH));
				double maxDecreaseRate = StockUtil.changeRate(nextOpen, originalStockCandleArray.getMinStockPrice(index, days, StockCandleDataType.LOW));
				bw.write(StockCandleArray.formatPrice(maxIncreaseRate * 100) + "%,");
				bw.write(StockCandleArray.formatPrice(maxDecreaseRate * 100) + "%,");
			}
			bw.newLine();
			
			drawPattern.setPatternIndex(index);
			drawPattern.drawChart(stockCandleArray);
		}
	}
	
	/**
	 * Abstract class for checking whether a pattern exists or not. All pattern tests need to implement
	 * this function.
	 * @param stockPattern Stock pattern class, which contains the API functions for checking whether a pattern exists.
	 * @param index Current index (day) considered in the stock candle array.
	 * @return True if pattern exists, False otherwise.
	 */
	public abstract boolean hasPattern(StockPattern stockPattern, int index);

}
