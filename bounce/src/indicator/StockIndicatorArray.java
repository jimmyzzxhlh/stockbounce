package indicator;

import java.util.ArrayList;
import java.util.Date;

public class StockIndicatorArray {
	
	private ArrayList<StockIndicator> stockIndicatorArray = new ArrayList<StockIndicator>();
	
	public int size(){
		return stockIndicatorArray.size();
	}
	
	public StockIndicator get(int index) {
		return stockIndicatorArray.get(index);
	}
	
	public String getSymbol(int index) {
		return stockIndicatorArray.get(index).getSymbol();
	}
	
	public Date getDate(int index){
		return stockIndicatorArray.get(index).getDate();
	}
	
	public double getStockGain(int index){
		return stockIndicatorArray.get(index).getStockGain();
	}
	
	public int getStockGainClassification(int index) {
		return stockIndicatorArray.get(index).getStockGainClassification();
	}
	
	public double getRSI(int index){
		return stockIndicatorArray.get(index).getRSI();
	}
	
	public double getBollingerBandsPercentB(int index) {
		return stockIndicatorArray.get(index).getBollingerBandsPercentB();
	}
	
	public double getBollingerBandsBandwidth(int index) {
		return stockIndicatorArray.get(index).getBollingerBandsBandwidth();
	}
	
	public double getEMADistance(int index) {
		return stockIndicatorArray.get(index).getEMADistance();
	}
	
	public ArrayList<StockIndicator> getStockIndicatorArray(){
		return stockIndicatorArray;
	}
	
	public void setStockIndicatorArray(ArrayList<StockIndicator> stockIndicatorArray){
		this.stockIndicatorArray = stockIndicatorArray;
	}
	
	public void add(StockIndicator stockIndicator){
		stockIndicatorArray.add(stockIndicator);
	}
	
	/**
	 * Copy each indicator from one array list to the other.
	 * @param stockIndicatorArrayOne
	 * @param stockIndicatorArrayTwo
	 */
	public static void copyStockIndicatorArray(StockIndicatorArray stockIndicatorArrayOne, StockIndicatorArray stockIndicatorArrayTwo) {
		for (int i = 0; i < stockIndicatorArrayOne.size(); i++) {
			stockIndicatorArrayTwo.add(stockIndicatorArrayOne.get(i));
		}
	}
}
