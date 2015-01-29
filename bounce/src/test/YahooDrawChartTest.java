package test;

import javax.swing.WindowConstants;

import paint.StockFrame;
import stock.StockCandleArray;
import yahoo.YahooParser;

public class YahooDrawChartTest {

	private static final int FRAME_HEIGHT = 500;
	private static final String symbol = "MSFT";
	private static final int CANDLE_DAYS = 1;
	private static final int CANDLE_DAYS_OFFSET = 0;
	private static final int MAX_CANDLES = -1;
	
	public static void main(String args[]) throws Exception {
		drawChart();
	}
	

	/**
	 * Read a chart CSV file and draw the chart.
	 * @throws Exception
	 */
	private static void drawChart() throws Exception {
		StockFrame stockFrame;
		StockCandleArray stockCandleArray;
		
		stockCandleArray = YahooParser.readCSVFile(symbol, MAX_CANDLES);
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
