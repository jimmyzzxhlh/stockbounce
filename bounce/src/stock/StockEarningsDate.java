package stock;

import java.util.Date;

import stock.StockEnum.EarningsTimeType;
import util.StockUtil;

public class StockEarningsDate implements Comparable<StockEarningsDate>{

	/**
	 * @author Dongyue Xue
	 */
	private String symbol;
	private Date date;
	private EarningsTimeType type; //AMC, NONE, BTO
	private double estimate;
	private double reported; //999: N/A
	
	//true if dates are equal
	public boolean equals(Object o){
		if (!(o instanceof StockEarningsDate)){
			return false;
		}
		StockEarningsDate stockEarningsDate = (StockEarningsDate) o;
		return (stockEarningsDate.getDate() == date);
	}
	
	public int compareTo(StockEarningsDate stockEarningsDate) {
		return date.compareTo(stockEarningsDate.getDate());
	}
	
	public StockEarningsDate(String symbol, Date date, double estimate, EarningsTimeType type, double reported){
		this.symbol = symbol;
		this.date = date;
		this.type = type;
		this.estimate = estimate;
		this.reported = reported;
	}
	
	public StockEarningsDate(String symbol, Date date){
		this.symbol = symbol;
		this.date = date;
	}

	public String getSymbol(){
		return symbol;
	}
	
	public Date getDate(){
		return date;
	}
	
	public EarningsTimeType getType(){
		return type;
	}
	
	public double getEstimate(){
		return estimate;
	}

	public double getReported(){
		return reported;
	}
	
	public double getSurprise(){
		if (!hasEstimate() || !hasReported()) {
			return -1;
		}
		return (reported - estimate);
	}
	
	public boolean hasEstimate() {
		return !StockUtil.isEqualDouble(estimate, 999);
	}
	
	public boolean hasReported() {
		return !StockUtil.isEqualDouble(reported, 999);
	}
	
	public String toStringForGUI() {
		StringBuilder sb = new StringBuilder();
		sb.append("Earnings Time: ");
		sb.append(type.toString());
		sb.append("   Reported: ");
		if (hasReported()) {
			sb.append(StockUtil.getRoundTwoDecimals(reported));
		}
		else {
			sb.append("None");
		}
		sb.append("   Estimate: ");
		if (hasEstimate()) {
			sb.append(StockUtil.getRoundTwoDecimals(estimate));
		}
		else {
			sb.append("None");
		}		
		return sb.toString();
	}
	
	public String toStringForGUIFutureDate() {
		StringBuilder sb = new StringBuilder();
		sb.append("Next Earnings Date: " + StockUtil.formatDate(date));
		sb.append("   Estimate: ");
		if (hasEstimate()) {
			sb.append(StockUtil.getRoundTwoDecimals(estimate));
		}
		else {
			sb.append("None");
		}		
		return sb.toString();
	}
}
