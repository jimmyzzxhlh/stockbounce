package intraday;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import stock.StockConst;
import stock.StockEnum.StockIntraDayClass;
import util.StockUtil;

public class IntraDayGetLowHigh {


	private HashMap<StockIntraDayClass, Integer> lowIntervalMap;
	private HashMap<StockIntraDayClass, Integer> highIntervalMap;
	
	
	
	
	public static void getLowInterval(StockIntraDayClass intraDayClass) throws Exception {
		File directory = new File(StockConst.INTRADAY_DIRECTORY_PATH_GOOGLE);
		File[] directoryList = directory.listFiles();
		for (File file : directoryList) {
			String symbol = StockUtil.getSymbolFromFile(file);
//			if (!StockMarketCap.isLargeMarketCap(symbol)) continue;
			ArrayList<IntraDayStockCandleArray> mdStockCandleArray = IntraDayAnalysisGoogle.getIntraDayStockCandleArray(file);
			double estimatedTotalCapital = 0;
			double realTotalCapital = 0;
			for (int i = 0; i < mdStockCandleArray.size(); i++) {
				IntraDayStockCandleArray idStockCandleArray = mdStockCandleArray.get(i);
				double averagePrice = (idStockCandleArray.getOpen() + idStockCandleArray.getClose() + idStockCandleArray.getHigh() + idStockCandleArray.getLow()) * 0.25;
				estimatedTotalCapital += averagePrice * idStockCandleArray.getVolume();
				realTotalCapital += idStockCandleArray.getTotalCapital();				
			}
			double errorTotalCapital = (estimatedTotalCapital - realTotalCapital) / realTotalCapital * 100;
			System.out.println(symbol + " " + StockUtil.getRoundDecimals(errorTotalCapital, 5) + "%");
		}
	}
	
	public static void getHighInterval(StockIntraDayClass intraDayClass) {
		
	}
	
}
