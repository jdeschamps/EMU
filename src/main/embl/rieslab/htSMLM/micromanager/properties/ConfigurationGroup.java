package main.embl.rieslab.htSMLM.micromanager.properties;

import mmcorej.StrVector;

public class ConfigurationGroup {

	private StrVector configs_;
	private String name_;
	
	public ConfigurationGroup(String name, StrVector config){
		name_ = name;
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
	
	public boolean hasConfiguration(String s){
		for(int i=0;i<configs_.size();i++){
			if(s.equals(configs_.get(i))){
				return true;
			}
		}
		return false;
	}
	
	public String getName(){
		return name_;
	}
}
