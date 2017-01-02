package test;

import indicator.StockGain;

import java.io.File;

import stock.StockAPI;
import stock.CandleList;
import stock.StockConst;

/**
 * 这个测试没啥用，不过有一个结果是，如果能够找出区域最低点的话（比如20天之内的左右最低点），那么考察在最低点之后
 * 的20天内，能够让StockGain>=20%的天数，要占到所有StockGain>=20%的天数的60%，也就是说，找到最低点的话基本上
 * 就找到了60%赚的点位。
 * 问题是，这里找的最低点是左右都是最低点，然而实际不可能知道右边（未来）的价格。
 * @author jimmyzzxhlh-Dell
 *
 */
public class LocalMinCloseTest {
	
	
	private static final int PERIOD = 40;
	private static final double PERCENTAGE = 0.01;
	
	public static void main(String args[]) throws Exception {
		testLocalMin();
	}
	
	public static void testLocalMin() throws Exception {
		File directory = new File(StockConst.STOCK_CSV_DIRECTORY_PATH);
		int stockGainNum = 0;
		int stockGainTotal = 0;
		int stockGainNumFromMinClose = 0;
		int minCloseNum = 0;
		int minCloseGetStockGainNum = 0;
		
		for (File csvFile : directory.listFiles()) {
			System.out.println("Reading File: " + csvFile.getName());
			CandleList stockCandleList = StockAPI.getstockCandleListYahoo(csvFile);
			double[] stockGain = StockGain.getStockGain(stockCandleList);
			stockGainTotal += stockGain.length;
			
			boolean[] stockFlag = new boolean[stockCandleList.size()];
			for (int i = 0; i < stockCandleList.size(); i++) {
				if (stockGain[i] >= 20) {
					stockGainNum++;
				}
				if (isMinClose(stockCandleList, i)) {
//				if (isMinCloseBefore(stockCandleList, i)) {
					minCloseNum++;
					boolean hasStockGain = false;
					for (int j = i; j < Math.min(i + PERIOD / 2, stockCandleList.size()); j++) {
						if (stockFlag[j]) continue;
						stockFlag[j] = true;
						if (stockGain[j] >= 20) {
							stockGainNumFromMinClose++;
							hasStockGain = true;							
						}
					}
					if (hasStockGain) minCloseGetStockGainNum++;
				}
			}
			
			stockCandleList = null;
		}
		System.out.println("Total Stock Gain: " + stockGainTotal);
		System.out.println("Stock Gain >= 20: " + stockGainNum + ", Percentage: " + stockGainNum * 1.0 / stockGainTotal);
		System.out.println("Stock Gain >= 20 from Min Close: " + stockGainNumFromMinClose + ", Percentage: " + stockGainNumFromMinClose * 1.0 / stockGainTotal);
		System.out.println("Min Close: " + minCloseNum);
		System.out.println("Min Close Get Stock Gain: " + minCloseGetStockGainNum + ", Percentage: " + minCloseGetStockGainNum * 1.0 / minCloseNum);
		
		
	}
	
	public static boolean isMinClose(CandleList stockCandleList, int index) {
		if (index < PERIOD) return false;
		if (index + PERIOD >= stockCandleList.size()) return false;
		double currentClose = stockCandleList.getClose(index) * (1 - PERCENTAGE);
		for (int i = index - PERIOD; i <= index + PERIOD; i++) {
			if (i == index) continue;
			if (stockCandleList.getClose(i) < currentClose) return false;
		}
		return true;
	}
	
	public static boolean isMinCloseBefore(CandleList stockCandleList, int index) {
		if (index < PERIOD) return false;
		double currentClose = stockCandleList.getClose(index) * (1 - PERCENTAGE);
		for (int i = index - PERIOD; i < index; i++) {
			if (stockCandleList.getClose(i) < currentClose) return false;
		}
		return true;
	}
}
