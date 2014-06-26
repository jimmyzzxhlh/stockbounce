package trend;

import java.util.ArrayList;

public class WeightedOrdinaryLinearRegression extends LinearRegression {
	
	public ArrayList<Double> weight = new ArrayList<Double>();
	
	public WeightedOrdinaryLinearRegression() {
		super();
	}
	
	public void setDefaultWeight() {
		newWeight();
		for (int i = 0; i < data.size(); i++) {
			weight.add((i + 1) * 1.0);
		}
	}
	
	public void newWeight() {
		weight = new ArrayList<Double>();
	}
	
	public void setCoefficient(double c[]) {
		double x[] = new double[data.size()];
		for (int i = 0; i < data.size(); i++) {
			x[i] = i + 1;
		}
		for (int i = 0; i < data.size(); i++) {
			c[1] += weight.get(i);
			c[2] += weight.get(i) * x[i];
			c[3] -= weight.get(i) * data.get(i);
			c[5] += weight.get(i) * x[i] * x[i];
			c[6] -= weight.get(i) * x[i] * data.get(i);
		}
		c[4] = c[2];
	}
	
	public double getSlope() {
		double c[] = new double[7];
		setCoefficient(c);
		return (c[2] * c[6] - c[3] * c[5]) / (c[1] * c[5] - c[2] * c[4]);		
	}
	
	public double getOffset() {
		double c[] = new double[7];
		setCoefficient(c);
		return (c[3] * c[4] - c[1] * c[6]) / (c[1] * c[5] - c[2] * c[4]);
	}	
	
}
