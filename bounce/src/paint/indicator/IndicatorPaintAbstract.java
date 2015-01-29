package paint.indicator;

import java.awt.Graphics2D;

import paint.StockChartPanel;
import stock.StockCandleArray;

public abstract class IndicatorPaintAbstract {
	
	protected double[] indicatorArray;
	
	protected StockChartPanel stockChartPanel;
	
	public IndicatorPaintAbstract(StockChartPanel stockChartPanel) {
		this.stockChartPanel = stockChartPanel;
	}
	
	public abstract void setIndicator(StockCandleArray stockCandleArray);
	
	public abstract void paintIndicator(Graphics2D g2);
	
	public void destroy() {
		indicatorArray = null;
		stockChartPanel = null;
	}
	
}
