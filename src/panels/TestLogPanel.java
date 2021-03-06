package panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
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
	private final Dimension COMP_SZ = new Dimension(85, 30);
	private final Dimension TEXT_SZ = new Dimension(700,500);
	@SuppressWarnings("unused")
	private String title;
	private String[] testCases;
	public JLabel notSaved;
	private String[] testCmd;
	
	public JTextArea logText;
	public JTextField newLogEntry;
	public JComboBox<String> testCaseSelection;
	public JComboBox<String> testCommandSelection;
	public JButton addTestCmd;
	
	public JButton saveLog;
	public JButton exitLog;
	public JButton newLog;
	public JButton export;
	
	/**
	 * Panel constructor
	 * 
	 *  @param String title
	 *  @param String[] test cases
	 */
	public TestLogPanel(String title){
		this.title = title;
		this.testCases = new String[]{"-Select-", "Note"};
		this.testCmd = new String[]{"-Select-"};
		showPanel(null);
	}
	
	/**
	 * Panel constructor
	 * 
	 *  @param String title
	 *  @param String[] test cases
	 */
	public TestLogPanel(String title, String[] testCases){
		this.title = title;
		this.testCases = testCases;
		this.testCmd = new String[]{"-Select-"};
		showPanel(null);
	}
	
	/**
	 * Panel constructor
	 * 
	 *  @param String title
	 *  @param String[] test cases
	 *  @param File fp
	 */
	public TestLogPanel(String title, String[] testCases, File fp){
		this.title = title;
		this.testCases = testCases;
		this.testCmd = new String[]{"-Select-"};
		showPanel(fp);
	}
	
	/**
	 * Show main application panel 
	 * 
	 * @param none
	 * @return none
	 */
	private void showPanel(File fp){
		//FileReader reader = null;
		
		JPanel controlPanel = new JPanel(new GridLayout(3,1));
		controlPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		//JPanel controlPanel = new JPanel(new BorderLayout());
		JPanel editorPanel = new JPanel(new BorderLayout());
		
		this.notSaved = new JLabel("\u2022 Unsaved Changes");
		this.notSaved.setForeground(Color.RED);	
	
		this.logText = new JTextArea();
		if(fp != null) {
			try {
				this.logText.read(new FileReader(fp), null);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		editorPanel.add(logText, BorderLayout.CENTER);
		
		JPanel newEntryPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		this.newLogEntry = new JTextField(50);
		
		JLabel newLogLabel = new JLabel("New Log Entry:");
		this.testCaseSelection = new JComboBox<String>(this.testCases);
		this.testCaseSelection.setPreferredSize(COMP_SZ);
		this.testCaseSelection.setToolTipText("Select test command to add to the log");
		
		JLabel newCmdLabel = new JLabel("Select Test CMD:");
		this.testCommandSelection = new JComboBox<String>(this.testCmd);
		this.testCommandSelection.setPreferredSize(new Dimension(563, 27));
		this.testCommandSelection.setAlignmentX(CENTER_ALIGNMENT);
		
		this.addTestCmd = new JButton("Add CMD");
		this.addTestCmd.setToolTipText("Add the selected command to the log");
		this.addTestCmd.setPreferredSize(COMP_SZ);
		
		JPanel newCmdPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		newCmdPanel.add(newCmdLabel);
		newCmdPanel.add(this.testCommandSelection);
		newCmdPanel.add(this.addTestCmd);
		
		newEntryPanel.add(newLogLabel);
		newEntryPanel.add(newLogEntry);
		newEntryPanel.add(testCaseSelection);
		
		JPanel buttonPanel = new JPanel(new BorderLayout());
		
		JPanel leftButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		this.export = new JButton("Export");
		this.export.setPreferredSize(BUTTON_SZ);
		this.export.setToolTipText("Export test commands to spreadsheet");
		leftButtonPanel.add(export);
		leftButtonPanel.add(this.notSaved);
		

		JPanel rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		this.saveLog = new JButton("Save");
		this.exitLog = new JButton("Exit");
		this.newLog = new JButton("Clear");
		this.saveLog.setPreferredSize(BUTTON_SZ);
		this.saveLog.setToolTipText("Save the current log");
		this.exitLog.setPreferredSize(BUTTON_SZ);
		this.exitLog.setToolTipText("Close current log without saving and exit");
		this.newLog.setPreferredSize(BUTTON_SZ);
		this.newLog.setToolTipText("Close the current log without saving and start new");
		rightButtonPanel.add(exitLog);
		rightButtonPanel.add(newLog);
		rightButtonPanel.add(saveLog);
		
		buttonPanel.add(leftButtonPanel, BorderLayout.WEST);
		buttonPanel.add(rightButtonPanel, BorderLayout.EAST);
		
		//editorPanel
		controlPanel.add(newEntryPanel);
		controlPanel.add(newCmdPanel);
		controlPanel.add(buttonPanel);
		JScrollPane editorPane = new JScrollPane(editorPanel);
		editorPane.setPreferredSize(TEXT_SZ);
		editorPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JSplitPane contentPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, editorPane, controlPanel);
		contentPane.setEnabled(false);
		this.add(contentPane);
	}
}
