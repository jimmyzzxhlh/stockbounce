package simulation;

import intraday.MultipleDaysStockCandleArray;
import stock.StockCandleArray;

public class StrategySimluation {

	private MultipleDaysStockCandleArray mdStockCandleArray;
	
	private StockCandleArray stockCandleArray;
	
	private double trailingStopAmount;
	
	private StockOrder stockOrder;   //Right now we are only supporting one order per symbol;
	
	private String symbol;
	
	public StrategySimluation() {
		
	}
	
	public void setMultipleDaysStockCandleArray(MultipleDaysStockCandleArray mdStockCandleArray) {
		this.mdStockCandleArray = mdStockCandleArray;
	}
	
	public void setStockCandleArray(StockCandleArray stockCandleArray) {
		this.stockCandleArray = stockCandleArray;
	}
	
	public void destroy() {
		mdStockCandleArray = null;
		stockCandleArray = null;
	}
	
	
	
	
}
