package stock;

import indicator.StockIndicatorConst;

public class StockSellRate {

	private static double[] sellRates = null; //sellRates[days]:  <days> is the number of holding days 
	private StockSellRate() {
		
	}
	
	public static double[] getSellRates() {
		if (sellRates == null) setSellRates();
		return sellRates;
	}
	
	public static double getSellRate(int days) { 
		return getSellRates()[days];
	}
	
	private static void setSellRates() {
		double[] sellRates =  new double[StockIndicatorConst.MAX_SELL_PERIOD];
		//manual rates till day 5
		//we do not consider HFT here, so rates[0] = 0;
		sellRates[1] = 0.2;
		sellRates[2] = (1 - getSellRateCDF(sellRates, 2)) * 0.15;
		sellRates[3] = (1 - getSellRateCDF(sellRates, 3)) * 0.10;
		sellRates[4] = (1 - getSellRateCDF(sellRates, 4)) * 0.075;
		sellRates[5] = (1 - getSellRateCDF(sellRates, 5)) * 0.05;
		//rates according to geometric distribution from day 6
		//p: parameter for geometric distribution 
		double p = 1 - Math.pow(Math.E, Math.log(getSellRateCDF(sellRates, 5)) / (StockIndicatorConst.MAX_SELL_PERIOD - 6));
		//System.out.println("p: " + p);
		for (int i = 0; i < StockIndicatorConst.MAX_SELL_PERIOD - 6; i ++){
			sellRates[6 + i] = Math.pow(1-p, i)*p;
		}
		//System.out.println("CDF of Day 4: " + getSellRateCDF(rates, 4));
		//System.out.println("CDF of Day 9: " + getSellRateCDF(rates, 9));
		//System.out.println("CDF of Day 19: " + getSellRateCDF(rates, 19));
		//System.out.println("CDF of Day 39: " + getSellRateCDF(rates, 39));
	}
	
	//get sum of rates until <days>
	private static double getSellRateCDF(double[] rates, int days){
		double cdf = 0;
		for (int i = 0; i <= days; i++){
			cdf += rates [i];
		}
		return cdf;
	}
}
