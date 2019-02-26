package main.java.embl.rieslab.emu.configuration;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import main.java.embl.rieslab.emu.configuration.globalsettings.BoolGlobalSetting;
import main.java.embl.rieslab.emu.controller.SystemConstants;
import main.java.embl.rieslab.emu.controller.SystemController;
import main.java.embl.rieslab.emu.micromanager.mmproperties.MMPropertiesRegistry;
import main.java.embl.rieslab.emu.ui.ConfigurableFrame;

/**
 * Controller class for the configuration of the current UI. This class bridges the {@link main.java.embl.rieslab.emu.controller.SystemController}
 * with the {@link ConfigurationWizard}, the {@link GlobalConfiguration}. The ConfigurationController starts the configuration wizard to allow
 * the user to modify the current configuration. It also contains inform the SystemController on the different configurations. Finally, it
 * calls the {@link ConfigurationIO} to read and write the configurations from/to files.
 * 
 * @author Joran Deschamps
 *
 */
public class ConfigurationController {
	
	private SystemController controller_; // overall controller
	private ConfigurationWizard wizard_; // graphical interface to create/edit the current configuration
	private GlobalConfiguration configuration_; // configurations of the UI
	
	/**
	 * Constructor.
	 * 
	 * @param controller System controller.
	 */
	public ConfigurationController(SystemController controller){
		controller_ = controller;
	}

	/**
	 * Returns the default path to the configuration file as defined in {@link main.java.embl.rieslab.emu.controller.SystemConstants}.
	 * 
	 * @return Default configuration file.
	 */
	public File getDefaultConfigurationFile() {
		return new File(SystemConstants.CONFIG_NAME);
	}

	/**
	 * Reads the default configuration file.
	 * 
	 * @return True if the configuration has been successfully read, false otherwise.
	 */
	public boolean readDefaultConfiguration(){
		if(getDefaultConfigurationFile().exists()){
			configuration_ = ConfigurationIO.read(getDefaultConfigurationFile());
			if(configuration_ == null){  
				configuration_ = new GlobalConfiguration(); // empty one
				return false;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Reads a configuration file {@code f}.
	 * 
	 * @param f File to read
	 * @return True if a configuration was successfully read, false otherwise.
	 */
	public boolean readConfiguration(File f){	
		if(f.exists()){	
			configuration_ = ConfigurationIO.read(f);
			if(configuration_ == null){
				configuration_ = new GlobalConfiguration(); // empty one
				return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * Writes the configurations to the default configuration file.
	 * 
	 * @return True if successfully written, false otherwise.
	 */
	public boolean writeConfiguration(){
		return ConfigurationIO.write(getDefaultConfigurationFile(), getConfiguration());
	}

	/**
	 * Writes the configurations to {@code f}.
	 * 
	 * @param f File to write the configuration to.
	 * @return True if successfully written, false otherwise.
	 */
	public boolean writeConfiguration(File f){
		return ConfigurationIO.write(f, getConfiguration());
	}

	/**
	 * Returns the {@link GlobalConfiguration}.
	 * 
	 * @return Global configuration.
	 */
	public GlobalConfiguration getConfiguration(){
		return configuration_;
	}

	/**
	 * Checks if a configuration called {@code configName} exists in the {@link GlobalConfiguration}.
	 *  
	 * @param configName Name of the configuration.
	 * @return True if the configuration exists, false otherwise.
	 */
	public boolean doesConfigurationExist(String configName){
		if(configuration_ == null){
			return false;
		}
		return configuration_.doesConfigurationExist(configName);
	}
	
	/**
	 * Returns an array containing the names of configurations compatible with {@code pluginName}.
	 * 
	 * @param pluginName Plugin under consideration.
	 * @return Array of compatible configuration names. The array can be of size 0. 
	 */
	public String[] getCompatibleConfigurations(String pluginName){
		if(configuration_ != null){
			return configuration_.getCompatibleConfigurations(pluginName);
		}
		return new String[0]; 
	}

	/**
	 * Checks if the configuration contains all the properties and parameters as defined in the {@link main.java.embl.rieslab.emu.ui.ConfigurableFrame}.
	 * 
	 * @param maininterface Current plugin' ConfigurableFrame. 
	 * @return True if the current configuration contains all the properties and parameters defined in the plugin's ConfigurableFrame. 
	 */
	public boolean sanityCheck(ConfigurableFrame maininterface) {
		if(configuration_ == null){
			return false;
		} else {
			// just check if some UIProperties or UIParameters are missing from the configuration.
			// When editing the settings, the PropertiesTable only displays the ConfigurableFrame properties, therefore properties
			// present in the configuration but not in the ConfigurableFrame will be ignored. The same mechanism applies when
			// the controller pairs the UI and MM properties. As a result the sanity check only makes sense with respect to the 
			// actual UIProperties and UIParameters of the ConfigurableFrame.
			
			// check if the plugin configuration contains all the UIProperties
			Set<String> uipropkeys =  new HashSet<String>(maininterface.getUIProperties().keySet());
			uipropkeys.removeAll(configuration_.getCurrentPluginConfiguration().getProperties().keySet());
			if(uipropkeys.size() > 0){
				return false;
			}
			
			// check if the plugin configuration contains all the UIParameters
			Set<String> uiparamkeys =   new HashSet<String>(maininterface.getUIParameters().keySet());
			uiparamkeys.removeAll(configuration_.getCurrentPluginConfiguration().getParameters().keySet());
			if(uiparamkeys.size() > 0){
				return false;
			}
			
			return true;
		}
	}
	
	/**
	 * Starts a new configuration wizard. If a wizard is already running, then does nothing and returns {@code false}.
	 * 
	 * @param pluginName Current plugin's name.
	 * @param maininterface plugin's ConfigurableFrame.
	 * @param mmproperties Micro-manager properties.
	 * @return True if a new wizard was started, false if it was already running. 
	 */
	public boolean startWizard(String pluginName, ConfigurableFrame maininterface, MMPropertiesRegistry mmproperties){
		// launch wizard
		if(!isWizardRunning()){	
			wizard_ = new ConfigurationWizard(this);
			
			if(configuration_ != null){ // start a wizard with the current configuration loaded
				wizard_.start(pluginName, configuration_, maininterface, mmproperties);
			} else { // start a fresh wizard
				configuration_ = new GlobalConfiguration();				
				wizard_.start(pluginName, configuration_, maininterface, mmproperties);
			}
			
			return true;
		}
		return false;
	}
	
	/**
	 * Returns true if a ConfigurationWizard is running, false otherwise.
	 * 
	 * @return true if a ConfigurationWizard is running, false otherwise
	 */
	public boolean isWizardRunning(){
		if(wizard_ == null){
			return false;
		}
		return wizard_.isRunning();
	}

	/**
	 * Retrieves the pairs of UIProperty name and MMProperty names (and UIProperty state values), as well as the pairs of UIParameter names and values,
	 * and writes them to the configuration file. It then calls the {@link main.java.embl.rieslab.emu.controller.SystemController} to update the system. 
	 * This method is called by the {@link ConfigurationWizard} upon saving of the configuration by the user.
	 * 
	 * @param configName Name of the configuration.
	 * @param pluginName Name of the current plugin.
	 * @param uiproperties Mapping of the UIProperties with MMProperties and their states.
	 * @param uiparameters Mapping of the UIParameters' states.
	 * @param globset_ Mapping of the GlobalSettings' states.
	 */
	public void applyWizardSettings(String configName, String pluginName, Map<String, String> uiproperties, Map<String, String> uiparameters, HashMap<String, String> globset_) {
		if(configuration_ == null){
			return;
		}
		
		if (configuration_.getCurrentConfigurationName().equals(configName)) {
			// the configuration has the same name
			PluginConfiguration plugin = new PluginConfiguration();
			plugin.configure(configName, pluginName, uiproperties, uiparameters);
			configuration_.substituteConfiguration(plugin);
		} else {
			// new configuration has a different name
			PluginConfiguration plugin = new PluginConfiguration();
			plugin.configure(configName, pluginName, uiproperties, uiparameters);
			configuration_.addConfiguration(plugin);
		}

		// set global settings
		configuration_.setGlobalSettings(globset_);

		// set current configuration
		configuration_.setCurrentConfiguration(configName);

		writeConfiguration();

		// update system
		controller_.loadConfiguration(configName);
	}
	
	/**
	 * Sets the default configuration to {@code newDefault}.
	 * 
	 * @param newDefault New default configuration.
	 * @return True if the configuration was changed, false otherwise.
	 */
	public boolean setDefaultConfiguration(String newDefault){
		if(configuration_ == null){
			return false;
		}
		
		return configuration_.setCurrentConfiguration(newDefault);
	}
	
	/**
	 * Returns the default configuration's name. Null if there is no configuration yet.
	 * 
	 * @return Default configuration's name.
	 */
	public String getDefaultConfiguration(){
		if(configuration_ == null){
			return null;
		}
		
		return configuration_.getCurrentConfigurationName();
	}
	
	/**
	 * Returns the properties configuration. Null if there is none.
	 * 
	 * @return Pairs of UIProperty names (keys) and MMProperty names (values), as well as UIProperty state names (keys)
	 * and UIProperty state values (values)
	 */
	public TreeMap<String,String> getPropertiesConfiguration(){
		if(configuration_ == null){
			return null;
		}
		
		return configuration_.getCurrentPluginConfiguration().getProperties();
	}
	
	/**
	 * Returns the parameters configuration. Null if there is none.
	 * 
	 * @return Pairs of UIParameter names (keys) and their value (values)
	 */
	public TreeMap<String,String> getParametersConfiguration(){
		if(configuration_ == null){
			return null;
		}
		
		return configuration_.getCurrentPluginConfiguration().getParameters();
	}
	
	/**
	 * Closes the ConfigurationWizard window (if running). This method is called upon closing the plugin.
	 * 
	 */
	public void shutDown(){
		if (wizard_ != null) {
			wizard_.shutDown();
		}
	}
	
	/**
	 * Returns the GlobalSetting corresponding to the enabling or disabling of the unallocated properties warning.
	 * 
	 * @return BoolGlobalSetting
	 */
	public BoolGlobalSetting getEnableUnallocatedWarnings(){
		if(configuration_ == null){
			return null;
		}
		
		return configuration_.getEnableUnallocatedWarningsSetting();
	}
}
