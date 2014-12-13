package test;

import intraday.IntraDayAnalysis;
import intraday.IntraDayStockCandle;
import intraday.IntraDayStockCandleArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import stock.StockConst;
import util.StockUtil;

public class AnalysisIntraDayTest {
	public static void main(String args[]) throws Exception {
//		testPrintDailyVolume();
		testReadIntraDayStockCandleArray();
	}
	
	private static void testReadIntraDayStock() throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(StockConst.INTRADAY_DIRECTORY_PATH + "GGACU.txt"));
		String line;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
		br.close();
	}
	
	private static void testAnalysisIntraDayStocks() throws Exception {
//		IntraDayAnalysis.analyzeVolume();
		
	}
	
	private static void testPrintDailyVolume() throws Exception {
		IntraDayAnalysis.printDailyVolumeForSingleStock();
	}
	
	private static void testAnalysisIntraDayPrice() throws Exception {
		IntraDayAnalysis.analyzePrice();
	}
	
	private static void testReadIntraDayStockCandleArray() throws Exception {
		String symbol = "GOOG";
		File file = new File(StockConst.INTRADAY_DIRECTORY_PATH + symbol + ".txt");
		ArrayList<IntraDayStockCandleArray> mdStockCandleArray = IntraDayAnalysis.getIntraDayStockCandleArray(file);
		for (int i = 0; i < mdStockCandleArray.size(); i++) {
			IntraDayStockCandleArray idStockCandleArray = mdStockCandleArray.get(i);
			System.out.println(idStockCandleArray.getTimestamp());
			for (int j = 0; j < idStockCandleArray.size(); j++) {
				IntraDayStockCandle idStockCandle = idStockCandleArray.get(j);
				System.out.print(idStockCandle.getInterval() + ": ");
				System.out.print(StockUtil.getRoundTwoDecimals(idStockCandle.close) + " ");
				System.out.print(StockUtil.getRoundTwoDecimals(idStockCandle.high) + " ");
				System.out.print(StockUtil.getRoundTwoDecimals(idStockCandle.low) + " ");
				System.out.print(StockUtil.getRoundTwoDecimals(idStockCandle.open) + " ");
				System.out.print(idStockCandle.volume + " ");
				System.out.println();
				
			}
		}
	}
	
}
