package main.embl.rieslab.htSMLM.controller.ui;

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
	private PropertiesTable propertytable_;
	private ParametersTable parametertable_;
	private HelpWindow help_;
	private Configuration config_;
	private JFrame frame_;
	private boolean running_ = false;
	
	public UIWizard(Configuration config) {
		config_ = config;
		
		prop_ = new HashMap<String, String>();
		param_ = new HashMap<String, String>();
	}
	
	/**
	 * Creates a new Wizard configuration frame. 
	 * 
	 * @param uipropertySet HashMap containing the UI properties.
	 * @param uiparameterSet HashMap containing the UI parameters.
	 * @param mmproperties Object holding the device properties from Micro-manager.
	 */
	@SuppressWarnings("rawtypes")
	public void newConfiguration(final HashMap<String, UIProperty> uipropertySet,
			final HashMap<String, UIParameter> uiparameterSet, final MMProperties mmproperties) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				running_ = true;

				help_ = new HelpWindow("Click on a row to display the description");
				
				// Table defining the properties
				propertytable_ = new PropertiesTable(uipropertySet, mmproperties, help_);
				propertytable_.setOpaque(true); 
				
				// and parameters
				parametertable_ = new ParametersTable(uiparameterSet, uipropertySet, help_);
				parametertable_.setOpaque(true); 
				
				frame_ = createFrame(propertytable_, parametertable_, help_);
			}
		});
	}

	/**
	 * Creates a Wizard configuration frame from an existing configuration.
	 * 
	 * @param uipropertySet HashMap containing the UI properties.
	 * @param uiparameterSet HashMap containing the UI parameters.
	 * @param mmproperties Object holding the device properties from Micro-manager.
	 * @param configprop HashMap linking mm properties to ui properties from the configuration.
	 * @param configparam HashMap holding the values of the ui parameters from the configuration.
	 */
	@SuppressWarnings("rawtypes")
	public void existingConfiguration(final HashMap<String, UIProperty> uipropertySet,
			final HashMap<String, UIParameter> uiparameterSet, final MMProperties mmproperties, final HashMap<String, String> configprop,
			final HashMap<String, String> configparam) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				running_ = true;
				
				help_ = new HelpWindow("Click on a row to display the description");

				// Table defining the properties using configuration
				propertytable_ = new PropertiesTable(uipropertySet, mmproperties, configprop, help_);
				propertytable_.setOpaque(true); 
				
				// now parameters
				parametertable_ = new ParametersTable(uiparameterSet, configparam, uipropertySet, help_);
				parametertable_.setOpaque(true); 
				
				frame_ = createFrame(propertytable_,parametertable_, help_);
			}
		});
	}
	
	private JFrame createFrame(final PropertiesTable propertytable, final ParametersTable parametertable, final HelpWindow help){
		JFrame frame = new JFrame("UI properties wizard");
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// show dialogue  
				boolean b = true;
				if(b){
					help.disposeHelp();
					running_ = false;
					e.getWindow().dispose();
				}
			}
		});   
		
		
		// Tab containing the tables
		JTabbedPane tabbedpane = new JTabbedPane();
		tabbedpane.addTab("Properties", null, propertytable, null);
		tabbedpane.addTab("Parameters", null, parametertable, null);
		
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
		
		return frame;
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
		help_.disposeHelp();
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
