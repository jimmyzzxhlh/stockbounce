package paint;

import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;

import javax.swing.text.DateFormatter;

public class StockGUIConst {
	
	public static final Color GOLD = Color.decode("#ffdf32");
	public static final Font STANDARD_FONT = new Font("Arial", Font.BOLD, 16);
	//Define panel height percentage. The sum of the percentage should be 1. 
	public static final double STOCK_CANDLE_INFO_PANEL_HEIGHT_PERCENTAGE = 0.1;
	public static final double STOCK_CHART_PANEL_HEIGHT_PERCENTAGE = 0.6;
	public static final double STOCK_INDICATOR_PANEL_HEIGHT_PERCENTAGE = 0.2;
	public static final double SETTINGS_PANEL_HEIGHT_PERCENTAGE = 0.1;
	
	
	public static final Color STOCK_GUI_BACKGROUND_COLOR = Color.black;
	
	//Label constants
	public static final Font LABEL_FONT = STANDARD_FONT;
	public static final Color LABEL_COLOR = Color.white;
	
	//Text field constants
	public static final Font TEXTFIELD_FONT = STANDARD_FONT;
	public static final Color TEXTFIELD_FOREGROUND_COLOR = Color.white;
	public static final Color TEXTFIELD_BACKGROUND_COLOR = STOCK_GUI_BACKGROUND_COLOR;
	public static final Color TEXTFIELD_BORDER_COLOR = Color.yellow;
	
	public static final DateFormatter DATE_FORMATTER = new DateFormatter(new SimpleDateFormat("yyyy-MM-dd"));
	
	//Button constants
	public static final Font BUTTON_FONT = STANDARD_FONT;
	public static final Color BUTTON_FOREGROUND_COLOR = Color.white;
	public static final Color BUTTON_BACKGROUND_COLOR = STOCK_GUI_BACKGROUND_COLOR;
	
	//CheckBox constants
	public static final Font CHECKBOX_FONT = STANDARD_FONT;
	public static final Color CHECKBOX_FOREGROUND_COLOR = Color.white;
	public static final Color CHECKBOX_BACKGROUND_COLOR = STOCK_GUI_BACKGROUND_COLOR;
	
	//Stock Chart Panel consts
	public static final int CHART_LEFT_BORDER_DISTANCE = 50;
	public static final int CHART_BOTTOM_BORDER_DISTANCE = 50;
	public static final int CHART_RIGHT_BORDER_DISTANCE = 50;
	public static final int CHART_TOP_BORDER_DISTANCE = 50;
	
//	public static final double STOCK_CANDLE_SHADOW_WIDTH_RATIO = 0.2;
//	public static final double STOCK_CANDLE_DISTANCE_WIDTH_RATIO = 0.2;
	public static final double STOCK_CANDLE_SHADOW_WIDTH_MAX = 1;
	public static final double STOCK_CANDLE_DISTANCE_WIDTH_MAX = 3;
	
	
	public static final Color MOUSE_HORIZONTAL_LINE_COLOR = Color.white;
	public static final Color MOUSE_VERTICAL_LINE_COLOR = MOUSE_HORIZONTAL_LINE_COLOR;
	
	public static final double STOCK_PRICE_UNIT[] = {0.10, 0.20, 0.25, 0.50, 1.0, 2.0, 2.5, 5.0, 10.0, 20.0, 25.0, 50.0, 100.0};
	public static final Font PRICE_SCALE_FONT = new Font("Arial", Font.PLAIN, 16);
	public static final Color PRICE_SCALE_COLOR = Color.gray; 
	
	public static final Font PRICE_LABEL_FONT = new Font("Arial", Font.BOLD, 14);
	public static final Color PRICE_LABEL_COLOR = Color.white;
	
	//Stock Candle consts
	public static final Color WHITE_CANDLE_COLOR = Color.green;
	public static final Color BLACK_CANDLE_COLOR = Color.red;
	public static final Color NOCOLOR_CANDLE_COLOR = Color.white;
	public static final Color EARNINGS_DATE_CANDLE_COLOR = GOLD;
	
	
	//Stock Candle Info Panel consts
	public static final Font STOCK_CANDLE_INFO_FONT = new Font("Arial", Font.BOLD, 16);
	public static final Color STOCK_CANDLE_INFO_COLOR = Color.white;
	
	//Lower Indicator Panel consts
	public static final int LOWER_INDICATOR_BOTTOM_BORDER_DISTANCE = 5;
	public static final int LOWER_INDICATOR_LEFT_BORDER_DISTANCE = CHART_LEFT_BORDER_DISTANCE;
	public static final int LOWER_INDICATOR_RIGHT_BORDER_DISTANCE = CHART_RIGHT_BORDER_DISTANCE;
	public static final int LOWER_INDICATOR_TOP_BORDER_DISTANCE = 5;
	
	
		
}
