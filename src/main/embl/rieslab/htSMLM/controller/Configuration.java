package main.embl.rieslab.htSMLM.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import main.embl.rieslab.htSMLM.controller.uiwizard.UIWizard;
import main.embl.rieslab.htSMLM.micromanager.properties.MMProperties;
import main.embl.rieslab.htSMLM.ui.uiparameters.UIParameter;
import main.embl.rieslab.htSMLM.ui.uiproperties.UIProperty;

public class Configuration {

	// Keys
	public static String KEY_UNALLOCATED = "Unallocated";
	public static String KEY_UIPROPERTY = "UI Property: ";
	public static String KEY_UIPARAMETER = "UI Parameter: ";

	private HashMap<String,String> uiproperties_;
	private HashMap<String,String> uiparameters_;
	
	public Configuration(){
		uiproperties_ = new HashMap<String,String>();
		uiparameters_ = new HashMap<String,String>();
	}
	
	@SuppressWarnings("rawtypes")
	public boolean readConfiguration(HashMap<String,UIProperty> uipropertySet, HashMap<String,UIParameter> uiparameterSet,
			MMProperties mmproperties){		
		File configFile = new File("config.properties");
		 
		try {
		    Properties props = new Properties();
		    FileReader reader = new FileReader(configFile);

		    props.load(reader);
		 
		    String mmhash, uihash, paramhash, paramval;
		    
		    Iterator<String> it = uipropertySet.keySet().iterator();
		    while(it.hasNext()){
		    	uihash = it.next();
		    	mmhash = props.getProperty(KEY_UIPROPERTY+uihash, KEY_UNALLOCATED);
		    	
		    	uiproperties_.put(uihash, mmhash);
		    }	 
		    
		    it = uiparameterSet.keySet().iterator();
		    while(it.hasNext()){
		    	paramhash = it.next();
		    	paramval = props.getProperty(KEY_UIPARAMETER+paramhash, KEY_UNALLOCATED);
		    
		    	uiparameters_.put(paramhash, paramval);
		    }	 
		    
		    reader.close();
		} catch (FileNotFoundException ex) {
			
			// launch wizard
			UIWizard wizard = new UIWizard();
			wizard.newConfiguration(uipropertySet, uiparameterSet, mmproperties);
			
			// thread wait for end?
			
			if(true){
				uiproperties_ = wizard.getWizProperties();
				uiparameters_ = wizard.getWizParameters();
			} 
			
			
		    return false;
		} catch (IOException ex) {
			// show error message?
			
		    return false;
		}
		return true;
	}

	public boolean saveConfiguration(HashMap<String,String> uipropertySet, HashMap<String,String> uiparameterSet){
		File configFile = new File("config.properties");
		 
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
	
	public HashMap<String,String> getPropertiesConfiguration(){
		return uiproperties_;
	}
	
	public HashMap<String,String> getParametersConfiguration(){
		return uiparameters_;
	}
}
