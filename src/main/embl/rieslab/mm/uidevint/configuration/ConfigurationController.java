package main.embl.rieslab.mm.uidevint.configuration;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import main.embl.rieslab.mm.uidevint.configuration.ui.ConfigurationWizard;
import main.embl.rieslab.mm.uidevint.controller.SystemConstants;
import main.embl.rieslab.mm.uidevint.controller.SystemController;
import main.embl.rieslab.mm.uidevint.mmproperties.MMProperties;
import main.embl.rieslab.mm.uidevint.ui.PropertyMainFrameInterface;
import main.embl.rieslab.mm.uidevint.ui.uiparameters.UIParameter;

public class ConfigurationController {
	
	private SystemController controller_;
	private ConfigurationWizard wizard_;
	private GlobalConfiguration configuration_;
	
	public ConfigurationController(SystemController controller){
		controller_ = controller;
	}

	private File getDefaultConfigurationFile() {
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
	
	public String[] getCompatibleConfigurations(String pluginName){
		if(configuration_ != null){
			return configuration_.getCompatibleConfigurations(pluginName);
		}
		return null; 
	}

	@SuppressWarnings("rawtypes")
	public boolean sanityCheck(PropertyMainFrameInterface maininterface, MMProperties mmproperties) {
		boolean sane = true;
		PluginConfiguration pluginconf = configuration_.getCurrentPluginConfiguration();

		// check that all the expected UIProperties are present
		Iterator<String> uipropit = maininterface.getUIProperties().keySet().iterator();
		while(uipropit.hasNext()){
			String s = uipropit.next();
			if(!pluginconf.getProperties().containsKey(s)){ // if the UIProperty is not found, then add it as unallocated
				sane = false;
				pluginconf.getProperties().put(s, GlobalConfiguration.KEY_UNALLOCATED);
			}
		}
		
		// same with parameters
		Iterator<String> uiparamit = maininterface.getUIParameters().keySet().iterator();
		while(uiparamit.hasNext()){
			String s = uiparamit.next();
			if(!pluginconf.getParameters().containsKey(s)){ // UIParameter is not found, add it with default value
				sane = false;
				pluginconf.getParameters().put(s, ((UIParameter) maininterface.getUIParameters().get(s)).getStringValue());
			}
		}
		
		// remove configuration properties that are not present in the UI (old version)
		// and set as unallocated the values that do not match mmproperties
		Iterator<String> confpropit = pluginconf.getProperties().keySet().iterator();
		while(confpropit.hasNext()){
			String s = confpropit.next();
			if(!maininterface.getUIProperties().containsKey(s)){ // if the UIProperty does not exist, remove it
				pluginconf.getProperties().remove(s);
			} else { 
				if(!mmproperties.getProperties().containsKey(pluginconf.getProperties().get(s))){ // if the MMProperty does not exist, set as unallocated
					pluginconf.getProperties().remove(s);
					pluginconf.getProperties().put(s, GlobalConfiguration.KEY_UNALLOCATED);
				}
			}
		}
		
		// same with parameters
		Iterator<String> confparamit = pluginconf.getParameters().keySet().iterator();
		while(confparamit.hasNext()){
			String s = confparamit.next();
			if(!maininterface.getUIParameters().containsKey(s)){ // if the UIParameter does not exist, remove it
				pluginconf.getParameters().remove(s);
			}
		}

		return sane;
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
	 */
	public void setWizardSettings() {
		if (wizard_ != null) {
			// Retrieves the Maps and the name
			String name = wizard_.getConfigurationName();
			String pluginName = wizard_.getPluginName();
			Map<String, String> uiproperties = wizard_.getWizardProperties();
			Map<String, String> uiparameters = wizard_.getWizardParameters();

			if(configuration_.getCurrentConfigurationName().equals(name)){
				// the configuration has the same name
				configuration_.substituteConfiguration(new PluginConfiguration(name, pluginName, uiproperties, uiparameters));
			} else {
				// new configuration has a different name  
				configuration_.addConfiguration(new PluginConfiguration(name, pluginName, uiproperties, uiparameters));
			}
			
			// set current configuration
			configuration_.setCurrentConfiguration(name);
			
			writeConfiguration();

			// update system
			controller_.applyConfiguration();
		}
	}
	
	public boolean setDefaultConfiguration(String configuration){
		return configuration_.setCurrentConfiguration(configuration);
	}
	
	/**
	 * Returns the properties configuration.
	 * 
	 * @return Pairs of UIProperty names (keys) and MMProperty names (values), as well as UIProperty state names (keys)
	 * and UIProperty state values (values)
	 */
	public TreeMap<String,String> getPropertiesConfiguration(){
		return configuration_.getCurrentPluginConfiguration().getProperties();
	}
	
	/**
	 * Returns the parameters configuration/
	 * 
	 * @return Pairs of UIParameter names (keys) and their value (values)
	 */
	public TreeMap<String,String> getParametersConfiguration(){
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

}
