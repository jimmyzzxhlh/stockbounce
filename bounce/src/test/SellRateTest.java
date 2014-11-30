package test;

import stock.StockDayTradingDistribution;
import stock.StockSellRate;
import java.util.*;

public class SellRateTest {
	public static void main(String args[]) throws Exception {
		//double rate = StockSellRate.getSellRate(1);
		
		double rate = StockDayTradingDistribution.getDayTradingRate(1);
	}
}
