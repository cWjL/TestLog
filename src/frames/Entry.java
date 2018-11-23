package frames;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import panels.LandingPanel;

public class Entry extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3589089200466664223L;
	private final Dimension SEL_SIZE = new Dimension(500, 300);
	private final Dimension FRAME_SIZE = new Dimension(600, 400);
	
	
	/*
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
		this.setVisible(true);
		
		
		landPanel.newLogButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				//swapPanel(newPanel);
			}
		});
	}
	
	/*
	 * Swap panels
	 * 
	 * @param none
	 * @return none
	 */
	public void swapPanel(JPanel newPanel){
		this.removeAll();
		this.setContentPane(newPanel);
		this.pack();
		this.validate();
	}
}
