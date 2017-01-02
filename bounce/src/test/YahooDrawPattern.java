package test;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.imageio.ImageIO;

import paint.StockFrame;
import stock.CandleList;
import stock.StockCandle;
import stock.StockConst;

public class YahooDrawPattern {
	private StockFrame stockFrame;
	private static final int FRAME_HEIGHT = 500;
	private static final int CANDLE_DAYS = 1;
	private static final int CANDLE_DAYS_OFFSET = 0;
	private int patternIndex = -1; 
	private static final int WIDTH = 30; // The duration (days) before/after the indexed candle
	
	
	public void setPatternIndex(int patternIndex){
		this.patternIndex = patternIndex;
	}
	
	
	/**
	 * Read a chart CSV file and draw the chart.
	 * @throws Exception
	 */
	public void drawChart(CandleList inputCandleList) throws Exception {
		if (patternIndex == -1) return;
		
		CandleList candleList = new CandleList(inputCandleList);
		int startIndex = (patternIndex-WIDTH > 0) ? (patternIndex - WIDTH) : 0;
		int endIndex = (patternIndex + WIDTH < candleList.size()) ? (patternIndex+WIDTH) : candleList.size() - 1;
		patternIndex = patternIndex - startIndex;
		
		for (int index = startIndex; index <= endIndex; index++){
			candleList.add(candleList.get(index));
		}
		
		int frameWidth;
		String symbol = candleList.getSymbol();

		candleList.sortByDate();
		candleList.normalizeStockCandle(FRAME_HEIGHT);
		
		frameWidth = (candleList.size() + 1) * 5;
		stockFrame = new StockFrame(frameWidth, FRAME_HEIGHT);
		stockFrame.candleDays = CANDLE_DAYS;
		stockFrame.candleDaysOffset = CANDLE_DAYS_OFFSET;
		stockFrame.candleList = candleList;
		stockFrame.setIndex(patternIndex);
		
		StockCandle indexedStockCandle = candleList.get(patternIndex);
		DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
		String date = df.format(indexedStockCandle.getInstant());
		String path = StockConst.SNAPSHOT_DIRECTORY_PATH + symbol + "_" + date+".png";
		
		//Create the directory if the path does not exist.
		File directory = new File(StockConst.SNAPSHOT_DIRECTORY_PATH);
		if (!directory.exists()) {
			System.out.println("Creating directory: " + StockConst.SNAPSHOT_DIRECTORY_PATH);
			try {
				directory.mkdir();				
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
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

