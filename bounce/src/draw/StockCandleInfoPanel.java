package draw;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import stock.StockCandle;

/**
 * Panel for displaying price / volume information of one single stock candle at the top of the main panel.
 * @author jimmyzzxhlh-Dell
 *
 */
@SuppressWarnings("serial")
public class StockCandleInfoPanel extends JPanel {
	
	private StockCandle stockCandle;
	
	public StockCandleInfoPanel() {
		this.setPreferredSize(new Dimension(getPanelWidth(), getPanelHeight()));
		this.setBorder(BorderFactory.createLineBorder(Color.red));
		this.setOpaque(false);
	}
	
	public void setStockCandle(StockCandle stockCandle) {
		this.stockCandle = stockCandle;
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawString("Stock candle info panel", 500, 50);
	}
	
	public int getPanelHeight() {
		return (int) Math.floor(StockGUIUtil.getScreenHeight() * StockGUIConst.STOCK_CANDLE_INFO_PANEL_HEIGHT_PERCENTAGE);
	}
	
	public int getPanelWidth() {
		return StockGUIUtil.getScreenWidth();
	}
	
}
