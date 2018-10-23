package main.embl.rieslab.mm.uidevint.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

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
	
	public PluginConfiguration getDefaultPluginConfiguration(){
		Iterator<PluginConfiguration> it = plugins.iterator();
		while(it.hasNext()){
			PluginConfiguration plugin = it.next();
			
			if(plugin.getPluginName().equals(default_ui)){
				return plugin;
			}
		}
		return null;
	}
	
	public String[] getPluginConfigurationList(){
		int n = plugins.size();

		if(n>0){
			String[] config_list = new String[n];

			for(int i=0;i<n;i++){
				config_list[i] = plugins.get(i).getName();
			}
			Arrays.sort(config_list);
			
			return config_list;	
		} 
		return null;
	}
}
