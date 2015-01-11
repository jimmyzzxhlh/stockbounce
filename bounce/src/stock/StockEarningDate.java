package stock;

import java.util.Date;

public class StockEarningDate implements Comparable<StockEarningDate>{

	/**
	 * @author Dongyue Xue
	 */
	private String symbol;
	private Date date;
	private String type; //AMC, NONE, BTO
	private double estimate;
	private double reported;//999: N/A
	
	//true if dates are equal
	public boolean equals(Object o){
		if (!(o instanceof StockEarningDate)){
			return false;
		}
		StockEarningDate stockEarningDate = (StockEarningDate) o;
		return (stockEarningDate.getDate() == date);
	}
	
	public int compareTo(StockEarningDate stockEarningDate) {
		return date.compareTo(stockEarningDate.getDate());
}
	
	public StockEarningDate(String symbol, Date date, double estimate, String type, double reported){
		this.symbol = symbol;
		this.date = date;
		this.type = type;
		this.estimate = estimate;
		this.reported = reported;
	}
	
	public StockEarningDate(String symbol, Date date){
		this.symbol = symbol;
		this.date = date;
	}

	public String getSymbol(){
		return symbol;
	}
	
	public Date getDate(){
		return date;
	}
	
	public String getType(){
		return type;
	}
	
	public double getEstimate(){
		return estimate;
	}

	public double getReported(){
		return reported;
	}
	
	public double getSurprise(){
		if (reported == 999){
			return -1;
		}
		return (reported - estimate);
	}
}
