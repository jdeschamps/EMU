package main.embl.rieslab.mm.uidevint.configuration;

import java.util.ArrayList;
import java.util.Collections;

public class GlobalConfiguration {

	public String default_ui;
	public ArrayList<PluginConfiguration> plugins;
	
	public GlobalConfiguration(String default_ui, ArrayList<PluginConfiguration> plugins){
		this.default_ui = default_ui;
		
		this.plugins = plugins;
		Collections.sort(this.plugins); // alphabetical sorting
	}	
	
	public ArrayList<PluginConfiguration> getPlugins(){
		return plugins;
	}
	
	public int getPluginsNumber(){
		return plugins.size();
	}
	
}
