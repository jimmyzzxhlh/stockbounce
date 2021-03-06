package paint;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import stock.CandleList;
import stock.DailyCandle;


@SuppressWarnings("serial")
/**
 * 
 * Frame to paint the candle chart.
 * In the text box of at the top, enter two numbers delimited by comma.
 * The first number represents the number of candles being combined together. If set to 1, then it will be the daily chart.
 * The second number represents is between 0 to first number - 1. When combining X candles together, we will have X number of
 * different charts, based on which candle to start with at the beginning.
 * TODO:
 * 1. Paint components instead of painting the charts directly on the frame.
 * 2. Enhance the UI to show volume.
 * 3. Enhance the UI to show X number of different charts using a button instead of a text box.
 */
public class StockFrame extends JFrame implements ActionListener {
	static final int CANDLE_WIDTH = 5;
	public CandleList candleList;
	public int candleDays = 1;             // How many candles are combined together, 1 - Daily chart 
	public int candleDaysOffset = 0;       // We have total number of <candleDays> possible charts
	private JTextField textField;
	private JPanel panel;
	private JScrollPane scrollBar;
	private int patternIndex = -1;
	private static final int RADIUS = CANDLE_WIDTH *5;
	
	public StockFrame() {
		super();
		//
	}
	
	public void setIndex(int patternIndex){
		this.patternIndex = patternIndex;
	}
	
	public StockFrame(int width, int height) {
		super();
		this.setSize(width, height);
		this.setLayout(new BorderLayout());
		//panel = new JPanel();
		//scrollBar = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		//this.getContentPane().add(scrollBar);
		
		addTextField();
	}
	
	public void paint(Graphics g) {
		paintCandles(g);
		if (patternIndex == -1) return;
		
		//Draw a circle centering the indexed candle
		DailyCandle indexedStockCandle = candleList.get(patternIndex);
		
		int openInt = (int) Math.floor(indexedStockCandle.open);
		int closeInt = (int) Math.floor(indexedStockCandle.close);
		int frameHeight = this.getHeight();
		int x = CANDLE_WIDTH * patternIndex + 3 -RADIUS/2;
		int y = frameHeight - (int)((openInt+closeInt)/2) -RADIUS/2;
		
		g.setColor(Color.YELLOW);
		g.drawOval(x,y,RADIUS,RADIUS);
		
	}
	
	
	/**
	 * Paint each candle on the frame.
	 * 
	 * @param g
	 */
	public void paintCandles(Graphics g) {
		DailyCandle currentStockCandle;
		DailyCandle formattedStockCandle;
		if (candleList == null) return;
		int i = candleDaysOffset;
		while (i < candleList.size()) {
			formattedStockCandle = new DailyCandle();
			//Combine several candles together
			for (int j = i; j < i + candleDays; j++) {
				if (j >= candleList.size()) break;
				currentStockCandle = candleList.get(j);
				if (j == i) {
					formattedStockCandle.setDate(currentStockCandle.getDate());
					formattedStockCandle.setOpen(currentStockCandle.getOpen());					
				}
				formattedStockCandle.setClose(currentStockCandle.getClose());
				formattedStockCandle.setLowOverride(currentStockCandle.getLow());
				formattedStockCandle.setHighOverride(currentStockCandle.getHigh());
				formattedStockCandle.setVolume(formattedStockCandle.getVolume() + currentStockCandle.getVolume());				
			}
			//System.out.println(i + " " + formattedStockCandle.toString());
			paintCandle(g, CANDLE_WIDTH * i + 3, formattedStockCandle);
			i += candleDays;
			
		}	
	}
	
	public void paintCandle(Graphics g, int x, DailyCandle stockCandle) {
		int frameHeight = 0;
		int bodyHeight;
		int upperShadowStart;
		int upperShadowEnd;
		int lowerShadowStart;
		int lowerShadowEnd;
		//TODO: Do we really need to convert them to integer to paint?
		int openInt = (int) Math.floor(stockCandle.open);
		int highInt = (int) Math.floor(stockCandle.high);
		int lowInt = (int) Math.floor(stockCandle.low);
		int closeInt = (int) Math.floor(stockCandle.close);
		
		frameHeight = this.getHeight();
		if (frameHeight == 0) return;
		upperShadowStart = frameHeight - highInt;
		lowerShadowEnd = frameHeight - lowInt;
		//Increasing
		if (stockCandle.close > stockCandle.open) {
			g.setColor(Color.GREEN);
			upperShadowEnd = frameHeight - closeInt;
			lowerShadowStart = frameHeight - openInt; 
			bodyHeight = closeInt - openInt; 
		}
		//Decreasing
		else {
			g.setColor(Color.RED);
			upperShadowEnd =  frameHeight - openInt;
			lowerShadowStart =  frameHeight - closeInt;
			bodyHeight = openInt - closeInt;
		}
		//Draw the upper shadow of the candle
		g.drawLine(x, upperShadowStart, x, upperShadowEnd);
		
		//Draw the candle body
		g.fillRect(x - 2, upperShadowEnd, CANDLE_WIDTH, bodyHeight);
		
		//Draw the lower shadow of the candle
		g.drawLine(x, lowerShadowStart, x, lowerShadowEnd);
		
	}
	
		
	public void addTextField() {
		textField = new JTextField();
		this.add(textField, BorderLayout.NORTH);
		
		textField.addActionListener(this);
	}

	public void actionPerformed(ActionEvent ae) {
		try {
			String textFieldStr[] = textField.getText().split(",");
			candleDays = Integer.parseInt(textFieldStr[0]);
			candleDaysOffset = Integer.parseInt(textFieldStr[1]);
			if (candleDaysOffset >= candleDays) {
				candleDaysOffset = 0;
			}
		}
		catch (Exception e) {
			candleDays = 1;
			candleDaysOffset = 0;
		}
		this.setVisible(false);
		this.repaint();
		this.setVisible(true);
	}
	
}


