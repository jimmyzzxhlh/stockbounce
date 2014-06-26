package trend;

import java.util.ArrayList;

public abstract class LinearRegression {
	public ArrayList<Double> data = new ArrayList<Double>();
	
	public abstract double getSlope();
	
	public abstract double getOffset();
		
}
