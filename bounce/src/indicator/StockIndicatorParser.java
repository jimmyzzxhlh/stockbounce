package indicator;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import stock.StockCandle;
import stock.StockCandleArray;
import stock.StockFileWriter;
import stock.StockParser;
import util.StockUtil;
import yahoo.YahooParser;

/**
 * Parse indicator CSV file.
 * The file format is:
 * 1. Date
 * 2. Expected stock gain percentage. 
 * 3. Indicator lists. Current supported indicators:
 * (1) RSI
 * (2) Bollinger Bands Percentage B
 * (3) Bollinger Bands Bandwidth
 * (4) EMA Distance
 * @author Dongyue Xue
 *
 */

public class StockIndicatorParser extends StockParser {
	private static final String DELIMITER = ","; 
	
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
	
	/**
	 * Parse one line from the indicator CSV file.
	 * @param line
	 * @param stockIndicator
	 */
	public void parseIndicatorLine(String line, StockIndicator stockIndicator) {
		String lineArray[] = line.split(DELIMITER);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			stockIndicator.setDate(formatter.parse(lineArray[StockIndicatorConst.DATE_PIECE]));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		stockIndicator.setStockGain(Double.parseDouble(lineArray[StockIndicatorConst.STOCKGAIN_PIECE]));
		stockIndicator.setStockGainClassificationFromStockGain();
		stockIndicator.setRSI(Double.parseDouble(lineArray[StockIndicatorConst.RSI_PIECE]));
		stockIndicator.setBollingerBandsPercentB(Double.parseDouble(lineArray[StockIndicatorConst.BOLLINGER_BANDS_PERCENTB_PIECE]));
		stockIndicator.setBollingerBandsBandwidth(Double.parseDouble(lineArray[StockIndicatorConst.BOLLINGER_BANDS_BANDWIDTH_PIECE]));
		stockIndicator.setEMADistance(Double.parseDouble(lineArray[StockIndicatorConst.EMA_DISTANCE_PIECE]));
		stockIndicator.setVolume(Integer.parseInt(lineArray[StockIndicatorConst.VOLUME_PIECE]));
		
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
	
	/**
	 * Read indicator values from one CSV file and return an array of indicators.
	 * @param csvFile
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static StockIndicatorArray readCSVFile(File csvFile, Date startDate, Date endDate) {
		StockIndicatorArray stockIndicatorArray = new StockIndicatorArray();
		StockIndicatorParser parser = new StockIndicatorParser(csvFile);
		String symbol = StockUtil.getSymbolFromFileName(csvFile.getName());
		
		parser.startReadFile();
		String line;
		
		try {
			while ((line = parser.nextLine()) != null) {
				StockIndicator stockIndicator = new StockIndicator();
				stockIndicator.setSymbol(symbol);
				parser.parseIndicatorLine(line, stockIndicator);
				//If start date is specified but the current date is before the start date, then ignore the line.
				if ((startDate != null) && (stockIndicator.getDate().before(startDate))) {
					continue;
				}
				//If end date is specified but the current date is after the end date, then ignore the line.
				if ((endDate != null) && (stockIndicator.getDate().after(endDate))) {
					continue;
				}
				//If any indicator value is NAN, then ignore the line.
				if (stockIndicator.hasNANValue()) {
					continue;
				}
//				if (stockIndicator.filterIndicator()) {
//					continue;
//				}
				//If the volume is too low then not counting the indicator.
				if (stockIndicator.getVolume() < StockIndicatorConst.MIN_VOLUME) {
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
	
	/**
	 * Read indicator values from multiple CSV files in a directory.
	 * @param directoryName
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static StockIndicatorArray readCSVFiles(String directoryName, Date startDate, Date endDate) {
		File directory = new File(directoryName);
		File[] directoryList = directory.listFiles();
		if (directoryList == null) return null;
		StockIndicatorArray stockIndicatorArray = new StockIndicatorArray();
		for (File csvFile : directoryList) {
//			char c = csvFile.getName().charAt(0);
//			if (c >= 'F') break;
			System.out.println("Reading File: " + csvFile.getName());
			StockIndicatorArray singleStockIndicatorArray = readCSVFile(csvFile, startDate, endDate);
			StockIndicatorArray.copyStockIndicatorArray(singleStockIndicatorArray, stockIndicatorArray);
			singleStockIndicatorArray = null;
		}
		return stockIndicatorArray;
	}
	
	public static StockIndicatorArray readCSVFiles(String directoryName) {
		return readCSVFiles(directoryName, null, null);
	}
	
	/**
	 * Compute indicator values and write to CSV file. 
	 * @throws Exception
	 */
	public static void writeStockIndicators() throws Exception {
		File directory = new File(StockIndicatorConst.STOCK_CSV_DIRECTORY_PATH);
		File[] directoryList = directory.listFiles();
		StockFileWriter sfw;
		StockCandleArray stockCandleArray;
		
		if (directoryList == null) return;
		
		for (File csvFile : directoryList) {
			//Initialize parser and parse each line of the stock data.
			System.out.println(csvFile.getName());
			stockCandleArray = YahooParser.readCSVFile(csvFile, 0);
			sfw = new StockFileWriter(StockIndicatorConst.INDICATOR_CSV_DIRECTORY_PATH + stockCandleArray.getSymbol() + "_Indicators.csv");
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			
			sfw.write("Date" + ",");
			sfw.write("Stock Gain (" + StockIndicatorConst.STOCK_GAIN_PERIOD + " days),");
			sfw.write("RSI" + ",");
			sfw.write("Bollinger Bands Percent B" + ",");
			sfw.write("Bollinger Bands Bandwidth" + ",");
			sfw.write("EMA Distance" + ",");
			sfw.write("Volume" + ",");
			//Add new indicators here
			sfw.newLine();
			
			Date[] stockDates = new Date[stockCandleArray.size()];
			for (int i = 0; i < stockCandleArray.size(); i++) {
				stockDates[i] = stockCandleArray.getDate(i);
			}
			double[] stockGains = StockGain.getStockGain(stockCandleArray, StockIndicatorConst.STOCK_GAIN_PERIOD);
			double[] rsi = StockIndicatorAPI.getRSI(stockCandleArray, StockIndicatorConst.RSI_PERIOD);
			double[] bbandsPercentB = StockIndicatorAPI.getBollingerBandsPercentB(stockCandleArray, StockIndicatorConst.BOLLINGER_BANDS_PERIOD, StockIndicatorConst.BOLLINGER_BANDS_K);
			double[] bbandsBandwidth = StockIndicatorAPI.getBollingerBandsBandwidth(stockCandleArray, StockIndicatorConst.BOLLINGER_BANDS_PERIOD, StockIndicatorConst.BOLLINGER_BANDS_K);
			double[] emaDistance = StockIndicatorAPI.getExponentialMovingAverageDistance(stockCandleArray, StockIndicatorConst.EMA_DISTANCE_PERIOD);
			int[] volume = StockIndicatorAPI.getVolume(stockCandleArray);
			//Add new indicators here
			
			for (int i = 0; i < stockCandleArray.size(); i++) {
				String strDate = formatter.format(stockDates[i]);
				sfw.write(strDate + ",");
				sfw.write(stockGains[i] + ",");
				sfw.write(rsi[i] + ",");
				sfw.write(bbandsPercentB[i] + ",");
				sfw.write(bbandsBandwidth[i] + ",");
				sfw.write(emaDistance[i] + ",");
				sfw.write(volume[i] + ",");
				//Add new indicators here
				sfw.newLine();
			}		
			sfw.close();
		}
	}
}
