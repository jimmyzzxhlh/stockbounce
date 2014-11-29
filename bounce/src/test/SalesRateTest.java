package test;

import stock.StockHFTDistribution;
import stock.StockSellRate;
import java.util.*;

public class SalesRateTest {
	public static void main(String args[]) throws Exception {
		//double rate = StockSellRate.getSellRate(1);
		
		double rate = StockHFTDistribution.getDayTradingRate(1);
	}
}
