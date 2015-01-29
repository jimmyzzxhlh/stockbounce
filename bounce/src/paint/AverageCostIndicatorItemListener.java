package paint;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import paint.indicator.AverageCostIndicatorPaint;
import paint.indicator.IndicatorPaintAbstract;

public class AverageCostIndicatorItemListener implements ItemListener {
	
	private StockChartPanel stockChartPanel;
	
	public void setStockChartPanel(StockChartPanel stockChartPanel) {
		this.stockChartPanel = stockChartPanel;
	}
	
	public void itemStateChanged(ItemEvent ie) {
		if (stockChartPanel == null) return;
		if (!stockChartPanel.hasChart()) return;
		if (ie.getStateChange() == ItemEvent.SELECTED) {
			initializeIndicatorPaint();			
		}
		else if (ie.getStateChange() == ItemEvent.DESELECTED) {
			stockChartPanel.destroyIndicatorPaint();
			System.gc();
		}
		stockChartPanel.repaint();
		
	}
	
	public void initializeIndicatorPaint() {
		IndicatorPaintAbstract indicatorPaint = new AverageCostIndicatorPaint(stockChartPanel);
		indicatorPaint.setIndicator(stockChartPanel.getStockCandleArray());
		stockChartPanel.setIndicatorPaint(indicatorPaint);
	}

}
