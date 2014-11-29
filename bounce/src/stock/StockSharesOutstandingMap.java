package stock;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class StockSharesOutstandingMap {
	
	private static HashMap<String, Long> map = null;
	private StockSharesOutstandingMap() {
		
	}
	
	public static HashMap<String, Long> getMap() { 
		if (map == null) map = getSharesOutstandingMap();
		return map;
	}
	
	private static HashMap<String, Long> getSharesOutstandingMap() {
		HashMap<String, Long> sharesOutstandingMap = new HashMap<String, Long>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(StockConst.MARKET_CAP_FILENAME));
			String line;			
			while ((line = br.readLine()) != null) {
				String[] lineArray = line.split(" ");
				for (int i = 0; i < lineArray.length; i++) lineArray[i].trim();
				String symbol = lineArray[0].substring(1, lineArray[0].length() - 2);
				String sharesOutStandingStr = lineArray[lineArray.length - 1];
				sharesOutStandingStr = sharesOutStandingStr.replace(",", "");
				sharesOutstandingMap.put(symbol, Long.parseLong(sharesOutStandingStr));
			}
			br.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return sharesOutstandingMap;
		
	}
}
