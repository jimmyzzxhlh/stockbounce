package paint;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import stock.DailyCandle;
import stock.EarningsDate;
import util.StockUtil;

/**
 * Panel for displaying price / volume information of one single stock candle at the top of the main panel.
 * @author jimmyzzxhlh-Dell
 *
 */
@SuppressWarnings("serial")
public class StockCandleInfoPanel extends JPanel {
	
	private DailyCandle stockCandle;
	
	public StockCandleInfoPanel() {
		this.setPreferredSize(new Dimension(getPanelWidth(), getPanelHeight()));
		this.setBorder(BorderFactory.createLineBorder(Color.red));
		this.setOpaque(false);
	}
	
	public void setStockCandle(DailyCandle stockCandle) {
		this.stockCandle = stockCandle;
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
//		g.drawString("Stock candle info panel", 500, 50);
		if (stockCandle != null) {
			paintStockCandleInfo(g);
			EarningsDate stockEarningsDate = stockCandle.getNextEarningsDate();
			if ((stockEarningsDate != null) && (stockEarningsDate.getDate().equals(stockCandle.getDate()))) {
				paintEarningsDateInfo(g, stockEarningsDate);			
			}
			else {
				paintNextEarningsDateInfo(g, stockEarningsDate);
			}
		}
	}
	
	private void paintStockCandleInfo(Graphics g) {
		g.setFont(StockGUIConst.STOCK_CANDLE_INFO_FONT);
		g.setColor(StockGUIConst.STOCK_CANDLE_INFO_COLOR);
		StringBuilder sb = new StringBuilder();
		sb.append(StockUtil.formatDate(stockCandle.getDate()));
		sb.append("   Open: ");
		sb.append(StockUtil.getRoundTwoDecimals(stockCandle.getOpen()));
		sb.append("   High: ");
		sb.append(StockUtil.getRoundTwoDecimals(stockCandle.getHigh()));
		sb.append("   Low: ");
		sb.append(StockUtil.getRoundTwoDecimals(stockCandle.getLow()));
		sb.append("   Close: ");
		sb.append(StockUtil.getRoundTwoDecimals(stockCandle.getClose()));
		sb.append("   Volume: ");
		sb.append(stockCandle.getVolume());
		g.drawString(sb.toString(), 10, 30);
	}
	
	private void paintEarningsDateInfo(Graphics g, EarningsDate stockEarningsDate) {
		g.drawString(stockEarningsDate.toStringForGUI(), 10, 60);
	}
	
	private void paintNextEarningsDateInfo(Graphics g, EarningsDate stockEarningsDate) {
		if (stockEarningsDate == null) {
			g.drawString("Next Earnings Date: N/A" , 10, 60);
		}
		else {
			g.drawString(stockEarningsDate.toStringForGUIFutureDate(), 10, 60);
		}
	}
	
	public int getPanelHeight() {
		return (int) Math.floor(StockGUIUtil.getScreenHeight() * StockGUIConst.STOCK_CANDLE_INFO_PANEL_HEIGHT_PERCENTAGE);
	}
	
	public int getPanelWidth() {
		return StockGUIUtil.getScreenWidth();
	}
	
}
