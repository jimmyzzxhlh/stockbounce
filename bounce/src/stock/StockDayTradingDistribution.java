package stock;


public class StockDayTradingDistribution {
	private static final double BASE_DAY_TRADING_RATE = 0.25;
	private static final int DELTA = 50;
	private static final double AVERAGE_DAY_TRADING_RATE = 0.50;
	private static double k;
	//dayTradingRate(turnoverRate) = baseDayTradingRate, if turnoverRate <= delta;
	//dayTradingRate(turnoverRate) = baseDayTradingRate + k * (turnoverRate - delta), if turnoverRate > delta;
	
	private static double dayTradingRates[] = null;
	
	private StockDayTradingDistribution() {
			
	}
	
	public static double[] getDayTradingDistribution() {
		if (dayTradingRates == null) setDayTradingRates();
		return dayTradingRates;
	}
	
	public static double getDayTradingRate(int i){
		return getDayTradingDistribution()[i];
	}
	
	/**
	 * ƽ��day trading rateӦ����50%��
	 * turnoverRateDistribution(x)��ʾ��turnover rateΪx��ʱ�����ж���probability
	 * b��ʾbase day trading rate����turnover rate<=delta��ʱ�򶼼��������ֵ
	 * k��ʾ��turnover rate>delta��ʱ������turnover rate�Ӵ�day trading rate�Ӵ��б��
	 * Sigma(b * turnoverRateDistribution(x), x = 0 -> delta) + Sigma[b + k * (x - delta), x = delta + 1 -> 1000] * turnoverRateDistribution(x) = 50%
	 * ������
	 * b + Sigma[k * (x - delta) * turnoverRateDistribution(x), x = delta + 1 -> 1000] = 50%
	 * ->
	 * k = (0.5 - b) / Sigma[(x - delta) * turnoverRateDistribution(x), x = delta + 1 -> 1000] 
	 */
	private static void setDayTradingRates() {
		dayTradingRates = new double[StockConst.TURNOVER_RATE_DISTRIBUTION_ARRAY_LENGTH];
		double[] turnoverRates = StockTurnoverRateDistribution.getDistribution();
		
		double sum = 0;
		for (int x = DELTA + 1; x < StockConst.TURNOVER_RATE_DISTRIBUTION_ARRAY_LENGTH; x++) {
			sum += (x - DELTA) * turnoverRates[x];
		}
		
//		System.out.println("Sum is: " + sum);
		k = (AVERAGE_DAY_TRADING_RATE - BASE_DAY_TRADING_RATE) / sum;
//		System.out.println("Parameter k is: " + k);
		
		for (int x = 0; x < StockConst.TURNOVER_RATE_DISTRIBUTION_ARRAY_LENGTH; x++)
		{
			if (x <= DELTA)
			{
				dayTradingRates[x] = BASE_DAY_TRADING_RATE;
			}
			else 
			{
				dayTradingRates[x] = BASE_DAY_TRADING_RATE + k * (x  - DELTA);
			}
		}
		
//		sum = 0;
//		for (int x = 0; x < StockConst.TURNOVER_RATE_DISTRIBUTION_ARRAY_LENGTH; x++)
//		{
//			sum += dayTradingRates[x] * turnoverRates[x];
//		}
//		System.out.println("Average day trading rate is: " + sum);
	}
	
}
