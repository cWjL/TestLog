package frames;

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

import panels.TestLogPanel;

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
	}
	
	/**
	 * Show the GUI
	 * 
	 * @param none
	 * @return none
	 */
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
		
		/* Pack and show the frame */
		TestLogPanel runPanel = new TestLogPanel(this.title, this.testCase);
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
						if(TestLogger.this.out != null) {
							save(runPanel.logText);
						}else {
							saveAs(TestLogger.this, runPanel.logText);
						}
					}else {
						TestLogger.this.dispose();
						System.exit(0);
					}
				}
			}
		});
		
		runPanel.newLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if(!runPanel.logText.getText().equals("")) {
					int res = JOptionPane.showConfirmDialog(runPanel, "Do you want to save the current log?", "Test Log", JOptionPane.YES_NO_OPTION);
					if(res == 0) {
						if(TestLogger.this.out != null) {
							save(runPanel.logText);
						}else {
							saveAs(TestLogger.this, runPanel.logText);
						}
					}else {
						TestLogger.this.out = null;
					}
					runPanel.logText.setText("");
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
				Date date = new Date();
				runPanel.logText.append("["+TestLogger.this.currentDate.format(date)+"] ["+runPanel.testCaseSelection.getSelectedItem().toString()+"] "
						+runPanel.newLogEntry.getText()+'\n');
				runPanel.newLogEntry.setText("");
			}
		});
	}
	
	/**
	 * Save to already chosen file descriptor 
	 * 
	 * @param JTestArea to save
	 * @return none
	 * @exception IOException
	 */
	public void save(JTextArea text) {
		try(BufferedWriter fileOut = new BufferedWriter(new FileWriter(this.out))){
			text.write(fileOut);
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
	public void saveAs(JFrame frame, JTextArea text) {
		JFileChooser fileChooser = new JFileChooser();
		if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
		  this.out = fileChooser.getSelectedFile();
		  try(BufferedWriter fileOut = new BufferedWriter(new FileWriter(this.out))){
			  text.write(fileOut);
		  } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		  }
		}
	}
}
