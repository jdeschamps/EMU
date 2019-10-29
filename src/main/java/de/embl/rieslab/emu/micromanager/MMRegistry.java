package de.embl.rieslab.emu.micromanager;

import java.util.ArrayList;
import java.util.Iterator;

import org.micromanager.Studio;

import de.embl.rieslab.emu.controller.log.Logger;
import de.embl.rieslab.emu.micromanager.configgroups.MMConfigurationGroupsRegistry;
import de.embl.rieslab.emu.micromanager.mmproperties.ConfigGroupAsMMProperty;
import de.embl.rieslab.emu.micromanager.mmproperties.MMDevice;
import de.embl.rieslab.emu.micromanager.mmproperties.MMPropertiesRegistry;
import de.embl.rieslab.emu.micromanager.mmproperties.MMProperty;

/**
 * Class instantiating the {@link MMPropertiesRegistry} and {@link MMConfigurationGroupsRegistry}. In addition, it
 * registers the configuration groups as MMProperties and add them to the MMPropertiesRegistry.
 * 
 * @author Joran Deschamps
 *
 */
public class MMRegistry {

	private MMPropertiesRegistry mmPropRegistry_; // holds the Micro-Manager device properties
	private MMConfigurationGroupsRegistry mmConfigGroupsRegistry_; // holds the configuration groups from Micro-manager
	private Studio studio_;
	private Logger logger_;
	
	/**
	 * Constructor. Instantiate the {@link MMPropertiesRegistry} and {@link MMConfigurationGroupsRegistry} and register
	 * the configuration groups as MMProperties.
	 * 
	 * @param studio Micro-Manager studio
	 */
	public MMRegistry(Studio studio, Logger logger) {
		
		studio_ = studio;
		logger_ = logger;
		
		// extracts MM properties
		mmPropRegistry_ = new MMPropertiesRegistry(studio_.getCMMCore(), logger_);
		mmConfigGroupsRegistry_ = new MMConfigurationGroupsRegistry(studio_.getCMMCore(), mmPropRegistry_);
		
		// registers mmconfigs as mmproperties (so that they can be linked to UIProperties)
		registerMMConfAsDevice();
	}
	
	/**
	 * Returns the {@link MMPropertiesRegistry}.
	 * 
	 * @return Instance of {@link MMPropertiesRegistry}
	 */
	public MMPropertiesRegistry getMMPropertiesRegistry() {
		return mmPropRegistry_;
	}
	
	/**
	 * Returns the {@link MMConfigurationGroupsRegistry}.
	 * 
	 * @return Instance of {@link MMConfigurationGroupsRegistry}
	 */
	public MMConfigurationGroupsRegistry getMMConfigurationGroupsRegistry() {
		return mmConfigGroupsRegistry_;
	}
	
	/**
	 * Wraps the configuration groups as MMProperties and adds them to the MMPropertiesRegistry.
	 * 
	 */
	@SuppressWarnings("rawtypes")
	private void registerMMConfAsDevice(){
		MMDevice dev = new MMDevice(ConfigGroupAsMMProperty.KEY_MMCONFDEVICE);

		Iterator<String> it = mmConfigGroupsRegistry_.getMMConfigurationGroups().keySet().iterator();
		while(it.hasNext()){
			String group = it.next();
			String[] values = mmConfigGroupsRegistry_.getMMConfigurationGroups().get(group).getConfigurations().toArray();
			ArrayList<MMProperty> affectedmmprops = mmConfigGroupsRegistry_.getMMConfigurationGroups().get(group).getAffectedProperties();
			
			dev.registerProperty(new ConfigGroupAsMMProperty(studio_.app(), studio_.core(), logger_, group, values, affectedmmprops));
		}
		
		mmPropRegistry_.addMMDevice(dev);
	}
	
}
