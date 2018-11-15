package main.embl.rieslab.emu.micromanager.configgroups;

import mmcorej.StrVector;

/**
 * Wraps a configuration group from Micro-manager into an object.
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
	 * @param config StrVector returned by Micro-manager and representing the different configurations in the group.
	 */
	public MMConfigurationGroup(String name, StrVector config){
		name_ = name;
		configs_ = config;
	}
	
	/**
	 * Returns the different configurations in the group.
	 * 
	 * @return
	 */
	public StrVector getConfigurations(){
		return configs_;
	}
	
	/**
	 * Return the size of the configuration group.
	 * @return
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
			if(s.equals(configs_.get(i))){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets the name of the configuration group.
	 * 
	 * @return
	 */
	public String getName(){
		return name_;
	}
}
