package paint.upperindicator;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import paint.StockChartPanel;

/**
 * Item listener for average cost indicator checkbox.
 * @author jimmyzzxhlh-Dell
 *
 */
public class AverageCostIndicatorItemListener implements ItemListener {
	
	private StockChartPanel stockChartPanel;
	
	private String symbol;
	
	private AverageCostUpperIndicator indicator;
	
	public void setStockChartPanel(StockChartPanel stockChartPanel) {
		this.stockChartPanel = stockChartPanel;
	}
	
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	/**
	 * Triggered when the average cost indicator checkbox is checked or unchecked.
	 */
	public void itemStateChanged(ItemEvent ie) {
		if (stockChartPanel == null) return;
		if (!stockChartPanel.hasChart()) return;
		 
		if (ie.getStateChange() == ItemEvent.SELECTED) {
			if (indicator == null) {			
				initializeIndicator();
			}
			setIsShown(true);
		}
		else if (ie.getStateChange() == ItemEvent.DESELECTED) {
			if (indicator != null) {
				setIsShown(false);
			}
		}
		//Repaint the indicator.
		stockChartPanel.repaint();
		
	}
	
	public void setIsShown(boolean isShown) {
		indicator.setIsShown(isShown);
	}
	
	/**
	 * Initialize the indicator and pass the indicator to the chart panel.
	 */
	public void initializeIndicator() {
		try {
			indicator = new AverageCostUpperIndicator(stockChartPanel, symbol);
			indicator.setIndicatorArray(stockChartPanel.getstockCandleList());
			stockChartPanel.setAverageCostUpperIndicator(indicator);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
