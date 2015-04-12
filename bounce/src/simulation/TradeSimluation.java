package simulation;

import intraday.IntraDayStockCandle;
import intraday.MultiDaysStockCandleArray;

import java.util.Date;
import java.util.HashMap;

import stock.StockAPI;
import stock.StockCandle;
import stock.StockCandleArray;
import stock.StockEnum.StockOrderType;

public class TradeSimluation {

	private Portfolio portfolio;
	
	//Store all the pending orders for each symbol.
	//Assume that we will not have multiple pending orders for one symbol.
	private HashMap<String, StockOrder> pendingOrdersMap;
	
	//Store all the daily stock candle array for each symbol.
	//Notice that this hashmap (and also the intraday stock candle array) may become very big.
	//We can consider destroy the stock candle array once we have cleared the position for the symbol. 
	private HashMap<String, StockCandleArray> stockCandleArrayMap;
	
	//Store all the intraday stock candle array for each symbol.
	private HashMap<String, MultiDaysStockCandleArray> mdStockCandleArrayMap;
	
	public TradeSimluation() {
		portfolio = new Portfolio();
		pendingOrdersMap = new HashMap<String, StockOrder>();
		stockCandleArrayMap = new HashMap<String, StockCandleArray>();
		mdStockCandleArrayMap = new HashMap<String, MultiDaysStockCandleArray>();
	}
	
	
	public void destroy() {
		if (portfolio != null) {
			portfolio.destroy();
			portfolio = null;		
		}
		if (pendingOrdersMap != null) {
			pendingOrdersMap.clear();
			pendingOrdersMap = null;
		}
		
		//We destory the stock candle array objects here as we are assuming that we created
		//new objects instead of passing the stock candle array objects from a different source (such as GUI).
		if (stockCandleArrayMap != null) {
			for (StockCandleArray stockCandleArray : stockCandleArrayMap.values()) {
				stockCandleArray.destroy();
			}
			stockCandleArrayMap.clear();
			stockCandleArrayMap = null;
		}
		
		if (mdStockCandleArrayMap != null) {
			for (MultiDaysStockCandleArray mdStockCandleArray : mdStockCandleArrayMap.values()) {
				mdStockCandleArray.destroy();
			}
			mdStockCandleArrayMap.clear();
			mdStockCandleArrayMap = null;
		}
	}
	
	
	public Portfolio getPortfolio() {
		return portfolio;
	}
	
	
	
	public void placeMarketOrderOpenPosition(String symbol, StockOrderType type, double price, long shares, double trailingStop) { 
		StockOrder order = new StockOrder();
		order.initMarketOrder(symbol, type, price, shares, trailingStop);
		updatePortfolio(symbol, order);
		order = null;
	}
	
	private void updatePortfolio(String symbol, StockOrder order) {
		portfolio.updatePosition(order);
		readStockCandleArray(symbol);
	}
	
	private void readStockCandleArray(String symbol) {
		if (!stockCandleArrayMap.containsKey(symbol)) { 
			StockCandleArray stockCandleArray = StockAPI.getStockCandleArrayYahoo(symbol);
			stockCandleArrayMap.put(symbol, stockCandleArray);
		}
		
		//Same for mdStockCandleArray
	}
	
	
	public void placeLimitOrderOpenPosition(String symbol, StockOrderType type, double price, long shares, double trailingStop) {
		StockOrder order = new StockOrder();
		order.initMarketOrder(symbol, type, price, shares, trailingStop);
		//Don't read stock candle array.
		pendingOrdersMap.put(symbol, order);
	}
	
	/**
	 * Place a market order to close a position.
	 * @param symbol
	 * @param closeDate
	 * @param type
	 * @param price
	 * @param shares
	 */
	public void placeMarketOrderClosePosition(String symbol, StockOrderType type, double price, long shares) {
		//1. Find how many shares we have. If there are not enough shares then error out.
		//
	}
	
	public void placeLimitOrderClosePosition(String symbol, StockOrderType type, double price, long shares) {
		
	}
	
	
	
	public double getFloatingProfit(String symbol, Date date) {
		double currentPrice = stockCandleArrayMap.get(symbol).getClose(date);
		return portfolio.getFloatingProfit(symbol, currentPrice);
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
