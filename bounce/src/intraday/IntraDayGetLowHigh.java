package intraday;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import stock.StockConst;
import stock.StockEnum.StockIntraDayClass;

public class IntraDayGetLowHigh {


	private static HashMap<StockIntraDayClass, Integer> lowIntervalMap;
	private static HashMap<StockIntraDayClass, Integer> highIntervalMap;
	
	
	
	
	public static Integer getLowInterval(StockIntraDayClass intraDayClass) {
		if (!lowIntervalMap.containsKey(intraDayClass)){		
			try {
				setInterval(intraDayClass);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return lowIntervalMap.get(intraDayClass);
		
	}
	
	private static void setInterval(StockIntraDayClass intraDayClass) throws Exception{
		File directory = new File(StockConst.INTRADAY_DIRECTORY_PATH_GOOGLE);
		File[] directoryList = directory.listFiles();
		int lowInterval = 0;
		int highInterval = 0;
		int sum = 0; //number of total intervals
		for (File file : directoryList) {
//			if (!StockMarketCap.isLargeMarketCap(symbol)) continue;
			ArrayList<IntraDayStockCandleArray> mdStockCandleArray = IntraDayAnalysisGoogle.getIntraDayStockCandleArray(file);
			for (int i = 0; i < mdStockCandleArray.size(); i++) {
				IntraDayStockCandleArray idStockCandleArray = mdStockCandleArray.get(i);
				if (idStockCandleArray.getIntraDayClass()!=intraDayClass)	continue;
				
				for (int index = 0; index < idStockCandleArray.getLowIntervals().size(); index ++){
					lowInterval += idStockCandleArray.getLowIntervals().get(index);
					sum ++;
				}
				for (int index = 0; index < idStockCandleArray.getHighIntervals().size(); index ++){
					highInterval += idStockCandleArray.getHighIntervals().get(index);
				}
			}
			lowIntervalMap.put(intraDayClass, lowInterval/sum);
			highIntervalMap.put(intraDayClass,highInterval/sum);
		}
	}
	
	public static Integer getHighInterval(StockIntraDayClass intraDayClass) {
		if (!highIntervalMap.containsKey(intraDayClass)){		
			try {
				setInterval(intraDayClass);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return highIntervalMap.get(intraDayClass);
	}
}
