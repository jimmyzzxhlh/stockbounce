package test;

import java.util.ArrayList;

import stock.StockSymbolList;
import stock.StockEnum.Exchange;

public class SymbolTest {
	public static void main(String args[]) {
		testPrintingSymbol();
		
	}
	
	private static void testPrintingSymbol() {
		ArrayList<String> symbolList = StockSymbolList.getSymbolListFromExchange(Exchange.NYSE);
		for (String symbol : symbolList) {
			System.out.println(symbol);
		}
	}
}
