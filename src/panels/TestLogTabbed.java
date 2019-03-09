package src.panels;

import java.io.File;
import javax.swing.JTabbedPane;
/**
 * Main application tabbed pane
 * 
 * @author Jacob Loden
 */
public class TestLogTabbed extends JTabbedPane{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String LOG_LABEL = "Log";
	private static final String CONFIG_LABEL = "Config";
	public TestLogPanel logPanel;
	public TestLogConfigPanel configPanel;
	
	/**
	 * Panel constructor
	 * 
	 *  @param String title
	 *  @return none
	 */
	public TestLogTabbed(String title){
		this.logPanel = new TestLogPanel(title);
		this.configPanel = new TestLogConfigPanel();
		this.add(LOG_LABEL, this.logPanel);
		this.add(CONFIG_LABEL, this.configPanel);
		
	}
	/**
	 * Panel constructor
	 * 
	 *  @param String title
	 *  @param String[] test cases
	 *  @return none
	 */
	public TestLogTabbed(String title, String[] testCases){
		this.logPanel = new TestLogPanel(title, testCases);
		this.configPanel = new TestLogConfigPanel(testCases);
		this.add(LOG_LABEL, this.logPanel);
		this.add(CONFIG_LABEL, this.configPanel);
		
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
