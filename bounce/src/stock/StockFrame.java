package stock;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class StockFrame extends JFrame {
	static final int CANDLE_WIDTH = 5;
	public ArrayList<StockPrice> stockPriceArray;
	
	public StockFrame() {
		super();
		//
		
	}
	
	public StockFrame(int width, int height) {
		super();
		this.setSize(width, height);
	}
	
	public void paint(Graphics g) {
		StockPrice stockPrice;
		if (stockPriceArray == null) return;
		for (int i = 0; i < stockPriceArray.size(); i++) {
			stockPrice = stockPriceArray.get(i);
			paintCandle(g, CANDLE_WIDTH * i + 3, stockPrice);
		}
	}
	public void paintCandle(Graphics g, int x, StockPrice stockPrice) {
		int frameHeight = 0;
		int bodyHeight;
		int upperShadowStart;
		int upperShadowEnd;
		int lowerShadowStart;
		int lowerShadowEnd;
		int openInt = (int) Math.floor(stockPrice.open);
		int highInt = (int) Math.floor(stockPrice.high);
		int lowInt = (int) Math.floor(stockPrice.low);
		int closeInt = (int) Math.floor(stockPrice.close);
		
		frameHeight = this.getHeight();
		if (frameHeight == 0) return;
		upperShadowStart = frameHeight - highInt;
		lowerShadowEnd = frameHeight - lowInt;
		//Increasing
		if (stockPrice.close > stockPrice.open) {
			g.setColor(Color.GREEN);
			upperShadowEnd = frameHeight - closeInt;
			lowerShadowStart = frameHeight - openInt; 
			bodyHeight = closeInt - openInt; 
		}
		//Decreasing
		else {
			g.setColor(Color.RED);
			upperShadowEnd = frameHeight - openInt;
			lowerShadowStart = frameHeight - closeInt;
			bodyHeight = openInt - closeInt;
		}
		//Draw the upper shadow of the candle
		g.drawLine(x, upperShadowStart, x, upperShadowEnd);
		
		//Draw the candle body
		g.fillRect(x - 2, upperShadowEnd, CANDLE_WIDTH, bodyHeight);
		
		//Draw the lower shadow of the candle
		g.drawLine(x, lowerShadowStart, x, lowerShadowEnd);
		
	}

}
