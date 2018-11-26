package frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;

import panels.NewLogPanel;

/**
 * Initial landing frame.  Main calls Entry.java frame which adds NewLogPanel.java panel
 * 
 *  @author Jacob Loden
 */
public class Entry extends JFrame{

	private static final long serialVersionUID = -3589089200466664223L;
	
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
		
		NewLogPanel newLog = new NewLogPanel();
		
		/* Pack and show frame */
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setContentPane(newLog);
		this.pack();
		this.setVisible(true);
		
		/* Action listeners */
		newLog.cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				Entry.this.dispose();
				System.exit(0);
			}
		});
		
		newLog.ok.addActionListener(new ActionListener(){
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
					String[] woOpts = newLog.testCaseText.getText().split(",");
					String[] withOpts = new String[woOpts.length+2];
					withOpts[0] = "-Select-";
					withOpts[1] = "Note";
					int j = 2;
					for(int i = 0; i<woOpts.length; i++) {
						withOpts[j] = woOpts[i];
						j++;
					}
					launchLogger(newLog.projTitleText.getText(), withOpts);
				}
			}
		});
		
		newLog.projTitleText.addFocusListener(new FocusListener(){
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
		
		newLog.testCaseText.addFocusListener(new FocusListener(){
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
		
		newLog.open.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				
			}
		});
	}
	/**
	 * Re-pack frame
	 * 
	 * @param none
	 * @return none
	 */
	public void repackFrame(){
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
	public void launchLogger(String title, String[] testCases){
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
				new TestLogger(title, testCases).showUI();;
			}
		});
	}
}
