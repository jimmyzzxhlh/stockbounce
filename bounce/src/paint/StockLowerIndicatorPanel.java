package paint;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import paint.lowerindicator.LowerIndicatorAbstract;

@SuppressWarnings("serial")
public class StockLowerIndicatorPanel extends JPanel {
	private StockMainPanel mainPanel;
	private LowerIndicatorAbstract lowerIndicator;
	
	public StockLowerIndicatorPanel() {
		
		this.setPreferredSize(new Dimension(getPanelWidth(), getPanelHeight()));
		this.setBorder(BorderFactory.createLineBorder(Color.green));
		this.setOpaque(false);
	}
	
	public void setMainPanel(StockMainPanel mainPanel) {
		this.mainPanel = mainPanel; 
	}
	
	public void setLowerIndicator(LowerIndicatorAbstract lowerIndicator) {
		this.lowerIndicator = lowerIndicator;
	}
	
	/**
	 * Paint the candle chart.
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawString("Stock indicator panel", 500, 50);
		if (lowerIndicator == null) return;
		lowerIndicator.paint();
	}
	
	public int getPanelHeight() {
		return (int) Math.floor(StockGUIUtil.getScreenHeight() * StockGUIConst.STOCK_INDICATOR_PANEL_HEIGHT_PERCENTAGE);
	}
	
	public int getPanelWidth() {
		return StockGUIUtil.getScreenWidth();
	}
}
