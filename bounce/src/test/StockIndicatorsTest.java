package test;

import java.text.SimpleDateFormat;
import java.util.Date;

import indicator.StockIndicators;


public class StockIndicatorsTest {
	public static void main(String args[]) {
		try {
			//StockIndicators.calculateStockIndicators();
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = formatter.parse("2012-01-01");
			Date endDate = formatter.parse("2012-01-31");
			String fileName = "D:\\zzx\\Stock\\Indicators_CSV\\ZIPR_Indicators.csv";
			StockIndicators stockIndicators = StockIndicators.getStockIndicators(fileName, startDate, endDate);
			for (int i = 0; i < stockIndicators.size(); i++){
				System.out.println(stockIndicators.getDate(i) + " " + stockIndicators.getStockGain(i) + " " + stockIndicators.getRSI(i));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
