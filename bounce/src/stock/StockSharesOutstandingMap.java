package stock;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

/**
 * This is a singleton class to prevent from reading the ouuts
 * @author jimmyzzxhlh-Dell
 *
 */
public class StockSharesOutstandingMap {
	
	private static HashMap<String, Long> map = null;
	private StockSharesOutstandingMap() {
		
	}
	
	public static HashMap<String, Long> getMap() { 
		if (map == null) setSharesOutstandingMap();
		return map;
	}
	
	private static void setSharesOutstandingMap() {
		map = new HashMap<String, Long>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(StockConst.SHARES_OUTSTANDING_FILENAME));
			String line;			
			while ((line = br.readLine()) != null) {
				String[] lineArray = line.split(" ");
				for (int i = 0; i < lineArray.length; i++) lineArray[i].trim();
				String symbol = lineArray[0].substring(1, lineArray[0].length() - 2);
				String sharesOutStandingStr = lineArray[lineArray.length - 1];
				sharesOutStandingStr = sharesOutStandingStr.replace(",", "");
				map.put(symbol, Long.parseLong(sharesOutStandingStr));
			}
			br.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
