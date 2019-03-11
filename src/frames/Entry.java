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
	private String oldTitle = null;
	
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
				if(newLog.projTitleText.getText().equals("")){
					newLog.errorMsg.setText("Project title missing");
					newLog.errorMsg.setVisible(true);
					repackFrame();
				}else if(!newLog.testCaseText.getText().isEmpty() && !newLog.testCaseText.getText().contains(",") && !newLog.testCaseText.getText().contains(" ")){
					newLog.errorMsg.setText("Enter test cases as comma separated list");
					newLog.errorMsg.setVisible(true);
					repackFrame();
				}else{
					Entry.this.dispose();
					launchLogger(newLog.projTitleText.getText().replaceAll("@", ""), getOpts(newLog.testCaseText.getText().replaceAll("@", "").split(",")), null);
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
						  Entry.this.dispose();
						  launchLogger(Entry.this.oldTitle, tc, file);
					  }
				  }else {
					  JOptionPane.showMessageDialog(newLog, "File not in proper format. Try again", "Test Log", JOptionPane.OK_OPTION);
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
				String line = "";
				String[] testLine;
				line = input.readLine();
			    if(line != null) {
			    	testLine = line.split("@");
				    if(testLine.length > 1) {
				    	if(testLine[1].contains("<") && testLine[1].contains(">")) {
				    		String[] ret = testLine[1].split(",");
				    		this.oldTitle = getPreviousTitle(testLine[0]); 
				    		for(int i = 0; i<ret.length; i++) {
				    			ret[i] = ret[i].replaceAll("<", "");
				    			ret[i] = ret[i].replaceAll(">", "");
				    		}
				    		input.close();
				    		return ret;
				    	}
				    }
			    }
			}
			input.close();
		} catch (IOException e) {

		}
		return null;
	}
	
	/**
	 * Get project title from input file
	 * 
	 * @param String first half of input file footer
	 * @return String project title
	 */	
	private String getPreviousTitle(String splitStr){
		return splitStr.substring(0, splitStr.indexOf("Test"));
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
				String line = "";
				String[] testLine;
				line = input.readLine();
				if(line != null) {
					testLine = line.split("@");
					if(testLine.length > 1) {
				    	if(testLine[1].contains("<") && testLine[1].contains(">")) {
				    		input.close();
				    		return true;
				    	}
				    }
				}
			}
			input.close();
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
		String[] newOpts;
		if(woOpts.length == 1 && woOpts[0].equals("")){
			newOpts = new String[woOpts.length+1];
		}else{
			newOpts = new String[woOpts.length+2];
		}
		newOpts[0] = "-Select-";
		newOpts[1] = "Note";
		if(newOpts.length > 2){
			int j = 2;
			for(int i = 0; i<woOpts.length; i++) {
				newOpts[j] = woOpts[i].replaceFirst("^\\s*", "");
				j++;
			}			
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
