package src.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
/**
 * Main application configuration panel
 * 
 * @author Jacob Loden
 */
public class TestLogConfigPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Class variables
	 */
	private static final String TEST_CASE = "Test Cases";
	private static final String TEST_CMDS = "Test Commands";
	private final Dimension TC_TEXT_SZ = new Dimension(350,500);
	private final Dimension CMD_TEXT_SZ = new Dimension(350,500);
	private final Dimension BUTTON_SZ = new Dimension(75,30);
	private JLabel TEST_CASE_LABEL;
	private JLabel TEST_CMD_LABEL;
	public JLabel notSaved;

	
	public JTextArea tcTextField;
	public JTextArea cmdsTextField;
	public JButton clearButton;
	public JButton saveButton;
	public JButton importButton;
	/**
	 * Panel constructor
	 * 
	 *  @param String[] test cases
	 */
	public TestLogConfigPanel(String[] testCases) {
		int v_gap = 10;
		int h_gap = 5;
		this.tcTextField = new JTextArea();
		this.setLayout(new BorderLayout(h_gap, h_gap));
		this.setBorder(new EmptyBorder(v_gap, h_gap, v_gap, h_gap));
		for(int i = 2; i < testCases.length; i++) {
			this.tcTextField.append(testCases[i] + '\n');
		}
		showPanel();
	}
	
	/**
	 * Panel constructor
	 * 
	 *  @param none
	 */
	public TestLogConfigPanel() {
		int v_gap = 10;
		int h_gap = 5;
		this.tcTextField = new JTextArea();
		this.setLayout(new BorderLayout(h_gap, h_gap));
		this.setBorder(new EmptyBorder(v_gap, h_gap, v_gap, h_gap));
		showPanel();
	}
	/**
	 * Build and show panel 
	 * 
	 * @param none
	 * @return none
	 */
	public void showPanel() {
		this.cmdsTextField = new JTextArea();
		this.TEST_CASE_LABEL = new JLabel(TEST_CASE, SwingConstants.CENTER);
		this.TEST_CMD_LABEL = new JLabel(TEST_CMDS, SwingConstants.CENTER);
		this.notSaved = new JLabel("\u2022 Unsaved Changes");
		this.notSaved.setForeground(Color.RED);
		
		JScrollPane tcPane = new JScrollPane(this.tcTextField);
		JScrollPane cmdPane = new JScrollPane(this.cmdsTextField);
		tcPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		tcPane.setPreferredSize(TC_TEXT_SZ);
		cmdPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		cmdPane.setPreferredSize(CMD_TEXT_SZ);

		JPanel cmdPanel = new JPanel();
		JPanel cmdLabelPanel = new JPanel(new BorderLayout());
		cmdPanel.setLayout(new BoxLayout(cmdPanel, BoxLayout.Y_AXIS));
		cmdLabelPanel.add(this.TEST_CMD_LABEL, BorderLayout.SOUTH);
		cmdPanel.add(cmdLabelPanel);
		cmdPanel.add(cmdPane);
		
		JPanel tcPanel = new JPanel();
		JPanel tcLabelPanel = new JPanel(new BorderLayout());
		tcPanel.setLayout(new BoxLayout(tcPanel, BoxLayout.Y_AXIS));
		tcLabelPanel.add(this.TEST_CASE_LABEL, BorderLayout.SOUTH);
		tcPanel.add(tcLabelPanel);
		tcPanel.add(tcPane);

		
		JSplitPane contentPane;
		contentPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tcPanel, cmdPanel);
		contentPane.setEnabled(false);
		
		JPanel buttonBasePanel = new JPanel(new BorderLayout());
		JPanel leftButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		this.clearButton = new JButton("Clear");
		this.clearButton.setSize(BUTTON_SZ);
		this.saveButton = new JButton("Save");
		this.saveButton.setSize(BUTTON_SZ);
		this.importButton = new JButton("Import");
		this.importButton.setSize(BUTTON_SZ);
		
		rightButtonPanel.add(importButton);
		rightButtonPanel.add(saveButton);
		leftButtonPanel.add(clearButton);
		leftButtonPanel.add(this.notSaved);
		
		buttonBasePanel.add(rightButtonPanel, BorderLayout.EAST);
		buttonBasePanel.add(leftButtonPanel, BorderLayout.WEST);
		
		this.add(buttonBasePanel, BorderLayout.SOUTH);
		this.add(contentPane, BorderLayout.CENTER);
		
	}
	
}
