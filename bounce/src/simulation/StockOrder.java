package simulation;

import intraday.IntraDayStockCandle;

import java.util.Date;

import stock.StockCandle;
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
	
	private double open;  //The buy / sell price when the order is opened. 
	
	private double close; //The buy / sell price when the order is closed.
	
	private double bid;   //For buy order
	
	private double ask;   //For sell order
	
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
			this.status = StockOrderStatus.OPENED;
		}
		else {
			this.status = StockOrderStatus.PENDING;
		}
		
		if (StockEnum.isBuyOrder(type)) {
			bid = price;
		}
		else if (StockEnum.isSellOrder(type)) {
			ask = price;
		}
	}
	
	/**
	 * Check the order status and process the order based on the current intraday stock candle.
	 * If the order is in pending status, check if the order will be triggered to open.
	 * If the order is in opened status, check if the order can be closed by automatic stop loss / take profit.
	 * @param idStockCandle
	 */
	public void process(IntraDayStockCandle idStockCandle) {
		
	}
	
	/**
	 * Check the order status and process the order based on the current daily stock candle.
	 * If the order is in pending status, check if the order will be triggered to open.
	 * If the order is in opened status, check if the order can be closed by automatic stop loss / take profit.
	 * @param stockCandle
	 */
	public void process(StockCandle stockCandle) {
		
	}
	
	

	
}
