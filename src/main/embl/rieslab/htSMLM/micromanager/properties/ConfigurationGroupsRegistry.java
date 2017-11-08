package main.embl.rieslab.htSMLM.micromanager.properties;

import java.util.HashMap;

import mmcorej.CMMCore;
import mmcorej.StrVector;

public class ConfigurationGroupsRegistry {

	private CMMCore core_;
	private HashMap<String, ConfigurationGroup> groups_;
	
	public ConfigurationGroupsRegistry(CMMCore core){
		core_ = core;
		
		groups_ = new HashMap<String, ConfigurationGroup>();
		
		retrieveConfigurationGroups();
	}

	private void retrieveConfigurationGroups() {
		StrVector groups = core_.getAvailableConfigGroups();
		
		if(groups != null){
			for(int i=0;i<groups.size();i++){
				groups_.put(groups.get(i), new ConfigurationGroup(core_.getAvailableConfigs(groups.get(i))));
			}
		}
	}
	
	public HashMap<String, ConfigurationGroup> getConfigurationGroups(){
		return groups_;
	}
}
  