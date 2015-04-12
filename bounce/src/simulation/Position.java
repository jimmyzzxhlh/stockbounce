package simulation;

public class Position {
	
	
	private String symbol;
	
	private double price;  //Average price;
	
	private long shares;
	
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public long getShares() {
		return shares;
	}

	public void setShares(long shares) {
		this.shares = shares;
	}

	public Position(String symbol, double price, long shares) {
		this.symbol = symbol;
		this.price = price;
		this.shares = shares;
	}
	
	public double getFloatingProfit(double currentPrice) {
		return (currentPrice - price) * shares;
	}
}
