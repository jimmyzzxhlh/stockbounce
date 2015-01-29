package paint;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.geom.Line2D;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class StockGUIUtil {
	
	private static GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	private static JFrame frame;
	private static Stroke dotted = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] {1,2}, 0);
	
	public static int getScreenWidth() {
		return graphicsDevice.getDisplayMode().getWidth();
	}
	
	public static int getScreenHeight() {
		return graphicsDevice.getDisplayMode().getHeight();
	}
	
	public static void setFrame(JFrame inputFrame) {
		frame = inputFrame;
	}
	
    public static void showWarningMessageDialog(Object message, String title) {
    	JOptionPane.showMessageDialog(frame, message, title, JOptionPane.WARNING_MESSAGE);
    }
    
    public static void drawDottedLine(Graphics2D g2, double x1, double y1, double x2, double y2, Color color) {
    	Stroke backupStroke = g2.getStroke();
    	g2.setStroke(dotted);
    	drawLine(g2, x1, y1, x2, y2, color);
    	g2.setStroke(backupStroke);    	
    }
    
    public static void drawLine(Graphics2D g2, double x1, double y1, double x2, double y2, Color color) {
    	Color backupColor = g2.getColor();
    	g2.setColor(color);
    	Shape line = new Line2D.Double(x1, y1, x2, y2);
    	g2.draw(line);
    	g2.setColor(backupColor);
    	line = null;
    }

    public static int getMonth(Date date) {
    	Calendar cal = Calendar.getInstance(); 
		cal.setTime(date);
		return cal.get(Calendar.MONTH);
    }
    
    
    public static double getStringWidth(Graphics2D g2, Font font, String str) {
    	FontRenderContext frc = g2.getFontRenderContext();
    	return font.getStringBounds(str, frc).getWidth();
    }
    
    public static double getStringHeight(Graphics2D g2, Font font, String str) {
    	FontRenderContext frc = g2.getFontRenderContext();
    	return font.getStringBounds(str, frc).getHeight();
    }
}
