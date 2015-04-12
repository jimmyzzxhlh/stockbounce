package simulation;

import intraday.IntraDayStockCandle;
import intraday.MultipleDaysStockCandleArray;

import java.util.Date;
import java.util.HashMap;

import stock.StockAPI;
import stock.StockCandle;
import stock.StockCandleArray;
import stock.StockEnum.StockOrderType;

public class StrategySimluation {

	private Portfolio portfolio;
	
	//Assume that we will not have multiple pending orders for one symbol.
	private HashMap<String, StockOrder> pendingOrdersMap;
	
	private HashMap<String, StockCandleArray> stockCandleArrayMap;
	
	private HashMap<String, MultipleDaysStockCandleArray> mdStockCandleArrayMap;
	
	public StrategySimluation() {
		portfolio = new Portfolio();
		pendingOrdersMap = new HashMap<String, StockOrder>();
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
