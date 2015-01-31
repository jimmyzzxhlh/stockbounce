package paint;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JPanel;

import paint.indicator.IndicatorPaintAbstract;
import stock.StockAPI;
import stock.StockCandle;
import stock.StockCandleArray;
import stock.StockEnum.StockCandleDataType;
import util.StockUtil;

@SuppressWarnings("serial")
public class StockChartPanel extends JPanel {
	
	private StockMainPanel mainPanel;
	private SettingsPanel settingsPanel;
	private StockCandleInfoPanel stockCandleInfoPanel;
	private Date startDate;
	private Date endDate;
	private int startDateIndex;
	private int endDateIndex;
	private int daysTotal;
	private String symbol;
	
	
	private double stockCandleTotalWidth;
	private double stockCandleBodyWidth;
	private double stockCandleDistanceWidth;
	private int chartWidth;
	private int chartHeight;
	
	private double maxHigh;
	private double minLow;
	private double priceUnit;
	private double maxPriceOnGrid;
	private double minPriceOnGrid;
	private int yLineNumber;
	private double xUnit;
	private double yUnit;
	
	private Line2D mouseHorizontalLine;
	private Line2D mouseVerticalLine;
	
	private String priceLabel;
	private double priceLabelX;
	private double priceLabelY;
	private int priceLabelWidth;
	private int priceLabelHeight; 
	
	private IndicatorPaintAbstract indicatorPaint;
	
	private StockCandleArray stockCandleArray;
	private StockCandleArray normalizedStockCandleArray;
	
	public StockChartPanel() {
		//Add mouse motion listener for displaying the current stock candle's information on the top.
		
		this.addMouseMotionListener(new MouseAdapter() {
			/**
			 * Call the stock candle info panel to update the price / volume information.
			 */
			public void mouseMoved(MouseEvent e) {
				handleMouseMoved(e);
			}
		});
		
		
		this.setPreferredSize(new Dimension(getPanelWidth(), getPanelHeight()));
//		this.setBorder(BorderFactory.createLineBorder(Color.yellow));
		this.setOpaque(false);
	}
	
	public void reset() {
		startDate = null;
		endDate = null;
		startDateIndex = 0;
		endDateIndex = 0;
		daysTotal = 0;
		symbol = null;
		stockCandleTotalWidth = 0;
		stockCandleBodyWidth = 0;
		stockCandleDistanceWidth = 0;
		
		maxHigh = 0;
		minLow = 0;;
		priceUnit = 0;
		maxPriceOnGrid = 0;
		minPriceOnGrid = 0;
		yLineNumber = 0;
		xUnit = 0;
		yUnit = 0;
		
		mouseHorizontalLine = null;
		mouseVerticalLine = null;
		
		priceLabel = null;
		priceLabelX = 0;
		priceLabelY = 0;
		priceLabelWidth = 0;
		priceLabelHeight = 0; 
		indicatorPaint = null;		
		if (stockCandleArray != null) { 
			stockCandleArray.destroy();
		}
		stockCandleArray = null;
		if (normalizedStockCandleArray != null) {
			normalizedStockCandleArray.destroy();
		}
		normalizedStockCandleArray = null;
	}
	
	private void handleMouseMoved(MouseEvent e) {
		if ((symbol == null) || (startDate == null) || (endDate == null)) return;
//		System.out.println(e.getX() + " " + e.getY());
		handleMouseHorizontalLine(e);
		handleMouseVerticalLine(e);
		paintStockCandleInfo(e);
		handlePriceLabel(e);
	}
	
	private void handleMouseHorizontalLine(MouseEvent e) {
		repaintLine(mouseHorizontalLine);
		double y1 = e.getY();
		if (y1 < StockGUIConst.CHART_TOP_BORDER_DISTANCE) return;
		if (y1 > getPanelHeight() - StockGUIConst.CHART_BOTTOM_BORDER_DISTANCE) return;
		double y2 = y1;
		double x1 = StockGUIConst.CHART_LEFT_BORDER_DISTANCE;
		double x2 = getPanelWidth() - StockGUIConst.CHART_RIGHT_BORDER_DISTANCE;
		mouseHorizontalLine = new Line2D.Double(x1, y1, x2, y2);
		repaintLine(mouseHorizontalLine);
	}
	
	private void handleMouseVerticalLine(MouseEvent e) {
		repaintLine(mouseVerticalLine);
		double x1 = e.getX();
		if (x1 < StockGUIConst.CHART_LEFT_BORDER_DISTANCE) return;
		if (x1 > getPanelWidth() - StockGUIConst.CHART_RIGHT_BORDER_DISTANCE) return;
		double x2 = x1;
		double y1 = StockGUIConst.CHART_TOP_BORDER_DISTANCE;
		double y2 = getPanelHeight() - StockGUIConst.CHART_BOTTOM_BORDER_DISTANCE;
		mouseVerticalLine = new Line2D.Double(x1, y1, x2, y2);
		repaintLine(mouseVerticalLine);
	}
	
	private void repaintLine(Line2D line) {
		if (line == null) return;
		int BUFFER = 2;
		double x1 = line.getX1();
		double y1 = line.getY1();
		double x2 = line.getX2();
		double y2 = line.getY2();
		int x1Int = (int) Math.floor(x1) - BUFFER;
		if (x1Int < 0) x1Int = 0;
		int y1Int = (int) Math.floor(y1) - BUFFER;
		if (y1Int < 0) y1Int = 0;
		int height = (int) Math.round(Math.abs(y1 - y2)) + 2 * BUFFER;
		int width = (int) Math.round(Math.abs(x1 - x2)) + 2 * BUFFER;
//		System.out.println(x1Int + " " + y1Int + " " + width + " " + height);
		repaint(x1Int, y1Int, width, height);		
		
	}
	
	
	
	private void paintStockCandleInfo(MouseEvent e) {
		double x = e.getX();
		if (!isXWithinChart(x)) return;
		x = x - StockGUIConst.CHART_LEFT_BORDER_DISTANCE;
		int index = getIndexFromTranslatedX(x);
		StockCandle stockCandle = stockCandleArray.get(index);
//		System.out.println(x + " " + index);
		stockCandleInfoPanel.setStockCandle(stockCandle);
		stockCandleInfoPanel.repaint();
	}
	
	private void handlePriceLabel(MouseEvent e) {
		if (!isYWithinChart(e.getY())) return;
		
		if (priceLabel != null) {
			int xInt = (int) Math.floor(priceLabelX);
			int yInt = (int) Math.floor(priceLabelY);
			repaint(xInt, yInt - priceLabelHeight, priceLabelWidth, priceLabelHeight);			
		}
		
		priceLabelX = e.getX();
		priceLabelY = e.getY();
		int xInt = (int) Math.floor(priceLabelX) + 3;  //Add a little distance so that the price label is not right next to the lines.
		int yInt = (int) Math.floor(priceLabelY) - 3;
//		System.out.println(xInt + " " + yInt);
		double price = getPriceFromY(priceLabelY);
		priceLabel = Double.toString(StockUtil.getRoundTwoDecimals(price));
		repaint(xInt, yInt - priceLabelHeight, priceLabelWidth, priceLabelHeight);
	}
	
	public void setMainPanel(StockMainPanel mainPanel) {
		this.mainPanel = mainPanel; 
	}
	
	public void setSettingsPanel(SettingsPanel settingsPanel) {
		this.settingsPanel = settingsPanel;
	}
	
	public void setStockCandleInfoPanel(StockCandleInfoPanel stockCandleInfoPanel) {
		this.stockCandleInfoPanel = stockCandleInfoPanel;
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
	
	public int getStartDateIndex() {
		return startDateIndex;
	}
	
	public int getEndDateIndex() {
		return endDateIndex;
	}
	
	public StockCandleArray getStockCandleArray() {
		return stockCandleArray;
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
	
	public boolean hasChart() {
		return (stockCandleArray != null);
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
		initializeChartParameters(g2);
		paintBackgroundLines(g2);
		paintStockPriceScale(g2);
		paintStockCandles(g2);		
		paintMouseHorizontalLine(g2);
		paintMouseVerticalLine(g2);
		paintPriceLabel(g2);
		paintIndicators(g2);
	}

	private void initializeChartParameters(Graphics2D g2) {
		chartWidth = getPanelWidth() - StockGUIConst.CHART_LEFT_BORDER_DISTANCE - StockGUIConst.CHART_RIGHT_BORDER_DISTANCE;
//		stockCandleShadowWidth = StockGUIConst.STOCK_CANDLE_SHADOW_WIDTH_MAX;
		stockCandleDistanceWidth = StockGUIConst.STOCK_CANDLE_DISTANCE_WIDTH_MAX;
		stockCandleTotalWidth = (chartWidth + stockCandleDistanceWidth) * 1.0 / daysTotal;
		stockCandleBodyWidth = stockCandleTotalWidth - stockCandleDistanceWidth;
		chartHeight = getPanelHeight() - StockGUIConst.CHART_TOP_BORDER_DISTANCE - StockGUIConst.CHART_BOTTOM_BORDER_DISTANCE;
		
		maxHigh = stockCandleArray.getMaxStockPrice(startDateIndex, daysTotal, StockCandleDataType.HIGH);
		minLow = stockCandleArray.getMinStockPrice(startDateIndex, daysTotal, StockCandleDataType.LOW);
		priceUnit = getPriceUnit(maxHigh - minLow);
		if (priceUnit < 0) {
			StockGUIUtil.showWarningMessageDialog("Cannot determine the right price unit for the chart grids.", "Price Unit Error");
			return;
		}
		minPriceOnGrid = Math.floor(minLow / priceUnit) * priceUnit;
		maxPriceOnGrid = (Math.floor(maxHigh / priceUnit) + 1) * priceUnit;
		yLineNumber = (int) (Math.round((maxPriceOnGrid - minPriceOnGrid) / priceUnit)) + 1;
		yUnit = chartHeight / (yLineNumber - 1);
		xUnit = stockCandleTotalWidth;
		
		//Normalize stock candle array.
		normalizedStockCandleArray = new StockCandleArray(stockCandleArray);
		normalizedStockCandleArray.normalizeStockCandle(chartHeight, minPriceOnGrid, maxPriceOnGrid, startDateIndex, endDateIndex);
		
		String maxPriceOnGridStr = Double.toString(StockUtil.getRoundTwoDecimals(maxPriceOnGrid));
		priceLabelWidth = (int) Math.round(StockGUIUtil.getStringWidth(g2, StockGUIConst.PRICE_LABEL_FONT, maxPriceOnGridStr)) + 1;
		priceLabelHeight = (int) Math.round(StockGUIUtil.getStringHeight(g2, StockGUIConst.PRICE_LABEL_FONT, maxPriceOnGridStr)) + 1;
		
//		System.out.println("Max High: " + maxHigh);
//		System.out.println("Min Low: " + minLow);
//		System.out.println("Price Unit: " + priceUnit);
//		System.out.println("Min Price on Grid: " + minPriceOnGrid);
//		System.out.println("Max Price on Grid: " + maxPriceOnGrid);
//		System.out.println("Y Line Number: " + yLineNumber);
//		System.out.println("Y Unit: " + yUnit);
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
	
	private Graphics2D getTranslatedG2(Graphics2D g2Input) {
		Graphics2D g2 = (Graphics2D) g2Input.create();
		g2.translate(StockGUIConst.CHART_LEFT_BORDER_DISTANCE, getPanelHeight() - StockGUIConst.CHART_BOTTOM_BORDER_DISTANCE);
		g2.scale(1.0, -1.0);
		return g2;
	}
	
	private void paintBackgroundLines(Graphics2D g2Input) {
		//Translate coordinates so that the (0,0) is at the bottom left.
		//Notice that translation is accumlative. So we need to create another instance of graph.
		Graphics2D g2 = getTranslatedG2(g2Input); 
		//Paint horizontal lines.
		for (int i = 0; i < yLineNumber; i++) {
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
		for (int i = 0; i < yLineNumber; i++) {
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
			stockCandlePaint.setX(getTranslatedXFromIndex(i));
//			System.out.println(StockUtil.formatDate(stockCandlePaint.date) + ", x = " + getTranslatedXFromIndex(i) +
//					", lowY = " + stockCandlePaint.getLowY() + ", priceLowY = " + getPriceFromTranslatedY(stockCandlePaint.getLowY()) +
//					", highY = " + stockCandlePaint.getHighY() + ", priceHighY = " + getPriceFromTranslatedY(stockCandlePaint.getHighY()));
			
			stockCandlePaint.paintCandle();
		}
		
	}
	
	private void paintMouseHorizontalLine(Graphics2D g2) {
		if (mouseHorizontalLine == null) return;
		g2.setColor(StockGUIConst.MOUSE_HORIZONTAL_LINE_COLOR);		
		g2.draw(mouseHorizontalLine);
	}
	
	private void paintMouseVerticalLine(Graphics2D g2) {
		if (mouseVerticalLine == null) return;
		g2.setColor(StockGUIConst.MOUSE_VERTICAL_LINE_COLOR);		
		g2.draw(mouseVerticalLine);
	}
	
	private void paintPriceLabel(Graphics2D g2) {
		if (priceLabel == null) return;
		g2.setFont(StockGUIConst.PRICE_LABEL_FONT);
		g2.setColor(StockGUIConst.PRICE_LABEL_COLOR);
		g2.drawString(priceLabel, (float) priceLabelX + 3, (float) priceLabelY - 3);
	}
	
	public void setIndicatorPaint(IndicatorPaintAbstract indicatorPaint) {
		this.indicatorPaint = indicatorPaint;
	}
	
	public void destroyIndicatorPaint() {
		if (indicatorPaint == null) return;
		indicatorPaint.destroy();
		indicatorPaint = null;
	}
	
	
	private void paintIndicators(Graphics2D g2Input) {
		if (indicatorPaint == null) return;
		Graphics2D g2 = getTranslatedG2(g2Input);
		indicatorPaint.paintIndicator(g2);
	}
	
	public double getTranslatedYFromPrice(double price) {
		return (price - minPriceOnGrid) / priceUnit * yUnit;
	}
	
	private double getPriceFromTranslatedY(double translatedY) {
		return translatedY / yUnit * priceUnit + minPriceOnGrid;
	}
	
	private double getPriceFromY(double y) { 
		return getPriceFromTranslatedY(getTranslatedY(y));
	}
	
	private double getTranslatedY(double y) {
		return (getPanelHeight() - StockGUIConst.CHART_BOTTOM_BORDER_DISTANCE) - y; 
	}
	
	public double getTranslatedXFromIndex(int index) {
		return (index - startDateIndex) * xUnit;
	}
	
	private int getIndexFromTranslatedX(double x) {
		return (int) Math.floor(x / xUnit) + startDateIndex;
	}
	
	
	public int getPanelHeight() {
		return (int) Math.floor(StockGUIUtil.getScreenHeight() * StockGUIConst.STOCK_CHART_PANEL_HEIGHT_PERCENTAGE);
	}
	
	public int getPanelWidth() {
		return StockGUIUtil.getScreenWidth();
	}
	
	
	private boolean isXWithinChart(double x) {
		if (x < StockGUIConst.CHART_LEFT_BORDER_DISTANCE) return false;
		if (x + StockGUIConst.CHART_RIGHT_BORDER_DISTANCE > getPanelWidth()) return false;
		return true;
	}
	
	private boolean isYWithinChart(double y) {
		if (y < StockGUIConst.CHART_TOP_BORDER_DISTANCE) return false;
		if (y + StockGUIConst.CHART_BOTTOM_BORDER_DISTANCE > getPanelHeight()) return false;
		return true;
	}
	
	
	public JFrame getFrame() {
		return mainPanel.getFrame();
	}
}
