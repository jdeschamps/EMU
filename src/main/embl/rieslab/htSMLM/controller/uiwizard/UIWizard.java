package main.embl.rieslab.htSMLM.controller.uiwizard;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import main.embl.rieslab.htSMLM.micromanager.properties.MMProperties;
import main.embl.rieslab.htSMLM.ui.uiparameters.UIParameter;
import main.embl.rieslab.htSMLM.ui.uiproperties.UIProperty;

public class UIWizard {

	HashMap<String, String> prop_;
	HashMap<String, String> param_;
	PropertyComboTable propertytable_;
	ParameterComboTable parametertable_;
	
	public UIWizard() {
		prop_ = new HashMap<String, String>();
		param_ = new HashMap<String, String>();
	}
	
	@SuppressWarnings("rawtypes")
	public void newConfiguration(final HashMap<String, UIProperty> uipropertySet,
			final HashMap<String, UIParameter> uiparameterSet, final MMProperties mmproperties) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				JFrame frame = new JFrame("UI properties wizard");
				frame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						propertytable_.disposeHelp();
						parametertable_.disposeHelp();
						e.getWindow().dispose();
					}
				});
				
				HelpWindow help = new HelpWindow("Click on a row to display the description");

				// Table defining the properties
				propertytable_ = new PropertyComboTable(uipropertySet, mmproperties, help);
				propertytable_.setOpaque(true); 
				
				// now parameters
				parametertable_ = new ParameterComboTable(uiparameterSet, help);
				parametertable_.setOpaque(true); 
				
				JTabbedPane tabbedpane = new JTabbedPane();
				
				
				
				
				
				
				
				
				frame.setContentPane(parametertable_);

				// Display the window.
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
