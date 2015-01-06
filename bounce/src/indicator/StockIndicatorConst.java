package indicator;

public class StockIndicatorConst {
	public static final int PIECE_NUMBER = 11;
	public static final int DATE_PIECE = 0;
	public static final int STOCK_OPEN_PIECE = 1;
	public static final int STOCK_CLOSE_PIECE = 2;
	public static final int STOCK_HIGH_PIECE = 3;
	public static final int STOCK_LOW_PIECE = 4;
	public static final int STOCKGAIN_PIECE = 5;
	public static final int RSI_PIECE = 6;
	public static final int BOLLINGER_BANDS_PERCENTB_PIECE = 7; 
	public static final int BOLLINGER_BANDS_BANDWIDTH_PIECE = 8;
	public static final int EMA_DISTANCE_PIECE = 9;
	public static final int VOLUME_PIECE = 10;
	//Add new indicators here
	
	
	public static final double NAN = -1e10;
	
	//Related to reading CSV
	public static final String INDICATOR_CSV_DIRECTORY_PATH = "D:\\zzx\\Stock\\Indicators_CSV\\";
	public static final int MIN_VOLUME = 0;
	
	public static final int STOCK_INDICATOR_COUNT = 4;
	public static final int STOCK_GAIN_CLASSIFICATION_COUNT = 2;
	public static final double STOCK_GAIN_MIN_FOR_ONE_CLASS_SVM = 20;
	
	//Define indicator period constants
	public static final int STOCK_GAIN_PERIOD = 20;
	public static final int RSI_PERIOD = 26;
	public static final int BOLLINGER_BANDS_PERIOD = 20;
	public static final int BOLLINGER_BANDS_K = 2;
	public static final int EMA_DISTANCE_PERIOD = 50;
	public static final int MAX_SELL_PERIOD = 40; //in days
	//Add new indicator constants here
	
	public static enum INDICATORENUM {
		RSI, BOLLINGER_BANDS_PERCENTB, BOLLINGER_BANDS_BANDWIDTH, EMA_DISTANCE
	}
	
	//Add new indicators here
	public static final double NEXTDAY_OPEN_DIFFERENCE_THRESHOLD = 0.075;
	public static final double SAMEDAY_OPEN_CLOSE_DIFFERENCE_THRESHOLD = 0.15;
	public static final double NEXTDAY_CLOSE_DIFFERENCE_THRESHOLD = 0.20; 
			
	
	/**
	 * Stock Average Cost Indicator
	 */
	//When a stock is reversed from the bottom, calculate the percentage difference between the close price
	//and the average cost price. It should be >= 4% for now to indicate a strong reverse.
	public static final double AVERAGE_COST_REVERSE_UP_MIN_DIFFERENCE = 0.04;
	
}
