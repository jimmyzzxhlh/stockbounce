package yahoo;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.joda.time.LocalDate;

import stock.StockAPI;
import stock.StockCandle;
import stock.StockCandleArray;
import stock.StockConst;
import stock.StockParser;
import util.StockUtil;
import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;

public class YahooParser extends StockParser {
	
	//Consider stock split. This should be turned off if you want to compare the indicator value
	//with some third party chart.
	private static final boolean USE_ADJ_CLOSE = true;  

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
	public void parseLine(String line, StockCandle stockCandle) {
		String lineArray[] = StockUtil.splitCSVLine(line);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			stockCandle.date = formatter.parse(lineArray[DATE_PIECE]);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		stockCandle.open = Double.parseDouble(lineArray[OPEN_PIECE]);
		stockCandle.high = Double.parseDouble(lineArray[HIGH_PIECE]);
		stockCandle.low = Double.parseDouble(lineArray[LOW_PIECE]);
		stockCandle.close = Double.parseDouble(lineArray[CLOSE_PIECE]);
		stockCandle.volume = Integer.parseInt(lineArray[VOLUME_PIECE]);
		stockCandle.adjClose = Double.parseDouble(lineArray[ADJ_CLOSE_PIECE]);
		
	}
	
	@Override
	public boolean isLineValid(String line) {
		if (this.lineNumber == 1) return false;
		return true;
	}
	
	public static StockCandleArray readCSVFile(File csvFile) {
		return readCSVFile(csvFile, -1);
	}
	
	public static StockCandleArray readCSVFile(String symbol) {
		return readCSVFile(symbol, -1);
	}
	
	public static StockCandleArray readCSVFile(String symbol, int maxCandle) {
		String filename = StockConst.STOCK_CSV_DIRECTORY_PATH + symbol + ".csv";
		File file = new File(filename);
		if (!file.exists()) return null;
		return readCSVFile(file, maxCandle);
	}
	
	public static StockCandleArray readCSVFile(File csvFile, int maxCandle) {
		StockCandleArray stockCandleArray;
		
		YahooParser parser = new YahooParser(csvFile);
		stockCandleArray = new StockCandleArray();
		String symbol = StockUtil.getSymbolFromFile(csvFile);
		stockCandleArray.setSymbol(symbol);
		HashMap<String, Long> sharesOutstandingMap = StockAPI.getSharesOutstandingMap();
		stockCandleArray.setSharesOutstanding(sharesOutstandingMap.get(symbol));
		parser.startReadFile();
		String line;
		
		int candleCount = 0;
		
		HolidayManager m = HolidayManager.getInstance(HolidayCalendar.UNITED_STATES);
		 
		
		try {
			while ((line = parser.nextLine()) != null) {
				candleCount++;
				if ((maxCandle > 0) & (candleCount > maxCandle)) break;
				StockCandle stockCandle = new StockCandle();
				parser.parseLine(line, stockCandle);
				stockCandle.setSymbol(symbol);
				//Get the raw price in case that stock split happens.
				if (USE_ADJ_CLOSE) {
					stockCandle.setPriceFromAdjClose();
				}
				//Ignore holiday or no trade date.
				if ((stockCandle.volume <= 0) || (m.isHoliday(new LocalDate(stockCandle.getDate())))) {
//					System.out.println(stockCandle.getDate() + " is a holiday. Data will be ignored.");
					continue;
				}
				stockCandleArray.add(stockCandle);				
				
			}
			parser.closeFile();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		stockCandleArray.sortByDate();
		return stockCandleArray;
	}
	
	
	
}
