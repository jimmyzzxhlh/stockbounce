package paint.upperindicator;

import indicator.StockAverageCostIndicator;

import java.awt.Color;
import java.awt.Graphics2D;

import paint.StockChartPanel;
import paint.StockGUIConst;
import paint.StockGUIUtil;
import stock.StockCandleArray;

public class AverageCostUpperIndicator {

	private double[] indicatorArray;
	
	private StockChartPanel stockChartPanel;
	
	private StockAverageCostIndicator indicator;

	private String symbol;
	
	private boolean isShown;
	
	public AverageCostUpperIndicator(StockChartPanel stockChartPanel, String symbol) {
		this.stockChartPanel = stockChartPanel;
		this.symbol = symbol;
	}
	
	public void destroy() {
		indicatorArray = null;
		stockChartPanel = null;
		if (indicator != null) {
			indicator.destroy();
			indicator = null;
		}
		symbol = null;
	}
	
	public void setIsShown(boolean isShown) {
		this.isShown = isShown;
	}
	
	public boolean getIsShown() {
		return isShown;
	}
	
	public void setIndicatorArray(StockCandleArray stockCandleArray) throws Exception {
		//If we are passing in exactly the same symbol then do not update and recompute the indicator again.
		//Notice that this has a few assumptions;
		//1. We are always reading in a full daily stock candle array.
		//2. We have already computed the indicator value for all the dates in the stock candle array.
		if (stockCandleArray.getSymbol().equals(symbol)) {
			return;
		}
		if (indicator != null) {
			indicator.destroy();
			indicator = null;
		}
		indicator = new StockAverageCostIndicator(stockCandleArray);
		indicatorArray = indicator.getAverageCostArray();
//		for (int i = 0; i < indicatorArray.length; i++) {
//			System.out.println(StockUtil.formatDate(stockCandleArray.getDate(i)) + " " + indicatorArray[i]);
//		}
	}

	public void paintIndicator(Graphics2D g2) {
		//g2 should already be translated.
//		System.out.println("indicator repainting");
		double[] xArray = new double[stockChartPanel.getStockCandleArray().size()];
		double[] yArray = new double[stockChartPanel.getStockCandleArray().size()];
		//Get half of the stock candle body width and apply it to the X coordinate, so that the indicator is drawn at the
		//middle instead of drawn at the left side of the stock candle.
		double stockCandleBodyWidthHalf = stockChartPanel.getStockCandleBodyWidth() * 0.5;
		for (int i = stockChartPanel.getStartDateIndex(); i <= stockChartPanel.getEndDateIndex(); i++) {
			xArray[i] = stockChartPanel.getTranslatedXFromIndex(i) + stockCandleBodyWidthHalf;
			yArray[i] = stockChartPanel.getTranslatedYFromPrice(indicatorArray[i]);			
		}		
		for (int i = stockChartPanel.getStartDateIndex() + 1; i <= stockChartPanel.getEndDateIndex(); i++) {
			StockGUIUtil.drawLine(g2, xArray[i - 1], yArray[i - 1], xArray[i], yArray[i], Color.orange);
		
		}
		xArray = null;
		yArray = null;
	}

}
