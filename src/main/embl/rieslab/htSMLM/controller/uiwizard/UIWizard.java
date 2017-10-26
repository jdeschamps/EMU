package main.embl.rieslab.htSMLM.controller.uiwizard;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;

import main.embl.rieslab.htSMLM.micromanager.properties.MMProperties;
import main.embl.rieslab.htSMLM.ui.uiparameters.UIParameter;
import main.embl.rieslab.htSMLM.ui.uiproperties.UIProperty;

public class UIWizard {

	HashMap<String, String> prop_;
	HashMap<String, String> param_;
	PropertyComboTable propertytable_;
	ParameterComboTable parametertable_;
	HelpWindow help_;
	
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
						// show dialogue
						boolean b = true;
						if(b){
							propertytable_.disposeHelp();
							parametertable_.disposeHelp();
							e.getWindow().dispose();
						}
					}
				});
				
				help_ = new HelpWindow("Click on a row to display the description");

				// Table defining the properties
				propertytable_ = new PropertyComboTable(uipropertySet, mmproperties, help_);
				propertytable_.setOpaque(true); 
				
				// now parameters
				parametertable_ = new ParameterComboTable(uiparameterSet, help_);
				parametertable_.setOpaque(true); 
				
				// Tab containing the tables
				JTabbedPane tabbedpane = new JTabbedPane();
				tabbedpane.addTab("Properties", null, propertytable_, null);
				tabbedpane.addTab("Parameters", null, parametertable_, null);
				
				// content pane
				JPanel contentpane = new JPanel();
				
				// gridbag layout for upper and lower panel
				JPanel upperpane = new JPanel();
				upperpane.setLayout(new GridLayout(0,4));

				JToggleButton helptoggle = new JToggleButton("HELP");
				helptoggle.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JToggleButton toggle = (JToggleButton) e.getSource();
						boolean selected = toggle.getModel().isSelected();
						showHelp(selected);
					}
				});
				upperpane.add(new JLabel(""));
				upperpane.add(new JLabel(""));
				upperpane.add(new JLabel(""));
				upperpane.add(helptoggle);

				JPanel lowerpane = new JPanel();
				lowerpane.setLayout(new GridLayout(0, 3));

				JButton save = new JButton("Save");
				save.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						saveConfiguration();
					}
				});

				lowerpane.add(save);
				lowerpane.add(new JLabel(""));
				lowerpane.add(new JLabel(""));
			
				contentpane.add(upperpane);
				contentpane.add(tabbedpane);
				contentpane.add(lowerpane);
				contentpane.setLayout(new BoxLayout(contentpane, BoxLayout.PAGE_AXIS));
		
				frame.setContentPane(contentpane);

				// Display the window.
				frame.pack();
				frame.setVisible(true);
			}
		});

	}

	public HashMap<String, String> getWizardProperties() {
		return prop_;
	}

	public HashMap<String, String> getWizardParameters() {
		return param_;
	}
	
	public void showHelp(boolean b){
		help_.showHelp(b);
	}
	
	public void saveConfiguration(){
		//prop_ = propertytable_.getSettings();
		//param_ = parametertable_.getSettings();
	}


	
}
