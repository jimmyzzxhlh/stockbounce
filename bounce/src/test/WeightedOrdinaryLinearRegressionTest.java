package test;

import trend.WeightedOrdinaryLinearRegression;

/**
 * See the following wiki: 
 * http://zh.wikipedia.org/wiki/%E6%9C%80%E5%B0%8F%E4%BA%8C%E4%B9%98%E6%B3%95 
 */
public class WeightedOrdinaryLinearRegressionTest {
	public static void main(String args[]) {
		WeightedOrdinaryLinearRegression wdlr = new WeightedOrdinaryLinearRegression();
		wdlr.data.add(6.0);
		wdlr.data.add(5.0);
		wdlr.data.add(7.0);
		wdlr.data.add(10.0);
		wdlr.newWeight();
//		for (int i = 0; i < 4; i++) {
//			wdlr.weight.add(1.0);
//		}
		wdlr.setDefaultWeight();
		
		System.out.println(wdlr.getSlope());
		System.out.println(wdlr.getOffset());
	}
}
