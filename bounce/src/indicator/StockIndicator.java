package indicator;

import java.util.Date;

public class StockIndicator {
	private Date date;
	private double stockGain; 
	private double rsi;
	
	public StockIndicator(){}
	
	public StockIndicator(Date date, double stockGain, double rsi){
		this.date = date;
		this.stockGain = stockGain;
		this.rsi = rsi;
	}
	
	public Date getDate(){
		return date;
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
}
