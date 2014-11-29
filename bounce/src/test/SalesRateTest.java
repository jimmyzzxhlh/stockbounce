package test;

import stock.StockSalesRate;
import java.util.*;

public class SalesRateTest {
	public static void main(String args[]) throws Exception {
		double rate = StockSalesRate.getSalesRate(1);
		System.out.println(rate);
	}
}
