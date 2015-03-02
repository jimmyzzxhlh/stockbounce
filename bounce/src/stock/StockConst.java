package stock;

public class StockConst {
	public static final String STOCK_CSV_DIRECTORY_PATH = "D:\\zzx\\Stock\\CSV\\";
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
	
	public static final String COMPANY_LIST_NASDAQ_FILENAME = "D:\\zzx\\Stock\\companylist_nasdaq.csv";
	public static final String COMPANY_LIST_NYSE_FILENAME = "D:\\zzx\\Stock\\companylist_nyse.csv";
	public static final String COMPANY_LIST_SSE_FILENAME = "D:\\zzx\\Stock\\companylist_sse.csv";
	public static final String COMPANY_LIST_SZSE_FILENAME = "D:\\zzx\\Stock\\companylist_szse.csv";
	
//	public static final String COMMA_DELIMITER = ",";  Do not use this comma or quote delimiter. Use StockUtil.splitCSVLine to split a line from CSV.
//	public static final String QUOTE_DELIMITER = "\"";
	
	//Download intraday stock parameters
	public static final int INTRADAY_DOWNLOAD_INTERVAL_GOOGLE = 60;
	public static final int INTRADAY_DOWNLOAD_PERIOD_GOOGLE = 15;  //Maximum days for downloading intraday stock data is 15 days.
	
	public static final String INTRADAY_LOW_POSITION_DIRECTORY_PATH = "D:\\zzx\\Stock\\IntraDayLowPosition\\";
	public static final String INTRADAY_VOLUME_DISTRIBUTION_FILENAME = "D:\\zzx\\Stock\\Distribution_All.txt";
	public static final String INTRADAY_LOW_HIGH_INTERVAL_FILENAME = "D:\\zzx\\Stock\\LowHighInterval.txt";
	
	public static final String INTRADAY_DIRECTORY_PATH_YAHOO = "D:\\zzx\\Stock\\IntraDay\\";
	public static final String INTRADAY_DIRECTORY_PATH_GOOGLE = "D:\\zzx\\Stock\\IntraDayGoogle\\";
	public static final String INTRADAY_DIRECTORY_SSE_PATH = "D:\\zzx\\Stock\\IntraDaySSE\\";
	public static final String INTRADAY_DIRECTORY_SZSE_PATH = "D:\\zzx\\Stock\\IntraDaySZSE\\";
	
	public static final int INTRADAY_DEFAULT_SLEEP_TIME = 500;
	
	public static final double LONG_DAY_PERCENTAGE = 0.01;
	
	//Earnings related
	public static final String EARNINGS_DATES_DIRECTORY_PATH_ZACH = "D:\\zzx\\Stock\\EarningsDatesZach\\";
	public static final String EARNINGS_DATES_DIRECTORY_PATH_STREET_INSIDER = "D:\\zzx\\Stock\\EarningsDatesStreetInsider\\";
	public static final String EARNINGS_DATES_DIRECTORY_PATH_THE_STREEET = "D:\\zzx\\Stock\\EarningsDatesTheStreet\\";
	public static final String EARNINGS_DATES_STREET_INSIDER_FILENAME = "D:\\zzx\\Stock\\EarningsDatesStreetInsider.csv";
	public static final String EARNINGS_DATES_ZACH_FILENAME = "D:\\zzx\\Stock\\EarningsDatesZach.csv";
	public static final String EARNINGS_DATES_THE_STREET_FILENAME = "D:\\zzx\\Stock\\EarningsDatesTheStreet.csv";
	public static final String EARNINGS_DATES_FILENAME = "D:\\zzx\\Stock\\EarningsDates.csv";
	public static final int CLOSE_TO_EARNING_DAYS = 10;
	
}
