package test;

import trend.SimpleLinearRegression;

public class SimpleLinearRegressionTest {
	
	public static void main(String args[]) {
		SimpleLinearRegression slr = new SimpleLinearRegression();
		double slope;
		slr.data.add(1.0);
		slr.data.add(2.0);
		slr.data.add(5.0);
		slr.data.add(7.0);
		slope = slr.getSlope();
		System.out.println(slope);		
		
	}
}
