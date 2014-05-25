package stock;

import java.util.ArrayList;

/**
 * See the following wiki:
 * http://en.wikipedia.org/wiki/Simple_linear_regression
 * 
 */
public class SimpleLinearRegression {
	public ArrayList<Double> data = new ArrayList<Double>();
	
	public SimpleLinearRegression() {
		
	}
	
	public double getSlope() {
		double xAverage, yAverage, xyAverage, xSquareAverage;
		double x,y;
		double slope;
		double n = data.size();
		if (data.size() < 1) return 0;
		xAverage = 0;
		yAverage = 0;
		xyAverage = 0;
		xSquareAverage = 0;
		for (int i = 0; i < data.size(); i++) {
			x = i + 1;
			y = data.get(i);
			xAverage += x;
			yAverage += y;
			xyAverage += (x * y);
			xSquareAverage += (x * x);			
		}
		xAverage /= n;
		yAverage /= n;
		xyAverage /= n;
		xSquareAverage /= n;
		slope = (xyAverage - xAverage * yAverage) / (xSquareAverage - (xAverage * xAverage));
		return slope;		
	}
}
