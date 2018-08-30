package main.embl.rieslab.mm.uidevint.mmproperties;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

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
		if(!hasProperty(p.getMMPropertyLabel())){
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
			if(properties_.get(prop).getMMPropertyLabel().equals(s)){
				return true;
			}
		}
		return false;
	}
	
	public String getFriendlyPropertyHash(String s){
		Iterator<String> keys = properties_.keySet().iterator();
		while(keys.hasNext()){
			String prop = keys.next();
			if(properties_.get(prop).getMMPropertyLabel().equals(s)){
				return prop;
			}
		}
		return null;
	}
	
	public String getLabel(){
		return label_;
	}

	public String[] getPropertiesHash(){
		String[] str = properties_.keySet().toArray(new String[properties_.size()]); 
		Arrays.sort(str);
		return str;
	}
	
	public String[] getPropertiesFriendlyName(){
		String[] str = new String[properties_.size()];

		Iterator<String> keys = properties_.keySet().iterator();
		int count = 0;
		while(keys.hasNext()){
			String prop = keys.next();
			str[count] = properties_.get(prop).getMMPropertyLabel();
			count ++;
		}
		
		Arrays.sort(str); 
		return str;
	}
	
}
