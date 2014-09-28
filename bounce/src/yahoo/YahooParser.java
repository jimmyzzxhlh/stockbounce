package yahoo;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.joda.time.LocalDate;

import stock.StockCandle;
import stock.StockCandleArray;
import stock.StockParser;
import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;

public class YahooParser extends StockParser {

	private static final int DATE_PIECE = 0;
	private static final int OPEN_PIECE = 1;
	private static final int HIGH_PIECE = 2;
	private static final int LOW_PIECE = 3;
	private static final int CLOSE_PIECE = 4;
	private static final int VOLUME_PIECE = 5;
	private static final String DELIMITER = ","; 
	
	 
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
		String lineArray[] = line.split(DELIMITER);
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
		
	}
	
	@Override
	public boolean isLineValid(String line) {
		if (this.lineNumber == 1) return false;
		return true;
	}

	public static StockCandleArray readCSVFile(String filename, int maxCandle) {
		File csvFile = new File(filename);
		return readCSVFile(csvFile, maxCandle);
	}
	
	public static StockCandleArray readCSVFile(File csvFile, int maxCandle) {
		StockCandleArray stockCandleArray;
		
		YahooParser parser = new YahooParser(csvFile);
		stockCandleArray = new StockCandleArray();
		String symbol = csvFile.getName().substring(0, csvFile.getName().length() - 4);
		stockCandleArray.setSymbol(symbol);
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
				if (m.isHoliday(new LocalDate(stockCandle.getDate()))) {
					System.out.println(stockCandle.getDate() + " is a holiday. Data will be ignored.");
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
