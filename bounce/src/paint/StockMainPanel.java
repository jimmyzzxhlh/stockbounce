package paint;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.joda.time.LocalDate;

/**
 * Main panel for all the stuff displayed on the frame.
 * @author jimmyzzxhlh-Dell
 *
 */
@SuppressWarnings("serial")

public class StockMainPanel extends JPanel {
	
	private JFrame frame;
	private StockCandleInfoPanel stockCandleInfoPanel;
	private StockChartPanel stockChartPanel;
	private SettingsPanel settingsPanel;
	private StockLowerIndicatorPanel stockLowerIndicatorPanel;
	
	public StockCandleInfoPanel getStockCandleInfoPanel() {
		return stockCandleInfoPanel;
	}

	public StockChartPanel getStockChartPanel() {
		return stockChartPanel;
	}

	public SettingsPanel getSettingsPanel() {
		return settingsPanel;
	}

	public StockLowerIndicatorPanel getStockIndicatorPanel() {
		return stockLowerIndicatorPanel;
	}
	
	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
	public StockMainPanel() {
		super();
		//Set layout of the whole frame.
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setOpaque(false);
		stockChartPanel = new StockChartPanel();
		settingsPanel = new SettingsPanel(stockChartPanel);
		stockCandleInfoPanel = new StockCandleInfoPanel();
		stockLowerIndicatorPanel = new StockLowerIndicatorPanel();
		
		stockChartPanel.setMainPanel(this);
		stockChartPanel.setSettingsPanel(settingsPanel);
		stockChartPanel.setStockCandleInfoPanel(stockCandleInfoPanel);
		this.add(settingsPanel);				
		this.add(stockCandleInfoPanel);
		this.add(stockChartPanel);
		this.add(stockLowerIndicatorPanel);
		System.out.println(stockCandleInfoPanel.getPanelHeight());
	}
	
	public int getPanelHeight() {
		return stockCandleInfoPanel.getPanelHeight() + stockChartPanel.getPanelHeight() + settingsPanel.getPanelHeight() + stockLowerIndicatorPanel.getPanelHeight();
	}
	
	public int getPanelWidth() {
		return StockGUIUtil.getScreenWidth();
	}
	
	private LocalDate getStartDate() {
		return settingsPanel.getStartLocalDate();
	}
	
	private LocalDate getEndDate() {
		return settingsPanel.getEndLocalDate();
	}
	
	/**
	 * Triggered by a mouse movement on the main panel.
	 * We need to update the stock candle information and a vertical line that represents the current candle. 
	 * @param x
	 * @param y
	 */
	
	//TODO
	public void handleMouseMovement(int x, int y) {
		//Update the text for the stock candle
		updateStockCandleInfo();
		
		//Update the vertical line. Need to remove the previous component and add a new component.
		drawCurrentStockCandleLine();
	}
	
	private void updateStockCandleInfo() {
		
	}
	
	private void drawCurrentStockCandleLine() {
		
	}
}
