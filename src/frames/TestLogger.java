package src.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import src.panels.TestLogTabbed;

/**
 * Main application frame
 * 
 *  @author Jacob Loden
 */
public class TestLogger extends JFrame{
	
	/**
	 * Class variables
	 */
	private static final long serialVersionUID = 6998372573965893306L;
	private String title;
	private String[] testCase;
	private DateFormat currentDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private File out = null;
	private File in = null;
	private Boolean saved = false;
	/**
	 * Class constructor
	 * 
	 * 
	 * @param String title
	 * @param String[] test cases
	 * @return none
	 */
	public TestLogger(String title, String[] testCases){
		this.title = title;
		this.testCase = testCases;
		//System.out.println(buildEmbedString(this.testCase));
	}
	
	/**
	 * Class constructor (overloaded)
	 * 
	 * 
	 * @param String title
	 * @param String[] test cases
	 * @param File fp
	 * @return none
	 */
	public TestLogger(String title, String[] testCases, File fp) {
		this.title = title;
		this.testCase = testCases;
		this.in = fp;
	}
	
	/**
	 * Show the GUI
	 * 
	 * @param none
	 * @return none
	 */
	@SuppressWarnings("serial")
	public void showUI(){
		//Boolean saved = false;
		ImageIcon h_well_img = new ImageIcon("resources/honeywell-sec-scaled-50-44.png");
		this.setIconImage(h_well_img.getImage());
		this.setTitle("Test Log");
		
		/* kill on frame exit */
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					e.getWindow().dispose();
					System.exit(0);
				}
		});

		TestLogTabbed tabPane;
		/* Pack and show the frame */
		if(this.in == null) {
			tabPane = new TestLogTabbed(this.title, this.testCase);
			tabPane.logPanel.logText.append(this.title+" Test Log Started: "+this.currentDate.format(new Date())+'@'+"<"+buildEmbedString(TestLogger.this.testCase)+">"+'\n');
			
		}else {
			tabPane = new TestLogTabbed(this.title, this.testCase, this.in);
			tabPane.logPanel.logText.append(this.title+" Test Log Continued: "+this.currentDate.format(new Date())+'\n');
		}

		this.setLocationRelativeTo(null);
		this.setResizable(false);

		tabPane.configPanel.notSaved.setVisible(false);
		this.setContentPane(tabPane);
		this.pack();
		this.setVisible(true);
		
		/**
		 * Config tab Test Case text field listener
		 * 
		 * Sets configPanel.notSaved to visible and this.saved to false
		 */
		tabPane.configPanel.tcTextField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				notifyUser();	
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				notifyUser();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				notifyUser();
			}
			
			public void notifyUser() {
				tabPane.configPanel.notSaved.setVisible(true);
				saved = false;
			}
		});
		
		/**
		 * Config tab Test Commands text field listener
		 * 
		 * Sets configPanel.notSaved to visible and this.saved to false
		 */
		tabPane.configPanel.cmdsTextField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				notifyUser();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				notifyUser();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				notifyUser();
			}
			
			public void notifyUser() {
				tabPane.configPanel.notSaved.setVisible(true);
				saved = false;
			}
		});
		
		/**
		 * Config tab clear button listener
		 * 
		 * Shows message box to select which text area to clear
		 */
		tabPane.configPanel.clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String[] options = {"","Test Cases","Test Commands"};
				Object clearThis = JOptionPane.showInputDialog(null, null, "Clear What? ",
						JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				if(clearThis != null) {
					if(clearThis.toString().equalsIgnoreCase("Test Cases")) {
						tabPane.configPanel.tcTextField.setText("");
						saved = false;
						//System.out.println(clearThis);
					}else if(clearThis.toString().equalsIgnoreCase("Test Commands")) {
						tabPane.configPanel.cmdsTextField.setText("");
						saved = false;
						//System.out.println(clearThis);
					}
				}
			}
		});
		
		/**
		 * Log tab exit button listener
		 * 
		 * Saves log content if not already saved
		 */
		tabPane.logPanel.exitLog.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				if(!tabPane.logPanel.logText.getText().equals("") && !saved) {
					int res = JOptionPane.showConfirmDialog(tabPane.logPanel, "Do you want to save the current log?", "Test Log", JOptionPane.YES_NO_OPTION);
					if(res == 0) {
						tabPane.logPanel.logText.append(TestLogger.this.title+" Test Log Finished: "+TestLogger.this.currentDate.format(new Date())+'\n');
						if(TestLogger.this.out != null) {
							save(tabPane.logPanel.logText);
						}else {
							saveAs(TestLogger.this, tabPane.logPanel.logText);
						}
						saved = true;
					}
				}
				TestLogger.this.dispose();
				System.exit(0);
			}
		});
		
		/**
		 * Log tab new log button listener
		 * 
		 * Saves log content if not already saved and prints log header to new log
		 */
		tabPane.logPanel.newLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if(!tabPane.logPanel.logText.getText().equals("") && !saved) {
					int res = JOptionPane.showConfirmDialog(tabPane.logPanel, "Do you want to save the current log?", "Test Log", JOptionPane.YES_NO_OPTION);
					if(res == 0) {
						tabPane.logPanel.logText.append(TestLogger.this.title+" Test Log Finished: "+TestLogger.this.currentDate.format(new Date())+'\n');
						if(TestLogger.this.out != null) {
							save(tabPane.logPanel.logText);
						}else {
							saveAs(TestLogger.this, tabPane.logPanel.logText);
						}
						saved = true;
					}else {
						TestLogger.this.out = null;
					}
					tabPane.logPanel.logText.setText("");
					tabPane.logPanel.logText.append(TestLogger.this.title+" Test Log Started: "+TestLogger.this.currentDate.format(new Date())+'@'+"<"+buildEmbedString(TestLogger.this.testCase)+">"+'\n');
					tabPane.logPanel.newLogEntry.setText("");
					tabPane.logPanel.testCaseSelection.setSelectedIndex(0);
					saved = false;
				}
			}
		});
		
		/**
		 * Log tab save button listener
		 */
		tabPane.logPanel.saveLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if(!saved) {
					if(TestLogger.this.out != null) {
						save(tabPane.logPanel.logText);
					}else {
						saveAs(TestLogger.this, tabPane.logPanel.logText);
					}
					saved = true;
				}
			}
		});
		
		/**
		 * Log tab new log entry listener
		 */
		tabPane.logPanel.newLogEntry.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				if(tabPane.logPanel.testCaseSelection.getSelectedIndex() != 0) {
					tabPane.logPanel.logText.append("["+TestLogger.this.currentDate.format(new Date())+"] ["+tabPane.logPanel.testCaseSelection.getSelectedItem().toString()+"] "
							+tabPane.logPanel.newLogEntry.getText()+'\n');
					tabPane.logPanel.newLogEntry.setText("");
					saved = false;
				}else{
					JOptionPane.showMessageDialog(tabPane.logPanel, "You need to select a test case", "Test Log", JOptionPane.OK_OPTION);
				}
			}
		});
	}
	
	/**
	 * Builds comma separated string representation of string array
	 * @param String[] arr
	 * @return String 
	 */
	private String buildEmbedString(String[] arr) {
		String embedStr = "";
		for(int i = 0; i<arr.length; i++) {
			if(i == arr.length-1){
				embedStr += arr[i];
			}else{
				embedStr += arr[i]+',';
			}
		}
		return embedStr;
	}
	
	/**
	 * Save to already chosen file descriptor 
	 * 
	 * @param JTestArea to save
	 * @return none
	 * @exception IOException
	 */
	private void save(JTextArea text) {
		try(BufferedWriter fileOut = new BufferedWriter(new FileWriter(this.out))){
			text.write(fileOut);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get and return file descriptor 
	 * 
	 * @param JFrame frame to model
	 * @param JTextArea to save
	 * @exception IOException
	 */
	private void saveAs(JFrame frame, JTextArea text) {
		JFileChooser fileChooser = new JFileChooser();
		if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
		  this.out = fileChooser.getSelectedFile();
		  try(BufferedWriter fileOut = new BufferedWriter(new FileWriter(this.out))){
			  text.write(fileOut);
		  } catch (IOException e) {
			e.printStackTrace();
		  }
		}
	}
}
