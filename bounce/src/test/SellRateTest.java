package test;

import stock.DayTradingDistribution;
import stock.StockSellRate;
import java.util.*;

public class SellRateTest {
	public static void main(String args[]) throws Exception {
		//double rate = StockSellRate.getSellRate(1);
		
		double rate = DayTradingDistribution.getDayTradingRate(1);
	}
}
