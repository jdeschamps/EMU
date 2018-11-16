package main.embl.rieslab.emu.micromanager.mmproperties;

import java.util.HashMap;
import java.util.Iterator;

import mmcorej.CMMCore;
import mmcorej.StrVector;

@SuppressWarnings("rawtypes")
public class MMProperties {

	CMMCore core_;
	HashMap<String, MMDevice> devices_;
	HashMap<String, MMProperty> properties_;
	
	public MMProperties(CMMCore core){
		core_ = core;
		devices_ = new HashMap<String, MMDevice>();
		properties_ = new HashMap<String,MMProperty>();
		
		initialize();
	}
	
	public void initialize() {
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
				devices_.put(dev.getLabel(),dev);
			}
		}
	}
	
	public MMProperty getProperty(String hash){
		return properties_.get(hash);
	}
	
	public HashMap<String, MMProperty> getProperties(){
		return properties_;
	}
	
	public MMDevice getDevice(String label){
		return devices_.get(label);
	}
	
	public HashMap<String, MMDevice> getDevices(){
		return devices_;
	}
	
	public String[] getDevicesList(){
		String[] s = new String[devices_.size()];
		devices_.keySet().toArray(s);
		return s;
	}
	
	public void addConfigGroupAsMMProperties(MMDevice mmconfdevice){
		if(mmconfdevice.getProperties().size() > 0){
			devices_.put(mmconfdevice.getLabel(),mmconfdevice);
			properties_.putAll(mmconfdevice.getProperties());
		}
	}
	
	public boolean isProperty(String hash){
		return properties_.containsKey(hash);
	}

	public void clearAllListeners(){
		Iterator<String> it = properties_.keySet().iterator();
		while(it.hasNext()){
			properties_.get(it.next()).clearAllListeners();
		}
	}
}
