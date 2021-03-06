package panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Initial landing panel.  Main calls Entry.java frame which adds this panel
 * 
 *  @author Jacob Loden
 */
public class NewLogPanel extends JPanel{

	/**
	 * Class variables
	 */
	private static final long serialVersionUID = 8335816020270057349L;
	private final Dimension BUTTON_SZ = new Dimension(75,30);
	public JButton cancel;
	public JButton ok;
	public JButton open;
	public JTextField projTitleText;
	public JTextField testerText;
	public JTextField testerRoleText;
	public JLabel errorMsg;
	
	/**
	 * Class constructor
	 * 
	 *  @param none
	 *  @return none
	 */
	public NewLogPanel(){
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		JPanel projectPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		JPanel testerPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		JPanel testerRolePanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JLabel projTitle = new JLabel("Project Name:");
		JLabel tester = new JLabel("Tester Name:");
		JLabel testerRole = new JLabel("Tester Role:");
		this.cancel = new JButton("Cancel");
		this.cancel.setToolTipText("Exit application");
		this.cancel.setPreferredSize(BUTTON_SZ);
		
		this.ok = new JButton("OK");
		this.ok.setToolTipText("Start new log with test cases and project title above");
		this.ok.setPreferredSize(BUTTON_SZ);
		
		this.open = new JButton("Open");
		this.open.setToolTipText("Open existing log file");
		this.open.setPreferredSize(BUTTON_SZ);
		
		this.projTitleText = new JTextField(20);
		this.projTitleText.setToolTipText("Enter the title of the project/product under test");
		this.testerText = new JTextField(20);
		this.testerText.setToolTipText("Enter tester name");
		this.testerRoleText = new JTextField(20);
		this.testerRoleText.setToolTipText("Enter tester title");
		this.errorMsg = new JLabel("");
		
		this.errorMsg.setForeground(Color.RED);
		this.errorMsg.setVisible(false);
		
		buttonPanel.add(this.open);
		buttonPanel.add(this.cancel);
		buttonPanel.add(this.ok);
		
		testerPanel.add(tester);
		testerPanel.add(testerText);
		
		testerRolePanel.add(testerRole);
		testerRolePanel.add(testerRoleText);
		
		projectPanel.add(projTitle);
		projectPanel.add(projTitleText);

		errorPanel.add(errorMsg);
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		this.add(errorPanel);
		this.add(projectPanel);
		this.add(testerPanel);
		this.add(testerRolePanel);
		this.add(buttonPanel);
	}
}
