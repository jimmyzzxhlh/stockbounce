package test;

import java.util.Arrays;

import util.StockUtil;

public class GeneralTest {

	public static void main(String args[]) {
		splitCSVLineTest();
	}
	
	
	private static void splitCSVLineTest() {
		String s = "\"MORE          \",\"Monogram Residential Trust, Inc.\",\"9.26\",\"1563814650.72\",\"n/a\",\"n/a\",\"n/a\",\"n/a\",\"http://www.nasdaq.com/symbol/more          \",";
		String[] splitted = StockUtil.splitCSVLine(s);
		for (int i = 0; i < splitted.length; i++) {
			System.out.println(splitted[i]);
		}
	}
}
