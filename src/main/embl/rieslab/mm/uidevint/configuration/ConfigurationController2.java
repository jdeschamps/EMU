package main.embl.rieslab.mm.uidevint.configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import main.embl.rieslab.mm.uidevint.configuration.ui.ConfigurationWizard;
import main.embl.rieslab.mm.uidevint.controller.SystemController;
import main.embl.rieslab.mm.uidevint.mmproperties.MMProperties;
import main.embl.rieslab.mm.uidevint.ui.uiparameters.UIParameter;
import main.embl.rieslab.mm.uidevint.ui.uiproperties.MultiStateUIProperty;
import main.embl.rieslab.mm.uidevint.ui.uiproperties.SingleStateUIProperty;
import main.embl.rieslab.mm.uidevint.ui.uiproperties.TwoStateUIProperty;
import main.embl.rieslab.mm.uidevint.ui.uiproperties.UIProperty;

/**
 * This class reads and writes a configuration file detailing the allocation of MMProperties to UIProperties,
 * as well as the values of UIProperty states and UIParameters. In addition, the Configuration can launch a
 * ConfigurationWizard to allow the user to interactively allocate the device properties from Micro-manager
 * to the UIProperties and set all values.
 * 
 * @author Joran Deschamps
 *
 */
public class ConfigurationController2 {

	// Keys
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

	private HashMap<String,String> uiproperties_;
	private HashMap<String,String> uiparameters_;
	
	private SystemController controller_;
	private ConfigurationWizard wizard_;
	
	public ConfigurationController2(SystemController controller){
		controller_ = controller;
		uiproperties_ = new HashMap<String,String>();
		uiparameters_ = new HashMap<String,String>();
	}
	
	/**
	 * Reads default configuration in the Micro-manager folder.
	 * 
	 * @param uipropertySet UI properties 
	 * @param uiparameterSet UI parameters
	 * @param mmproperties Micro-manager device properties
	 * @return True if loading successful
	 */
	@SuppressWarnings("rawtypes")
	public boolean readDefaultConfiguration(HashMap<String,UIProperty> uipropertySet, HashMap<String,UIParameter> uiparameterSet,
			MMProperties mmproperties){		
		return readConfiguration(uipropertySet,uiparameterSet,mmproperties,new File(SystemConstants.CONFIG_NAME));
	}
	
	/**
	 * Reads a configuration.
	 * 
	 * @param uipropertySet UIProperties of the current UI 
	 * @param uiparameterSet UIParameters of the current UI
	 * @param mmproperties Micro-manager device properties
	 * @param f Configuration to be read
	 * @return True if loading successful
	 */
	@SuppressWarnings("rawtypes")
	public boolean readConfiguration(HashMap<String,UIProperty> uipropertySet, HashMap<String,UIParameter> uiparameterSet,
			MMProperties mmproperties, File f){		
		File configFile = f;
		
		try {
		    Properties props = new Properties();
		    FileReader reader = new FileReader(configFile);

		    // loads Property list from file
		    props.load(reader);
		 
		    String mmhash, uihash, paramhash, paramval;
		    
		    // iterates through all uiproperties, if not found in the configuration file then mark unallocated
		    Iterator<String> it = uipropertySet.keySet().iterator();
		    while(it.hasNext()){
		    	uihash = it.next(); // get next line
		    
		    	// looks for the uihash in the Property list
		    	mmhash = props.getProperty(KEY_UIPROPERTY+uihash, KEY_UNALLOCATED);
		    	
		    	if(mmproperties.isProperty(mmhash)){ // if mmhash is an actual mmproperty found in the system
		    		uiproperties_.put(uihash, mmhash);
		    	} else { // else marks as unallocated
		    		uiproperties_.put(uihash, KEY_UNALLOCATED);
		    	}
		    	
		    	// Depending on the type of UIProperty (defined in the UI), entries are created to store the state values
		    	if(uipropertySet.get(uihash).isTwoState()){ // if two-state uiproperty, creates an entry for the on and off state
		    		String onval = uihash+TwoStateUIProperty.getOnStateName();
			    	String val = props.getProperty(KEY_UIPROPERTY+onval, KEY_UNALLOCATED);
		    		uiproperties_.put(onval, val);

		    		String offval = uihash+TwoStateUIProperty.getOffStateName();
			    	val = props.getProperty(KEY_UIPROPERTY+offval, KEY_UNALLOCATED);
		    		uiproperties_.put(offval, val);  
		    	} else if (uipropertySet.get(uihash).isSingleState()){ // if single-state, creates an entry for the state value
		    		String valname = uihash+SingleStateUIProperty.getValueName();
			    	String val = props.getProperty(KEY_UIPROPERTY+valname, KEY_UNALLOCATED);
		    		uiproperties_.put(valname, val);
		    	} else if (uipropertySet.get(uihash).isMultiState()){ // if multi-state, creates an entry for the number of states
		    		int numval = ((MultiStateUIProperty) uipropertySet.get(uihash)).getNumberOfStates();
		    		String name, val;
		    		for(int i=0;i<numval;i++){
		    			name = uihash+MultiStateUIProperty.getStateName(i);
		    			val =  props.getProperty(KEY_UIPROPERTY+name, KEY_UNALLOCATED);
			    		uiproperties_.put(name, val);
		    		}
		    	}

		    }	 
		    
		    // Parameters
		    it = uiparameterSet.keySet().iterator();
		    while(it.hasNext()){
		    	paramhash = it.next();
		    	paramval = props.getProperty(KEY_UIPARAMETER+paramhash, uiparameterSet.get(paramhash).getStringValue());
		    	uiparameters_.put(paramhash, paramval);
		    }	 
		    
		    reader.close();
		} catch (FileNotFoundException ex) {			
		    return false;
		} catch (IOException ex) {
			// show error message?
		    return false;
		}
		return true;
	}

	/**
	 * Writes the configuration (properties)
	 * 
	 * @param uipropertySet HashMap with the MMProperty or state values are indexed by the UIProperty names (or state names)
	 * @param uiparameterSet HashMap containing the values of each UIParameter, indexed by the UIParameter name
	 * @return true if successful, false otherwise
	 */
	public boolean saveConfiguration(HashMap<String,String> uipropertySet, HashMap<String,String> uiparameterSet){
		File configFile = new File(SystemConstants.CONFIG_NAME);
		 
		try {
		    Properties props = new Properties();

		    String uihash, paramhash;

		    // Writes the UIProperty name and corresponding state or MMProperty
		    Iterator<String> it = uipropertySet.keySet().iterator();
		    while(it.hasNext()){
		    	uihash = it.next();
		    	props.setProperty(KEY_UIPROPERTY+uihash, uipropertySet.get(uihash));
		    }
		    
		    // Wirtes the UIParameter names and values
		    it = uiparameterSet.keySet().iterator();
		    while(it.hasNext()){
		    	paramhash = it.next();
		    	props.setProperty(KEY_UIPARAMETER+paramhash, uiparameterSet.get(paramhash));
		    }
		    
		    FileWriter writer = new FileWriter(configFile);
		    props.store(writer, "UI properties");
		    writer.close();
		} catch (FileNotFoundException ex) {
		    return false;
		} catch (IOException ex) {
		    return false;
		}
		return true;
	}
	
	/**
	 * Creates a new ConfigurationWizard, allowing the user to create a configuration. This function is called when no configuration
	 * has been read. 
	 * 
	 * @param uipropertySet HashMap with the MMProperty or state values are indexed by the UIProperty names (or state names)
	 * @param uiparameterSet HashMap containing the values of each UIParameter, indexed by the UIParameter name
	 * @param mmproperties MMProperties of the current Micro-Manager instance
	 */
	@SuppressWarnings("rawtypes")
	public void launchNewWizard(HashMap<String,UIProperty> uipropertySet, HashMap<String,UIParameter> uiparameterSet,
			MMProperties mmproperties){
		// launch wizard
		wizard_ = new ConfigurationWizard(this);
		wizard_.newConfiguration(uipropertySet, uiparameterSet, mmproperties);
	}

	/**
	 * Runs a new instance of a ConfigurationWizard if not wizard is already running. This allows the user to modify the current configuration.
	 * 
	 * @param uipropertySet HashMap with the MMProperty or state values are indexed by the UIProperty names (or state names)
	 * @param uiparameterSet HashMap containing the values of each UIParameter, indexed by the UIParameter name
	 * @param mmproperties MMProperties of the current Micro-Manager instance
	 * @return false if a ConfigurationWizard is already running, true otherwise
	 */
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
