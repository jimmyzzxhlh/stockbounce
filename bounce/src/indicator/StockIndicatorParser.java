package indicator;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import stock.StockCandle;
import stock.StockParser;

/**
 * Parse indicator CSV file.
 * The file format is:
 * 1. Date
 * 2. Expected stock gain percentage. 
 * 3. Indicator lists. Current supported indicators:
 * (1) RSI
 * @author Dongyue Xue
 *
 */

public class StockIndicatorParser extends StockParser {
	private static final String DELIMITER = ","; 
	private static final int DATE_PIECE = 0;
	private static final int STOCKGAIN_PIECE = 1;
	private static final int RSI_PIECE = 2;
	
	public StockIndicatorParser() {
		super();
	}
	
	public StockIndicatorParser(String filename) {
		super(filename);
	}
	
	public StockIndicatorParser(File file) {
		super(file);
	}
	
	@Override
	public boolean isLineValid(String line) {
		if (this.lineNumber == 1) return false;
		return true;
	}

	@Override
	public void parseLine(String line, StockCandle stockCandle){
		return;
	}
	
	public void parseIndicatorLine(String line, StockIndicator stockIndicator) {
		String lineArray[] = line.split(DELIMITER);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			stockIndicator.setDate(formatter.parse(lineArray[DATE_PIECE]));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		stockIndicator.setStockGain(Double.parseDouble(lineArray[STOCKGAIN_PIECE]));
		stockIndicator.setRSI(Double.parseDouble(lineArray[RSI_PIECE]));
		//Add new indicators here
	}
	
	public static StockIndicatorArray readCSVFile(File csvFile) {
		return readCSVFile(csvFile, null, null);
	}
	
	public static StockIndicatorArray readCSVFile(String filename) {
		File csvFile = new File(filename);
		return readCSVFile(csvFile, null, null);
	}
	
	public static StockIndicatorArray readCSVFile(String filename, Date startDate, Date endDate) {
		File csvFile = new File(filename);
		return readCSVFile(csvFile, startDate, endDate);
	}
	
	public static StockIndicatorArray readCSVFile(File csvFile, Date startDate, Date endDate) {
		StockIndicatorArray stockIndicatorArray = new StockIndicatorArray();
		StockIndicatorParser parser = new StockIndicatorParser(csvFile);
		String symbol = csvFile.getName().substring(0, csvFile.getName().length() - 4);
		
		stockIndicatorArray.setSymbol(symbol);
		
		parser.startReadFile();
		String line;
		
		try {
			while ((line = parser.nextLine()) != null) {
				StockIndicator stockIndicator = new StockIndicator();
				parser.parseIndicatorLine(line, stockIndicator);
				//If start date is specified but the current date is before the start date, then ignore the line.
				if ((startDate != null) && (stockIndicator.getDate().before(startDate))) {
					continue;
				}
				//If end date is specified but the current date is after the end date, then ignore the line.
				if ((endDate != null) && (stockIndicator.getDate().after(endDate))) {
					continue;
				}
				stockIndicatorArray.add(stockIndicator);
				
			}
			parser.closeFile();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return stockIndicatorArray;
	}
	
}
