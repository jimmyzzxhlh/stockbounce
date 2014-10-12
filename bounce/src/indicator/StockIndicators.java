package indicator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import stock.StockCandleArray;
import yahoo.YahooParser;

public class StockIndicators {
	private static final String CSV_DIRECTORY_PATH = "D:\\zzx\\Stock\\CSV\\";
	private static final String OUTPUT_DIRECTORY_PATH = "D:\\zzx\\Stock\\Indicators_CSV\\";
	private static final int PERIOD_RSI = 14;
	private static final int PERIOD_STOCKGAINS = 20;

	private ArrayList<StockIndicator> stockIndicatorArray = new ArrayList<StockIndicator>();
	private String symbol;
	
	public void setSymbol(String symbol){
		this.symbol = symbol;
	}
	
	public String getSymbol(){
		return symbol;
	}
	
	public int size(){
		return stockIndicatorArray.size();
	}
	
	public Date getDate(int i){
		return stockIndicatorArray.get(i).getDate();
	}
	
	public double getStockGain(int i){
		return stockIndicatorArray.get(i).getStockGain();
	}
	
	public double getRSI(int i){
		return stockIndicatorArray.get(i).getRSI();
	}
	
	public ArrayList<StockIndicator> getStockIndicators(){
		return stockIndicatorArray;
	}
	
	public void setStockIndicators(ArrayList<StockIndicator> stockIndicatorArray){
		this.stockIndicatorArray = stockIndicatorArray;
	}
	
	public void add(StockIndicator stockIndicator){
		stockIndicatorArray.add(stockIndicator);
	}
	
	public static void calculateStockIndicators() throws Exception{
		File directory = new File(CSV_DIRECTORY_PATH);
		File[] directoryList = directory.listFiles();
		File outputFile;
		FileWriter fw;
		BufferedWriter bw;
		StockCandleArray stockCandleArray;
		
		if (directoryList == null) return;
		
		for (File csvFile : directoryList) {
			//Initialize parser and parse each line of the stock data.
			System.out.println(csvFile.getName());
			stockCandleArray = YahooParser.readCSVFile(csvFile, 0);
			outputFile = new File(OUTPUT_DIRECTORY_PATH + stockCandleArray.getSymbol() + "_Indicators.csv");
			outputFile.createNewFile();
			fw = new FileWriter(outputFile.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			
			bw.write("Date" + ",");
			bw.write("Stock Gain (" + PERIOD_STOCKGAINS + " days),");
			bw.write("RSI" + ",");
			//Add new indicators here
			bw.newLine();
			
			Date[] stockDates = new Date[stockCandleArray.size()];
			for (int i = 0; i < stockCandleArray.size(); i++){
				stockDates[i] = stockCandleArray.getDate(i);
			}
			double[] stockGains = StockGain.getStockGain(stockCandleArray, PERIOD_STOCKGAINS);
			double[] rsi = StockIndicatorAPI.getRSI(stockCandleArray, PERIOD_RSI);
			
			for (int i = 0; i < stockCandleArray.size(); i++) {
				String strDate = formatter.format(stockDates[i]);
				bw.write(strDate + ",");
				bw.write(stockGains[i] + ",");
				bw.write(rsi[i] + ",");
				bw.newLine();
			}		
			bw.close();
			fw.close();
		}
	}
	
	public static StockIndicators getStockIndicators(String fileName, Date startDate, Date endDate){
		File csvFile = new File(fileName);
		return getStockIndicators(csvFile, startDate, endDate);
	}
	
	public static StockIndicators getStockIndicators(File csvFile, Date startDate, Date endDate){
		ArrayList<StockIndicator> stockIndicatorArray;
		String symbol;
		StockIndicators retStockIndicators = new StockIndicators();
		
		if (startDate.after(endDate)) {
			return null;
		}
		
		symbol = csvFile.getName().substring(0, csvFile.getName().length() - 4);
		retStockIndicators.setSymbol(symbol);
		stockIndicatorArray = IndicatorParser.readCSVFile(csvFile);
		
		if ((startDate == null)||(endDate == null)){
			retStockIndicators.setStockIndicators(stockIndicatorArray);
			return retStockIndicators;
		}
		
		for (int i = 0; i < stockIndicatorArray.size(); i++){
			Date date = stockIndicatorArray.get(i).getDate();
			if ((startDate.after(date) == false) && (endDate.before(date) == false )){
				retStockIndicators.add(stockIndicatorArray.get(i));
			}
		}
		return retStockIndicators;
	}
}
