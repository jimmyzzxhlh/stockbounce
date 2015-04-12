package simulation;

import stock.StockEnum.StockOrderStatus;
import stock.StockEnum.StockOrderType;

/**
 * Class that represents an order.
 * An open order is different from the order that closes it.
 * @author jimmyzzxhlh-Dell
 *
 */
public class StockOrder {
	
	private StockOrderType type;
	
	private String symbol;
	
	//This is the price specified on the order
	private double price;  
	
	private long shares;
	
	private StockOrderStatus status;
	
	private double trailingStop;
	
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public StockOrderType getType() {
		return type;
	}

	public void setType(StockOrderType type) {
		this.type = type;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public double getTrailingStop() {
		return trailingStop;
	}

	public void setTrailingStop(double trailingStop) {
		this.trailingStop = trailingStop;
	}
	
	public long getShares() {
		return shares;
	}

	public void setShares(long shares) {
		this.shares = shares;
	}

	public StockOrderStatus getStatus() {
		return status;
	}

	public void setStatus(StockOrderStatus status) {
		this.status = status;
	}
	
	public StockOrder() {
		
	}
	
	/**
	 * Initialize a market order.
	 * Notice that this might not be useful for now, as for testing we are pretty much assuming that
	 * the market order will be traded immediately.
	 * @param symbol
	 * @param type
	 * @param price
	 * @param shares
	 * @param trailingStop
	 */
	public void initMarketOrder(String symbol, StockOrderType type, double price, long shares, double trailingStop) {
		this.symbol = symbol;
		this.type = type;
		this.price = price;
		this.shares = shares;
		this.trailingStop = trailingStop;
		//If the order type is market order, then we assume that the order is automatically opened.
		if (type == StockOrderType.BUY || type == StockOrderType.SELL) {
			this.status = StockOrderStatus.OPENED;
		}
		else {
			this.status = StockOrderStatus.PENDING;
		}
		
	}
	
//	public void initLimitOrder(String symbol, Date placeDate, StockOrderType type, double price, double trailingStopAmount) {
//		initMarketOrder(symbol, placeDate, type, price);
//		this.trailingStopAmount = trailingStopAmount;
//	}

	

	
}
