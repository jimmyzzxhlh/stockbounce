package draw;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import stock.StockCandle;

/**
 * This class extends the stock candle and is for painting a single stock candle on the StockCandlesPanel.
 * The class needs the position of stock candle on the panel and then it is responsible for doing the painting.
 * 
 * @author jimmyzzxhlh-Dell
 *
 */
public class StockCandlePaint extends StockCandle {

	private Graphics2D g2;
	private double x;
	
//	private double shadowWidth;
	private double bodyWidth;
	private Color color;
	
	private double lowY;
	private double minOpenCloseY;
	private double maxOpenCloseY;
	private double highY;
	
	public Graphics2D getGraphics2D() {
		return g2;
	}

	public void setGraphics2D(Graphics2D g2) {
		this.g2 = g2;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

//	public double getShadowWidth() {
//		return shadowWidth;
//	}
//
//	public void setShadowWidth(double shadowWidth) {
//		this.shadowWidth = shadowWidth;
//	}

	public double getBodyWidth() {
		return bodyWidth;
	}

	public void setBodyWidth(double bodyWidth) {
		this.bodyWidth = bodyWidth;
	}
	
	public StockCandlePaint(StockCandle stockCandle) {
		super(stockCandle);
		lowY = this.low;
		highY = this.high;
		if (this.isWhite()) {
			color = StockGUIConst.WHITE_CANDLE_COLOR;
			minOpenCloseY = this.open;
			maxOpenCloseY = this.close;
		}
		else if (this.isBlack()) {
			color = StockGUIConst.BLACK_CANDLE_COLOR;
			minOpenCloseY = this.close;
			maxOpenCloseY = this.open;
		}
		else {
			color = StockGUIConst.NOCOLOR_CANDLE_COLOR;
			minOpenCloseY = this.open;
			maxOpenCloseY = this.close;
		}
	}
	
	public void paintCandle() {
		paintBody();
		paintUpperShadow();
		paintLowerShadow();
	}
	
	private void paintBody() {
		Rectangle2D rectangle = new Rectangle2D.Double(x, minOpenCloseY, bodyWidth, getBodyLength());
		g2.setColor(color);
		if (this.isBlack()) {
			g2.fill(rectangle);
		}
		else {
			g2.draw(rectangle);
		}
			
	}
	
	private void paintUpperShadow() {
		double shadowX = getXForShadow();
//		Rectangle2D rectangle = new Rectangle2D.Double(shadowX, maxOpenCloseY, shadowWidth, getUpperShadowLength());
		Line2D line = new Line2D.Double(shadowX, maxOpenCloseY, shadowX, highY);
		
		g2.setColor(color);
		g2.draw(line);	
	}
	
	private void paintLowerShadow() {
		double shadowX = getXForShadow();
//		Rectangle2D rectangle = new Rectangle2D.Double(shadowX, lowY, shadowWidth, getLowerShadowLength());
		Line2D line = new Line2D.Double(shadowX, lowY, shadowX, minOpenCloseY);
		g2.setColor(color);
		g2.draw(line);
	}
	
	private double getXForShadow() {
		return x + bodyWidth * 0.5;
	}

}
