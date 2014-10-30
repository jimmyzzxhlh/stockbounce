package draw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import util.StockUtil;

@SuppressWarnings("serial")
public class DrawCoordinatesFrame extends JFrame {

	public double[] x;
	public double[] y;
	public int[] intX;
	public int[] intY;
	public double scaleX;
	public double scaleY;
	int width;
	int height;
	int coordinateWidth;
	int coordinateHeight;
	Color color[][];

	
	public DrawCoordinatesFrame(double[] x, double[] y, double scaleX, double scaleY) {
		super("Draw Coordinates");
		this.x = x;
		this.y = y;
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		convertDoubleArrayToIntArray();
		int maxX = StockUtil.getMax(intX);
		int maxY = StockUtil.getMax(intY);
		coordinateWidth = maxX;
		coordinateHeight = maxY;
		width = coordinateWidth + 100;
		height = coordinateHeight + 100;
		color = new Color[coordinateWidth + 1][coordinateHeight + 1];
		
		this.setSize(width, height);
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose(); System.exit(0);				
			}
		});
		
	}
	
	
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		initializeCoordinateSystem(g2);
		//We assume that length of X is equal to length of Y.
		for (int i = 0; i < x.length; i++) {
			g2.setColor(color[intX[i]][intY[i]]);
			g2.drawLine(intX[i] - 1, intY[i], intX[i] + 1, intY[i]);
			g2.drawLine(intX[i], intY[i] - 1, intX[i], intY[i] + 1);
			color[intX[i]][intY[i]] = color[intX[i]][intY[i]].darker(); 
		}
	}
	
	private void convertDoubleArrayToIntArray() {
		int length = Math.min(x.length, y.length);
		intX = new int[length];
		intY = new int[length];
		for (int i = 0; i < length; i++) {
			intX[i] = (int) Math.round(x[i] * scaleX);
			intY[i] = (int) Math.round(y[i] * scaleY);
		}
	}
	
	private void initializeCoordinateSystem(Graphics2D g2) {
		Color originalColor = g2.getColor();
		g2.setColor(Color.black);
		drawCoordinateLabels(g2);
		g2.translate(50, height - 50);
		g2.scale(1.0, -1.0);
		g2.drawLine(0, 0, coordinateWidth, 0);
		g2.drawLine(0, 0, 0, coordinateHeight);
		//Draw X axis
		int unitWidth = coordinateWidth / 10;
		for (int i = unitWidth; i <= coordinateWidth; i += unitWidth) {
			g2.drawLine(i, -5, i, 5);
		}
		//Draw Y axis
		int unitHeight = coordinateHeight / 10;
		for (int i = unitHeight; i <= coordinateHeight; i += unitHeight) {
			g2.drawLine(-5, i, 5, i);
		}
		for (int i = 0; i <= coordinateWidth; i++) {
			for (int j = 0; j <= coordinateHeight; j++) {
				color[i][j] = Color.red; 
			}
		}
		
	}
	
	private void drawCoordinateLabels(Graphics2D g2) {
		//Draw X axis label
		int unitWidth = coordinateWidth / 10;
		for (int i = 0; i <= coordinateWidth; i+= unitWidth) {
			g2.drawString(String.valueOf(i * 1.0 / scaleX), i + 50, height - 30);
		}
		//Draw Y axis label
		int unitHeight = coordinateHeight / 10;
		for (int i = 0; i <= coordinateHeight; i += unitHeight) {
			g2.drawString(String.valueOf(i * 1.0 / scaleY), 15, height - i - 45);
		}
	}
}