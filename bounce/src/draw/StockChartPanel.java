package draw;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JPanel;

import stock.StockAPI;
import stock.StockCandleArray;
import stock.StockEnum.StockCandleDataType;
import util.StockUtil;

@SuppressWarnings("serial")
public class StockChartPanel extends JPanel {
	
	private StockMainPanel mainPanel;
	private SettingsPanel settingsPanel;
	private Date startDate;
	private Date endDate;
	private int startDateIndex;
	private int endDateIndex;
	private int daysTotal;
	private int candleNum;
	private String symbol;
	
	
	private double stockCandleTotalWidth;
	private double stockCandleBodyWidth;
//	private double stockCandleShadowWidth;
	private double stockCandleDistanceWidth;
	private int chartWidth;
	private int chartHeight;
	private double backgroundGridHeight;
	private double backgroundGridWidth;
	
	private double maxHigh;
	private double minLow;
	private double priceUnit;
	private double maxPriceOnGrid;
	private double minPriceOnGrid;
	private int xLineNumber;
	private int yLineNumber;
	private double xUnit;
	private double yUnit;
	
	
	private StockCandleArray stockCandleArray;
	private StockCandleArray normalizedStockCandleArray;
	
	public StockChartPanel() {
		//Add mouse motion listener for displaying the current stock candle's information on the top.
		
		this.addMouseMotionListener(new MouseAdapter() {;
			/**
			 * Call the stock candle info panel to update the price / volume information.
			 */
			public void mouseMoved(MouseEvent e) {
			
			}
		});
		
		
		this.setPreferredSize(new Dimension(getPanelWidth(), getPanelHeight()));
//		this.setBorder(BorderFactory.createLineBorder(Color.yellow));
		this.setOpaque(false);
	}
	
	public void setMainPanel(StockMainPanel mainPanel) {
		this.mainPanel = mainPanel; 
	}
	
	public void setSettingsPanel(SettingsPanel settingsPanel) {
		this.settingsPanel = settingsPanel;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	public void setStartDateIndex(int startDateIndex) {
		this.startDateIndex = startDateIndex;
	}
	
	public void setEndDateIndex(int endDateIndex) {
		this.endDateIndex = endDateIndex;
	}
	
	public boolean initializeStockCandleArray() {
		this.stockCandleArray = StockAPI.getStockCandleArrayYahoo(symbol);
		if (stockCandleArray == null) {
			StockGUIUtil.showWarningMessageDialog("Symbol " + symbol + " is not valid.", "Invalid Symbol");
			return false;
		}
		startDateIndex = stockCandleArray.getDateIndex(startDate, true);
		endDateIndex = stockCandleArray.getDateIndex(endDate, true);
		//Update the start date and end date since they can be invalid or on holidays.
		startDate = stockCandleArray.getDate(startDateIndex);
		endDate = stockCandleArray.getDate(endDateIndex);
		settingsPanel.updateDates(startDate, endDate);
		daysTotal = endDateIndex - startDateIndex + 1;
		return true;
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		paintComponent(g2);
	}
	/**
	 * Paint the candle chart.
	 */
	protected void paintComponent(Graphics2D g2) {
		g2.drawString("Stock chart panel", 500, 50);
		if ((symbol == null) || (startDate == null) || (endDate == null)) return;
		initializeChartParameters();
		paintBackgroundLines(g2);
		paintStockPriceScale(g2);
		paintStockCandles(g2);
	}

	private void initializeChartParameters() {
		chartWidth = getPanelWidth() - StockGUIConst.CHART_LEFT_BORDER_DISTANCE - StockGUIConst.CHART_RIGHT_BORDER_DISTANCE;
//		stockCandleShadowWidth = StockGUIConst.STOCK_CANDLE_SHADOW_WIDTH_MAX;
		stockCandleDistanceWidth = StockGUIConst.STOCK_CANDLE_DISTANCE_WIDTH_MAX;
		stockCandleTotalWidth = (chartWidth + stockCandleDistanceWidth) * 1.0 / daysTotal;
		stockCandleBodyWidth = stockCandleTotalWidth - stockCandleDistanceWidth;
		chartHeight = getPanelHeight() - StockGUIConst.CHART_TOP_BORDER_DISTANCE - StockGUIConst.CHART_BOTTOM_BORDER_DISTANCE;
		//Normalize stock candle array.
		normalizedStockCandleArray = new StockCandleArray(stockCandleArray);
		normalizedStockCandleArray.normalizeStockCandle(chartHeight, startDateIndex, endDateIndex);
		
		maxHigh = stockCandleArray.getMaxStockPrice(startDateIndex, daysTotal, StockCandleDataType.HIGH);
		minLow = stockCandleArray.getMinStockPrice(startDateIndex, daysTotal, StockCandleDataType.LOW);
		priceUnit = getPriceUnit(maxHigh - minLow);
		if (priceUnit < 0) {
			StockGUIUtil.showWarningMessageDialog("Cannot determine the right price unit for the chart grids.", "Price Unit Error");
			return;
		}
		minPriceOnGrid = Math.floor(minLow / priceUnit) * priceUnit;
		maxPriceOnGrid = (Math.floor(maxHigh / priceUnit) + 1) * priceUnit;
		yLineNumber = (int) (Math.floor(maxPriceOnGrid - minPriceOnGrid) / priceUnit) + 1;
		yUnit = chartHeight / (yLineNumber - 1);
		xUnit = stockCandleTotalWidth;
		
		System.out.println("Max High: " + maxHigh);
		System.out.println("Min Low: " + minLow);
		System.out.println("Price Unit: " + priceUnit);
		System.out.println("Min Price on Grid: " + minPriceOnGrid);
		System.out.println("Max Price on Grid: " + maxPriceOnGrid);
		System.out.println("Y Line Number: " + yLineNumber);
		System.out.println("Y Unit: " + yUnit);
	}
	
	private double getPriceUnit(double priceRange) {
		double unit = -1;
		for (int i = 0; i < StockGUIConst.STOCK_PRICE_UNIT.length; i++) {
			unit = StockGUIConst.STOCK_PRICE_UNIT[i];
			double gridNumber = priceRange / unit;
			if ((gridNumber >= 5) && (gridNumber <= 10)) break;
		}
		return unit;
	}
	
	private double getYFromPrice(double price) {
		return (price - minPriceOnGrid) / priceUnit * yUnit;
	}
	
	private double getPriceFromY(double y) {
		return y / yUnit * priceUnit + minPriceOnGrid;
	}
	
	private void paintBackgroundLines(Graphics2D g2Input) {
		//Translate coordinates so that the (0,0) is at the bottom left.
		//Notice that translation is accumlative. So we need to create another instance of graph.
		Graphics2D g2 = (Graphics2D) g2Input.create();
		g2.translate(StockGUIConst.CHART_LEFT_BORDER_DISTANCE, getPanelHeight() - StockGUIConst.CHART_BOTTOM_BORDER_DISTANCE);
		g2.scale(1.0, -1.0);
		//Paint horizontal lines.
		for (int i = 0; i <= yLineNumber; i++) {
			double x1 = 0;
			double x2 = chartWidth;
			double y1 = i * yUnit;
			double y2 = y1;
			StockGUIUtil.drawDottedLine(g2, x1, y1, x2, y2, Color.white);
		}
		//Paint vertical lines.
		for (int i = startDateIndex; i <= endDateIndex; i++) {
			boolean drawVerticalLine = false;
			if ((i == startDateIndex) || (i == endDateIndex)) drawVerticalLine = true;
			else {
				int thisMonth = StockGUIUtil.getMonth(stockCandleArray.getDate(i));
				int previousMonth = StockGUIUtil.getMonth(stockCandleArray.getDate(i - 1));
				if (thisMonth != previousMonth) {
					drawVerticalLine = true;
				}
			}
			if (drawVerticalLine) {
				double x1 = (i - startDateIndex) * stockCandleTotalWidth;
				double y1 = 0;
				double x2 = x1;
				double y2 = chartHeight;
				StockGUIUtil.drawDottedLine(g2, x1, y1, x2, y2, Color.white);
			}
		}
	}
	
	private void paintStockPriceScale(Graphics2D g2Input) {
		Graphics2D g2 = (Graphics2D) g2Input.create();
		g2.setFont(StockGUIConst.PRICE_SCALE_FONT);
		g2.setColor(StockGUIConst.PRICE_SCALE_COLOR);
		for (int i = 0; i <= yLineNumber; i++) {
			double x = 0;
			double y = getPanelHeight() - (StockGUIConst.CHART_BOTTOM_BORDER_DISTANCE + i * yUnit);
			double price = StockUtil.getRoundTwoDecimals(minPriceOnGrid + i * priceUnit);
			g2.drawString(Double.toString(price), (float) x, (float) y);	
			
		}		
	}
	
	private void paintStockCandles(Graphics2D g2Input) {
		Graphics2D g2 = (Graphics2D) g2Input.create();
		g2.translate(StockGUIConst.CHART_LEFT_BORDER_DISTANCE, getPanelHeight() - StockGUIConst.CHART_BOTTOM_BORDER_DISTANCE);
		g2.scale(1.0, -1.0);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		for (int i = startDateIndex; i <= endDateIndex; i++) {
			StockCandlePaint stockCandlePaint = new StockCandlePaint(normalizedStockCandleArray.get(i));
			stockCandlePaint.setGraphics2D(g2);
			stockCandlePaint.setBodyWidth(stockCandleBodyWidth);
//			stockCandlePaint.setShadowWidth(stockCandleShadowWidth);
			stockCandlePaint.setX(getXFromIndex(i));
			stockCandlePaint.paintCandle();
		}
		
	}
	
	private double getXFromIndex(int index) {
		return (index - startDateIndex) * xUnit;
	}
	
	public int getPanelHeight() {
		return (int) Math.floor(StockGUIUtil.getScreenHeight() * StockGUIConst.STOCK_CHART_PANEL_HEIGHT_PERCENTAGE);
	}
	
	public int getPanelWidth() {
		return StockGUIUtil.getScreenWidth();
	}
	
	public JFrame getFrame() {
		return mainPanel.getFrame();
	}
}
