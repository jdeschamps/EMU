package main.java.embl.rieslab.emu.ui;

import java.awt.Dimension;
import java.util.HashMap;

import javax.swing.JPanel;

import main.java.embl.rieslab.emu.controller.SystemController;
import main.java.embl.rieslab.emu.tasks.TaskHolder;
import main.java.embl.rieslab.emu.ui.ConfigurableMainFrame;
import main.java.embl.rieslab.emu.ui.uiparameters.UIParameter;
import main.java.embl.rieslab.emu.ui.uiproperties.UIProperty;

public class EmptyPropertyMainFrame extends ConfigurableMainFrame{

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

	@Override
	public HashMap<String, UIProperty> getUIProperties() {
		return new HashMap<String, UIProperty>();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public HashMap<String, UIParameter> getUIParameters() {
		return new HashMap<String, UIParameter>();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public HashMap<String, TaskHolder> getUITaskHolders() {
		// TODO Auto-generated method stub
		return  new HashMap<String, TaskHolder>();
	}

}
