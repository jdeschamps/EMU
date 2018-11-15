package main.embl.rieslab.emu.configuration;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import main.embl.rieslab.emu.configuration.globalsettings.BoolGlobalSettings;
import main.embl.rieslab.emu.controller.SystemConstants;
import main.embl.rieslab.emu.controller.SystemController;
import main.embl.rieslab.emu.micromanager.mmproperties.MMProperties;
import main.embl.rieslab.emu.ui.PropertyMainFrameInterface;
import main.embl.rieslab.emu.ui.uiproperties.MultiStateUIProperty;
import main.embl.rieslab.emu.ui.uiproperties.SingleStateUIProperty;
import main.embl.rieslab.emu.ui.uiproperties.TwoStateUIProperty;

public class ConfigurationController {
	
	private SystemController controller_;
	private ConfigurationWizard wizard_;
	private GlobalConfiguration configuration_;
	
	public ConfigurationController(SystemController controller){
		controller_ = controller;
	}

	public File getDefaultConfigurationFile() {
		return new File(SystemConstants.CONFIG_NAME);
	}

	public boolean readDefaultConfiguration(){
		if(getDefaultConfigurationFile().exists()){
			configuration_ = ConfigurationIO.read(getDefaultConfigurationFile());
			if(configuration_ == null){
				return false;
			}
			return true;
		}
		return false;
	}
	
	public boolean readConfiguration(File f){	
		if(f.exists()){	
			configuration_ = ConfigurationIO.read(f);
			if(configuration_ == null){
				return false;
			}
			return true;
		}
		return false;
	}

	public boolean writeConfiguration(){
		return ConfigurationIO.write(getDefaultConfigurationFile(), getConfiguration().getGlobalConfiguration());
	}

	public boolean writeConfiguration(File f){
		return ConfigurationIO.write(f, getConfiguration().getGlobalConfiguration());
	}

	public GlobalConfiguration getConfiguration(){
		return configuration_;
	}

	public boolean isValidConfiguration(String configName){
		if(configuration_ == null){
			return false;
		}
		return configuration_.isValidConfiguration(configName);
	}
	
	public String[] getCompatibleConfigurations(String pluginName){
		if(configuration_ != null){
			return configuration_.getCompatibleConfigurations(pluginName);
		}
		return null; 
	}

	public boolean sanityCheck(PropertyMainFrameInterface maininterface, MMProperties mmproperties) {
		if(configuration_ == null){
			return false;
		}
		
		// just check if something is missing. When editing the settings, the PropertiesTable takes care
		// of removing old properties and such.
		
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
	
	public boolean startWizard(String pluginName, PropertyMainFrameInterface maininterface, MMProperties mmproperties){
		// launch wizard
		if(!isWizardRunning()){	
			wizard_ = new ConfigurationWizard(this);
			
			if(configuration_ != null){
				wizard_.start(pluginName, configuration_, maininterface, mmproperties);
			} else {
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
	 * Retrieves the pairs of UIProperty name and MMProperty names (or UIProperty state values), as well as the pairs UIParameter names and values,
	 * and writes them to the configuration file. It then calls the SystemController to update the system. This method is called by the ConfigurationWizard
	 * upon saving of the configuration by the user.
	 * @param globset_ 
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
	
	public boolean setDefaultConfiguration(String configuration){
		if(configuration_ == null){
			return false;
		}
		
		return configuration_.setCurrentConfiguration(configuration);
	}
	
	public String getDefaultConfiguration(){
		if(configuration_ == null){
			return null;
		}
		
		return configuration_.getCurrentConfigurationName();
	}
	
	/**
	 * Returns the properties configuration.
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
	 * Returns the parameters configuration/
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
	 * Tests if the string has been generated by a SingelStateUIProperty, a TwoStateUIProperty or a MultiStateUIProperty.
	 * 
	 * @param s String to test, value in the first column of the table
	 * @return True if corresponds to a field value.
	 */
	public static boolean isStateValue(String s){
		if (s.contains(SingleStateUIProperty.getValueName())
				|| s.contains(TwoStateUIProperty.getOnStateName()) 
				|| s.contains(TwoStateUIProperty.getOffStateName())
				|| s.matches(".*"+MultiStateUIProperty.getGenericStateName()+".*")){
			return true;
		}
		return false;
	}

	public BoolGlobalSettings getEnableUnallocatedWarnings(){
		if(configuration_ == null){
			return null;
		}
		
		return configuration_.getEnableUnallocatedWarnings();
	}
}
