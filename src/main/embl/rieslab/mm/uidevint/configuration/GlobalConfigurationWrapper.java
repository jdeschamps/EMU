package main.embl.rieslab.mm.uidevint.configuration;

import java.util.ArrayList;
import java.util.Collections;

public class GlobalConfigurationWrapper {

	private final String defaultConfigurationName;
	private final ArrayList<PluginConfiguration> plugins;
	
	public GlobalConfigurationWrapper(String default_ui, ArrayList<PluginConfiguration> plugins){
		this.defaultConfigurationName = default_ui;
		
		this.plugins = plugins;
		Collections.sort(this.plugins); // alphabetical sorting
	}	
	
	public ArrayList<PluginConfiguration> getPlugins(){
		return plugins;
	}
	
	public String getDefaultUIName(){
		return defaultConfigurationName;
	}
}
