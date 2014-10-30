package test;

import draw.DrawCoordinatesFrame;

public class DrawCoordinatesTest {
	public static void main(String args[]) {
		testDraw();
	}
	
	public static void testDraw() {
		double x[] = new double[100 * 100];
		double y[] = new double[100 * 100];
		for (int i = 0; i < 100; i++) {			
			for (int j = 0; j < 100; j++) { 
				x[i * 100 + j] = (int)(Math.random() * 100);
				y[i * 100 + j] = (int)(Math.random() * 100);
			}
		}
		DrawCoordinatesFrame f = new DrawCoordinatesFrame(x, y, 5, 5);
	}
}
