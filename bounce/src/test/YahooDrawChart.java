package test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import javax.swing.WindowConstants;

import stock.StockFrame;
import stock.StockPrice;
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
public class YahooDrawChart {

	public StockFrame stockFrame;
	public ArrayList<StockPrice> stockPriceArray = new ArrayList<StockPrice>();
	public static final int FRAME_HEIGHT = 500;
	public static final String filename = "D:\\CAMT.csv";
	
	public static void main(String args[]) throws Exception {
		drawChart();
		
		
	}
	
	private static void drawChart() throws Exception {
		String line;
		StockPrice stockPrice = null;
		YahooParser parser = new YahooParser(filename);
		YahooDrawChart mainProgram = new YahooDrawChart();
		int frameWidth;
		
		parser.startReadFile();
		
		while ((line = parser.nextLine()) != null) {
			stockPrice = new StockPrice();
			parser.parseLine(line, stockPrice);
			mainProgram.stockPriceArray.add(stockPrice);
		}
		parser.closeFile();
		Collections.sort(mainProgram.stockPriceArray, new Comparator<StockPrice>() {
			public int compare(StockPrice a, StockPrice b) {
				if (a.date.before(b.date)) return -1;
				return 1;
			}
		});
		mainProgram.normalizeStockPrice();
		for (int i = 0; i < mainProgram.stockPriceArray.size(); i++) {
			System.out.println(mainProgram.stockPriceArray.get(i).toString());
		}
		
		frameWidth = (mainProgram.stockPriceArray.size() + 1) * 5;
		mainProgram.stockFrame = new StockFrame(frameWidth, FRAME_HEIGHT);
		mainProgram.stockFrame.stockPriceArray = mainProgram.stockPriceArray;
		mainProgram.stockFrame.setVisible(true);
		mainProgram.stockFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	private void normalizeStockPrice() {
		StockPrice stockPrice;
		double max = 0;
		double min = 1e10;
		double scale = 1;
		for (int i = 0; i < stockPriceArray.size(); i++) {
			stockPrice = stockPriceArray.get(i);
			if (stockPrice.low < min) {
				min = stockPrice.low;
			}
			if (stockPrice.high > max) {
				max = stockPrice.high;
			}
		}
		scale = FRAME_HEIGHT / (max - min);
		for (int i = 0; i < stockPriceArray.size(); i++) {
			stockPrice = stockPriceArray.get(i);
			stockPrice.open = (stockPrice.open - min) * scale;
			stockPrice.close = (stockPrice.close - min) * scale;
			stockPrice.high = (stockPrice.high - min) * scale;
			stockPrice.low = (stockPrice.low - min) * scale;			
		}
		
	}
	
		
	
}
