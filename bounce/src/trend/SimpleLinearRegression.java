package trend;


/**
 * See the following wiki:
 * http://en.wikipedia.org/wiki/Simple_linear_regression
 * 
 */
public class SimpleLinearRegression extends LinearRegression {
	
	public SimpleLinearRegression() {
		
	}
	
	/**
	 * Given a list of data points (x[i],y[i]) i=1..n. Find a straight line such that 
	 * y = a + b * x
	 * where sigma((y[i] - a - b * x[i])^2) is minimum.
	 * b = ((Average of x*y) - (Average of x) * (Average * y)) / (Average of (x^2) - (Average of x)^2)  
	 * @return b (slope)
	 */
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

	@Override
	public double getOffset() {
		// TODO Auto-generated method stub
		return 0;
	}
}
