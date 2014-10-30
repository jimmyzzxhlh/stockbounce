package test;

import indicator.StockIndicatorArray;
import indicator.StockIndicatorConst;
import indicator.StockIndicatorParser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import svm.SVMTrain;
import draw.DrawCoordinatesFrame;

public class StockGainAnalysisTest {
	
	public static void main(String args[]) throws Exception {
//		testSVMStockGainAnalysis();
		testRSIAnalysis();
//		testBBPercentBAnalysis();
//		testBBBandwidthAnalysis();
//		testEMADistanceAnalysis();
	}
	

	
	/**
	 * Analyze the distribution of the stock gain.
	 * It turns out that the distribution is very symmetric. However most of the training data 
	 * has gain between -10% to 10%, so we need to adjust SVM parameters, or we need to balance
	 * the data set.
	 */
	private static void testSVMStockGainAnalysis() {
		SVMTrain svmTrain = new SVMTrain();
		svmTrain.initializeStockIndicatorArray(StockIndicatorConst.INDICATOR_CSV_DIRECTORY_PATH);
		StockIndicatorArray stockIndicatorArray = svmTrain.getStockIndicatorArray();
		int[] stockGainArray = new int[201];
		for (int i = 0; i < stockIndicatorArray.size(); i++) {
			int stockGain = (int)(Math.round(stockIndicatorArray.getStockGain(i))) + 100;
			if (stockGain >= 200) stockGain = 200;
			if (stockGain < 0) stockGain = 0;
			stockGainArray[stockGain]++;
		}
		for (int i = 0; i < stockGainArray.length; i++) {
			System.out.println(stockGainArray[i]);
			
		}
	}
	
	
	/**
	 * 结论：只看RSI数值并不能区分StockGain
	 * StockGain >= 20:
	 *   Volume没有限制，RSI的范围如下:
	 *     30-70: 95.56%
	 *     30-50: 53.12%
	 *     20-60: 87.18%
	 *   Volume >= 50000时，RSI的范围如下:
	 *     30-70: 94.07% 
	 * StockGain没有限制:
	 *   Volume没有限制，RSI的范围如下：
	 *     30-70: 97.17%
	 *     30-50: 46.98%
	 *   Volume >= 50000时，RSI的范围如下:
	 *     30-70:  
	 *     28-72: 
	 *     25-75:               
	 * @throws Exception
	 */
	public static void testRSIAnalysis() throws Exception {
		int minStockGain = -10000;
		double rsiMin = 20;
		double rsiMax = 60;
		StockIndicatorArray stockIndicatorArray = StockIndicatorParser.readCSVFiles(StockIndicatorConst.INDICATOR_CSV_DIRECTORY_PATH);
		
//		double x[] = new double[stockIndicatorArray.size()];
//		double y[] = new double[stockIndicatorArray.size()];
		int length = stockIndicatorArray.getStockGainCount(minStockGain);
		double x[] = new double[length];
		double y[] = new double[length];
		int index = -1;
		int rsiCount = 0; 
		for (int i = 0; i < stockIndicatorArray.size(); i++) {
			double stockGain = stockIndicatorArray.getStockGain(i);
			double rsi = stockIndicatorArray.getRSI(i);
			if (stockGain < minStockGain) continue;
			if ((rsi >= rsiMin) && (rsi <= rsiMax)) rsiCount++;
			index++;
			y[index] = rsi;
			 
			if (stockGain >= 200) {
				x[index] = 300;
				continue;
			}
			if (stockGain <= -100) {
				x[index] = -100;
				continue;
			}
			x[index] = stockGain + 100;
						
		}
		System.out.println("RSI count: " + rsiCount);
		System.out.println("Total count: " + index);
		System.out.println("Percentage: " + rsiCount * 100.0 / index);
//		DrawCoordinatesFrame f = new DrawCoordinatesFrame(x, y, 2, 10);
		
	}
	
	public static void testBBPercentBAnalysis() {
		int minStockGain = 40;
		try {
			StockIndicatorArray stockIndicatorArray = StockIndicatorParser.readCSVFiles(StockIndicatorConst.INDICATOR_CSV_DIRECTORY_PATH);
			File f = new File("D:\\zzx\\Stock\\BBPercentB_" + minStockGain + ".csv");
			FileWriter fw = new FileWriter(f.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("Gain,BBPercentB");
			bw.newLine();
			for (int i = 0; i < stockIndicatorArray.size(); i++) {
				double stockGain = stockIndicatorArray.getStockGain(i);
//				if (stockGain > 1000) {
//					System.err.println(stockIndicatorArray.getSymbol(i) + " " + stockIndicatorArray.getDate(i) + " " + stockIndicatorArray.getStockGain(i));
//				}
				if (stockGain < minStockGain) continue;
				bw.write(stockGain + "," + stockIndicatorArray.getBollingerBandsPercentB(i));
				bw.newLine();
			}
			bw.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void testBBBandwidthAnalysis() {
		int minStockGain = 40;
		try {
			StockIndicatorArray stockIndicatorArray = StockIndicatorParser.readCSVFiles(StockIndicatorConst.INDICATOR_CSV_DIRECTORY_PATH);
			File f = new File("D:\\zzx\\Stock\\BBBandwidth_" + minStockGain + ".csv");
			FileWriter fw = new FileWriter(f.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("Gain,BBBandwidth");
			bw.newLine();
			for (int i = 0; i < stockIndicatorArray.size(); i++) {
				double stockGain = stockIndicatorArray.getStockGain(i);
//				if (stockGain > 1000) {
//					System.err.println(stockIndicatorArray.getSymbol(i) + " " + stockIndicatorArray.getDate(i) + " " + stockIndicatorArray.getStockGain(i));
//				}
				if (stockGain < minStockGain) continue;
				bw.write(stockGain + "," + stockIndicatorArray.getBollingerBandsBandwidth(i));
				bw.newLine();
			}
			bw.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void testEMADistanceAnalysis() {
		int minStockGain = 40;
		try {
			StockIndicatorArray stockIndicatorArray = StockIndicatorParser.readCSVFiles(StockIndicatorConst.INDICATOR_CSV_DIRECTORY_PATH);
			File f = new File("D:\\zzx\\Stock\\emaDistance_" + minStockGain + ".csv");
			FileWriter fw = new FileWriter(f.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("Gain,emaDistance");
			bw.newLine();
			for (int i = 0; i < stockIndicatorArray.size(); i++) {
				double stockGain = stockIndicatorArray.getStockGain(i);
//				if (stockGain > 1000) {
//					System.err.println(stockIndicatorArray.getSymbol(i) + " " + stockIndicatorArray.getDate(i) + " " + stockIndicatorArray.getStockGain(i));
//				}
				if (stockGain < minStockGain) continue;
				bw.write(stockGain + "," + stockIndicatorArray.getEMADistance(i));
				bw.newLine();
			}
			bw.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
