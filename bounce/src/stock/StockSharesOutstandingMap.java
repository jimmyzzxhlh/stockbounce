package stock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import util.StockUtil;
import download.StockDownload;

/**
 * This is a singleton class to prevent from reading the CSV file multiple times.
 * Get the following HashMap
 * <Symbol, outstanding shares>
 * 
 * @author jimmyzzxhlh-Dell
 *
 */
public class StockSharesOutstandingMap {
	
	private static HashMap<String, Long> map = null;
	private StockSharesOutstandingMap() {
		
	}
	
	public static HashMap<String, Long> getMap() { 
		if (map == null) setSharesOutStandingMap();
		return map;
	}
	

	private static void setSharesOutStandingMap() {
		map = new HashMap<String, Long>();
		try {
			File file = new File(StockConst.SHARES_OUTSTANDING_FILENAME);
			if (!file.exists()) {
				StockDownload.downloadOutstandingSharesCSV();
			}
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;			
			while ((line = br.readLine()) != null) {
				String data[] = StockUtil.splitCSVLine(line);
				String symbol = data[0];
				//Handle N/A condition.
				boolean hasSharesOutStanding = true;
				long sharesOutStanding = 0;
				for (int i = 1; i < data.length; i++) {
					if (data[i].equals("N/A")) {
						hasSharesOutStanding = false;
						break;
					}
					sharesOutStanding = sharesOutStanding * 1000L + Long.parseLong(data[i]);
				}
				if (!hasSharesOutStanding) continue;
				//Compute shares outstanding.
				map.put(symbol, sharesOutStanding);
			}
			br.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
