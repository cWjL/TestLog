package panels;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JButton;

public class LandingPanel extends JPanel{
	private final Dimension BUTTON_SIZE = new Dimension(200, 50);
	private final Dimension PANEL_SIZE = new Dimension(50,50);
	
	public LandingPanel(){
		this.setLayout(new GridBagLayout()); //GridLayout(int rows, int cols, int hgap, int vgap)
		JButton newLogButton = new JButton("New Log");
		newLogButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		newLogButton.setToolTipText("Start a new test log document");
		newLogButton.setPreferredSize(BUTTON_SIZE);
		JButton openLogButton = new JButton("Open Log");
		openLogButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
		openLogButton.setToolTipText("Open an existing test log document");
		openLogButton.setPreferredSize(BUTTON_SIZE);
		setLayout(newLogButton, openLogButton);
	}
	
	private void setLayout(JButton logButton, JButton openButton){
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		this.add(logButton, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		JPanel blankPanel = new JPanel();
		blankPanel.setPreferredSize(PANEL_SIZE);
		this.add(blankPanel,c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 1;
		this.add(openButton, c);
		
		logButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){

			}
		});
		
		openButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){

			}
		});
		
	}
}
