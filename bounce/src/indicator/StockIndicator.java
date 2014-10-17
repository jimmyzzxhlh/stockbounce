package indicator;

import java.util.Date;

/**
 * Stock indicator class.
 * Store value of each indicator that is used for training.
 * @author jimmyzzxhlh-Dell
 *
 */
public class StockIndicator {
	
	private Date date;
	private String symbol;
	private double stockGain;  //The gain should be a percentage value.
	private int stockGainClassification;
	private double rsi;
	private double bollingerBandsPercentB;
	private double bollingerBandsBandwidth;
	private double emaDistance;
	//Add new indicators here.
	
	public StockIndicator(){}
	
	public StockIndicator(Date date, double stockGain, double rsi){
		this.date = date;
		this.stockGain = stockGain;
		this.rsi = rsi;
	}
	
	public Date getDate(){
		return date;
	}
	
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	public double getStockGain(){
		return stockGain;
	}
	
	public double getRSI(){
		return rsi;
	}
	
	public void setDate(Date date){
		this.date = date;
	}
	
	public void setStockGain(double stockGain){
		this.stockGain = stockGain;
	}
	
	public void setRSI(double rsi){
		this.rsi = rsi;
	}
	
	public double getBollingerBandsPercentB() {
		return bollingerBandsPercentB;
	}

	public void setBollingerBandsPercentB(double bollingerBandsPercentB) {
		this.bollingerBandsPercentB = bollingerBandsPercentB;
	}

	public double getBollingerBandsBandwidth() {
		return bollingerBandsBandwidth;
	}

	public void setBollingerBandsBandwidth(double bollingerBandsBandwidth) {
		this.bollingerBandsBandwidth = bollingerBandsBandwidth;
	}

	public double getEMADistance() {
		return emaDistance;
	}

	public void setEMADistance(double emaDistance) {
		this.emaDistance = emaDistance;
	}
	
	/**
	 * Normalize the vector for indicator so that the length of the vector is 1.
	 * 
	 */
	public double[] getNormalizedIndicatorVector() {
		if (hasNANValue()) return null;
		
		double[] vector = new double[StockIndicatorConst.STOCK_INDICATOR_COUNT];
		vector[0] = rsi;
		vector[1] = bollingerBandsPercentB;
		vector[2] = bollingerBandsBandwidth;
		vector[3] = emaDistance;
		//Add new indicators here.
		
		//Compute the length
		double length = 0;
		for (int i = 0; i < vector.length; i++) {
			length += vector[i] * vector[i];
		}
		length = Math.sqrt(length);
		
		//Divide each indicator by the length
		for (int i = 0; i < vector.length; i++) {
			vector[i] = vector[i] * 1.0 / length;
		}
		return vector;		
		
	}

	public int getStockGainClassification() {
		return stockGainClassification;
	}

	public void setStockGainClassification(int stockGainClassification) {
		this.stockGainClassification = stockGainClassification;
	}
	
	/**
	 * Set classification from the stock gain.
	 * The number of classifications should be equal to StockIndicatorConst.STOCK_GAIN_CLASSIFICATION_COUNT
	 */
	public void setStockGainClassificationFromStockGain() {
//		if ((stockGain >= 0) && (stockGain < 5))     {stockGainClassification = 1; return;}
//		if ((stockGain >= 5) && (stockGain < 10))    {stockGainClassification = 2; return;}
//		if ((stockGain >= 10) && (stockGain < 15))   {stockGainClassification = 3; return;}
//		if ((stockGain >= 10) && (stockGain < 20))   {stockGainClassification = 4; return;}
//		if ((stockGain >= 20))                       {stockGainClassification = 5; return;}
//		if ((stockGain < 0) && (stockGain >= -5))    {stockGainClassification = -1; return;}
//		if ((stockGain < -5) && (stockGain >= -10))  {stockGainClassification = -2; return;}
//		if ((stockGain < -10) && (stockGain >= -15)) {stockGainClassification = -3; return;}
//		if ((stockGain < -15) && (stockGain >= -20)) {stockGainClassification = -4; return;}
//		if ((stockGain < -20))                       {stockGainClassification = -5; return;}
		
		if ((stockGain >= -5) && (stockGain < 5))    {stockGainClassification = 1; return;}
		if ((stockGain >= 5) && (stockGain < 15))    {stockGainClassification = 2; return;}
		if ((stockGain >= 15))                       {stockGainClassification = 3; return;}
		if ((stockGain < -5) && (stockGain >= -15))  {stockGainClassification = -2; return;}
		if ((stockGain < -15))                       {stockGainClassification = -3; return;}
		
	}

	/**
	 * If any of the indicator has a NAN value then the vector should not be trained or tested. 
	 * 
	 */
	public boolean hasNANValue() {
		if (this.rsi == StockIndicatorConst.NAN) return true;
		if (this.bollingerBandsPercentB == StockIndicatorConst.NAN) return true;
		if (this.bollingerBandsBandwidth == StockIndicatorConst.NAN) return true;
		if (this.emaDistance == StockIndicatorConst.NAN) return true;
		//Add new indicators here.
		return false;
	}	
}
