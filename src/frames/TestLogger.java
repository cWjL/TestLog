package frames;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import panels.TestLogPanel;

public class TestLogger extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6998372573965893306L;
	private static final Dimension MAX_SIZE = new Dimension(800,500);
	private String title;
	private String[] testCase;
	/**
	 * Frame constructor
	 * 
	 * 
	 * @param String title
	 * @param String[] test cases
	 * @return none
	 */
	public TestLogger(String title, String[] testCases){
		
		this.title = title;
		this.testCase = testCases;
	}
	
	/**
	 * 
	 */
	public void showUI(){
/*		for(int i = 0; i < this.testCase.length; i++){
			System.out.println(title + " " + this.testCase[i]);
		}
		System.exit(0);*/
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
		
		TestLogPanel runPanel = new TestLogPanel(this.title, this.testCase);
		//this.setPreferredSize(MAX_SIZE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setContentPane(runPanel);
		this.pack();
		this.setVisible(true);
	}

}
