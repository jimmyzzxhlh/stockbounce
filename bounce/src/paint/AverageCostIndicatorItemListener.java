package paint;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import paint.upperindicator.AverageCostUpperIndicator;
import paint.upperindicator.UpperIndicatorAbstract;

/**
 * Item listener for average cost indicator checkbox.
 * @author jimmyzzxhlh-Dell
 *
 */
public class AverageCostIndicatorItemListener implements ItemListener {
	
	private StockChartPanel stockChartPanel;
	
	public void setStockChartPanel(StockChartPanel stockChartPanel) {
		this.stockChartPanel = stockChartPanel;
	}
	
	/**
	 * Triggered when the average cost indicator checkbox is checked or unchecked.
	 */
	public void itemStateChanged(ItemEvent ie) {
		if (stockChartPanel == null) return;
		if (!stockChartPanel.hasChart()) return;
		//If the checkbox is selected then initialize the indicator
		if (ie.getStateChange() == ItemEvent.SELECTED) {
			initializeIndicator();			
		}
		//If the checkbox is deselected then destroy the indicator
		else if (ie.getStateChange() == ItemEvent.DESELECTED) {
			stockChartPanel.destroyUpperIndicator();
			System.gc();
		}
		//Repaint the indicator.
		stockChartPanel.repaint();
		
	}
	
	/**
	 * Initialize the indicator and pass the indicator to the chart panel.
	 */
	public void initializeIndicator() {
		UpperIndicatorAbstract indicator = new AverageCostUpperIndicator(stockChartPanel);
		indicator.setIndicator(stockChartPanel.getStockCandleArray());
		stockChartPanel.setUpperIndicator(indicator);
	}

}
