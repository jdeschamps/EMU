package main.java.embl.rieslab.emu.micromanager;

import java.util.ArrayList;
import java.util.Iterator;

import org.micromanager.Studio;

import main.java.embl.rieslab.emu.micromanager.configgroups.MMConfigurationGroupsRegistry;
import main.java.embl.rieslab.emu.micromanager.mmproperties.ConfigGroupAsMMProperty;
import main.java.embl.rieslab.emu.micromanager.mmproperties.MMDevice;
import main.java.embl.rieslab.emu.micromanager.mmproperties.MMPropertiesRegistry;
import main.java.embl.rieslab.emu.micromanager.mmproperties.MMProperty;
import mmcorej.Configuration;

public class MMRegistry {

	private MMPropertiesRegistry mmproperties_; // holds the Micro-Manager device properties
	private MMConfigurationGroupsRegistry mmconfiggroups_; // holds the configuration groups from Micro-manager
	private Studio studio_;
	
	public MMRegistry(Studio studio) {
		
		studio_ = studio;
		
		// extracts MM properties
		mmproperties_ = new MMPropertiesRegistry(studio_.getCMMCore());
		mmconfiggroups_ = new MMConfigurationGroupsRegistry(studio_.getCMMCore());
		
		// registers mmconfigs as mmproperties (so that they can be linked to UIProperties)
		registerMMConfAsDevice();
	}
	
	public MMPropertiesRegistry getMMPropertiesRegistry() {
		return mmproperties_;
	}
	
	public MMConfigurationGroupsRegistry getMMConfigurationGroupsRegistry() {
		return mmconfiggroups_;
	}
	
	@SuppressWarnings("rawtypes")
	private void registerMMConfAsDevice(){
		MMDevice dev = new MMDevice(MMConfigurationGroupsRegistry.KEY_MMCONFDEVICE);

		Iterator<String> it = mmconfiggroups_.getMMConfigurationGroups().keySet().iterator();
		while(it.hasNext()){
			String group = it.next();
			String[] values = mmconfiggroups_.getMMConfigurationGroups().get(group).getConfigurations().toArray();
			
			ArrayList<MMProperty> affectedmmprops = new ArrayList<MMProperty>();
			Configuration conf;
			try {
				conf = studio_.core().getConfigGroupState(group);
				for(int i=0;i<conf.size();i++){
					affectedmmprops.add(mmproperties_.getProperties().get(conf.getSetting(i).getDeviceLabel()+"-"+conf.getSetting(i).getPropertyName()));
				}
				
				dev.registerProperty(new ConfigGroupAsMMProperty(studio_.app(), studio_.core(), 
						MMConfigurationGroupsRegistry.KEY_MMCONFDEVICE, group, values, affectedmmprops));

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		mmproperties_.addMMDevice(dev);
	}
	
}
