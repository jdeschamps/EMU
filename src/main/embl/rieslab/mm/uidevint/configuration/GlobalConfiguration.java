package main.embl.rieslab.mm.uidevint.configuration;

import java.util.ArrayList;
import java.util.Collections;

public class GlobalConfiguration {

	private final String defaultUIName;
	private final ArrayList<PluginConfiguration> plugins;
	
	public GlobalConfiguration(String default_ui, ArrayList<PluginConfiguration> plugins){
		this.defaultUIName = default_ui;
		
		this.plugins = plugins;
		Collections.sort(this.plugins); // alphabetical sorting
	}	
	
	public ArrayList<PluginConfiguration> getPlugins(){
		return plugins;
	}
	
	public String getDefaultUIName(){
		return defaultUIName;
	}
}
