package paint.upperindicator;

import java.awt.Graphics2D;

import paint.StockChartPanel;
import stock.StockCandleArray;

public abstract class UpperIndicatorAbstract {
	
	protected double[] indicatorArray;
	
	protected StockChartPanel stockChartPanel;
	
	public UpperIndicatorAbstract(StockChartPanel stockChartPanel) {
		this.stockChartPanel = stockChartPanel;
	}
	
	public abstract void setIndicator(StockCandleArray stockCandleArray) throws Exception;
	
	public abstract void paintIndicator(Graphics2D g2);
	
	public void destroy() {
		indicatorArray = null;
		stockChartPanel = null;
	}
	
}
