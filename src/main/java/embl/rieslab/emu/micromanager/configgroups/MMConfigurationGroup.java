package main.java.embl.rieslab.emu.micromanager.configgroups;

import mmcorej.StrVector;

// I am a bit confused with the micromanager terminology (configuration, group and channel) and I am not sure if it was conserved in MM2.

/**
 * Class wrapper for a Micro-manager configuration group.
 * 
 * @author Joran Deschamps
 *
 */
public class MMConfigurationGroup {

	private StrVector configs_;
	private String name_;
	
	/**
	 * Constructor
	 * 
	 * @param name Name of the configuration group
	 * @param config StrVector returned by Micro-manager and representing the different channels in the configuration group.
	 */
	public MMConfigurationGroup(String name, StrVector config){
		name_ = name;
		configs_ = config;
	}
	
	/**
	 * Returns the different configurations in the group.
	 * 
	 * @return Vector of the group's configurations.
	 */
	public StrVector getConfigurations(){
		return configs_;
	}
	
	/**
	 * Return the size of the configuration group.
	 * 
	 * @return Size of the configuration group.
	 */
	public int getGroupSize(){
		return (int) configs_.size();
	}
	
	/**
	 * Returns the label of the configuration indexed by ind.
	 * 
	 * @param ind Index of the configuration in the group.
	 * @return Name of the configuration.
	 */
	public String getConfiguration(int ind){
		if(ind<configs_.size()){
			return configs_.get(ind);
		}
		return null;
	}
	
	/**
	 * Tests if the configuration s is in the configuration group.
	 * @param s Name of the configuration
	 * @return True if the configuration is part of the group, false otherwise.
	 */
	public boolean hasConfiguration(String s){	
		for(int i=0;i<configs_.size();i++){
			if(configs_.get(i).equals(s)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets the name of the configuration group.
	 * 
	 * @return Name of the configuration group.
	 */
	public String getName(){
		return name_;
	}
}
