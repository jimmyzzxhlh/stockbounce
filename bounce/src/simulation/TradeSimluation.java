package simulation;

import java.util.HashMap;

import org.joda.time.LocalDate;

import intraday.IntraDayCandle;
import intraday.MultiDaysCandleList;
import stock.CandleList;
import stock.DailyCandle;
import stock.StockAPI;
import stock.StockEnum.StockOrderType;

public class TradeSimluation {

	private Portfolio portfolio;
	
	//Store all the pending orders for each symbol.
	//Assume that we will not have multiple pending orders for one symbol.
	private HashMap<String, StockOrder> pendingOrdersMap;
	
	//Store all the daily stock candle array for each symbol.
	//Notice that this hashmap (and also the intraday stock candle array) may become very big.
	//We can consider destroy the stock candle array once we have cleared the position for the symbol. 
	private HashMap<String, CandleList> stockCandleListMap;
	
	//Store all the intraday stock candle array for each symbol.
	private HashMap<String, MultiDaysCandleList> mdstockCandleListMap;
	
	public TradeSimluation() {
		portfolio = new Portfolio();
		pendingOrdersMap = new HashMap<String, StockOrder>();
		stockCandleListMap = new HashMap<String, CandleList>();
		mdstockCandleListMap = new HashMap<String, MultiDaysCandleList>();
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
		if (stockCandleListMap != null) {
			for (CandleList stockCandleList : stockCandleListMap.values()) {
				stockCandleList.destroy();
			}
			stockCandleListMap.clear();
			stockCandleListMap = null;
		}
		
		if (mdstockCandleListMap != null) {
			for (MultiDaysCandleList mdstockCandleList : mdstockCandleListMap.values()) {
				mdstockCandleList.destroy();
			}
			mdstockCandleListMap.clear();
			mdstockCandleListMap = null;
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
		readstockCandleList(symbol);
	}
	
	private void readstockCandleList(String symbol) {
		if (!stockCandleListMap.containsKey(symbol)) { 
			CandleList stockCandleList = StockAPI.getstockCandleListYahoo(symbol);
			stockCandleListMap.put(symbol, stockCandleList);
		}
		
		//Same for mdstockCandleList
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
	
	
	
	public double getFloatingProfit(String symbol, LocalDate date) {
		double currentPrice = stockCandleListMap.get(symbol).getClose(date);
		return portfolio.getFloatingProfit(symbol, currentPrice);
	}
	
	
	
	/**
	 * Check the order status and process the order based on the current intraday stock candle.
	 * If the order is in pending status, check if the order will be triggered to open.
	 * If the order is in opened status, check if the order can be closed by automatic stop loss / take profit.
	 * @param idStockCandle
	 */
	public void process(IntraDayCandle idStockCandle) {
		
	}
	
	/**
	 * Check the order status and process the order based on the current daily stock candle.
	 * If the order is in pending status, check if the order will be triggered to open.
	 * If the order is in opened status, check if the order can be closed by automatic stop loss / take profit.
	 * @param stockCandle
	 */
	public void process(DailyCandle stockCandle) {
		
	}
	
	
	
	
	
}
