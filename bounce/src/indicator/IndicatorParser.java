package indicator;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import stock.StockCandle;
import stock.StockParser;

public class IndicatorParser extends StockParser {
	private static final String DELIMITER = ","; 
	private static final int DATE_PIECE = 0;
	private static final int STOCKGAIN_PIECE = 1;
	private static final int RSI_PIECE = 2;
	
	public IndicatorParser() {
		super();
	}
	
	public IndicatorParser(String filename) {
		super(filename);
	}
	
	public IndicatorParser(File file) {
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
	
	public static ArrayList<StockIndicator> readCSVFile(File csvFile) {
		ArrayList<StockIndicator> stockIndicatorArray = new ArrayList<StockIndicator>();
		
		
		IndicatorParser parser = new IndicatorParser(csvFile);

		parser.startReadFile();
		String line;
		
		try {
			while ((line = parser.nextLine()) != null) {
				StockIndicator stockIndicator = new StockIndicator();
				parser.parseIndicatorLine(line, stockIndicator);
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
