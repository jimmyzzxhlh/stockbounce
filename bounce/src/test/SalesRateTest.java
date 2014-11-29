package test;

import stock.StockSellRate;
import java.util.*;

public class SalesRateTest {
	public static void main(String args[]) throws Exception {
		double rate = StockSellRate.getSellRate(1);
		System.out.println(rate);
	}
}
