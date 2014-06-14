package test;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import javax.swing.WindowConstants;

import download.StockData;

import stock.StockFrame;
import stock.StockCandle;
import stock.StockCandleArray;
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
	public StockCandleArray stockCandleArray;
	public static final int FRAME_HEIGHT = 500;
	public static final String filename = "D:\\zzx\\Stock\\CSV\\MSFT.csv";
	public static final int CANDLE_DAYS = 1;
	public static final int CANDLE_DAYS_OFFSET = 0;
	
	public static void main(String args[]) throws Exception {
		StockData stockData = new StockData(null,null);
		stockData.downloadStocks();
		drawChart();
		
		
	}
	
	private static void downloadChart() throws Exception{
		File file = new File(filename);
		file.createNewFile();
		
		URL website = new URL("http://ichart.finance.yahoo.com/table.csv?s=MSFT&d=0&e=28&f=2013&g=d&a=3&b=12&c=2009&ignore=.csv");
		ReadableByteChannel rbc = Channels.newChannel(website.openStream());
		FileOutputStream fos = new FileOutputStream(filename);
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		fos.close();
	}
	/**
	 * Read a chart CSV file and draw the chart.
	 * @throws Exception
	 */
	private static void drawChart() throws Exception {
		String line;
		StockCandle stockCandle = null;
		YahooParser parser = new YahooParser(filename);
		YahooDrawChartTest mainProgram = new YahooDrawChartTest();
		mainProgram.stockCandleArray = new StockCandleArray();

		int frameWidth;
		
		parser.startReadFile();
		
		while ((line = parser.nextLine()) != null) {
			stockCandle = new StockCandle();
			parser.parseLine(line, stockCandle);
			mainProgram.stockCandleArray.getStockCandleArray().add(stockCandle);
		}
		parser.closeFile();
		mainProgram.stockCandleArray.sortByDate();
		mainProgram.stockCandleArray.normalizeStockCandle(FRAME_HEIGHT);
//		for (int i = 0; i < mainProgram.stockCandleArray.size(); i++) {
//			System.out.println(mainProgram.stockCandleArray.get(i).toString());
//		}
		
		frameWidth = (mainProgram.stockCandleArray.getStockCandleArray().size() + 1) * 5;
		mainProgram.stockFrame = new StockFrame(frameWidth, FRAME_HEIGHT + 100);
		mainProgram.stockFrame.candleDays = CANDLE_DAYS;
		mainProgram.stockFrame.candleDaysOffset = CANDLE_DAYS_OFFSET;
		mainProgram.stockFrame.stockCandleArray = mainProgram.stockCandleArray.getStockCandleArray();
		mainProgram.stockFrame.setVisible(true);
		mainProgram.stockFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	
		
	
}
