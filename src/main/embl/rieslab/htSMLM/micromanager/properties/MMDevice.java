package main.embl.rieslab.htSMLM.micromanager.properties;

import java.util.HashMap;

import main.embl.rieslab.htSMLM.controller.uiwizard.StringSorting;

public class MMDevice {

	@SuppressWarnings("rawtypes")
	private HashMap<String, MMProperty> properties_;
	private String label_;
	
	@SuppressWarnings("rawtypes")
	public MMDevice(String label){
		this.label_ = label;
		properties_ = new HashMap<String,MMProperty>();
	}
	
	public void registerProperty(@SuppressWarnings("rawtypes") MMProperty p){
		if(!hasProperty(p.getPropertyName())){
			properties_.put(p.getHash(), p);
		}
	}
	
	public boolean hasProperty(String s){
		return properties_.containsKey(s);
	}
	
	public String getLabel(){
		return label_;
	}
	
	public String[] getPropertiesHash(){
		String[] str = new String[properties_.size()];
		String[] sorted = StringSorting.sort(properties_.keySet().toArray(str)); 
		return sorted;
	}
}
