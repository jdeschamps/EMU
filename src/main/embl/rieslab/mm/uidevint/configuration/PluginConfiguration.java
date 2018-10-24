package main.embl.rieslab.mm.uidevint.configuration;

import java.util.Map;
import java.util.TreeMap;


public class PluginConfiguration implements Comparable<PluginConfiguration>{
	
	private String configurationName;
	private String pluginName;
	private TreeMap<String,String> properties;
	private TreeMap<String,String> parameters;
	
	public PluginConfiguration(String configurationName, String pluginName, Map<String,String> props, Map<String,String> params){
		this.configurationName = configurationName;
		this.pluginName = pluginName;

		properties = new TreeMap<String,String>(props);
		parameters = new TreeMap<String,String>(params);	
	}
	
	public String getConfigurationName(){
		return configurationName;
	}

	public String getPluginName(){
		return pluginName;
	}
	
	public TreeMap<String,String> getProperties(){
		return properties;
	}
	
	public TreeMap<String,String> getParameters(){
		return parameters;
	}
	
	@Override
	public int compareTo(PluginConfiguration OtherUIPluginWrapper) {
		return configurationName.compareTo(OtherUIPluginWrapper.getConfigurationName());
	}
	
}
