package test;

import java.io.File;

import pattern.StockPattern;
import stock.StockCandle;
import stock.StockCandleArray;
import stock.StockEnum.StockCandleDataType;
import yahoo.YahooParser;

public class PatternTest {
	private static final String CSV_DIRECTORY_PATH = "D:\\zzx\\Stock\\CSV\\";
	private static final int STOCK_CANDLE_ARRAY_MAX = 250;
	private static final double STOCK_CANDLE_NORMALIZE_MAX = 500;
	
	public static void main(String args[]) throws Exception {
		PatternTest patternTest = new PatternTest();
		patternTest.testChart();
	}

	
	public void testChart() throws Exception {
		File directory = new File(CSV_DIRECTORY_PATH);
		File[] directoryList = directory.listFiles();
		StockCandleArray stockCandleArray, originalStockCandleArray;
		if (directoryList == null) return;
		
		System.out.print("Symbol" + ",");
		System.out.print("Date" + ",");
		System.out.print("Current" + ",");
		for (int days = 5; days <= 15; days+=5) {
			System.out.print(days + "d Max High" + ",");
			System.out.print(days + "d Max Close" + ",");
			System.out.print(days + "d Min Low" + ",");
			System.out.print(days + "d Min Close" + ",");
		}
		System.out.println();
		
		for (File csvFile : directoryList) {
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
				stockCandleArray.getStockCandleArray().add(stockCandle);
				if (stockCandleArray.getStockCandleArray().size() >= STOCK_CANDLE_ARRAY_MAX) break;
			}
			parser.closeFile();
			stockCandleArray.sortByDate();
			originalStockCandleArray = new StockCandleArray(stockCandleArray);
			StockCandleArray.normalizeStockCandle(stockCandleArray.getStockCandleArray(), STOCK_CANDLE_NORMALIZE_MAX);
			
			checkPattern(stockCandleArray, originalStockCandleArray);
		}
		
	}
	
	
	private void checkPattern(StockCandleArray stockCandleArray, StockCandleArray originalStockCandleArray) {
		StockPattern stockPattern = new StockPattern(stockCandleArray.getStockCandleArray());
		stockPattern.setSymbol(stockCandleArray.getSymbol());
		
		for (int i = 0; i < stockCandleArray.getStockCandleArray().size(); i++) {
			if (stockPattern.isBearishEngulfing(i)) {
				System.out.print(stockPattern.getSymbol() + ",");
				System.out.print(stockPattern.getDate(i) + ",");
				System.out.print(StockCandleArray.formatPrice(originalStockCandleArray.getStockCandleArray().get(i).close) + ",");
				for (int days = 5; days <= 15; days+=5) {
					System.out.print(StockCandleArray.formatPrice(originalStockCandleArray.getMaxStockPrice(i, days, StockCandleDataType.HIGH)) + ",");
					System.out.print(StockCandleArray.formatPrice(originalStockCandleArray.getMaxStockPrice(i, days, StockCandleDataType.CLOSE)) + ",");
					System.out.print(StockCandleArray.formatPrice(originalStockCandleArray.getMinStockPrice(i, days, StockCandleDataType.LOW)) + ",");
					System.out.print(StockCandleArray.formatPrice(originalStockCandleArray.getMinStockPrice(i, days, StockCandleDataType.CLOSE)) + ",");
				}
				System.out.println();
			}
				
		}
	}

}
