package panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class TestLogPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5833830482301963023L;
	private final Dimension BUTTON_SZ = new Dimension(75,30);
	private final Dimension TEXT_SZ = new Dimension(700,500);
	private String title;
	private String[] testCases;
	/**
	 * Panel constructor
	 * 
	 *  @param String title
	 *  @return String[] test cases
	 */
	public TestLogPanel(String title, String[] testCases){
		this.title = title;
		this.testCases = testCases;
		showPanel();
	}
	
	/**
	 * Show main application panel 
	 * 
	 * @param none
	 * @return none
	 */
	@SuppressWarnings("unchecked")
	private void showPanel(){
		DateFormat currentDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		//add this in the action listener for the text field
		Date date = new Date();
		
		JPanel controlPanel = new JPanel(new GridLayout(0,1));
		JPanel editorPanel = new JPanel(new BorderLayout());
		JTextArea logText = new JTextArea();
		logText.setPreferredSize(TEXT_SZ);
		editorPanel.add(logText, BorderLayout.CENTER);
		
		JPanel newEntryPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		JTextField newLogEntry = new JTextField(50);
		JLabel newLogLabel = new JLabel("New Log Entry:");
		JComboBox<String> testCaseSelection = new JComboBox<String>(this.testCases);		
		newEntryPanel.add(newLogLabel);
		newEntryPanel.add(newLogEntry);
		newEntryPanel.add(testCaseSelection);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		JButton saveLog = new JButton("Save");
		JButton exitLog = new JButton("Cancel");
		JButton newLog = new JButton("Clear");
		saveLog.setPreferredSize(BUTTON_SZ);
		saveLog.setToolTipText("Save the current log");
		exitLog.setPreferredSize(BUTTON_SZ);
		exitLog.setToolTipText("Close current log without saving and exit");
		newLog.setPreferredSize(BUTTON_SZ);
		newLog.setToolTipText("Close the current log without saving and start new");
		buttonPanel.add(exitLog);
		buttonPanel.add(newLog);
		buttonPanel.add(saveLog);
		
		//editorPanel
		controlPanel.add(newEntryPanel);		
		controlPanel.add(buttonPanel);

		this.add(new JSplitPane(JSplitPane.VERTICAL_SPLIT, controlPanel, editorPanel));
	}
	

}
