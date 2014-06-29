package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import pattern.StockPattern;
import stock.StockAPI;
import stock.StockCandle;
import stock.StockCandleArray;
import stock.StockEnum.StockCandleDataType;
import yahoo.YahooParser;

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
			System.out.println(csvFile.getName());
			YahooParser parser = new YahooParser(csvFile);
			stockCandleArray = new StockCandleArray();
			String filename = csvFile.getName();
			String symbol = filename.substring(0, filename.length() - 4);
			stockCandleArray.setSymbol(symbol);
			parser.startReadFile();
			String line;
			while ((line = parser.nextLine()) != null) {
				StockCandle stockCandle = new StockCandle();
				parser.parseLine(line, stockCandle);
				stockCandleArray.add(stockCandle);
			}
			parser.closeFile();
			stockCandleArray.sortByDate();
			originalStockCandleArray = new StockCandleArray(stockCandleArray);
			for (int i = stockCandleArray.size() - 1; i > stockCandleArray.size() - TEST_DAYS; i--) {
				if (i < 0) break;
				int start = i - STOCK_CANDLE_ARRAY_NORMALIZE_DAYS + 1;
				int end = i;
				if (start < 0) start = 0;
				if (stockCandleArray.get(i).volume < MIN_VOLUME) continue;
				stockCandleArray.normalizeStockCandle(STOCK_CANDLE_NORMALIZE_MAX, start, end);
				checkPattern(bw, end, stockCandleArray, originalStockCandleArray);
				stockCandleArray = new StockCandleArray(originalStockCandleArray);
			}
			
		}
		bw.close();
		fw.close();
	}
	
	
	private void checkPattern(BufferedWriter bw, int index, StockCandleArray stockCandleArray, StockCandleArray originalStockCandleArray) throws Exception {
		StockPattern stockPattern = new StockPattern(stockCandleArray.getStockCandleArray());
		stockPattern.setSymbol(stockCandleArray.getSymbol());
		YahooDrawPattern drawPattern = new YahooDrawPattern();
		if (index >= originalStockCandleArray.size() - 1) return;
		double nextOpen = originalStockCandleArray.get(index + 1).open;
		
		if (hasPattern(stockPattern, index)) {
			bw.write(stockPattern.getSymbol() + ",");
			bw.write(stockPattern.getDate(index) + ",");
			bw.write(StockCandleArray.formatPrice(originalStockCandleArray.get(index).close) + ",");
			if (index < originalStockCandleArray.size() - 1) {
				bw.write(StockCandleArray.formatPrice(nextOpen) + ",");
			}
			else {
				bw.write(0 + ",");
			}
			
			for (int days = 5; days <= 15; days+=5) {
				double maxIncreaseRate = StockAPI.changeRate(nextOpen, originalStockCandleArray.getMaxStockPrice(index, days, StockCandleDataType.HIGH));
				double maxDecreaseRate = StockAPI.changeRate(nextOpen, originalStockCandleArray.getMinStockPrice(index, days, StockCandleDataType.LOW));
				bw.write(StockCandleArray.formatPrice(maxIncreaseRate * 100) + "%,");
				bw.write(StockCandleArray.formatPrice(maxDecreaseRate * 100) + "%,");
			}
			bw.newLine();
			
			drawPattern.setPatternIndex(index);
			drawPattern.drawChart(stockCandleArray);
		}
	}
	
	public abstract boolean hasPattern(StockPattern stockPattern, int index);

}
