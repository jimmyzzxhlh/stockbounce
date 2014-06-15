package test;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.imageio.ImageIO;

import stock.StockCandle;
import stock.StockCandleArray;
import stock.StockFrame;

public class YahooDrawPattern {
	private StockFrame stockFrame;
	private static final int FRAME_HEIGHT = 500;
	private static final int CANDLE_DAYS = 1;
	private static final int CANDLE_DAYS_OFFSET = 0;
	private int patternIndex = -1;
	private static final String SNAPSHOT_DIRECTORY_PATH = "D:\\zzx\\Stock\\Snapshots\\";
	
	
	public void setPatternIndex(int patternIndex){
		this.patternIndex = patternIndex;
	}
	
	
	/**
	 * Read a chart CSV file and draw the chart.
	 * @throws Exception
	 */
	public void drawChart(StockCandleArray stockCandleArray) throws Exception {
		
		if (patternIndex == -1) return;
		
		int frameWidth;
		String symbol = stockCandleArray.getSymbol();

		stockCandleArray.sortByDate();
		stockCandleArray.normalizeStockCandle(FRAME_HEIGHT);
		
		frameWidth = (stockCandleArray.getStockCandleArray().size() + 1) * 5;
		stockFrame = new StockFrame(frameWidth, FRAME_HEIGHT + 100);
		stockFrame.candleDays = CANDLE_DAYS;
		stockFrame.candleDaysOffset = CANDLE_DAYS_OFFSET;
		stockFrame.stockCandleArray = stockCandleArray.getStockCandleArray();
		stockFrame.setIndex(patternIndex);
		
		StockCandle indexedStockCandle = stockCandleArray.get(patternIndex);
		DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
		String date = df.format(indexedStockCandle.date);
		String path = SNAPSHOT_DIRECTORY_PATH + symbol + "_" + date+".png";
		
		BufferedImage image = new BufferedImage(stockFrame.getWidth(), stockFrame.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics g = image.getGraphics();
        stockFrame.paint (g);
        g.dispose ();
		try
        {
            ImageIO.write(image, "jpg", new File(path));
        }
        catch (IOException ex)
        {
            ex.printStackTrace ();
        }
	}
	
	
	
	
}
