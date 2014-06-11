package yahoo;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import stock.StockCandle;
import stock.StockParser;

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


	
}
