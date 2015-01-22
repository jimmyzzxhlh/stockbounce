package draw;

import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;

import javax.swing.text.DateFormatter;

public class StockGUIConst {
	//Define panel height percentage. The sum of the percentage should be 1. 
	public static final double STOCK_CANDLE_INFO_PANEL_HEIGHT_PERCENTAGE = 0.1;
	public static final double STOCK_CHART_PANEL_HEIGHT_PERCENTAGE = 0.6;
	public static final double STOCK_INDICATOR_PANEL_HEIGHT_PERCENTAGE = 0.2;
	public static final double SETTINGS_PANEL_HEIGHT_PERCENTAGE = 0.1;
	
	
	public static final Color STOCK_GUI_BACKGROUND_COLOR = Color.black;
	
	//Label constants
	public static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 16);
	public static final Color LABEL_COLOR = Color.white;
	
	//Text field constants
	public static final Font TEXTFIELD_FONT = new Font("Arial", Font.PLAIN, 16);
	public static final Color TEXTFIELD_FOREGROUND_COLOR = Color.white;
	public static final Color TEXTFIELD_BACKGROUND_COLOR = STOCK_GUI_BACKGROUND_COLOR;
	public static final Color TEXTFIELD_BORDER_COLOR = Color.yellow;
	
	public static final DateFormatter DATE_FORMATTER = new DateFormatter(new SimpleDateFormat("yyyy-MM-dd"));
	
	//Button constants
	public static final Font BUTTON_FONT = new Font("Arial", Font.PLAIN, 16);
	public static final Color BUTTON_FOREGROUND_COLOR = Color.white;
	public static final Color BUTTON_BACKGROUND_COLOR = STOCK_GUI_BACKGROUND_COLOR;
	
	//Stock Chart Panel consts
	public static final int CHART_LEFT_BORDER_DISTANCE = 50;
	public static final int CHART_BOTTOM_BORDER_DISTANCE = 50;
	public static final int CHART_RIGHT_BORDER_DISTANCE = 50;
	public static final int CHART_TOP_BORDER_DISTANCE = 50;
	
//	public static final double STOCK_CANDLE_SHADOW_WIDTH_RATIO = 0.2;
//	public static final double STOCK_CANDLE_DISTANCE_WIDTH_RATIO = 0.2;
	public static final double STOCK_CANDLE_SHADOW_WIDTH_MAX = 1;
	public static final double STOCK_CANDLE_DISTANCE_WIDTH_MAX = 3;
	
	public static final Color STOCK_CANDLE_LONG_COLOR = Color.green;
	public static final Color STOCK_CANDLE_SHORT_COLOR = Color.red;
	
	public static final double STOCK_PRICE_UNIT[] = {0.10, 0.20, 0.25, 0.50, 1.0, 2.0, 2.5, 5.0, 10.0, 20.0, 25.0, 50.0, 100.0};
	public static final Font PRICE_SCALE_FONT = new Font("Arial", Font.PLAIN, 16);
	public static final Color PRICE_SCALE_COLOR = Color.gray; 
	
	//Stock Candle consts
	public static final Color WHITE_CANDLE_COLOR = Color.green;
	public static final Color BLACK_CANDLE_COLOR = Color.red;
	public static final Color NOCOLOR_CANDLE_COLOR = Color.white;
}
