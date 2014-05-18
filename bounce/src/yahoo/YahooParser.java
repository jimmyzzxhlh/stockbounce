package yahoo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import stock.StockParser;
import stock.StockPrice;

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
	@Override
	public void parseLine(String line, StockPrice stockPrice) {
		String lineArray[] = line.split(DELIMITER);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			stockPrice.date = formatter.parse(lineArray[DATE_PIECE]);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		stockPrice.open = Double.parseDouble(lineArray[OPEN_PIECE]);
		stockPrice.high = Double.parseDouble(lineArray[HIGH_PIECE]);
		stockPrice.low = Double.parseDouble(lineArray[LOW_PIECE]);
		stockPrice.close = Double.parseDouble(lineArray[CLOSE_PIECE]);
		stockPrice.volume = Integer.parseInt(lineArray[VOLUME_PIECE]);
		
	}
	
	@Override
	public boolean isLineValid(String line) {
		if (this.lineNumber == 1) return false;
		return true;
	}


	
}
