package main.embl.rieslab.mm.uidevint.configuration;

import java.util.HashMap;
import java.util.TreeMap;


public class PluginConfiguration implements Comparable<PluginConfiguration>{
	
	public String name;
	public String plugin_name;
	public TreeMap<String,String> properties;
	public TreeMap<String,String> parameters;
	
	public PluginConfiguration(String name, HashMap<String,String> props, HashMap<String,String> params){
		this.name = name;

		properties = new TreeMap<String,String>(props);
		parameters = new TreeMap<String,String>(params);	
	}
	
	public String getName(){
		return name;
	}

	public String getPluginName(){
		return plugin_name;
	}
	
	public TreeMap<String,String> getProperties(){
		return properties;
	}
	
	public TreeMap<String,String> getParameters(){
		return parameters;
	}
	
	@Override
	public int compareTo(PluginConfiguration OtherUIPluginWrapper) {
		return name.compareTo(OtherUIPluginWrapper.getName());
	}
	
}
