package draw;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

import org.joda.time.LocalDate;

import util.StockUtil;

/**
 * Panel for adjusting settings at the bottom of the main panel.
 * @author jimmyzzxhlh-Dell
 *
 */
@SuppressWarnings("serial")
public class SettingsPanel extends JPanel {
	
	private JTextField symbolTextField;
	private JTextField startDateField;
	private JTextField endDateField;
	
	private String symbol;
	private LocalDate startLocalDate;
	private LocalDate endLocalDate;
	
	private StockChartPanel stockChartPanel;
	
	public SettingsPanel() {
		this.setPreferredSize(new Dimension(getPanelWidth(), getPanelHeight()));
		this.setBorder(BorderFactory.createLineBorder(Color.blue));
		this.setOpaque(false);
		FlowLayout layout = new FlowLayout(FlowLayout.LEADING);
		this.setLayout(layout);
		
		addSymbolLabel();
		addSymbolTextField();
		addStartDateLabel();
		addStartDateField();
		addEndDateLabel();
		addEndDateField();
		addDrawButton();
	}
	
	public LocalDate getStartLocalDate() {
		return startLocalDate;
	}
	
	public LocalDate getEndLocalDate() {
		return endLocalDate;
	}
	
	public void setStockChartPanel(StockChartPanel stockChartPanel) {
		this.stockChartPanel = stockChartPanel;
	}
	
	public void updateDates(Date newStartDate, Date newEndDate) {
		startDateField.setText(StockUtil.formatDate(newStartDate));
		endDateField.setText(StockUtil.formatDate(newEndDate));
		parseInput();
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawString("Settings panel", 500, 100);
		
	}
	
	private void addSymbolLabel() {
		JLabel symbolLabel = new JLabel("Symbol:");
		setLabelProperties(symbolLabel);
		this.add(symbolLabel);		
	}
	
	private void setLabelProperties(JLabel label) {
		label.setFont(StockGUIConst.LABEL_FONT);
		label.setForeground(StockGUIConst.LABEL_COLOR);
	}
	
	private void addSymbolTextField() {
		symbolTextField = new JTextField(5);
		setTextFieldProperties(symbolTextField);
		((AbstractDocument)symbolTextField.getDocument()).setDocumentFilter(new UpperCaseDocumentFilter());
		this.add(symbolTextField);		
	}
	
	
	
	
	private void setTextFieldProperties(JTextField textField) {
		textField.setFont(StockGUIConst.TEXTFIELD_FONT);
		textField.setForeground(StockGUIConst.TEXTFIELD_FOREGROUND_COLOR);
		textField.setBackground(StockGUIConst.TEXTFIELD_BACKGROUND_COLOR);
		textField.setBorder(BorderFactory.createLineBorder(StockGUIConst.TEXTFIELD_BORDER_COLOR));
		textField.setCaretColor(StockGUIConst.TEXTFIELD_FOREGROUND_COLOR);
	}
	
	private void addStartDateLabel() {
		JLabel startDateLabel = new JLabel("Start Date:");
		setLabelProperties(startDateLabel);
		this.add(startDateLabel);
	}
	
	private void addStartDateField() {
		startDateField = new JTextField(6);
		setTextFieldProperties(startDateField);
		this.add(startDateField);	
	}
	
	private void addEndDateLabel() {
		JLabel endDateLabel = new JLabel("End Date:");
		setLabelProperties(endDateLabel);
		this.add(endDateLabel);
	}
	
	private void addEndDateField() {
		endDateField = new JTextField(6);
		setTextFieldProperties(endDateField);
		this.add(endDateField);
	}
	
	private void addDrawButton() {
		JButton drawButton = new JButton("Draw");
		drawButton.setFont(StockGUIConst.BUTTON_FONT);
		drawButton.setForeground(StockGUIConst.BUTTON_FOREGROUND_COLOR);
		drawButton.setBackground(StockGUIConst.BUTTON_BACKGROUND_COLOR);
		drawButton.setPreferredSize(new Dimension(80, 24));
		drawButton.setOpaque(false);
		drawButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionPerformedDraw();
			}
		});
		this.add(drawButton);
	}
	
	private void actionPerformedDraw() {
		if (!parseInput()) return;
		stockChartPanel.setSymbol(symbol);
		stockChartPanel.setStartDate(startLocalDate.toDate());
		stockChartPanel.setEndDate(endLocalDate.toDate());
		if (!stockChartPanel.initializeStockCandleArray()) return;
		stockChartPanel.repaint();
		
	}
	
	private boolean parseInput() {
		symbol = symbolTextField.getText();
		startLocalDate = StockUtil.parseLocalDate(startDateField.getText());
		endLocalDate = StockUtil.parseLocalDate(endDateField.getText());
		if (startLocalDate == null) {
			StockGUIUtil.showWarningMessageDialog("Start Date " + startDateField.getText() + " is not valid.", "Invalid Date");
			return false;
		}
		
		if (endLocalDate == null) {
			StockGUIUtil.showWarningMessageDialog("End Date " + endDateField.getText() + " is not valid.", "Invalid Date");
			return false;
		}
		
		if (startLocalDate.isAfter(endLocalDate)) {
			StockGUIUtil.showWarningMessageDialog("Start Date " + StockUtil.formatLocalDate(startLocalDate) + " is after End Date " + StockUtil.formatLocalDate(endLocalDate) + ".", "Invalid Date");
			return false;
		}
		
		startDateField.setText(StockUtil.formatLocalDate(startLocalDate));
		endDateField.setText(StockUtil.formatLocalDate(endLocalDate));
		return true;

	}
	
	
	public int getPanelHeight() {
		return (int) Math.floor(StockGUIUtil.getScreenHeight() * StockGUIConst.SETTINGS_PANEL_HEIGHT_PERCENTAGE);
	}
	
	public int getPanelWidth() {
		return StockGUIUtil.getScreenWidth();
	}
}
