package yahoo;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.joda.time.LocalDate;

import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;
import stock.CandleList;
import stock.DailyCandle;
import stock.OutstandingShares;
import stock.StockConst;
import stock.StockParser;
import util.StockUtil;

public class YahooParser extends StockParser {
	
	private static final int DATE_PIECE = 0;
	private static final int OPEN_PIECE = 1;
	private static final int HIGH_PIECE = 2;
	private static final int LOW_PIECE = 3;
	private static final int CLOSE_PIECE = 4;
	private static final int VOLUME_PIECE = 5;
	private static final int ADJ_CLOSE_PIECE = 6;
	
	 
	public YahooParser() {
		super();
	}
	
	public YahooParser(String filename) {
		super(filename);
	}
	
	public YahooParser(File file) {
		super(file);
	}
	
	@Override
	public DailyCandle parseLine(String line) {
		String lineArray[] = StockUtil.splitCSVLine(line);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		LocalDate date = null;
		try {
			date = new LocalDate(formatter.parse(lineArray[DATE_PIECE]));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		double open = Double.parseDouble(lineArray[OPEN_PIECE]);
		double close = Double.parseDouble(lineArray[CLOSE_PIECE]);		
		double high = Double.parseDouble(lineArray[HIGH_PIECE]);
		double low = Double.parseDouble(lineArray[LOW_PIECE]);
		long volume = Long.parseLong(lineArray[VOLUME_PIECE]);
		double adjClose = Double.parseDouble(lineArray[ADJ_CLOSE_PIECE]);
		return new DailyCandle(date, open, close, high, low, volume, adjClose);
		
	}
	
	@Override
	public boolean isLineValid(String line) {
		if (this.lineNumber == 1) return false;
		return true;
	}
	
	public static CandleList readCSVFile(File csvFile) {
		return readCSVFile(csvFile, -1);
	}
	
	public static CandleList readCSVFile(String symbol) {
		return readCSVFile(symbol, -1);
	}
	
	public static CandleList readCSVFile(String symbol, int maxCandle) {
		String filename = StockConst.STOCK_CSV_DIRECTORY_PATH + symbol + ".csv";
		File file = new File(filename);
		if (!file.exists()) return null;
		return readCSVFile(file, maxCandle);
	}
	
	public static CandleList readCSVFile(File csvFile, int maxCandle) {
		CandleList stockCandleList;
		
		YahooParser parser = new YahooParser(csvFile);
		String symbol = StockUtil.getSymbolFromFile(csvFile);
		long outstandingShares = OutstandingShares.getOutstandingShares(symbol);
		stockCandleList = new CandleList(symbol, outstandingShares);
		parser.startReadFile();
		String line;
		
		int candleCount = 0;
		
		HolidayManager m = HolidayManager.getInstance(HolidayCalendar.UNITED_STATES);
		 
		
		try {
			while ((line = parser.nextLine()) != null) {
				candleCount++;
				if ((maxCandle > 0) & (candleCount > maxCandle)) break;
				DailyCandle candle = parser.parseLine(line);
				//Ignore holiday or no trade date.
				if (candle.getVolume() <= 0 || m.isHoliday(candle.getInstant())) {
//					System.out.println(stockCandle.getDate() + " is a holiday. Data will be ignored.");
					continue;
				}
				stockCandleList.add(candle);				
				
			}
			parser.closeFile();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		stockCandleList.sortByDate();
		return stockCandleList;
	}
	
	
	
}
