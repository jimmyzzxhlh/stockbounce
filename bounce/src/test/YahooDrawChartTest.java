package test;

import javax.swing.WindowConstants;

import stock.StockFrame;
import stock.StockPrice;
import stock.StockPriceArray;
import yahoo.YahooParser;

//To get historical price:
//http://ichart.finance.yahoo.com/table.csv?s=YHOO&d=0&e=28&f=2010&g=d&a=3&b=12&c=1996&ignore=.csv
//sn = TICKER
//a = fromMonth-1
//b = fromDay (two digits)
//c = fromYear
//d = toMonth-1
//e = toDay (two digits)
//f = toYear
//g = d for day, m for month, y for yearly
public class YahooDrawChartTest {

	public StockFrame stockFrame;
	public StockPriceArray stockPriceArray;
	public static final int FRAME_HEIGHT = 500;
	public static final String filename = "D:\\zzx\\Stock\\CSV\\NQ.csv";
	public static final int CANDLE_DAYS = 1;
	public static final int CANDLE_DAYS_OFFSET = 0;
	
	public static void main(String args[]) throws Exception {
		drawChart();
		
		
	}
	
	/**
	 * Read a chart CSV file and draw the chart.
	 * @throws Exception
	 */
	private static void drawChart() throws Exception {
		String line;
		StockPrice stockPrice = null;
		YahooParser parser = new YahooParser(filename);
		YahooDrawChartTest mainProgram = new YahooDrawChartTest();
		mainProgram.stockPriceArray = new StockPriceArray();

		int frameWidth;
		
		parser.startReadFile();
		
		while ((line = parser.nextLine()) != null) {
			stockPrice = new StockPrice();
			parser.parseLine(line, stockPrice);
			mainProgram.stockPriceArray.getStockPriceArray().add(stockPrice);
		}
		parser.closeFile();
		mainProgram.stockPriceArray.sortByDate();
		mainProgram.stockPriceArray.normalizeStockPrice(FRAME_HEIGHT);
//		for (int i = 0; i < mainProgram.stockPriceArray.size(); i++) {
//			System.out.println(mainProgram.stockPriceArray.get(i).toString());
//		}
		
		frameWidth = (mainProgram.stockPriceArray.getStockPriceArray().size() + 1) * 5;
		mainProgram.stockFrame = new StockFrame(frameWidth, FRAME_HEIGHT + 100);
		mainProgram.stockFrame.candleDays = CANDLE_DAYS;
		mainProgram.stockFrame.candleDaysOffset = CANDLE_DAYS_OFFSET;
		mainProgram.stockFrame.stockPriceArray = mainProgram.stockPriceArray.getStockPriceArray();
		mainProgram.stockFrame.setVisible(true);
		mainProgram.stockFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	
		
	
}