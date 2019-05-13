package main.java.de.embl.rieslab.emu.ui;

import java.awt.Dimension;
import java.util.HashMap;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import main.java.de.embl.rieslab.emu.controller.SystemController;
import main.java.de.embl.rieslab.emu.ui.ConfigurableMainFrame;
import main.java.de.embl.rieslab.emu.ui.uiparameters.UIParameter;
import main.java.de.embl.rieslab.emu.ui.uiproperties.UIProperty;

/**
 * Empty ConfigurableMainFrame. Used when no plugin is found.
 * 
 * @author Joran Deschamps
 *
 */
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

	@Override
	protected void showWizardRunning() {
		JOptionPane.showMessageDialog(null,
				"No plugin found.",
				"Information", JOptionPane.INFORMATION_MESSAGE);
	}
	
}