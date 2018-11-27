package src.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;

import src.panels.NewLogPanel;

/**
 * Initial landing frame.  Main calls Entry.java frame which adds NewLogPanel.java panel
 * 
 *  @author Jacob Loden
 */
public class Entry extends JFrame{

	private static final long serialVersionUID = -3589089200466664223L;
	public NewLogPanel newLog;
	
	/**
	 * Show landing GUI
	 * 
	 * @param None
	 * @return None
	 */
	public void showUI(){
		/*
		 * Initial frame view with mode select panel
		 */
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
		
		this.newLog = new NewLogPanel();
		
		/* Pack and show frame */
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setContentPane(newLog);
		this.pack();
		this.setVisible(true);
		
		/* Action listeners */
		this.newLog.cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				Entry.this.dispose();
				System.exit(0);
			}
		});
		
		this.newLog.ok.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				if(newLog.projTitleText.getText().equals("") || newLog.testCaseText.getText().equals("")){
					newLog.errorMsg.setText("Project title/Test cases missing");
					newLog.errorMsg.setVisible(true);
					repackFrame();
				}else if(!newLog.testCaseText.getText().contains(",") && !newLog.testCaseText.getText().contains(" ")){
					newLog.errorMsg.setText("Enter test cases as comma separated list");
					newLog.errorMsg.setVisible(true);
					repackFrame();
				}else{
					Entry.this.dispose();
					launchLogger(newLog.projTitleText.getText(), getOpts(newLog.testCaseText.getText().split(",")), null);
				}
			}
		});
		
		this.newLog.projTitleText.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e) {
				newLog.errorMsg.setText("");
				newLog.errorMsg.setVisible(false);
				repackFrame();
				
			}
			@Override
			public void focusLost(FocusEvent e) {
				
			}
		});
		
		this.newLog.testCaseText.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e) {
				newLog.errorMsg.setText("");
				newLog.errorMsg.setVisible(false);
				repackFrame();
				
			}
			@Override
			public void focusLost(FocusEvent e) {
				
			}
		});
		
		this.newLog.open.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				JFileChooser fileChooser = new JFileChooser();
				if (fileChooser.showOpenDialog(newLog) == JFileChooser.APPROVE_OPTION) {
				  File file = fileChooser.getSelectedFile();
				  if(checkFile(file)) {
					  String[] tc = getInputFileTestCases(file);
					  if(tc != null) {
						  launchLogger(newLog.projTitleText.getText(), tc, file);
					  }
				  }else {
					  JOptionPane.showMessageDialog(newLog, "File not in proper format", "Test Log", JOptionPane.OK_OPTION);
				  }
				}
			}
		});
	}
	
	/**
	 * Get test cases from input file
	 * 
	 * @param File file pointer to 
	 * @return String[] formatted test cases
	 */	
	private String[] getInputFileTestCases(File fp) {
		try {
			BufferedReader input = new BufferedReader(new FileReader(fp));
			if(fp.getAbsolutePath().endsWith(".txt")) {
				String last = "", line;
				String[] testLine;
			    while ((line = input.readLine()) != null) { 
			        last = line;
			    }
			    testLine = last.split("\t");
			    if(testLine.length > 1) {
			    	if(testLine[1].contains("<") && testLine[1].contains(">")) {
			    		String[] ret = testLine[1].split(",");
			    		for(int i = 0; i<ret.length; i++) {
			    			ret[i] = ret[i].replaceAll("<", "");
			    			ret[i] = ret[i].replaceAll(">", "");
			    		}
			    		return ret;
			    	}
			    }
			}
		} catch (IOException e) {
			
		}
		return null;
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
				String last = "", line;
				String[] testLine;
			    while ((line = input.readLine()) != null) { 
			        last = line;
			    }
			    testLine = last.split("\t");
			    if(testLine.length > 1) {
			    	if(testLine[1].contains("<") && testLine[1].contains(">")) {
			    		return true;
			    	}
			    }
			}
		} catch (IOException e) {
			
		}
		return false;
	}
	
	/**
	 * Format test case array
	 * 
	 * @param String[] user test cases
	 * @return String[] formatted test cases
	 */
	private String[] getOpts(String[] woOpts) {
		String[] newOpts = new String[woOpts.length+2];
		newOpts[0] = "-Select-";
		newOpts[1] = "Note";
		int j = 2;
		for(int i = 0; i<woOpts.length; i++) {
			newOpts[j] = woOpts[i].replaceFirst("^\\s*", "");
			j++;
		}
		return newOpts;
	}
	/**
	 * Re-pack frame
	 * 
	 * @param none
	 * @return none
	 */
	private void repackFrame(){
		this.pack();
		this.validate();
	}
	/**
	 * Gets String of installed LAF based on selection String passed as parameter
	 * 
	 * @param String lafString
	 * @return String info.getClassName from OS
	 */
	public static String getLookAndFeelClassName(String lafString){
		LookAndFeelInfo[] plafs = UIManager.getInstalledLookAndFeels();
		for(LookAndFeelInfo info : plafs){
			if(info.getName().contains(lafString)){
				return info.getClassName();
			}
		}
		return null;
	}
	/**
	 * Deploys UI in AWT event-dispatching thread with selected LAF
	 * 
	 * @param String title
	 * @param String[] test cases
	 * @return none
	 */
	private void launchLogger(String title, String[] testCases, File fp){
		final String lafClassName = getLookAndFeelClassName("Nimbus");
		
		SwingUtilities.invokeLater(new Runnable(){	// launch UI in AWT event-dispatching thread
			public void run(){
				try{
					UIManager.setLookAndFeel(lafClassName);
				}catch(ClassNotFoundException ex){
					Logger.getLogger(Entry.class.getName()).log(Level.SEVERE, null, ex);
				}catch(InstantiationException ex){
					Logger.getLogger(Entry.class.getName()).log(Level.SEVERE, null, ex);
				}catch(IllegalAccessException ex){
					Logger.getLogger(Entry.class.getName()).log(Level.SEVERE, null, ex);
				}catch(UnsupportedLookAndFeelException ex){
					Logger.getLogger(Entry.class.getName()).log(Level.SEVERE, null, ex);
				}
				if(fp == null) {
					new TestLogger(title, testCases).showUI();
				}else {
					new TestLogger(title, testCases, fp).showUI();
				}
				
			}
		});
	}
}
