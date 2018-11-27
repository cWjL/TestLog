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

import src.panels.TestLogPanel;

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
	 * Class constructor
	 * 
	 * 
	 * @param String title
	 * @param String[] test cases
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
		TestLogPanel runPanel;
		/* Pack and show the frame */
		if(this.in == null) {
			runPanel = new TestLogPanel(this.title, this.testCase);
		}else {
			runPanel = new TestLogPanel(this.title, this.testCase, this.in);
		}

		runPanel.logText.append(this.title+" Test Log Started: "+this.currentDate.format(new Date())+'\n');
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setContentPane(runPanel);
		this.pack();
		this.setVisible(true);

		/* Action Listeners */
		runPanel.exitLog.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				if(!runPanel.logText.getText().equals("")) {
					int res = JOptionPane.showConfirmDialog(runPanel, "Do you want to save the current log?", "Test Log", JOptionPane.YES_NO_OPTION);
					if(res == 0) {
						runPanel.logText.append(TestLogger.this.title+" Test Log Finished: "+TestLogger.this.currentDate.format(new Date())
							+'@'+"<"+buildEmbedString(TestLogger.this.testCase)+">"+'\n');
					}else{
						runPanel.logText.append(TestLogger.this.title+" Test Log Closed: "+TestLogger.this.currentDate.format(new Date())
							+'@'+"<"+buildEmbedString(TestLogger.this.testCase)+">"+'\n');
					}
					if(TestLogger.this.out != null) {
						save(runPanel.logText);
					}else {
						saveAs(TestLogger.this, runPanel.logText);
					}
				}
				TestLogger.this.dispose();
				System.exit(0);
			}
		});
		
		runPanel.newLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if(!runPanel.logText.getText().equals("")) {
					int res = JOptionPane.showConfirmDialog(runPanel, "Do you want to save the current log?", "Test Log", JOptionPane.YES_NO_OPTION);
					if(res == 0) {
						runPanel.logText.append(TestLogger.this.title+" Test Log Finished: "+TestLogger.this.currentDate.format(new Date())
							+'@'+"<"+buildEmbedString(TestLogger.this.testCase)+">"+'\n');
						if(TestLogger.this.out != null) {
							save(runPanel.logText);
						}else {
							saveAs(TestLogger.this, runPanel.logText);
						}
					}else {
						TestLogger.this.out = null;
					}
					runPanel.logText.setText("");
					runPanel.logText.append(TestLogger.this.title+" Test Log Started: "+TestLogger.this.currentDate.format(new Date())+'\n');
					runPanel.newLogEntry.setText("");
					runPanel.testCaseSelection.setSelectedIndex(0);
				}
			}
		});
		
		runPanel.saveLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if(TestLogger.this.out != null) {
					save(runPanel.logText);
				}else {
					saveAs(TestLogger.this, runPanel.logText);
				}
			}
		});
		
		runPanel.newLogEntry.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				if(runPanel.testCaseSelection.getSelectedIndex() != 0) {
					runPanel.logText.append("["+TestLogger.this.currentDate.format(new Date())+"] ["+runPanel.testCaseSelection.getSelectedItem().toString()+"] "
							+runPanel.newLogEntry.getText()+'\n');
					runPanel.newLogEntry.setText("");
				}else{
					JOptionPane.showMessageDialog(runPanel, "You need to select a test case", "Test Log", JOptionPane.OK_OPTION);
				}
			}
		});
	}
	
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
