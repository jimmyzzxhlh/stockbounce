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
		if (lowIntervalMap == null){
			lowIntervalMap = new HashMap<StockIntraDayClass, Integer>();
		}
		if (highIntervalMap == null){
			highIntervalMap = new HashMap<StockIntraDayClass, Integer>();
		}
		
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
		int lowIntervalCount = 0; 
		int highIntervalCount = 0;
		for (File file : directoryList) {
//			if (!StockMarketCap.isLargeMarketCap(symbol)) continue;
			ArrayList<IntraDayStockCandleArray> mdStockCandleArray = IntraDayAnalysisGoogle.getIntraDayStockCandleArray(file);
			for (int i = 0; i < mdStockCandleArray.size(); i++) {
				IntraDayStockCandleArray idStockCandleArray = mdStockCandleArray.get(i);
				if (idStockCandleArray.getIntraDayClass()!=intraDayClass)	continue;
				
				for (int index = 0; index < idStockCandleArray.getLowIntervals().size(); index ++){
					lowInterval += idStockCandleArray.getLowIntervals().get(index);
					lowIntervalCount ++;
				}
				for (int index = 0; index < idStockCandleArray.getHighIntervals().size(); index ++){
					highInterval += idStockCandleArray.getHighIntervals().get(index);
					highIntervalCount ++;
				}
			}
		}
		lowIntervalMap.put(intraDayClass, lowInterval/lowIntervalCount);
		highIntervalMap.put(intraDayClass,highInterval/highIntervalCount);
	}
	
	public static Integer getHighInterval(StockIntraDayClass intraDayClass) {
		if (lowIntervalMap == null){
			lowIntervalMap = new HashMap<StockIntraDayClass, Integer>();
		}
		if (highIntervalMap == null){
			highIntervalMap = new HashMap<StockIntraDayClass, Integer>();
		}
		
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
