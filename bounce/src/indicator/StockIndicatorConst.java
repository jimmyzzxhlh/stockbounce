package indicator;

public class StockIndicatorConst {
	public static final int DATE_PIECE = 0;
	public static final int STOCKGAIN_PIECE = 1;
	public static final int RSI_PIECE = 2;
	public static final int BOLLINGER_BANDS_PERCENTB_PIECE = 3; 
	public static final int BOLLINGER_BANDS_BANDWIDTH_PIECE = 4;
	public static final int EMA_DISTANCE_PIECE = 5;
	public static final int VOLUME_PIECE = 6;
	//Add new indicators here
	
	
	public static final double NAN = -1e10;
	
	//Related to reading CSV
	public static final String STOCK_CSV_DIRECTORY_PATH = "D:\\zzx\\Stock\\CSV\\";
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
	
	//Add new indicators here
}