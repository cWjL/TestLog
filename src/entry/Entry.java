package entry;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.table.DefaultTableCellRenderer;

import panels.LandingPanel;

public class Entry extends JFrame{
	
	private final Dimension SEL_SIZE = new Dimension(500, 300);
	private final Dimension FRAME_SIZE = new Dimension(600, 400);
	
	/**
	 * Gets String of installed LAF based on selection String passed as parameter
	 * 
	 * @param lafString
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
	
	/*
	 * Show GUI method
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
		
		/*kill on frame exit*/
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					
					e.getWindow().dispose();
					System.exit(0);
				}
		});
		
		this.setLocationRelativeTo(null);
		this.setPreferredSize(SEL_SIZE);
		this.setResizable(false);
		LandingPanel landPanel = new LandingPanel();
		this.setContentPane(landPanel);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
		
	}
	
	/**
	 * Entry main.  Deploys UI in AWT event-dispatching thread with selected LAF
	 * @param args
	 */
	public static void main(String[] args){
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
				new Entry().showUI();
			}
		});
	}

}
