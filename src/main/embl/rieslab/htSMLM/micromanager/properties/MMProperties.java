package main.embl.rieslab.htSMLM.micromanager.properties;

import java.util.HashMap;

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
		MMPropertyBuilder builder = new MMPropertyBuilder(core_);

		for (String device : deviceList) {
			if(!device.substring(0, 3).equals("COM")){
							MMDevice dev = new MMDevice(device);
			//System.out.println("- Adding new device: "+device);
			try {
				propertyList = core_.getDevicePropertyNames(device);
				for (String property : propertyList) {
					//System.out.println("--- Adding new property: "+property);
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
	
	public boolean isProperty(String hash){
		return properties_.containsKey(hash);
	}

}
