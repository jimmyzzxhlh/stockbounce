package paint.upperindicator;

import indicator.StockAverageCostIndicator;

import java.awt.Color;
import java.awt.Graphics2D;

import paint.StockChartPanel;
import paint.StockGUIConst;
import paint.StockGUIUtil;
import stock.StockCandleArray;

public class AverageCostUpperIndicator extends UpperIndicatorAbstract {

	public AverageCostUpperIndicator(StockChartPanel stockChartPanel) {
		super(stockChartPanel);
	}
	
	@Override
	public void setIndicator(StockCandleArray stockCandleArray) throws Exception {
		StockAverageCostIndicator indicator = new StockAverageCostIndicator(stockCandleArray);
		indicatorArray = null;
		indicatorArray = indicator.getAverageCostArray();
		indicator.destroy();
		indicator = null;
//		for (int i = 0; i < indicatorArray.length; i++) {
//			System.out.println(StockUtil.formatDate(stockCandleArray.getDate(i)) + " " + indicatorArray[i]);
//		}
	}

	@Override
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
