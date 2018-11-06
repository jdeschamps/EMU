package main.embl.rieslab.mm.uidevint.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class GlobalConfiguration {

	/**
	 * Value given to unallocated UIProperty.
	 */
	public final static String KEY_UNALLOCATED = "Unallocated";
	private final static String KEY_NONE = "None";
	
	private String currentConfiguration;
	private ArrayList<PluginConfiguration> plugins;

	public GlobalConfiguration(){
		this.currentConfiguration = KEY_NONE;		
		this.plugins = new ArrayList<PluginConfiguration>();
	}	
	
	@SuppressWarnings("unchecked")
	public GlobalConfiguration(GlobalConfigurationWrapper configuration){
		this.currentConfiguration = configuration.getDefaultConfigurationName();		
		this.plugins = (ArrayList<PluginConfiguration>) configuration.getPluginConfigurations().clone();
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
	
	public GlobalConfigurationWrapper getGlobalConfiguration(){
		GlobalConfigurationWrapper conf =  new GlobalConfigurationWrapper();
		conf.setDefaultConfigurationName(currentConfiguration);
		conf.setPluginConfigurations(plugins);
		return conf;
	}
	
	public String getCurrentPluginName(){
		if(getCurrentPluginConfiguration() == null){
			return KEY_NONE;
		}
		return getCurrentPluginConfiguration().getPluginName();
	}
	
	public boolean setCurrentConfiguration(String new_default){
		Iterator<PluginConfiguration> it = plugins.iterator();
		while(it.hasNext()){
			PluginConfiguration plugin = it.next();
			
			if(plugin.getConfigurationName().equals(new_default)){
				currentConfiguration = plugin.getConfigurationName();
				return true;
			}
		}
		return false;
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
