package main.java.de.embl.rieslab.emu.micromanager.mmproperties;

import java.util.HashMap;
import java.util.Iterator;

import mmcorej.CMMCore;
import mmcorej.StrVector;

/**
 * Class referencing the devices loaded in Micro-Manager and their device properties. 
 * 
 * @author Joran Deschamps
 *
 */
@SuppressWarnings("rawtypes")
public class MMPropertiesRegistry {

	private CMMCore core_;
	private HashMap<String, MMDevice> devices_;
	private HashMap<String, MMProperty> properties_;
	
	/**
	 * Constructor. Calls a private initialization method to extract the devices and their properties. It ignores "COM" devices.
	 * 
	 * @param core Micro-manager core
	 */
	public MMPropertiesRegistry(CMMCore core){
		core_ = core;
		devices_ = new HashMap<String, MMDevice>();
		properties_ = new HashMap<String,MMProperty>();
		
		initialize();
	}
	
	private void initialize() {
		StrVector deviceList = core_.getLoadedDevices();
		StrVector propertyList;
		MMPropertyFactory builder = new MMPropertyFactory(core_);

		for (String device : deviceList) {
			if(!device.substring(0, 3).equals("COM")){
				MMDevice dev = new MMDevice(device);
				try {
					propertyList = core_.getDevicePropertyNames(device);
					for (String property : propertyList) {
						MMProperty prop = builder.getNewProperty(device, property);
						dev.registerProperty(prop);
						properties_.put(prop.getHash(),prop);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				devices_.put(dev.getDeviceLabel(),dev);
			}
		}
	}
	
	/**
	 * Returns the property with hash {@code propertyHash} (see {@link MMProperty} for the definition of the hash).
	 * 
	 * @param propertyHash Hash of the requested property.
	 * @return Micro-manager property.
	 */
	public MMProperty getProperty(String propertyHash){
		return properties_.get(propertyHash);
	}
	
	/**
	 * Returns the map of {@link MMProperty} indexed by their hash.
	 * 
	 * @return Micro-manager properties map.
	 */
	public HashMap<String, MMProperty> getProperties(){
		return properties_;
	}
	
	/**
	 * Returns the device with label {@code deviceLabel}.
	 * 
	 * @param deviceLabel Label of the device
	 * @return Micro-manager device
	 */
	public MMDevice getDevice(String deviceLabel){
		return devices_.get(deviceLabel);
	}
	
	/**
	 * Returns the map of {@link MMDevice} indexed by their label.
	 * 
	 * @return Micro-manager devices map.
	 */
	public HashMap<String, MMDevice> getDevices(){
		return devices_;
	}
	
	/**
	 * Returns the names of the devices in a String array. 
	 * 
	 * @return Array of device names
	 */
	public String[] getDevicesList(){
		String[] s = new String[devices_.size()];
		devices_.keySet().toArray(s);
		return s;
	}
	
	/**
	 * Adds a device to the map of devices.
	 * 
	 * @param device Device to be added.
	 */
	public void addMMDevice(MMDevice device){
		if(device.getProperties().size() > 0){
			devices_.put(device.getDeviceLabel(),device);
			properties_.putAll(device.getProperties());
		}
	}
	
	/**
	 * Checks if {@code hash} corresponds to a known Micro-manager device property.
	 * 
	 * @param hash Hash to be tested
	 * @return True if the hash corresponds to a device property, false otherwise.
	 */
	public boolean isProperty(String hash){
		return properties_.containsKey(hash);
	}

	/**
	 * Clears all Micro-manager device property listeners (which are of the class UIProperty). 
	 * Called during reloading of the system by the {@link main.java.de.embl.rieslab.emu.controller.SystemController}.
	 * 
	 */
	public void clearAllListeners(){
		Iterator<String> it = properties_.keySet().iterator();
		while(it.hasNext()){
			properties_.get(it.next()).clearAllListeners();
		}
	}
}