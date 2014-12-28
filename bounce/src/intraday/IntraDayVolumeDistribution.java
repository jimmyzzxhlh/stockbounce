package intraday;

import java.io.BufferedReader;
import java.io.FileReader;

import stock.StockConst;

/**
 * Return intraday volume distribution.
 * The length of the array is 390.
 * distribution[i] = Volume percentage of the interval i.
 * @author jimmyzzxhlh-Dell
 *
 */
public class IntraDayVolumeDistribution {
	
	private static double distribution[] = null;
	
	private IntraDayVolumeDistribution() {
			
	}
	
	public static double[] getDistribution() {
		if (distribution == null) setDistribution();
		return distribution;
	}
	
	public static double getVolumeRate(int i){
		return getDistribution()[i];
	}
	
	private static void setDistribution() {
		distribution = new double[391];
		try {
			BufferedReader br = new BufferedReader(new FileReader(StockConst.INTRADAY_VOLUME_DISTRIBUTION_FILENAME));
			String line;
			int interval = -1;
			while ((line = br.readLine()) != null) {
				String[] lineArray = line.split(" ");
				interval++;
				double value = Double.parseDouble(lineArray[0]);
				distribution[interval] = value;
			}
			br.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
