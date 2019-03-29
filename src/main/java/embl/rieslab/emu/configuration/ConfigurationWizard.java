package main.java.embl.rieslab.emu.configuration;

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
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import main.java.embl.rieslab.emu.configuration.globalsettings.GlobalSetting;
import main.java.embl.rieslab.emu.configuration.ui.GlobalSettingsTable;
import main.java.embl.rieslab.emu.configuration.ui.HelpWindow;
import main.java.embl.rieslab.emu.configuration.ui.ParametersTable;
import main.java.embl.rieslab.emu.configuration.ui.PropertiesTable;
import main.java.embl.rieslab.emu.micromanager.mmproperties.MMPropertiesRegistry;
import main.java.embl.rieslab.emu.ui.ConfigurableFrame;

/**
 * UI used to configure the system by allocating UI properties to existing device properties in Micro-manager, the 
 * values of their state if applicable and the values of the UI parameters.
 * 
 * @author Joran Deschamps
 *
 */
public class ConfigurationWizard {
	
	/**
	 * Value given to unallocated UIProperty states and UIParameters values.
	 */
	public final static String KEY_ENTERVALUE = "Enter value";
	public final static String KEY_UIPROPERTY = "UI Property: ";
	public final static String KEY_UIPARAMETER = "UI Parameter: ";

	private HashMap<String, String> prop_; // stores the name of the uiproperties and their corresponding mmproperty (or Configuration.KEY_UNALLOCATED)
	private HashMap<String, String> param_; // stores the name of the uiparameters and their value
	private HashMap<String, String> globset_; 
	private PropertiesTable propertytable_; // panel used by the user to pair ui- and mmproperties
	private ParametersTable parametertable_; // panel used by the user to set the values of uiparameters
	private GlobalSettingsTable globsettingstable_; 
	private HelpWindow help_; // help window to display the description of uiproperties and uiparameters
	private ConfigurationController config_; // configuration class
	private JFrame frame_; // overall frame for the configuration wizard
	private boolean running_ = false;
	private String plugin_name_;
	private JTextField config_name_;
	
	public ConfigurationWizard(ConfigurationController config) {
		config_ = config;
		
		prop_ = new HashMap<String, String>();
		param_ = new HashMap<String, String>();
		globset_ = new HashMap<String, String>();

		plugin_name_ = "";
	}

	private void newConfiguration(final String plugin_name, final ConfigurableFrame maininterface, final MMPropertiesRegistry mmproperties) {
		// makes sure it runs on EDT
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@SuppressWarnings("rawtypes")
			public void run() {
				running_ = true;

				help_ = new HelpWindow("Click on a row to display the description");
				
				// Table defining the properties
				propertytable_ = new PropertiesTable(maininterface.getUIProperties(), mmproperties, help_);
				propertytable_.setOpaque(true); 

				// and parameters
				parametertable_ = new ParametersTable(maininterface.getUIParameters(), maininterface.getUIProperties(), help_);
				parametertable_.setOpaque(true);
				
				// and global settings
				HashMap<String, GlobalSetting> glob = new HashMap<String, GlobalSetting>();
				glob.put(config_.getEnableUnallocatedWarnings().getName(), config_.getEnableUnallocatedWarnings());
				globsettingstable_ = new GlobalSettingsTable(glob, help_);
				globsettingstable_.setOpaque(true); 
				
				frame_ = createFrame(plugin_name, propertytable_, parametertable_, globsettingstable_, help_);
			}
		});
	}

	/**
	 * Creates a Wizard configuration frame from an existing configuration.
	 * 
	 * @param uipropertySet HashMap containing the UI properties.
	 * @param uiparameterSet HashMap containing the UI parameters.
	 * @param mmproperties Object holding the device properties from Micro-manager.
	 * @param configprop HashMap linking MM properties to UI properties from the configuration.
	 * @param configparam HashMap holding the values of the UI parameters from the configuration.
	 */
	private void existingConfiguration(final ConfigurableFrame maininterface,
			final MMPropertiesRegistry mmproperties, final GlobalConfiguration configuration) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@SuppressWarnings("rawtypes")
			public void run() {
				running_ = true;

				help_ = new HelpWindow("Click on a row to display the description");

				// Table defining the properties using the  configuration
				propertytable_ = new PropertiesTable(maininterface.getUIProperties(), mmproperties, 
						configuration.getCurrentPluginConfiguration().getProperties(), help_);
				propertytable_.setOpaque(true);

				// now parameters
				parametertable_ = new ParametersTable(maininterface.getUIParameters(), 
						configuration.getCurrentPluginConfiguration().getParameters(), maininterface.getUIProperties(), help_);
				parametertable_.setOpaque(true);				
				
				// and global settings
				HashMap<String, GlobalSetting> glob = new HashMap<String, GlobalSetting>();
				glob.put(config_.getEnableUnallocatedWarnings().getName(), config_.getEnableUnallocatedWarnings());
				globsettingstable_ = new GlobalSettingsTable(glob, help_);
				globsettingstable_.setOpaque(true); 
				
				frame_ = createFrame(configuration.getCurrentConfigurationName(), propertytable_, parametertable_, globsettingstable_, help_);

			}
		});
	}
	
	
	// Sets up the frame used for the interactive configuration.
	private JFrame createFrame(String conf_name, final PropertiesTable propertytable, final ParametersTable parametertable, final GlobalSettingsTable globsettingstable, final HelpWindow help){
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
		tabbedpane.addTab("Global Settings", null, globsettingstable, null);
		
		// content pane
		JPanel contentpane = new JPanel();
		
		// gridbag layout for upper and lower panel
		JPanel upperpane = new JPanel();
		upperpane.setLayout(new GridLayout(0,4));
		
		JLabel conf_name_label = new JLabel("   Configuration's name:");
		config_name_ = new JTextField(conf_name);

		JToggleButton helptoggle = new JToggleButton("HELP");
		helptoggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JToggleButton toggle = (JToggleButton) e.getSource();
				boolean selected = toggle.getModel().isSelected();
				showHelp(selected);
			}
		});
		upperpane.add(conf_name_label);
		upperpane.add(config_name_);
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
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		return frame;
	}
	
	/**
	 * Checks if the wizard is already running by returning an internal member variable.
	 * 
	 * @return True if running, false otherwise.
	 */
	public boolean isRunning(){
		return running_;
	}
	
	/**
	 * Returns the entries of the property table as a HashMap with the keys being the UIProperty names or state names, and the 
	 * values being the allocated MMProperty names or state values.
	 * 
	 * @return HashMap of the UIProperty names or state names as keys and the mapped MMProperty names or state values.
	 */
	public HashMap<String, String> getWizardProperties() {
		return prop_;
	}
	
	/**
	 * Returns the entries of the parameter table as a HashMap with the keys being the UIParameter names and the values
	 * being the parameter values.
	 * 
	 * @return HashMap of the UIParameter names as keys and corresponding values.
	 */
	public HashMap<String, String> getWizardParameters() {
		return param_;
	}
	
	/**
	 * Sets the help window visible and displays the description of the currently selected UIProperty or UIParameter.
	 * 
	 * @param b True if the help window is to be displayed, false otherwise
	 */
	public void showHelp(boolean b){
		if(help_ != null){
			help_.showHelp(b);
		}
	}
	
	//  Retrieves the UIProperty and UIParameter name/value pairs from the tables, updates the Configuration and closes
	//  all windows. 
	private void saveConfiguration(){
		prop_ = propertytable_.getSettings();
		param_ = parametertable_.getSettings();
		globset_ = globsettingstable_.getSettings();
		
		config_.applyWizardSettings(config_name_.getText(),plugin_name_,prop_,param_,globset_);
		
		frame_.dispose();
		help_.disposeHelp();
		running_ = false;
	}

	/**
	 * Closes open windows (wizard frame and help)
	 */
	public void shutDown(){
		if(help_ != null){
			help_.disposeHelp();
		}
		if(frame_ != null){
			frame_.dispose();
		}
		running_ = false;
	}

	/**
	 * Starts a new configuration wizard. If {@code configuration} is not compatible with {@code pluginName} then
	 * the wizard starts a new configuration, otherwise the wizard will allow editing {@code configuration}.
	 * 
	 * @param pluginName Name of the plugin to be configured.
	 * @param configuration Current configuration loaded in EMU.
	 * @param maininterface ConfigurableFrame of the plugin. 
	 * @param mmproperties Device properties from Micro-Manager.
	 */
	public void start(String pluginName, GlobalConfiguration configuration,
			ConfigurableFrame maininterface, MMPropertiesRegistry mmproperties) {
		
		plugin_name_ = pluginName;
		if(configuration.getCurrentPluginName() != null && configuration.getCurrentPluginName().equals(pluginName)){
			existingConfiguration(maininterface, mmproperties, configuration);
		} else {
			newConfiguration(pluginName, maininterface, mmproperties);
		}
			
	}
}
