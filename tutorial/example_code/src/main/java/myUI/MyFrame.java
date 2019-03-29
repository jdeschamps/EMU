package main.java.myUI;

import java.awt.EventQueue;

import javax.swing.JFrame;

import main.java.embl.rieslab.emu.controller.SystemController;
import main.java.embl.rieslab.emu.ui.ConfigurableMainFrame;
import java.awt.BorderLayout;

public class MyFrame extends ConfigurableMainFrame {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MyFrame frame = new MyFrame("", null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MyFrame( String title, SystemController controller) {
		super(title, controller);


	}

	@Override
	protected void initComponents() {
		setBounds(100, 100, 414, 398);
		getContentPane().setLayout(null);
		
		LaserPanel laserPanel = new LaserPanel("Laser 1");
		laserPanel.setBounds(0, 0, 116, 280);
		getContentPane().add(laserPanel);
		
		LaserPanel laserPanel_1 = new LaserPanel("Laser 2");
		laserPanel_1.setBounds(139, 0, 116, 280);
		getContentPane().add(laserPanel_1);
		
		FilterWheelPanel filterWheelPanel = new FilterWheelPanel("Filterwheel");
		filterWheelPanel.setBounds(0, 278, 395, 57);
		getContentPane().add(filterWheelPanel);
		
		LaserPanel laserPanel_2 = new LaserPanel("Laser 3");
		laserPanel_2.setBounds(279, 0, 116, 280);
		getContentPane().add(laserPanel_2);
	}
}
