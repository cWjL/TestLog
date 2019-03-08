package src.panels;

import java.io.File;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
/**
 * Main application tabbed pane
 * 
 * @author Jacob Loden
 */
public class TestLogTabbed extends JTabbedPane{
	private static final String LOG_LABEL = "Log";
	private static final String CONFIG_LABEL = "Config";
	public TestLogPanel logPanel;
	/**
	 * Panel constructor
	 * 
	 *  @param String title
	 *  @param String[] test cases
	 *  @return none
	 */
	public TestLogTabbed(String title, String[] testCases){
		this.logPanel = new TestLogPanel(title, testCases);
		this.add(LOG_LABEL, this.logPanel);
		this.add(CONFIG_LABEL, this.logPanel);
		
	}
	/**
	 * Panel constructor
	 * 
	 *  @param String title
	 *  @param String[] test cases
	 *  @param File fp
	 *  @return none
	 */
	public TestLogTabbed(String title, String[] testCases, File fp){
		this.logPanel = new TestLogPanel(title, testCases, fp);
		this.add(LOG_LABEL, this.logPanel);
		this.add(CONFIG_LABEL, this.logPanel);		
	}
	
	/**
	 * Show main application pane 
	 * 
	 * @param none
	 * @return none
	 */
	public void showPane(){
		
	}
}
