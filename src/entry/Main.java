package src.entry;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;

import src.frames.Entry;

/**
 * Entry main.
 * 
 *  @author Jacob Loden
 *  @version 1.0
 */
public class Main {
	private static final String VERSION = "1.0";
	
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

	/**
	 * Entry main.  Deploys UI in AWT event-dispatching thread with selected LAF
	 * 
	 * @param args
	 * @return none
	 */
	public static void main(String[] args){
		/**
		 * Print version number and exit 
		 */
		if(args.length > 0){
			if(args[0].equals("--version") || args[0].equals("-v")){
				System.out.println(VERSION);
				System.exit(0);
			}
		}
		
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
