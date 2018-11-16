package main.embl.rieslab.emu.micromanager.configgroups;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.micromanager.api.ScriptInterface;

import main.embl.rieslab.emu.micromanager.mmproperties.ConfigGroupAsMMProperty;
import main.embl.rieslab.emu.micromanager.mmproperties.MMDevice;
import main.embl.rieslab.emu.micromanager.mmproperties.MMProperties;
import main.embl.rieslab.emu.micromanager.mmproperties.MMProperty;
import mmcorej.CMMCore;
import mmcorej.Configuration;
import mmcorej.StrVector;

/**
 * Class holding a HashMap of the ConfigurationGroups and their name as present in the 
 * current Micro-manager session.
 * 
 * @author Joran Deschamps
 *
 */
public class MMConfigurationGroupsRegistry {

	public final static String KEY_MMCONFDEVICE = "Configurations";
	
	private CMMCore core_;
	private ScriptInterface script_;
	private HashMap<String, MMConfigurationGroup> groups_;
	
	/**
	 * The constructor receives the current Micro-manager core instance and extract all the
	 * configuration groups, building a HashMap<group name, ConfigurationGroup>.
	 * 
	 * @param core
	 */
	public MMConfigurationGroupsRegistry(ScriptInterface script, CMMCore core){
		core_ = core;
		script_ = script;
		
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
	 * Returns the configuration groups HashMap
	 * 
	 * @return
	 */
	public HashMap<String, MMConfigurationGroup> getMMConfigurationGroups(){
		return groups_;
	}
	
	/**
	 * Returns a HashMap mapping the configuration group names (keys) and an array of 
	 * string representing the names of the different configuration within each group.
	 * 
	 * @return
	 */
	public HashMap<String, String[]> getMMConfigurationChannelsMap(){
		HashMap<String, String[]> map = new HashMap<String, String[]>();
		
		Iterator<String> it = groups_.keySet().iterator();
		String s;
		while(it.hasNext()){
			s=it.next();
			map.put(s, groups_.get(s).getConfigurations().toArray());
		}
		return map;
	}
	
	public boolean hasMMConfigurationGroup(String mmconfig){
		return groups_.containsKey(mmconfig);
	}
	
	public String getMMConfigurationState(String mmconfig){
		if(hasMMConfigurationGroup(mmconfig)){
			try {
				return core_.getCurrentConfig(mmconfig);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	@SuppressWarnings("rawtypes")
	public void registerMMConfAsDevice(MMProperties mmproperties){
		MMDevice dev = new MMDevice(KEY_MMCONFDEVICE);

		Iterator<String> it = groups_.keySet().iterator();
		while(it.hasNext()){
			String group = it.next();
			String[] values = groups_.get(group).getConfigurations().toArray();
			
			ArrayList<MMProperty> affectedmmprops = new ArrayList<MMProperty>();
			Configuration conf;
			try {
				conf = core_.getConfigGroupState(group);
				for(int i=0;i<conf.size();i++){
					affectedmmprops.add(mmproperties.getProperties().get(conf.getSetting(i).getDeviceLabel()+"-"+conf.getSetting(i).getPropertyName()));
				}
				
				dev.registerProperty(new ConfigGroupAsMMProperty(script_, core_, KEY_MMCONFDEVICE, group, values, affectedmmprops));

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		mmproperties.addConfigGroupAsMMProperties(dev);
	}
	
}
  