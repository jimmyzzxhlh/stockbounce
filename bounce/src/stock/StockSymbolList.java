package stock;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import stock.StockEnum.Exchange;
import util.StockUtil;

public class StockSymbolList {
	
	private static ArrayList<String> symbolList = null;
	private static ArrayList<String> allSymbolList = null;
	private static boolean getAll = false;
	
	private StockSymbolList() {
		
	}
	
	public static ArrayList<String> getSymbolList() {
		if (symbolList == null) {
			setSymbolList();
		}
		return symbolList;
	}
	
	public static ArrayList<String> getAllSymbolList() {
		if (allSymbolList == null) {
			setAllSymbolList();			
		}
		return allSymbolList;
	}
	
	private static void setSymbolList() {
		getAll = false;
		symbolList = new ArrayList<String>();
		readCompanyListFile(Exchange.NASDAQ);
		readCompanyListFile(Exchange.NYSE);
		Collections.sort(symbolList);		
	}
	
	private static void setAllSymbolList() {
		getAll = true;
		symbolList = new ArrayList<String>();
		allSymbolList = new ArrayList<String>();
		readCompanyListFile(Exchange.NASDAQ);
		readCompanyListFile(Exchange.NYSE);
		Collections.sort(allSymbolList);
	}
	
	public static void readCompanyListFile(Exchange exchange) {
		String filename = StockExchange.getCompanyListFilename(exchange);
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line;
			int lineNumber = 0;
			while ((line = br.readLine()) != null) {
				lineNumber++;
				//Ignore the first line as it is a header.
				if (lineNumber == 1) continue;
				//Split the data
				String data[] = StockUtil.splitCSVLine(line);
				String symbol = data[0];
				String marketCap = data[3];
				//If getting all symbols then just add the symbol. Do not check market cap or shares outstanding.
				if (getAll) {
					allSymbolList.add(symbol);
					continue;
				}
				if (marketCap.equals("n/a")) continue;				
				//If there is no shares outstanding at all then ignore.
				HashMap<String, Long> sharesOutStandingMap = StockSharesOutstandingMap.getMap();
				if (!sharesOutStandingMap.containsKey(symbol) || (sharesOutStandingMap.get(symbol) <= 0)) {
	//				System.out.println(symbol + " does not have shares outstanding data.");
					continue;					
				}
				symbolList.add(symbol);
			}
			br.close();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}	
	
	}
	
}
