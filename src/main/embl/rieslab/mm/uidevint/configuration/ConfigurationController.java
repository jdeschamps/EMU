package main.embl.rieslab.mm.uidevint.configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import main.embl.rieslab.mm.uidevint.configuration.ui.ConfigurationWizard;
import main.embl.rieslab.mm.uidevint.controller.SystemController;
import main.embl.rieslab.mm.uidevint.mmproperties.MMProperties;
import main.embl.rieslab.mm.uidevint.ui.PropertyMainFrameInterface;
import main.embl.rieslab.mm.uidevint.ui.uiparameters.UIParameter;
import main.embl.rieslab.mm.uidevint.ui.uiproperties.MultiStateUIProperty;
import main.embl.rieslab.mm.uidevint.ui.uiproperties.SingleStateUIProperty;
import main.embl.rieslab.mm.uidevint.ui.uiproperties.TwoStateUIProperty;
import main.embl.rieslab.mm.uidevint.ui.uiproperties.UIProperty;

public class ConfigurationController {
	
	private SystemController controller_;
	private ConfigurationWizard wizard_;
	private GlobalConfigurationWrapper configuration_;
	
	public ConfigurationController(SystemController controller){
		controller_ = controller;
		configuration_ = new GlobalConfigurationWrapper();
	}

	private File getDefaultConfigurationFile() {
		return new File(SystemConstants.CONFIG_NAME);
	}

	public boolean readDefaultConfiguration(){
		configuration_ = ConfigurationIO.read(getDefaultConfigurationFile());
		if(configuration_ == null){
			return false;
		}
		return true;
	}
	
	public boolean readConfiguration(File f){		
		configuration_ = ConfigurationIO.read(f);
		if(configuration_ == null){
			return false;
		}
		return true;
	}

	public boolean writeConfiguration(){
		return ConfigurationIO.write(getDefaultConfigurationFile(), getConfiguration());
	}

	public boolean writeConfiguration(File f){
		return ConfigurationIO.write(f, getConfiguration());
	}
	
	public GlobalConfigurationWrapper getConfiguration(){
		return configuration_;
	}

	public boolean startWizard(PropertyMainFrameInterface maininterface, MMProperties mmproperties){
		// launch wizard
		if(!isWizardRunning()){	
			wizard_ = new ConfigurationWizard(this);
			wizard_.start(configuration_.getCurrentPluginConfiguration(), maininterface, mmproperties);
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
	 */
	public void setWizardSettings() {
		if (wizard_ != null) {
			// Retrieves the Maps and the name
			String name = wizard_.getConfigurationName();
			Map<String, String> uiproperties = wizard_.getWizardProperties();
			Map<String, String> uiparameters = wizard_.getWizardParameters();

			if(configuration_.getCurrentConfigurationName().equals(name)){
				// the configuration has the same name
				configuration_.substituteConfiguration(new PluginConfiguration(name, configuration_.getCurrentPluginName(), uiproperties, uiparameters));
			} else {
				// new configuration has a different name  
				configuration_.addConfiguration(new PluginConfiguration(name, configuration_.getCurrentPluginName(), uiproperties, uiparameters));
			}
			
			saveConfiguration(uiproperties_, uiparameters_); // writes to file

			// update system
			controller_.applyConfiguration();
		}
	}
	
	/**
	 * Returns the properties configuration.
	 * 
	 * @return Pairs of UIProperty names (keys) and MMProperty names (values), as well as UIProperty state names (keys)
	 * and UIProperty state values (values)
	 */
	public TreeMap<String,String> getPropertiesConfiguration(){
		return uiproperties_;
	}
	
	/**
	 * Returns the parameters configuration/
	 * 
	 * @return Pairs of UIParameter names (keys) and their value (values)
	 */
	public TreeMap<String,String> getParametersConfiguration(){
		return uiparameters_;
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
}
