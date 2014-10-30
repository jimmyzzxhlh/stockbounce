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
	
	public int getVolume(int index) {
		return stockIndicatorArray.get(index).getVolume();
	}
	
	//Add new indicators here.
	
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
	 * @param singleStockIndicatorArray
	 * @param mainStockIndicatorArray
	 */
	public static void copyStockIndicatorArray(StockIndicatorArray singleStockIndicatorArray, StockIndicatorArray mainStockIndicatorArray) {
		for (int i = 0; i < singleStockIndicatorArray.size(); i++) {
			mainStockIndicatorArray.add(singleStockIndicatorArray.get(i));
		}
	}
	
	public int getStockGainCount(double minStockGain) {
		int stockGainCount = 0;
		for (int i = 0; i < stockIndicatorArray.size(); i++) {
			StockIndicator stockIndicator = stockIndicatorArray.get(i);
			if (stockIndicator.hasEnoughStockGain(minStockGain)) {
				stockGainCount++;
			}
		}
		return stockGainCount;
	}
}
