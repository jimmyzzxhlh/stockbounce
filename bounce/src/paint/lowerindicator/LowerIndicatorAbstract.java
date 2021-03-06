package paint.lowerindicator;

import java.awt.Graphics2D;

import paint.StockGUIConst;
import paint.StockLowerIndicatorPanel;
import stock.CandleList;

public abstract class LowerIndicatorAbstract {

	protected StockLowerIndicatorPanel lowerIndicatorPanel;
	protected CandleList stockCandleList;
	protected int startDateIndex;
	protected int endDateIndex;
	protected Graphics2D g2Input;
	
	protected int chartWidth;
	protected int chartHeight;
	
	public LowerIndicatorAbstract() {
		
	}
	
	public LowerIndicatorAbstract(Graphics2D g2Input, StockLowerIndicatorPanel lowerIndicatorPanel, CandleList stockCandleList, int startDateIndex, int endDateIndex) {
		this.g2Input = g2Input;
		this.lowerIndicatorPanel = lowerIndicatorPanel;
		this.stockCandleList = stockCandleList;
		this.startDateIndex = startDateIndex;
		this.endDateIndex = endDateIndex;
		chartWidth = lowerIndicatorPanel.getPanelWidth() - StockGUIConst.LOWER_INDICATOR_LEFT_BORDER_DISTANCE - StockGUIConst.LOWER_INDICATOR_RIGHT_BORDER_DISTANCE;
		chartHeight = lowerIndicatorPanel.getPanelHeight() - StockGUIConst.LOWER_INDICATOR_TOP_BORDER_DISTANCE - StockGUIConst.LOWER_INDICATOR_BOTTOM_BORDER_DISTANCE;
		initializeParameters();
	}
	
	public void paint() {		
		paintBackgroundLines();
		paintScaleLabel();
		paintIndicators();
	}
	
	private boolean hasChart() {
		return (stockCandleList != null);
	}
	
	protected abstract void paintBackgroundLines();
	
	protected abstract void paintIndicators();
	
	protected abstract void paintScaleLabel();
	
	protected abstract void initializeParameters();
	
}
