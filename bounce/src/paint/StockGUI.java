package paint;

import java.awt.Color;

import javax.swing.JFrame;


/**
 */
public class StockGUI {
	
	public static void main(String args[]) {
		JFrame frame = new JFrame("Stock Chart");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		StockMainPanel stockMainPanel = new StockMainPanel();
		stockMainPanel.setFrame(frame);
		frame.add(stockMainPanel);
		StockGUIUtil.setFrame(frame);

//		stockMainPanel.setOpaque(false);
//		stockMainPanel.getSettingsPanel().setOpaque(false);
//		stockMainPanel.getSettingsPanel().setBackground(Color.black);
//		stockMainPanel.setBackground(Color.black);
		
		frame.getContentPane().setBackground(StockGUIConst.STOCK_GUI_BACKGROUND_COLOR);
		
		//TODO: Set the size of the frame based on how many candles are in the chart.
		frame.setVisible(true);
		
				
		
	}
	
	
}
