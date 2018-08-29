package main.embl.rieslab.mm.uidevint.ui.uiproperties.filters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import main.embl.rieslab.mm.uidevint.ui.uiproperties.UIProperty;

public abstract class PropertyFilter {
	
	private PropertyFilter additionalfilter_;
	
	public PropertyFilter(){
	}
	
	public PropertyFilter(PropertyFilter additionalfilter){
		additionalfilter_ = additionalfilter;
	}

	public HashMap<String, UIProperty> filterProperties(HashMap<String, UIProperty> properties){
		HashMap<String, UIProperty> filteredproperties = new HashMap<String, UIProperty>();
		
		Iterator<String> it;
		if(additionalfilter_ != null){
			it = additionalfilter_.filterProperties(properties).keySet().iterator();
		} else {
			it = properties.keySet().iterator();
		}
		
		String s;
		while(it.hasNext()){
			s = it.next();
			if(!filterOut(properties.get(s))){
				filteredproperties.put(s, properties.get(s));
			}
		}
		
		return filteredproperties;
	}
	

	public String[] filterStringProperties(HashMap<String, UIProperty> properties){
		HashMap<String, UIProperty> filteredproperties = new HashMap<String, UIProperty>();
		
		Iterator<String> it;
		if(additionalfilter_ != null){
			it = additionalfilter_.filterProperties(properties).keySet().iterator();
		} else {
			it = properties.keySet().iterator();
		}
		
		String s;
		while(it.hasNext()){
			s = it.next();
			if(!filterOut(properties.get(s))){
				filteredproperties.put(s, properties.get(s));
			}
		}
		
		String[] stringprop = filteredproperties.keySet().toArray(new String[0]);
		Arrays.sort(stringprop);
		
		return stringprop;
	}
	
	public HashMap<String, UIProperty> filteredProperties(HashMap<String, UIProperty> properties){
		HashMap<String, UIProperty> filteredproperties = new HashMap<String, UIProperty>();
		
		Iterator<String> it;
		if(additionalfilter_ != null){
			it = additionalfilter_.filterProperties(properties).keySet().iterator();
		} else {
			it = properties.keySet().iterator();
		}
		
		String s;
		while(it.hasNext()){
			s = it.next();
			if(filterOut(properties.get(s))){
				filteredproperties.put(s, properties.get(s));
			}
		}
		
		return filteredproperties;
	}

	protected abstract boolean filterOut(UIProperty property);
}
