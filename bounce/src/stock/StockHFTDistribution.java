package stock;


public class StockHFTDistribution {
	private static final double baseDayTradingRate = 0.25;
	private static final int delta = 50;
	private static double k;
	//dayTradingRate(turnoverRate) = baseDayTradingRate, if turnoverRate <= delta;
	//dayTradingRate(turnoverRate) = baseDayTradingRate + k * (turnoverRate - delta), if turnoverRate > delta;
	
	private static double dayTradingRates[] = null;
	
	private StockHFTDistribution() {
			
	}
	
	public static double[] getDayTradingDistribution() {
		if (dayTradingRates == null) setDayTradingRates();
		return dayTradingRates;
	}
	
	public static double getDayTradingRate(int i){
		return getDayTradingDistribution()[i];
	}
	
	private static void setDayTradingRates() {
		dayTradingRates = new double[StockConst.TURNOVER_RATE_DISTRIBUTION_ARRAY_LENGTH];
		double[] turnoverRates = StockTurnoverRateDistribution.getDistribution();
		double sum = 0;
		for (int i = delta; i < StockConst.TURNOVER_RATE_DISTRIBUTION_ARRAY_LENGTH; i ++)
		{
			sum += (i + 1 - delta) * turnoverRates[i];
		}
		//System.out.println("Sum is: " + sum);
		k = (0.5 - baseDayTradingRate)/sum;
		System.out.println("Parameter k is: " + k);
		
		for (int i = 0; i < StockConst.TURNOVER_RATE_DISTRIBUTION_ARRAY_LENGTH; i ++)
		{
			if (i < delta)
			{
				dayTradingRates[i] = baseDayTradingRate;
			}
			else 
			{
				dayTradingRates[i] = baseDayTradingRate + k * (i + 1 - delta);
			}
		}
		
		sum = 0;
		for (int i = 0; i < StockConst.TURNOVER_RATE_DISTRIBUTION_ARRAY_LENGTH; i ++)
		{
			sum += dayTradingRates[i] * turnoverRates[i];
		}
		//System.out.println("Average day trading rate is: " + sum);
	}
	
}
