package main.embl.rieslab.htSMLM.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import main.embl.rieslab.htSMLM.controller.uiwizard.UIWizard;
import main.embl.rieslab.htSMLM.micromanager.properties.MMProperties;
import main.embl.rieslab.htSMLM.ui.uiparameters.UIParameter;
import main.embl.rieslab.htSMLM.ui.uiproperties.ToggleUIProperty;
import main.embl.rieslab.htSMLM.ui.uiproperties.UIProperty;

public class Configuration {

	// Keys
	public static String KEY_UNALLOCATED = "Unallocated";
	public static String KEY_ENTERVALUE = "Enter value";
	public static String KEY_UIPROPERTY = "UI Property: ";
	public static String KEY_UIPARAMETER = "UI Parameter: ";
	
	// Configuration
	public static String CONFIG_NAME = "config.uicfg";
	public static String CONFIG_EXT = "uicfg";

	private HashMap<String,String> uiproperties_;
	private HashMap<String,String> uiparameters_;
	
	private SystemController controller_;
	private UIWizard wizard_;
	
	public Configuration(SystemController controller){
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
	public boolean readConfiguration(HashMap<String,UIProperty> uipropertySet, HashMap<String,UIParameter> uiparameterSet,
			MMProperties mmproperties){		
		File configFile = new File(CONFIG_NAME);
		 
		try {
		    Properties props = new Properties();
		    FileReader reader = new FileReader(configFile);

		    props.load(reader);
		 
		    String mmhash, uihash, paramhash, paramval;
		    
		    Iterator<String> it = uipropertySet.keySet().iterator();
		    while(it.hasNext()){
		    	uihash = it.next();
		    	mmhash = props.getProperty(KEY_UIPROPERTY+uihash, KEY_UNALLOCATED);
		    	
		    	if(mmproperties.isProperty(mmhash)){
		    		uiproperties_.put(uihash, mmhash);
		    	} else {
		    		uiproperties_.put(uihash, KEY_UNALLOCATED);
		    	}
		    	
		    	if(uipropertySet.get(uihash).isToggle()){
		    		String onval = uihash+ToggleUIProperty.getToggleOnName();
			    	mmhash = props.getProperty(KEY_UIPROPERTY+onval, KEY_UNALLOCATED);
		    		uiproperties_.put(onval, mmhash);

		    		String offval = uihash+ToggleUIProperty.getToggleOffName();
			    	mmhash = props.getProperty(KEY_UIPROPERTY+offval, KEY_UNALLOCATED);
		    		uiproperties_.put(offval, mmhash);
		    	}
		    }	 
		    
		    it = uiparameterSet.keySet().iterator();
		    while(it.hasNext()){
		    	paramhash = it.next();
		    	paramval = props.getProperty(KEY_UIPARAMETER+paramhash, KEY_UNALLOCATED);
		    
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
	 * Reads a configuration in the Micro-manager folder.
	 * 
	 * @param uipropertySet UI properties 
	 * @param uiparameterSet UI parameters
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

		    props.load(reader);
		    
		    if(props.isEmpty()){
		    	return false;
		    } 
		    
		    uiproperties_.clear();
		    uiparameters_.clear();
		   
		    String mmhash, uihash, paramhash, paramval;
		    
		    Iterator<String> it = uipropertySet.keySet().iterator();
		    while(it.hasNext()){
		    	uihash = it.next();
		    	mmhash = props.getProperty(KEY_UIPROPERTY+uihash, KEY_UNALLOCATED);
		    	
		    	if(mmproperties.isProperty(mmhash)){
		    		uiproperties_.put(uihash, mmhash);
		    	} else {
		    		uiproperties_.put(uihash, KEY_UNALLOCATED);
		    	}
		    	
		    	if(uipropertySet.get(uihash).isToggle()){
		    		String onval = uihash+ToggleUIProperty.getToggleOnName();
			    	mmhash = props.getProperty(KEY_UIPROPERTY+onval, KEY_UNALLOCATED);
			    	uiproperties_.put(onval, mmhash);

		    		String offval = uihash+ToggleUIProperty.getToggleOffName();
			    	mmhash = props.getProperty(KEY_UIPROPERTY+offval, KEY_UNALLOCATED);
			    	uiproperties_.put(offval, mmhash);
		    	}
		    }	 
		    
		    it = uiparameterSet.keySet().iterator();
		    while(it.hasNext()){
		    	paramhash = it.next();
		    	paramval = props.getProperty(KEY_UIPARAMETER+paramhash, KEY_UNALLOCATED);
		    
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

	public boolean saveConfiguration(HashMap<String,String> uipropertySet, HashMap<String,String> uiparameterSet){
		File configFile = new File(CONFIG_NAME);
		 
		try {
		    Properties props = new Properties();

		    String uihash, paramhash;

		    Iterator<String> it = uipropertySet.keySet().iterator();
		    while(it.hasNext()){
		    	uihash = it.next();
		    	props.setProperty(KEY_UIPROPERTY+uihash, uipropertySet.get(uihash));
		    }
		    
		    it = uiparameterSet.keySet().iterator();
		    while(it.hasNext()){
		    	paramhash = it.next();
		    	props.setProperty(KEY_UIPARAMETER+paramhash, uiparameterSet.get(paramhash));
		    }
		    
		    FileWriter writer = new FileWriter(configFile);
		    props.store(writer, "htSMLM properties");
		    writer.close();
		} catch (FileNotFoundException ex) {
		    return false;
		} catch (IOException ex) {
		    return false;
		}
		return true;
	}
	
	@SuppressWarnings("rawtypes")
	public void launchNewWizard(HashMap<String,UIProperty> uipropertySet, HashMap<String,UIParameter> uiparameterSet,
			MMProperties mmproperties){
		// launch wizard
		wizard_ = new UIWizard(this);
		wizard_.newConfiguration(uipropertySet, uiparameterSet, mmproperties);
	}

	public boolean launchWizard(){
		if(!isWizardRunning()){	
			System.out.println("Launch wizard");
			wizard_ = new UIWizard(this);
			wizard_.existingConfiguration(uiproperties_, uiparameters_);
			return true;
		}
		return false;
	}
	
	public boolean isWizardRunning(){
		if(wizard_ == null){
			return false;
		}
		return wizard_.isRunning();
	}

	public void getWizardSettings() {
		if (wizard_ != null) {
			uiproperties_ = wizard_.getWizardProperties();
			uiparameters_ = wizard_.getWizardParameters();

			saveConfiguration(uiproperties_, uiparameters_);

			controller_.getConfiguration();
		}
	}
	
	public HashMap<String,String> getPropertiesConfiguration(){
		return uiproperties_;
	}
	
	public HashMap<String,String> getParametersConfiguration(){
		return uiparameters_;
	}
	
	public void shutDown(){
		if (wizard_ != null) {
			wizard_.shutDown();
		}
	}
}
