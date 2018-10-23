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

import main.embl.rieslab.mm.uidevint.configuration.ui.ConfigurationWizard;
import main.embl.rieslab.mm.uidevint.controller.SystemController;
import main.embl.rieslab.mm.uidevint.mmproperties.MMProperties;
import main.embl.rieslab.mm.uidevint.ui.uiparameters.UIParameter;
import main.embl.rieslab.mm.uidevint.ui.uiproperties.MultiStateUIProperty;
import main.embl.rieslab.mm.uidevint.ui.uiproperties.SingleStateUIProperty;
import main.embl.rieslab.mm.uidevint.ui.uiproperties.TwoStateUIProperty;
import main.embl.rieslab.mm.uidevint.ui.uiproperties.UIProperty;

public class ConfigurationController {

	/**
	 * Value given to unallocated UIProperty.
	 */
	public final static String KEY_UNALLOCATED = "Unallocated";
	/**
	 * Value given to unallocated UIProperty states and UIParameters values.
	 */
	public final static String KEY_ENTERVALUE = "Enter value";
	
	private final static String KEY_UIPROPERTY = "UI Property: ";
	private final static String KEY_UIPARAMETER = "UI Parameter: ";
	private final static String KEY_DEFAULTUI = "Default UI: ";
	
	private Map<String,String> uiproperties_;
	private Map<String,String> uiparameters_;
	
	private SystemController controller_;
	private ConfigurationWizard wizard_;
	private GlobalConfiguration configuration_;
	
	public ConfigurationController(SystemController controller){
		controller_ = controller;
		uiproperties_ = new HashMap<String,String>();
		uiparameters_ = new HashMap<String,String>();
	}
	

	private Object getDefaultConfigurationFile() {
		return new File(SystemConstants.CONFIG_NAME);
	}

	private boolean grabConfiguration(Object read) {
		return false;
	}



	@SuppressWarnings("rawtypes")
	public boolean readDefaultConfiguration(HashMap<String,UIProperty> uipropertySet, HashMap<String,UIParameter> uiparameterSet,
			MMProperties mmproperties){		
		return grabConfiguration(ConfigurationIO.read(getDefaultConfigurationFile(),uipropertySet, uiparameterSet, mmproperties));
	}
	

	@SuppressWarnings("rawtypes")
	public boolean readConfiguration(HashMap<String,UIProperty> uipropertySet, HashMap<String,UIParameter> uiparameterSet,
			MMProperties mmproperties, File f){		
		return grabConfiguration(ConfigurationIO.read(f,uipropertySet, uiparameterSet, mmproperties));
	}

	public boolean writeConfiguration(){
		return ConfigurationIO.write(getDefaultConfigurationFile(), getConfiguration());
	}
	

	private GlobalConfiguration getConfiguration(){
		return null;
	}


	@SuppressWarnings("rawtypes")
	public void launchNewWizard(HashMap<String,UIProperty> uipropertySet, HashMap<String,UIParameter> uiparameterSet,
			MMProperties mmproperties){
		// launch wizard
		wizard_ = new ConfigurationWizard(this);
		wizard_.newConfiguration(uipropertySet, uiparameterSet, mmproperties);
	}


	@SuppressWarnings("rawtypes")
	public boolean launchWizard(HashMap<String,UIProperty> uipropertySet, 
			HashMap<String,UIParameter> uiparameterSet, MMProperties mmproperties){
		if(!isWizardRunning()){	
			wizard_ = new ConfigurationWizard(this);
			wizard_.existingConfiguration(uipropertySet, uiparameterSet, mmproperties, uiproperties_, uiparameters_);
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
			// Retrieves the HashMap
			uiproperties_ = wizard_.getWizardProperties();
			uiparameters_ = wizard_.getWizardParameters();

			saveConfiguration(uiproperties_, uiparameters_); // writes to file

			// update system
			controller_.setConfiguration();
		}
	}
	
	/**
	 * Returns the properties configuration.
	 * 
	 * @return Pairs of UIProperty names (keys) and MMProperty names (values), as well as UIProperty state names (keys)
	 * and UIProperty state values (values)
	 */
	public HashMap<String,String> getPropertiesConfiguration(){
		return uiproperties_;
	}
	
	/**
	 * Returns the parameters configuration/
	 * 
	 * @return Pairs of UIParameter names (keys) and their value (values)
	 */
	public HashMap<String,String> getParametersConfiguration(){
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
