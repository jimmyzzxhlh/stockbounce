package test;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import javax.swing.WindowConstants;

import stock.StockCandleArray;
import stock.StockConst;
import stock.StockFrame;
import yahoo.YahooParser;
import download.StockDownload;

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

	private static final int FRAME_HEIGHT = 500;
	private static final String filename = StockConst.STOCK_CSV_DIRECTORY_PATH + "JD.csv";
	private static final int CANDLE_DAYS = 1;
	private static final int CANDLE_DAYS_OFFSET = 0;
	private static final int MAX_CANDLES = 200;
	
	public static void main(String args[]) throws Exception {
//		StockData stockData = new StockData(null,null);
//		stockData.downloadStocks();
		drawChart();
		
		
	}
	
	@SuppressWarnings("unused")
	private static void downloadChart() throws Exception {
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
		StockFrame stockFrame;
		StockCandleArray stockCandleArray;
		
		stockCandleArray = YahooParser.readCSVFile(filename, MAX_CANDLES);
		stockCandleArray.normalizeStockCandle(FRAME_HEIGHT);
		for (int i = 0; i < stockCandleArray.size(); i++) {
			System.out.println(stockCandleArray.get(i).toString());
		}
		
		int frameWidth = (stockCandleArray.getStockCandleArray().size() + 1) * 5;
		stockFrame = new StockFrame(frameWidth, FRAME_HEIGHT + 100);
		stockFrame.candleDays = CANDLE_DAYS;
		stockFrame.candleDaysOffset = CANDLE_DAYS_OFFSET;
		stockFrame.stockCandleArray = stockCandleArray.getStockCandleArray();
		stockFrame.setVisible(true);
		stockFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	
		
	
}
