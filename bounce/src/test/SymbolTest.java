package test;

import java.util.List;

import stock.StockEnum.Exchange;
import stock.StockSymbolList;

public class SymbolTest {
	public static void main(String args[]) {
		testPrintingSymbol();
		
	}
	
	private static void testPrintingSymbol() {
		List<String> symbolList = StockSymbolList.getSymbolListFromExchange(Exchange.NYSE);
		for (String symbol : symbolList) {
			System.out.println(symbol);
		}
	}
}
