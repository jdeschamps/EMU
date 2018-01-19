package main.embl.rieslab.htSMLM.micromanager.properties;

import java.util.HashMap;
import java.util.Iterator;

import main.embl.rieslab.htSMLM.util.StringSorting;

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

	public boolean hasFriendlyProperty(String s){
		Iterator<String> keys = properties_.keySet().iterator();
		while(keys.hasNext()){
			String prop = keys.next();
			if(properties_.get(prop).getPropertyName().equals(s)){
				return true;
			}
		}
		return false;
	}
	
	public String getFriendlyPropertyHash(String s){
		Iterator<String> keys = properties_.keySet().iterator();
		while(keys.hasNext()){
			String prop = keys.next();
			if(properties_.get(prop).getPropertyName().equals(s)){
				return prop;
			}
		}
		return null;
	}
	
	public String getLabel(){
		return label_;
	}

	public String[] getPropertiesHash(){
		String[] str = new String[properties_.size()];
		String[] sorted = StringSorting.sort(properties_.keySet().toArray(str)); 
		return sorted;
	}
	
	public String[] getPropertiesFriendlyName(){
		String[] str = new String[properties_.size()];

		Iterator<String> keys = properties_.keySet().iterator();
		int count = 0;
		while(keys.hasNext()){
			String prop = keys.next();
			str[count] = properties_.get(prop).getPropertyName();
			count ++;
		}
		
		String[] sorted = StringSorting.sort(str); 
		return sorted;
	}
	
}
