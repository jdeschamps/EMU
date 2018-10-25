package main.embl.rieslab.mm.uidevint.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class GlobalConfigurationWrapper {

	/**
	 * Value given to unallocated UIProperty.
	 */
	public final static String KEY_UNALLOCATED = "Unallocated";
	
	private String currentConfiguration;
	private ArrayList<PluginConfiguration> plugins;

	public GlobalConfigurationWrapper(){
		this.currentConfiguration = "None";		
		this.plugins = new ArrayList<PluginConfiguration>();
	}	
	
	@SuppressWarnings("unchecked")
	public GlobalConfigurationWrapper(GlobalConfiguration configuration){
		this.currentConfiguration = configuration.getDefaultUIName();		
		this.plugins = (ArrayList<PluginConfiguration>) configuration.getPlugins().clone();
	}	
	
	public ArrayList<PluginConfiguration> getPlugins(){
		return plugins;
	}
	
	public int getPluginsNumber(){
		return plugins.size();
	}

	public String getCurrentConfigurationName(){
		return currentConfiguration;
	}
	
	public String getCurrentPluginName(){
		return getCurrentPluginConfiguration().getPluginName();
	}
	
	public void setCurrentConfiguration(String new_default){
		Iterator<PluginConfiguration> it = plugins.iterator();
		while(it.hasNext()){
			PluginConfiguration plugin = it.next();
			
			if(plugin.getConfigurationName().equals(new_default)){
				currentConfiguration = plugin.getConfigurationName();
			}
		}
	}
	
	public PluginConfiguration getCurrentPluginConfiguration(){
		return getPluginConfiguration(currentConfiguration);
	}
	
	public PluginConfiguration getPluginConfiguration(String name){
		Iterator<PluginConfiguration> it = plugins.iterator();
		while(it.hasNext()){
			PluginConfiguration plugin = it.next();
			
			if(plugin.getConfigurationName().equals(name)){
				return plugin;
			}
		}
		return null;
	}	
	
	public String[] getCompatibleConfigurations(String pluginName) {
		if(plugins.isEmpty()){
			return null;
		}
		
		ArrayList<String> configs = new ArrayList<String>();
		Iterator<PluginConfiguration> it = plugins.iterator();
		
		while(it.hasNext()){
			PluginConfiguration plug = it.next();

			if(plug.getPluginName().equals(pluginName)){
				configs.add(plug.getConfigurationName());
			}
		} 
		
		String[] confs = (String[]) configs.toArray(new String[0]);
		Arrays.sort(confs);
		
		return confs;
	}
	
	public String[] getPluginConfigurationList(){
		int n = plugins.size();

		if(n>0){
			String[] config_list = new String[n];

			for(int i=0;i<n;i++){
				config_list[i] = plugins.get(i).getConfigurationName();
			}
			Arrays.sort(config_list);
			
			return config_list;	
		} 
		return null;
	}

	public void substituteConfiguration(PluginConfiguration pluginConfiguration) {
		for(int i=0; i<plugins.size();i++){
			if(plugins.get(i).compareTo(pluginConfiguration) == 0){
				plugins.remove(i);
				plugins.add(pluginConfiguration);
			}
		}	
	}
	
	public boolean addConfiguration(PluginConfiguration pluginConfiguration){
		boolean exists = false;
		for(int i=0; i<plugins.size();i++){
			if(plugins.get(i).compareTo(pluginConfiguration) == 0){
				exists = true;
			}
		}
		
		if(!exists){
			plugins.add(pluginConfiguration);
			return true;
		}
		
		return exists;
	}

	
}
