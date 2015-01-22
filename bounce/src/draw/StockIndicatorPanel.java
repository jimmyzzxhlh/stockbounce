package draw;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class StockIndicatorPanel extends JPanel {
	private StockMainPanel mainPanel;
	
	public StockIndicatorPanel() {
		
		this.setPreferredSize(new Dimension(getPanelWidth(), getPanelHeight()));
		this.setBorder(BorderFactory.createLineBorder(Color.green));
		this.setOpaque(false);
	}
	
	public void setMainPanel(StockMainPanel mainPanel) {
		this.mainPanel = mainPanel; 
	}
	
	/**
	 * Paint the candle chart.
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawString("Stock indicator panel", 500, 50);
	}
	
	public int getPanelHeight() {
		return (int) Math.floor(StockGUIUtil.getScreenHeight() * StockGUIConst.STOCK_INDICATOR_PANEL_HEIGHT_PERCENTAGE);
	}
	
	public int getPanelWidth() {
		return StockGUIUtil.getScreenWidth();
	}
}
