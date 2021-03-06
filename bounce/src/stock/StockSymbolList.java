package stock;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import stock.StockEnum.Exchange;
import util.StockUtil;

public class StockSymbolList {
	
	private static List<String> usSymbolList = null;
	private static List<String> allUSSymbolList = null;
	private static List<String> sseSymbolList = null;
	private static List<String> szseSymbolList = null;
	private static boolean getAll = false;
	
	private StockSymbolList() {
		
	}
	
	public static List<String> getUSSymbolList() {
		if (usSymbolList == null) {
			setUSSymbolList();
		}
		return usSymbolList;
	}
	
	public static List<String> getAllUSSymbolList() {
		if (allUSSymbolList == null) {
			setAllUSSymbolList();			
		}
		return allUSSymbolList;
	}
	
	public static List<String> getSSESymbolList() {
		if (sseSymbolList == null) {
			setSSESymbolList();
		}
		return sseSymbolList;
	}
	
	public static List<String> getSZSESymbolList() {
		if (szseSymbolList == null) {
			setSZSESymbolList();
		}
		return szseSymbolList;
	}
	
	public static List<String> getSymbolListFromExchange(Exchange exchange) {
		List<String> symbolList = null;
		switch (exchange) {
		case SSE:
			symbolList = StockSymbolList.getSSESymbolList();
			break;
		case SZSE:
			symbolList = StockSymbolList.getSZSESymbolList();
			break;
		default:
			symbolList = StockSymbolList.getUSSymbolList();
			break;
		}
		return symbolList;
	}
	
	private static void setUSSymbolList() {
		getAll = false;
		usSymbolList = new ArrayList<String>();
		readUSCompanyList(Exchange.NASDAQ);
		readUSCompanyList(Exchange.NYSE);
		Collections.sort(usSymbolList);		
		addUSIndexToSymbolList(usSymbolList);
	}
	
	private static void setAllUSSymbolList() {
		getAll = true;
		allUSSymbolList = new ArrayList<>();
		readUSCompanyList(Exchange.NASDAQ);
		readUSCompanyList(Exchange.NYSE);
		Collections.sort(allUSSymbolList);
		//We are not adding index symbol here because they do not have outstanding shares data.
	}
	
	private static void addUSIndexToSymbolList(List<String> symbolList) {
		symbolList.add("^DJI");   //Dow jones
		symbolList.add("^IXIC");  //Nasdaq
		symbolList.add("^GSPC");  //S&P 500
	}
	
	private static void setSSESymbolList() {
		sseSymbolList = new ArrayList<>();
		readChinaCompanyListFile(Exchange.SSE);
		
		sseSymbolList.add("000001");   //SSE index
		sseSymbolList.add("000002");   //A stock index
		sseSymbolList.add("000003");   //B stock index
		sseSymbolList.add("000009");   //SSE 380 index
		sseSymbolList.add("000010");   //SSE 180 index
		sseSymbolList.add("000016");   //SSE 50 index
		sseSymbolList.add("000300");   //SSE + SZSE 300 index
		Collections.sort(sseSymbolList);
	}
	
	private static void setSZSESymbolList() {
		szseSymbolList = new ArrayList<>();
		readChinaCompanyListFile(Exchange.SZSE);
		
		szseSymbolList.add("399001");  //SZSE component index
		szseSymbolList.add("399004");  //SZSE 100R index
		szseSymbolList.add("399005");  //Mid/Small cap index
		szseSymbolList.add("399006");  //Growth enterprise index
		szseSymbolList.add("399106");  //SZSE all index		
		Collections.sort(szseSymbolList);
	}
	
	public static void readUSCompanyList(Exchange exchange) {
		String filename = ExchangeUtil.getCompanyListFilename(exchange);
		
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
//				String marketCap = data[3];
				//If getting all symbols then just add the symbol. Do not check market cap or shares outstanding.
				if (getAll) {
					if (!allUSSymbolList.contains(symbol)) allUSSymbolList.add(symbol);
					continue;
				}
//				if (marketCap.equals("n/a")) continue;				
				//If there is no shares outstanding at all then ignore.
				if (OutstandingShares.getOutstandingShares(symbol) <= 0) {
//					System.out.println(symbol + " does not have shares outstanding data.");
					continue;					
				}
				if (!usSymbolList.contains(symbol)) usSymbolList.add(symbol);
			}
			br.close();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}	
	
	}
	
	public static void readChinaCompanyListFile(Exchange exchange) {
		String filename = ExchangeUtil.getCompanyListFilename(exchange);
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line;
			while ((line = br.readLine()) != null) {
				switch (exchange) {
				case SSE:
					if (!sseSymbolList.contains(line)) sseSymbolList.add(line);
					break;
				case SZSE:
					if (!szseSymbolList.contains(line)) szseSymbolList.add(line);
					break;
				default:
					break;
				}
			}
			br.close();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}	
	
	}
	
}
