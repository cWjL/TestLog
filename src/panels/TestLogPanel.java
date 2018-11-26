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
/**
 * Main application panel
 * 
 * @author Jacob Loden
 */
public class TestLogPanel extends JPanel{

	/**
	 * Class variables
	 */
	private static final long serialVersionUID = 5833830482301963023L;
	private final Dimension BUTTON_SZ = new Dimension(75,30);
	private final Dimension TEXT_SZ = new Dimension(700,500);
	private String title;
	private String[] testCases;
	
	public JTextArea logText;
	public JTextField newLogEntry;
	public JComboBox<String> testCaseSelection;
	
	public JButton saveLog;
	public JButton exitLog;
	public JButton newLog;
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
	private void showPanel(){

		
		JPanel controlPanel = new JPanel(new GridLayout(0,1));
		JPanel editorPanel = new JPanel(new BorderLayout());
		this.logText = new JTextArea();
		logText.setPreferredSize(TEXT_SZ);
		editorPanel.add(logText, BorderLayout.CENTER);
		
		JPanel newEntryPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		this.newLogEntry = new JTextField(50);
		JLabel newLogLabel = new JLabel("New Log Entry:");
		this.testCaseSelection = new JComboBox<String>(this.testCases);		
		newEntryPanel.add(newLogLabel);
		newEntryPanel.add(newLogEntry);
		newEntryPanel.add(testCaseSelection);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		this.saveLog = new JButton("Save");
		this.exitLog = new JButton("Exit");
		this.newLog = new JButton("Clear");
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
		
		JSplitPane contentPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, controlPanel, editorPanel);
		contentPane.setEnabled(false);
		this.add(contentPane);
	}
	

}
