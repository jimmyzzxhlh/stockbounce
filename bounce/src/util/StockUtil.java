package util;


public class StockUtil {
	
	public static double getRoundTwoDecimals(double input) {
		return Math.round(input * 100.0) / 100.0;
	}
	
	public static int getIntervalNum(double start, double end, double interval) {
		if (end < start) return 0;
		return (int)((end - start) * 1.0 / interval) + 1;
	}
	
	public static double changeRate(double original, double result){
		if ((original <= 0)||(result <= 0))
			return 0;
		return (result - original) / original;
	}
	
	/**
	 * Get symbol name from the filename.
	 * If the filename is an indicator file like <Stock Symbol>_Indicators.csv, then we get the string before "_".
	 * If the filename is like <Stock Symbol>.csv, then we get the string before ".".
	 * @param filename
	 * @return
	 */
	public static String getSymbolFromFileName(String filename) {
		int underscorePos = filename.indexOf("_");
		if (underscorePos > 0) return filename.substring(0, underscorePos);
		int dotPos = filename.indexOf(".");
		if (dotPos > 0) return filename.substring(0, dotPos);
		return filename;
	}
	
	public static double getMax(double[] input) {
		double max = -1e10; 
		for (int i = 0; i < input.length; i++) {
			if (input[i] > max) {
				max = input[i];
			}
		}
		return max;
	}
		
	public static double getMin(double[] input) {
		double min = 1e10; 
		for (int i = 0; i < input.length; i++) {
			if (input[i] < min) {
				min = input[i];
			}
		}
		return min;
	}
	
	public static int getMax(int[] input) {
		int max = (int) -1e10; 
		for (int i = 0; i < input.length; i++) {
			if (input[i] > max) {
				max = input[i];
			}
		}
		return max;
	}
		
	public static int getMin(int[] input) {
		int min = (int) 1e10; 
		for (int i = 0; i < input.length; i++) {
			if (input[i] < min) {
				min = input[i];
			}
		}
		return min;
	}
	
	public static void setLimitForArray(double[] inputArray, double min, double max, double shift) {
		for (int i = 0; i < inputArray.length; i++) {
			inputArray[i] += shift;
			if (inputArray[i] < min) inputArray[i] = min;
			if (inputArray[i] > max) inputArray[i] = max;
		}
	}

	
	
}
