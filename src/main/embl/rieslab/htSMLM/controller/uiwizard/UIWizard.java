package main.embl.rieslab.htSMLM.controller.uiwizard;

import java.util.HashMap;

import javax.swing.JFrame;

import main.embl.rieslab.htSMLM.micromanager.properties.MMProperties;
import main.embl.rieslab.htSMLM.ui.uiparameters.UIParameter;
import main.embl.rieslab.htSMLM.ui.uiproperties.UIProperty;

public class UIWizard {

	HashMap<String, String> prop_;
	HashMap<String, String> param_;
	
	public UIWizard() {
		prop_ = new HashMap<String, String>();
		param_ = new HashMap<String, String>();
	}
	
	@SuppressWarnings("rawtypes")
	public void newConfiguration(final HashMap<String, UIProperty> uipropertySet,
			final HashMap<String, UIParameter> uiparameterSet, final MMProperties mmproperties) {
		
		// Create UI
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {

	        //Create and set up the window.
	        JFrame frame = new JFrame("UI properties wizard");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	        //Create and set up the content pane.
	        WizardComboTable newContentPane = new WizardComboTable(uipropertySet, uiparameterSet, mmproperties);
	        newContentPane.setOpaque(true); //content panes must be opaque
	        frame.setContentPane(newContentPane);

	        //Display the window.
	        frame.pack();
	        frame.setVisible(true);
            }
		});
		
	}

	public HashMap<String, String> getWizProperties() {
		return null;
	}

	public HashMap<String, String> getWizParameters() {
		return null;
	}

}
