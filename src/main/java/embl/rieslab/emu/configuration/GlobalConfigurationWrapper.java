package main.java.embl.rieslab.emu.configuration;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Wraps a {@link GlobalConfiguration} in a simple class in order to write and
 * read the class using jackson ObjectMapper.
 * 
 * @author Joran Deschamps
 *
 */
public class GlobalConfigurationWrapper {

	private String defaultConfigurationName;
	private boolean enableUnallocatedWarnings;
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
	
	public boolean getEnableUnallocatedWarnings(){
		return enableUnallocatedWarnings;
	}

	public void setPluginConfigurations(ArrayList<PluginConfiguration> pluginConfigurations){
		this.pluginConfigurations = pluginConfigurations;
		Collections.sort(this.pluginConfigurations); // alphabetical sorting
	}

	public void setDefaultConfigurationName(String defaultConfigurationName){
		this.defaultConfigurationName = defaultConfigurationName;
	}
	
	public void setEnableUnallocatedWarnings(boolean enableUnallocatedWarnings){
		this.enableUnallocatedWarnings = enableUnallocatedWarnings;
	}

}
