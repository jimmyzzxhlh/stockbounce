package test;

import stock.StockConst;
import stock.StockDayTradingDistribution;

public class IntraDayPriceVolumeTest {

	private static final String filename = StockConst.STOCK_CSV_DIRECTORY_PATH + "JD.csv";
	
	public static void main(String[] args) {
		
		double[] distribution = StockDayTradingDistribution.getDayTradingDistribution();
		for (int index = 0; index < distribution.length; index ++){
			System.out.println(index + ": " + distribution[index]);
		}
		
		//int lowInterval = IntraDayGetLowHigh.getLowInterval(StockcandleClass.BLACK_LONG);
		//int highInterval = IntraDayGetLowHigh.getHighInterval(StockcandleClass.BLACK_LONG);
		//System.out.println("Low interval: " + lowInterval);
		//System.out.println("High interval: " + highInterval);
		
		//StockCandleArray stockCandleArray = YahooParser.readCSVFile(filename);
		//StockCandle idStockCandle = stockCandleArray.get(stockCandleArray.size()-1);
				
		//HashMap<Integer, Long> map = IntraDayPriceVolumeMap.getMap(idStockCandle);
		//for (Integer key : map.keySet()){
			//	System.out.println("Price: " + key + "; Volume: " + map.get(key));
			//	
			//}
	} 

}
