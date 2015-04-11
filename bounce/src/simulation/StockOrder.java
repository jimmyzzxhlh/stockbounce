package simulation;

import intraday.IntraDayStockCandle;

import java.util.Date;

import stock.StockEnum;
import stock.StockEnum.StockOrderStatus;
import stock.StockEnum.StockOrderType;

/**
 * Class that represents an order 
 * @author jimmyzzxhlh-Dell
 *
 */
public class StockOrder {
	
	private StockOrderType type;
	
	private String symbol;
	
	private double open;
	
	private double close;
	
	private double bid;
	
	private double ask;
	
	private Date openDate;

	private Date closeDate;
	
	private StockOrderStatus status;
	
	private double trailingStopAmount;
	
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

	public double getOpen() {
		return open;
	}

	public void setOpen(double open) {
		this.open = open;
	}
	
	public double getClose() {
		return close;
	}

	public void setClose(double close) {
		this.close = close;
	}

	public double getBid() {
		return bid;
	}

	public void setBid(double bid) {
		this.bid = bid;
	}

	public double getAsk() {
		return ask;
	}

	public void setAsk(double ask) {
		this.ask = ask;
	}
	
	public double getTrailingStopAmount() {
		return trailingStopAmount;
	}

	public void setTrailingStopAmount(double trailingStopAmount) {
		this.trailingStopAmount = trailingStopAmount;
	}

	public StockOrderStatus getStatus() {
		return status;
	}

	public void setStatus(StockOrderStatus status) {
		this.status = status;
	}
	
	public StockOrder() {
		
	}
	
	public void placeOrder(String symbol, Date openDate, StockOrderType type, double price) {
		this.symbol = symbol;
		this.openDate = openDate;
		this.type = type;
		//If the order type is market order, then we assume that the order is automatically opened.
		if (type == StockOrderType.BUY || type == StockOrderType.SELL) {
			this.status = StockOrderStatus.OPENED_MARKET;
		}
		
		if (StockEnum.isBuyOrder(type)) {
			bid = price;
		}
		else if (StockEnum.isSellOrder(type)) {
			ask = price;
		}
	}
	
	public void checkOpenOrder(IntraDayStockCandle idStockCandle) {
		
	}
	
	public boolean closeOrder(String symbol, Date openDate, StockOrderType type, double closePrice) {
		return false;
	
	}
	
	

	
}
