package src.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

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
	public TestLogger(String title){
		this.title = title;
		this.testCase = new String[]{"-Select-", "Note"};
	}
	
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
		this.setIconImage(new ImageIcon(getClass().getResource("/src/resources/h_well_frame_icon.png")).getImage());
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

		tabPane.configPanel.notSaved.setVisible(false);
		tabPane.logPanel.notSaved.setVisible(false);
		this.setResizable(false);		
		this.setContentPane(tabPane);
		this.pack();
		this.setLocationRelativeTo(null);
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
		 * Log tab log text field listener
		 * 
		 * Sets logPanel.notSaved to visible and this.saved to false
		 */
		tabPane.logPanel.logText.getDocument().addDocumentListener(new DocumentListener(){

			@Override
			public void insertUpdate(DocumentEvent e) {
				notifyUser();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				notifyUser();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				notifyUser();
			}
			
			public void notifyUser() {
				tabPane.logPanel.notSaved.setVisible(true);
				saved = false;
			}
		});
		
		/**
		 * Config tab save button listener
		 */
		tabPane.configPanel.saveButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				if(!tabPane.configPanel.cmdsTextField.getText().equals("")){
					Vector<String> newCMD = new Vector<String>(Arrays.asList(tabPane.configPanel.cmdsTextField.getText().split("\\r?\\n")));
					Vector<String> oldCMD = new Vector<String>();
					for(int i = 1; i < tabPane.logPanel.testCommandSelection.getItemCount(); i++){
						oldCMD.add(tabPane.logPanel.testCommandSelection.getItemAt(i));
					}
					
					if(!oldCMD.equals(newCMD)){
						int j = 0;
						oldCMD.clear();
						oldCMD.add(tabPane.logPanel.testCommandSelection.getItemAt(j++));
						oldCMD.addAll(j, newCMD);
						tabPane.logPanel.testCommandSelection.removeAllItems();
						for(int i = 0; i<oldCMD.size(); i++){
							tabPane.logPanel.testCommandSelection.addItem(oldCMD.get(i));
						}
					}
				}else{
					Vector<String> oldTC = new Vector<String>();
					oldTC.add(tabPane.logPanel.testCommandSelection.getItemAt(0));
					tabPane.logPanel.testCommandSelection.removeAllItems();
					tabPane.logPanel.testCommandSelection.addItem(oldTC.get(0));
				}
				
				if(!tabPane.configPanel.tcTextField.getText().equals("")){
					Vector<String> newTC;
					Vector<String> oldTC = new Vector<String>();
					for(int i = 2; i < tabPane.logPanel.testCaseSelection.getItemCount(); i++){
						oldTC.add(tabPane.logPanel.testCaseSelection.getItemAt(i));
					}
					newTC = new Vector<String>(Arrays.asList(tabPane.configPanel.tcTextField.getText().split("\n")));

					if(!oldTC.equals(newTC)){
						int j = 0;
						oldTC.clear();
						oldTC.add(tabPane.logPanel.testCaseSelection.getItemAt(j++));
						oldTC.add(tabPane.logPanel.testCaseSelection.getItemAt(j++));
						oldTC.addAll(j, newTC);
						tabPane.logPanel.testCaseSelection.removeAllItems();
						for(int i = 0; i<oldTC.size(); i++){
							tabPane.logPanel.testCaseSelection.addItem(oldTC.get(i));
						}
					}
				}else{
					Vector<String> oldTC = new Vector<String>();
					oldTC.add(tabPane.logPanel.testCaseSelection.getItemAt(0));
					oldTC.add(tabPane.logPanel.testCaseSelection.getItemAt(1));
					tabPane.logPanel.testCaseSelection.removeAllItems();
					tabPane.logPanel.testCaseSelection.addItem(oldTC.get(0));
					tabPane.logPanel.testCaseSelection.addItem(oldTC.get(1));
				}
				tabPane.validate();
				tabPane.configPanel.notSaved.setVisible(false);
			}
		});
		
		/**
		 * Log tab add button listener
		 */
		tabPane.logPanel.addTestCmd.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				if(tabPane.logPanel.testCommandSelection.getSelectedIndex() == 0){
					JOptionPane.showMessageDialog(tabPane.logPanel, "You need to select a command", "Test Log", JOptionPane.OK_OPTION);
				}else if(tabPane.logPanel.testCaseSelection.getSelectedIndex() == 0){
					JOptionPane.showMessageDialog(tabPane.logPanel, "You need to select a test case", "Test Log", JOptionPane.OK_OPTION);
				}else{
					tabPane.logPanel.logText.append("[ "+TestLogger.this.currentDate.format(new Date())+" ] [ "+tabPane.logPanel.testCaseSelection.getSelectedItem().toString()+" ] "
							+"[ CMD ] "+tabPane.logPanel.testCommandSelection.getSelectedItem().toString()+'\n');
					tabPane.logPanel.testCommandSelection.setSelectedIndex(0);
					saved = false;
				}
			}
		});
		
		/**
		 * Log tab export button listener
		 */
		tabPane.logPanel.export.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				HSSFWorkbook workbook = new HSSFWorkbook();
				HSSFSheet sheet = workbook.createSheet("Commands");
				Map<String, Object[]> cmds = new HashMap<String, Object[]>();
				Boolean cmdFound = false;
				int i = 1;
				
				cmds.put(Integer.toString(i), new Object[] {"Test Case", "Date/Time", "Command"});
				for(String line : tabPane.logPanel.logText.getText().split("\\n")) {
					if(!line.contains("@") && !line.contains("Test Log Continued")) {
						String cmd;
						//System.out.println(line.substring((line.lastIndexOf("[")+1), line.lastIndexOf("]")).trim());
						if(line.substring((line.lastIndexOf("[")+1), line.lastIndexOf("]")).trim().equals("CMD")) {
							i++;
							cmd = line.substring(line.lastIndexOf("]")+2);
							String tc = line.substring((line.indexOf("[", line.indexOf("[")+1)+2), (line.indexOf("]", line.indexOf("]")+1)));
							String dateTime = line.substring((line.indexOf("[")+2), (line.indexOf("]")-1));
							cmds.put(Integer.toString(i), new Object[] {tc, dateTime, cmd});
							
							Set<String> keyset = cmds.keySet();
							int rownum = 0;
							for(String key : keyset) {
								Row row = sheet.createRow(rownum++);
								Object[] objarr = cmds.get(key);
								int cellnum = 0;
								for(Object obj : objarr) {
									Cell cell = row.createCell(cellnum++);
									if(obj instanceof Date) 
										cell.setCellValue((Date)obj);
									else if(obj instanceof Boolean)
										cell.setCellValue((Boolean)obj);
									else if(obj instanceof String)
										cell.setCellValue((String)obj);
									else if(obj instanceof Double)
										cell.setCellValue((Double)obj);
								}
							}
							try {
								FileOutputStream out = new FileOutputStream(new File(TestLogger.this.title+".xls"));
								workbook.write(out);
								out.close();
								workbook.close();
								cmdFound = true;
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
				if(!cmdFound) {
					JOptionPane.showMessageDialog(tabPane.logPanel, "No test commands found in log", "Test Log", JOptionPane.OK_OPTION);
				}else {
					JOptionPane.showMessageDialog(tabPane.logPanel, "Export complete", "Test Log", JOptionPane.OK_OPTION);
				}
			}
		});
		
		/**
		 * Config tab import button listener
		 */
		tabPane.configPanel.importButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				String[] options = {"Test Cases","Test Commands"};
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						Object importThis;
						importThis = JOptionPane.showInputDialog(null, null, "Import What? ",
								JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
						if(importThis != null) {
							JFileChooser fc = new JFileChooser();
							if(fc.showOpenDialog(tabPane) == JFileChooser.APPROVE_OPTION) {
								File file = fc.getSelectedFile();
								if(checkFile(file)) {
									Frame frame = Frame.getFrames()[0];
								    final JDialog loading = new JDialog(frame);
								    JLabel waitLabel = new JLabel("Please wait...");
								    waitLabel.setForeground(Color.RED);
								    waitLabel.setFont(new Font("",Font.PLAIN, 20));
								    JPanel p1 = new JPanel(new BorderLayout(10,10));
								    p1.add(waitLabel, BorderLayout.CENTER);
								    loading.setUndecorated(true);
								    loading.getContentPane().add(p1);
								    loading.pack();
								    loading.setLocationRelativeTo(tabPane);
								    loading.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
								    loading.setModal(true);
									if(importThis.toString().equalsIgnoreCase("Test Cases")) {
									    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
									    	String[] newcmds;
									        @Override
									        protected Void doInBackground() throws InterruptedException {
									        	newcmds = getTestCommands(file);
									        	return null;
									        }
									        @Override
									        protected void done() {
									        	if(newcmds != null) {
									        		for(int i = 0; i < newcmds.length; i++) {
									        			tabPane.configPanel.tcTextField.append(newcmds[i]+'\n');
									        		}
									        	}
									            loading.dispose();
									        }
									    };
									    worker.execute();
									    loading.setVisible(true);
									    try {
									        worker.get();
									    } catch (Exception e1) {
									        e1.printStackTrace();
									    }
										
									}else if(importThis.toString().equalsIgnoreCase("Test Commands")) {
									    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
									    	String[] newcmds;
									        @Override
									        protected Void doInBackground() throws InterruptedException {
									        	newcmds = getTestCommands(file);
									        	return null;
									        }
									        @Override
									        protected void done() {
									        	if(newcmds != null) {
									        		for(int i = 0; i < newcmds.length; i++) {
									        			tabPane.configPanel.cmdsTextField.append(newcmds[i]+'\n');
									        		}
									        	}
									            loading.dispose();
									        }
									    };
									    worker.execute();
									    loading.setVisible(true);
									    try {
									        worker.get();
									    } catch (Exception e1) {
									        e1.printStackTrace();
									    }
									}
								}
							}
						}
					}
				});
				tabPane.validate();
			}
		});
		
		/**
		 * Config tab drag and drop
		 */
		//tabPane.configPanel.cmdsTextField
		
		/**
		 * Config tab clear button listener
		 * 
		 * Shows message box to select which text area to clear
		 */
		tabPane.configPanel.clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String[] options = {"Test Cases","Test Commands"};
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						Object clearThis = JOptionPane.showInputDialog(null, null, "Clear What? ",
								JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
						if(clearThis != null) {
							if(clearThis.toString().equalsIgnoreCase("Test Cases")) {
								tabPane.configPanel.tcTextField.setText("");
								tabPane.logPanel.notSaved.setVisible(false);
								saved = false;
								//System.out.println(clearThis);
							}else if(clearThis.toString().equalsIgnoreCase("Test Commands")) {
								tabPane.configPanel.cmdsTextField.setText("");
								tabPane.logPanel.notSaved.setVisible(false);
								saved = false;
							}
						}
					}
				});
				tabPane.validate();
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
					tabPane.logPanel.notSaved.setVisible(false);
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
					tabPane.logPanel.logText.append("[ "+TestLogger.this.currentDate.format(new Date())+" ] [ "+tabPane.logPanel.testCaseSelection.getSelectedItem().toString()+" ] "
							+tabPane.logPanel.newLogEntry.getText().replaceAll("\\[", "|").replaceAll("\\]", "|")+'\n');
					tabPane.logPanel.newLogEntry.setText("");
					saved = false;
				}else{
					JOptionPane.showMessageDialog(tabPane.logPanel, "You need to select a test case", "Test Log", JOptionPane.OK_OPTION);
				}
			}
		});
	}
	
	/**
	 * Retrieves test commands from file
	 * 
	 * @param File file
	 * @return String[] test commands
	 */
	private String[] getTestCommands(File fp) {
		String[] nope = {"ls", "ls -l", "ls -al",
				"cd", "dir", "mkdir",
				"cp", "pwd","whoami",
				"man", "exit", "cat",
				"touch", "emacs", "uname",
				"clear", "ip", "service",
				"rm", "ipconfig", "ifconfig",
				"apt", "chmod", "gedit"};
		Vector<String> newCMDS = new Vector<String>();
		try (BufferedReader input = new BufferedReader(new FileReader(fp))){
			String line;
			while((line = input.readLine()) != null) {
				Boolean found = false;
				for(int i = 0; i < nope.length; i++) {
					if(line.contains(nope[i])) {
						found = true;
					}
				}
				if(!found) {
					newCMDS.add(line.replaceAll("\\[", "|").replaceAll("\\]", "|"));
				}
			}
			input.close();
			Collections.sort(newCMDS);
			return newCMDS.toArray(new String[newCMDS.size()]);
		}catch (IOException e) {
			
		}
		
		return null;
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
	
	/**
	 * Check input file format
	 * 
	 * @param File file pointer to 
	 * @return boolean file is valid
	 */	
	private boolean checkFile(File fp) {
		try {
			BufferedReader input = new BufferedReader(new FileReader(fp));
			if(fp.getAbsolutePath().endsWith(".txt")) {
				input.close();
				return true;
			}
			input.close();
		} catch (IOException e) {

		}
		return false;
	}
}
