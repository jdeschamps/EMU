package main.embl.rieslab.emu.ui;

import java.awt.Dimension;

import javax.swing.JPanel;

import main.embl.rieslab.emu.controller.SystemController;

public class EmptyPropertyMainFrame extends PropertyMainFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1840416249969324418L;

	public EmptyPropertyMainFrame(SystemController controller) {
		super("No plugin found", controller);
	}

	@Override
	protected void initComponents() {

		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(300,200));
		this.add(panel);

        this.pack(); 
        this.setResizable(false);
 	    this.setVisible(true); 
	}

}
