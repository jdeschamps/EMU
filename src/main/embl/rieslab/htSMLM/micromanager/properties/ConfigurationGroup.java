package main.embl.rieslab.htSMLM.micromanager.properties;

import mmcorej.StrVector;

public class ConfigurationGroup {

	StrVector configs_;
	
	public ConfigurationGroup(StrVector config){
		configs_ = config;
	}
	
	public StrVector getConfigurations(){
		return configs_;
	}
	
	public int getGroupSize(){
		return (int) configs_.size();
	}
	
	public String getConfiguration(int ind){
		if(ind<configs_.size()){
			return configs_.get(ind);
		}
		return null;
	}
}
