package main.java.embl.rieslab.emu.micromanager.configgroups;

import java.util.HashMap;
import java.util.Iterator;

import mmcorej.CMMCore;
import mmcorej.StrVector;

/**
 * Class holding a HashMap of the {@link MMConfigurationGroup}s and their name as displayed in the 
 * current Micro-manager session.
 * 
 * @author Joran Deschamps
 *
 */
public class MMConfigurationGroupsRegistry {

	private CMMCore core_;
	private HashMap<String, MMConfigurationGroup> groups_;
	
	/**
	 * The constructor receives the current Micro-manager core instance and extracts all the
	 * configuration groups, building a HashMap<group name, ConfigurationGroup>.
	 * 
	 * @param core Micro-manager CMMCore instance.
	 */
	public MMConfigurationGroupsRegistry(CMMCore core){
		core_ = core;
		
		groups_ = new HashMap<String, MMConfigurationGroup>();
		
		retrieveConfigurationGroups();
	}

	private void retrieveConfigurationGroups() {
		StrVector groups = core_.getAvailableConfigGroups();
		
		if(groups != null){
			for(int i=0;i<groups.size();i++){
				groups_.put(groups.get(i), new MMConfigurationGroup(groups.get(i),core_.getAvailableConfigs(groups.get(i))));
			}
		}
	}
	
	/**
	 * Returns the {@link MMConfigurationGroup}s indexed in a map by their name.
	 * 
	 * @return HashMap of the {@link MMConfigurationGroup}s.
	 */
	public HashMap<String, MMConfigurationGroup> getMMConfigurationGroups(){
		return groups_;
	}
	
	/**
	 * Returns a HashMap mapping the configuration group names (keys) and an array of 
	 * string representing the names of the different configuration within each group.
	 * 
	 * @return HashMap of the channels of each {@link MMConfigurationGroup} indexed by the name of the group.
	 */
	public HashMap<String, String[]> getMMConfigurationChannels(){
		HashMap<String, String[]> map = new HashMap<String, String[]>();
		
		Iterator<String> it = groups_.keySet().iterator();
		String s;
		while(it.hasNext()){
			s=it.next();
			map.put(s, groups_.get(s).getConfigurations().toArray());
		}
		return map;
	}
	
	/**
	 * Checks if the configuration group {@code mmconfig} exists.
	 * 
	 * @param mmconfig Name of a {@link MMConfigurationGroup}
	 * @return True if {@code mmconfig} exists, false otherwise.
	 */
	public boolean hasMMConfigurationGroup(String mmconfig){
		return groups_.containsKey(mmconfig);
	}
	
	/**
	 * Returns the current configuration of the MM configuration group {@code mmConfigurationGroup}.
	 * 
	 * @param mmConfigurationGroup MM configuration group/
	 * @return The state of mmConfigurationGroup.
	 */
	public String getCurrentMMConfigurationChannel(String mmConfigurationGroup){
		if(hasMMConfigurationGroup(mmConfigurationGroup)){
			try {
				return core_.getCurrentConfig(mmConfigurationGroup);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
  