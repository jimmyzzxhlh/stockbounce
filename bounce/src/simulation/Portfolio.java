package simulation;

import java.util.HashMap;

public class Portfolio {

	private HashMap<String, Position> positions;
	
	
	public Portfolio() {
		positions = new HashMap<String, Position>();
	}
	
	/**
	 * Assume that the order can be traded and we will update the position.
	 * @param order
	 */
	public void updatePosition(StockOrder order) {
		
	}
	
	 
	public void destroy() {
		
	}
	
	public double getFloatingProfit(String symbol, double currentPrice) {
		if (!positions.containsKey(symbol)) return 0;
		Position position = positions.get(symbol);
		return position.getFloatingProfit(currentPrice);
	}
	
	
}
