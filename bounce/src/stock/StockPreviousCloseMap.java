package stock;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class StockPreviousCloseMap {
	private static HashMap<String, Double> map = null;
	
	private StockPreviousCloseMap() {
		
	}
	
	public static HashMap<String, Double> getMap() {
		if (map == null) setMap();
		return map;
	}
	
	private static void setMap() {
		map = new HashMap<String, Double>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(StockConst.PREVIOUS_CLOSE_FILENAME));
			String line;			
			while ((line = br.readLine()) != null) {
				String[] lineArray = line.split(",");
				for (int i = 0; i < lineArray.length; i++) lineArray[i].trim();
				//Delete quotes. Notice that it is different from shares outstanding that we already
				//use comma to split the string, so there is no comma in the symbol!
				String symbol = lineArray[0].substring(1, lineArray[0].length() - 1);
				if (lineArray[1].contains("N/A")) continue;
				map.put(symbol, Double.parseDouble(lineArray[1]));
			}
			br.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
