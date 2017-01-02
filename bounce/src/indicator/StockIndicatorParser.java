package indicator;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import stock.DailyCandle;
import stock.CandleList;
import stock.StockConst;
import stock.StockParser;
import util.StockFileWriter;
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
	public void parseLine(String line, DailyCandle stockCandle){
		return;
	}
	
	/**
	 * Parse one line from the indicator CSV file.
	 * @param line
	 * @param stockIndicator
	 */
	public void parseIndicatorLine(String line, StockIndicator stockIndicator) {
		String lineArray[] = StockUtil.splitCSVLine(line);
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
		stockIndicator.setVolume(Long.parseLong(lineArray[StockIndicatorConst.VOLUME_PIECE]));
		
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
		StockIndicatorArray stockIndicatorArray = new StockIndicatorArray();
		for (File csvFile : directory.listFiles()) {
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
	
	public static StockIndicatorArray readCSVFiles() {
		return readCSVFiles(StockIndicatorConst.INDICATOR_CSV_DIRECTORY_PATH);
	}
	
	/**
	 * Compute indicator values and write to CSV file. 
	 * @throws Exception
	 */
	public static void writeStockIndicators() throws Exception {
		File directory = new File(StockConst.STOCK_CSV_DIRECTORY_PATH);
		StockFileWriter sfw;
		CandleList stockCandleList;
		int excludedDays = 0;
		int includedPoints = 0;
		
		for (File csvFile : directory.listFiles()) {
			//Initialize parser and parse each line of the stock data.
			System.out.println(csvFile.getName());
			stockCandleList = YahooParser.readCSVFile(csvFile, 0);
			sfw = new StockFileWriter(StockIndicatorConst.INDICATOR_CSV_DIRECTORY_PATH + stockCandleList.getSymbol() + "_Indicators.csv");
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			
			String headerLine[] = new String[StockIndicatorConst.PIECE_NUMBER];
			headerLine[StockIndicatorConst.DATE_PIECE] = "Date" + ",";
			headerLine[StockIndicatorConst.STOCK_OPEN_PIECE] = "Open" + ",";
			headerLine[StockIndicatorConst.STOCK_CLOSE_PIECE] = "Close" + ",";
			headerLine[StockIndicatorConst.STOCK_HIGH_PIECE] = "High" + ",";
			headerLine[StockIndicatorConst.STOCK_LOW_PIECE] = "Low" + ",";
			headerLine[StockIndicatorConst.STOCKGAIN_PIECE] = "Stock Gain (" + StockIndicatorConst.STOCK_GAIN_PERIOD + " days),";
			headerLine[StockIndicatorConst.RSI_PIECE] = "RSI" + ",";
			headerLine[StockIndicatorConst.BOLLINGER_BANDS_PERCENTB_PIECE] = "Bollinger Bands Percent B" + ",";
			headerLine[StockIndicatorConst.BOLLINGER_BANDS_BANDWIDTH_PIECE] = "Bollinger Bands Bandwidth" + ",";
			headerLine[StockIndicatorConst.EMA_DISTANCE_PIECE] = "EMA Distance" + ",";
			headerLine[StockIndicatorConst.VOLUME_PIECE] = "Volume" + ",";
			//Add new indicators here
			
			for (int i=0; i < StockIndicatorConst.PIECE_NUMBER; i ++){
				sfw.write(headerLine[i]);
			}
			sfw.newLine();
			
			Date[] stockDates = new Date[stockCandleList.size()];
			double[] stockOpens = new double[stockCandleList.size()];
			double[] stockCloses = new double[stockCandleList.size()];
			double[] stockHighs = new double[stockCandleList.size()];
			double[] stockLows = new double[stockCandleList.size()];
			for (int i = 0; i < stockCandleList.size(); i++) {
				stockDates[i] = stockCandleList.getDate(i);
				stockOpens[i] = stockCandleList.getOpen(i);
				stockCloses[i] = stockCandleList.getClose(i);
				stockHighs[i] = stockCandleList.getHigh(i);
				stockLows[i] = stockCandleList.getLow(i);
			}
			
			double[] stockGains = StockGain.getStockGain(stockCandleList, StockIndicatorConst.STOCK_GAIN_PERIOD);
			double[] rsi = StockIndicatorAPI.getRSI(stockCandleList, StockIndicatorConst.RSI_PERIOD);
			double[] bbandsPercentB = StockIndicatorAPI.getBollingerBandsPercentB(stockCandleList, StockIndicatorConst.BOLLINGER_BANDS_PERIOD, StockIndicatorConst.BOLLINGER_BANDS_K);
			double[] bbandsBandwidth = StockIndicatorAPI.getBollingerBandsBandwidth(stockCandleList, StockIndicatorConst.BOLLINGER_BANDS_PERIOD, StockIndicatorConst.BOLLINGER_BANDS_K);
			double[] emaDistance = StockIndicatorAPI.getExponentialMovingAverageDistance(stockCandleList, StockIndicatorConst.EMA_DISTANCE_PERIOD);
			long[] volume = StockIndicatorAPI.getVolume(stockCandleList);
			//Add new indicators here
			for (int i = 0; i < stockCandleList.size(); i++) 
			{
				if (stockCandleList.getClose(i) < 3){
					continue;
				}
				if (stockCandleList.getVolume(i) < 5e5){
					continue;
				}
				//if (stockCandleList.getVolume(i) * stockCandleList.getClose(i) < 1e7){
				//	continue;
				//}
				//Exclude stock gains impacted by breaking news
				boolean exclude = false;
				if (i + StockIndicatorConst.STOCK_GAIN_PERIOD < stockCandleList.size()) 
				{
					for (int j = 0; j < StockIndicatorConst.STOCK_GAIN_PERIOD; j ++)
					{			
						double closePrice = stockCandleList.getClose(i + j);
						double nextdayOpenPrice = stockCandleList.getOpen(i + j + 1);
						double nextdayClosePrice = stockCandleList.getClose(i + j + 1);
						if (Math.abs(nextdayOpenPrice - closePrice)/closePrice > StockIndicatorConst.NEXTDAY_OPEN_DIFFERENCE_THRESHOLD) {
							exclude = true;								
							//System.out.println(stockCandleList.getSymbol() + " Next day open difference exceeds threshold: " + stockCandleList.get(i + j).date);
							break;
						}
						if (Math.abs(nextdayClosePrice - nextdayOpenPrice)/closePrice > StockIndicatorConst.SAMEDAY_OPEN_CLOSE_DIFFERENCE_THRESHOLD) {
							exclude = true;
							//System.out.println(stockCandleList.getSymbol() + " Same day open/close difference exceeds threshold: " + stockCandleList.get(i + j).date);
							break;
						}
						if (Math.abs(nextdayClosePrice - closePrice)/closePrice > StockIndicatorConst.NEXTDAY_CLOSE_DIFFERENCE_THRESHOLD) {
							exclude = true;
							//System.out.println(stockCandleList.getSymbol() + " Next day close difference exceeds threshold: " + stockCandleList.get(i + j).date);
							break;
						}
					}
					if (exclude == true){
						excludedDays ++;
						continue;
					}
				}
				
				String indicatorLine[] = new String[StockIndicatorConst.PIECE_NUMBER];
				String strDate = formatter.format(stockDates[i]);
				indicatorLine[StockIndicatorConst.DATE_PIECE] = strDate + ",";
				indicatorLine[StockIndicatorConst.STOCK_OPEN_PIECE] = String.valueOf(stockOpens[i]) + ",";
				indicatorLine[StockIndicatorConst.STOCK_CLOSE_PIECE] = String.valueOf(stockCloses[i]) + ",";
				indicatorLine[StockIndicatorConst.STOCK_HIGH_PIECE] = String.valueOf(stockHighs[i]) + ",";
				indicatorLine[StockIndicatorConst.STOCK_LOW_PIECE] = String.valueOf(stockLows[i]) + ",";
				indicatorLine[StockIndicatorConst.STOCKGAIN_PIECE] = String.valueOf(stockGains[i]) + ",";
				indicatorLine[StockIndicatorConst.RSI_PIECE] = String.valueOf(rsi[i]) + ",";
				indicatorLine[StockIndicatorConst.BOLLINGER_BANDS_PERCENTB_PIECE] = String.valueOf(bbandsPercentB[i]) + ",";
				indicatorLine[StockIndicatorConst.BOLLINGER_BANDS_BANDWIDTH_PIECE] = String.valueOf(bbandsBandwidth[i]) + ",";
				indicatorLine[StockIndicatorConst.EMA_DISTANCE_PIECE] = String.valueOf(emaDistance[i]) + ",";
				indicatorLine[StockIndicatorConst.VOLUME_PIECE] = String.valueOf(volume[i]) + ",";
				//Add new indicators here
				
				for (int j = 0; j < StockIndicatorConst.PIECE_NUMBER; j ++){
					sfw.write(indicatorLine[j]);
				}
				
				sfw.newLine();
				includedPoints ++;
			}		
			sfw.close();
		}
		System.out.println("Number of excluded stock indicators: " + excludedDays);
		System.out.println("Number of included stock indicators: " + includedPoints);
	}
}
