package stock;

import intraday.IntraDayAnalysisGoogle;
import intraday.IntraDayAnalysisYahoo;
import intraday.IntraDayStockCandleArray;
import intraday.IntraDayVolumeDistribution;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import yahoo.YahooParser;
import download.StockDownload;

/**
 * This is a class for all common API functions so that we can use one single place to refer all APIs. 
 * @author jimmyzzxhlh-Dell
 *
 */
public class StockAPI {

	public static ArrayList<String> getSymbolList() {
		return StockDownload.getSymbolList();
	}
	
	public static ArrayList<IntraDayStockCandleArray> getIntraDayStockCandleArrayGoogle(File file) throws Exception {
		return IntraDayAnalysisGoogle.getIntraDayStockCandleArray(file);
	}
	
	public static ArrayList<IntraDayStockCandleArray> getIntraDayStockCandleArrayYahoo(String symbol) throws Exception {
		return IntraDayAnalysisYahoo.getIntraDayStockCandleArray(symbol);
	}
	
	public static double[] getVolumeDistribution() {
		return IntraDayVolumeDistribution.getDistribution();
	}
	
	public static HashMap<String, Long> getSharesOutstandingMap() {
		return StockAPI.getSharesOutstandingMap();
	}

	public static StockCandleArray getStockCandleArrayYahoo(String symbol) {
		return YahooParser.readCSVFile(symbol);
	}
	
	public static StockCandleArray getStockCandleArrayYahoo(File file) {
		return YahooParser.readCSVFile(file);
	}
}
