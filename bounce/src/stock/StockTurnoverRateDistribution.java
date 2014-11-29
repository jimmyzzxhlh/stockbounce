package stock;

import java.io.BufferedReader;
import java.io.FileReader;

public class StockTurnoverRateDistribution {
	private static double[] distribution = null;
	
	private StockTurnoverRateDistribution() {
		
	}
	
	public static double[] getDistribution() { 
		if (distribution == null) setDistribution();
		return distribution;
	}
	
	public static double getProbability(int turnoverRate) {
		return getDistribution()[turnoverRate];
	}
	
	private static void setDistribution() {
		distribution = new double[StockConst.TURNOVER_RATE_DISTRIBUTION_ARRAY_LENGTH];
		try {
			BufferedReader br = new BufferedReader(new FileReader(StockConst.TURNOVER_RATE_DISTRIBUTION_FILENAME));
			String line;
			int index = 0;
			while ((line = br.readLine()) != null) {
				distribution[index] = Double.parseDouble(line);
				index++;
			}
			br.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
