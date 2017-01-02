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
public class OutstandingShares {
	
	private static HashMap<String, Long> map;
	
	private OutstandingShares() {
		
	}
	
	public static long getOutstandingShares(String symbol) {
		if (map == null) {
			try {
				setMap();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map.getOrDefault(symbol, Long.MIN_VALUE);
	}
	
	private static void setMap() throws Exception {
		map = new HashMap<String, Long>();
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
			boolean hasOutstandingShares = true;
			long outstandingShares = 0;
			for (int i = 1; i < data.length; i++) {
				if (data[i].equals("N/A")) {
					hasOutstandingShares = false;
					break;
				}
				outstandingShares = outstandingShares * 1000L + Long.parseLong(data[i]);
			}
			if (!hasOutstandingShares) continue;
			//Compute shares outstanding.
			map.put(symbol, outstandingShares);
		}
		br.close();
		
	}
}
