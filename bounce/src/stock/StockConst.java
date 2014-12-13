package stock;

public class StockConst {
	public static final String STOCK_CSV_DIRECTORY_PATH = "D:\\zzx\\Stock\\CSV\\";
	public static final String INTRADAY_DIRECTORY_PATH = "D:\\zzx\\Stock\\IntraDay\\";
	public static final String SNAPSHOT_DIRECTORY_PATH = "D:\\zzx\\Stock\\Snapshots\\";
	public static final String SHARES_OUTSTANDING_FILENAME = "D:\\zzx\\Stock\\SharesOutstanding.csv";
	public static final String PREVIOUS_CLOSE_FILENAME = "D:\\zzx\\Stock\\PreviousClose.csv";
	public static final String TURNOVER_RATE_DISTRIBUTION_FILENAME = "D:\\zzx\\Stock\\TurnoverRateDistribution.csv";
	public static final int TURNOVER_RATE_DISTRIBUTION_ARRAY_LENGTH = 1001;
	
	//Market capitalization definition:
	//Large: >= 10 billion
	//Middle: 2-10 billion
	//Small: < 2 billion
	public static final double LARGE_MARKET_CAP_MIN = 1e10;
	public static final double MIDDLE_MARKET_CAP_MAX = LARGE_MARKET_CAP_MIN - 1;
	public static final double MIDDLE_MARKET_CAP_MIN = 2e9;
	public static final double SMALL_MARKET_CAP_MAX = MIDDLE_MARKET_CAP_MIN - 1;
	public static final double SMALL_MARKET_CAP_MIN = 0;
	
	public static final String COMPANY_LIST_NASDAQ_FILENAME = "D:\\zzx\\Stock\\companylist.csv";
	
	public static final String COMMA_DELIMITER = ",";
	public static final String QUOTE_DELIMITER = "\"";
	
	//Download intraday stock parameters
	public static final int INTRADAY_DOWNLOAD_INTERVAL = 60;
	public static final int INTRADAY_DOWNLOAD_PERIOD = 15;  //Maximum days for downloading intraday stock data is 15 days.
	
	public static final String INTRADAY_LOW_POSITION_DIRECTORY_PATH = "D:\\zzx\\Stock\\IntraDayLowPosition\\";
	public static final String INTRADAY_VOLUME_DISTRIBUTION_FILENAME = "D:\\zzx\\Stock\\IntraDayVolume\\Distribution_All.txt";
	
}
