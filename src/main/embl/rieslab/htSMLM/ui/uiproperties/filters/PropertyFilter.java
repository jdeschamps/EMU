package main.embl.rieslab.htSMLM.ui.uiproperties.filters;

import java.util.HashMap;
import java.util.Iterator;

import main.embl.rieslab.htSMLM.ui.uiproperties.UIProperty;

public abstract class PropertyFilter {

	public HashMap<String, UIProperty> filterProperties(HashMap<String, UIProperty> properties){
		HashMap<String, UIProperty> filteredproperties = new HashMap<String, UIProperty>();
		
		Iterator<String> it = properties.keySet().iterator();
		String s;
		while(it.hasNext()){
			s = it.next();
			if(!filterOut(properties.get(s))){
				filteredproperties.put(s, properties.get(s));
			}
		}
		
		return filteredproperties;
	}

	public HashMap<String, UIProperty> filterProperties(HashMap<String, UIProperty> properties, PropertyFilter additionalfilter){
		HashMap<String, UIProperty> filteredproperties = new HashMap<String, UIProperty>();
		
		Iterator<String> it = additionalfilter.filterProperties(properties).keySet().iterator();
		String s;
		while(it.hasNext()){
			s = it.next();
			if(!filterOut(properties.get(s))){
				filteredproperties.put(s, properties.get(s));
			}
		}
		
		return filteredproperties;
	}
	
	
	public abstract boolean filterOut(UIProperty property);
}
