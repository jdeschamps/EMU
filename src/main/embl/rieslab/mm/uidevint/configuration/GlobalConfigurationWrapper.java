package main.embl.rieslab.mm.uidevint.configuration;

import java.util.ArrayList;
import java.util.Collections;

public class GlobalConfigurationWrapper {

	private String defaultConfigurationName;
	private ArrayList<PluginConfiguration> pluginConfigurations;
	
	public GlobalConfigurationWrapper(){
		// do nothing
	}
	
	public ArrayList<PluginConfiguration> getPluginConfigurations(){
		Collections.sort(this.pluginConfigurations); // alphabetical sorting
		return pluginConfigurations;
	}
	
	public String getDefaultConfigurationName(){
		return defaultConfigurationName;
	}

	public void setPluginConfigurations(ArrayList<PluginConfiguration> pluginConfigurations){
		this.pluginConfigurations = pluginConfigurations;
		Collections.sort(this.pluginConfigurations); // alphabetical sorting
	}
	
	public void setDefaultConfigurationName(String defaultConfigurationName){
		this.defaultConfigurationName = defaultConfigurationName;
	}

}
