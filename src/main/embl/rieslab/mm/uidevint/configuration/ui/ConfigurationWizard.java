package main.embl.rieslab.mm.uidevint.configuration.ui;

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

import main.embl.rieslab.mm.uidevint.configuration.ConfigurationController;
import main.embl.rieslab.mm.uidevint.configuration.GlobalConfiguration;
import main.embl.rieslab.mm.uidevint.configuration.GlobalConfigurationWrapper;
import main.embl.rieslab.mm.uidevint.configuration.PluginConfiguration;
import main.embl.rieslab.mm.uidevint.mmproperties.MMProperties;
import main.embl.rieslab.mm.uidevint.ui.PropertyMainFrameInterface;

/**
 * UI used to configure the system by allocating UI properties to existing device properties in Micro-manager, the values of their state
 * and the values of the UI parameters.
 * 
 * @author Joran Deschamps
 *
 */
public class ConfigurationWizard {
	
	/**
	 * Value given to unallocated UIProperty states and UIParameters values.
	 */
	public final static String KEY_ENTERVALUE = "Enter value";
	private final static String KEY_UIPROPERTY = "UI Property: ";
	private final static String KEY_UIPARAMETER = "UI Parameter: ";

	private HashMap<String, String> prop_; // stores the name of the uiproperties and their corresponding mmproperty (or Configuration.KEY_UNALLOCATED)
	private HashMap<String, String> param_; // stores the name of the uiparameters and their value
	private PropertiesTable propertytable_; // panel used by the user to pair ui- and mmproperties
	private ParametersTable parametertable_; // panel used by the user to set the values of uiparameters
	private HelpWindow help_; // help window to display the description of uiproperties and uiparameters
	private ConfigurationController config_; // configuration class
	private JFrame frame_; // overall frame for the configuration wizard
	private boolean running_ = false;
	
	public ConfigurationWizard(ConfigurationController config) {
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
	public void newConfiguration(final PropertyMainFrameInterface maininterface, final MMProperties mmproperties) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				running_ = true;

				help_ = new HelpWindow("Click on a row to display the description");
				
				// Table defining the properties
				propertytable_ = new PropertiesTable(maininterface.getUIProperties(), mmproperties, help_);
				propertytable_.setOpaque(true); 
				
				// and parameters
				parametertable_ = new ParametersTable(maininterface.getUIParameters(), help_);
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
	public void existingConfiguration(final PropertyMainFrameInterface maininterface,
			final MMProperties mmproperties, final GlobalConfiguration configuration) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				running_ = true;

				help_ = new HelpWindow(
						"Click on a row to display the description");

				// Table defining the properties using configuration
				propertytable_ = new PropertiesTable(maininterface.getUIProperties(), mmproperties, help_);
				propertytable_.setOpaque(true);

				// now parameters
				parametertable_ = new ParametersTable(maininterface.getUIParameters(), configuration, help_);
				parametertable_.setOpaque(true);

				frame_ = createFrame(propertytable_, parametertable_, help_);
			}
		});
	}
	
	
	// Sets up the frame used for the interactive configuration.
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
	
	/**
	 * 
	 * @return true if running, false otherwise.
	 */
	public boolean isRunning(){
		return running_;
	}
	
	/**
	 * Returns the entries of the property table as a HashMap with the keys being the UIProperty names or state names, and the 
	 * values being the allocated MMProperty names or state values.
	 * 
	 * @return
	 */
	public HashMap<String, String> getWizardProperties() {
		return prop_;
	}
	
	/**
	 * Returns the entries of the parameter table as a HashMap with the keys being the UIParameter names and the values
	 * being the parameter values.
	 * 
	 * @return
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
		
		config_.setWizardSettings();
		
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

	public void start(String pluginName, GlobalConfigurationWrapper configuration_,
			PropertyMainFrameInterface maininterface, MMProperties mmproperties) {
		// TODO Auto-generated method stub
		if()
	}

	public String getConfigurationName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPluginName() {
		// TODO Auto-generated method stub
		return null;
	}
}
