package simulation;

import intraday.IntraDayStockCandle;

import java.util.Date;

import stock.StockCandle;
import stock.StockEnum;
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
	
	private double price;
	
	private long shares;
	
	private Date placeDate;

	private Date triggerDate;
	
	private StockOrderStatus status;
	
	private double trailingStop;
	
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Date getPlaceDate() {
		return placeDate;
	}

	public void setPlaceDate(Date placeDate) {
		this.placeDate = placeDate;
	}

	public Date getTriggerDate() {
		return triggerDate;
	}

	public void setTriggerDate(Date triggerDate) {
		this.triggerDate = triggerDate;
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

	public StockOrderStatus getStatus() {
		return status;
	}

	public void setStatus(StockOrderStatus status) {
		this.status = status;
	}
	
	public StockOrder() {
		
	}
	
	public void initMarketOrder(String symbol, StockOrderType type, double price, long shares, double trailingStop) {
		this.symbol = symbol;
		this.placeDate = placeDate;
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
