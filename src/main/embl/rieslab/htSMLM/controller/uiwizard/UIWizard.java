package main.embl.rieslab.htSMLM.controller.uiwizard;

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

import main.embl.rieslab.htSMLM.controller.Configuration;
import main.embl.rieslab.htSMLM.micromanager.properties.MMProperties;
import main.embl.rieslab.htSMLM.ui.uiparameters.UIParameter;
import main.embl.rieslab.htSMLM.ui.uiproperties.UIProperty;

public class UIWizard {

	private HashMap<String, String> prop_;
	private HashMap<String, String> param_;
	private PropertyComboTable propertytable_;
	private ParameterComboTable parametertable_;
	private HelpWindow help_;
	private Configuration config_;
	private JFrame frame_;
	private boolean running_ = false;
	
	public UIWizard(Configuration config) {
		config_ = config;
		
		prop_ = new HashMap<String, String>();
		param_ = new HashMap<String, String>();
	}
	
	@SuppressWarnings("rawtypes")
	public void newConfiguration(final HashMap<String, UIProperty> uipropertySet,
			final HashMap<String, UIParameter> uiparameterSet, final MMProperties mmproperties) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				running_ = true;
				
				frame_ = new JFrame("UI properties wizard");
				frame_.addWindowListener(new WindowAdapter() {
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
		
				frame_.setContentPane(contentpane);

				// Display the window.
				frame_.pack();
				frame_.setVisible(true);
			}
		});
	}

	public void existingConfiguration(HashMap<String, String> uipropertySet,
			HashMap<String, String> uiparameterSet) {
		
	}
	
	public boolean isRunning(){
		return running_;
	}
	
	public HashMap<String, String> getWizardProperties() {
		return prop_;
	}

	public HashMap<String, String> getWizardParameters() {
		return param_;
	}
	
	public void showHelp(boolean b){
		if(help_ != null){
			help_.showHelp(b);
		}
	}
	
	private void saveConfiguration(){
		prop_ = propertytable_.getSettings();
		param_ = parametertable_.getSettings();
		
		config_.getWizardSettings();
		
		frame_.dispose();
		running_ = false;
	}

	public void shutDown(){
		if(help_ != null){
			help_.disposeHelp();
		}
		if(frame_ != null){
			frame_.dispose();
		}
		running_ = false;
	}

	
}
