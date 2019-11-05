package de.embl.rieslab.emu.micromanager.configgroups;

import java.util.ArrayList;

import de.embl.rieslab.emu.micromanager.mmproperties.MMProperty;
import mmcorej.StrVector;

/**
 * Class wrapper for a Micro-manager configuration group.
 * 
 * @author Joran Deschamps
 *
 */
public class MMConfigurationGroup {

	private ArrayList<MMProperty> affectedmmprops_;
	private StrVector configs_;
	private String name_;
	
	/**
	 * Constructor
	 * 
	 * @param name Name of the configuration group.
	 * @param config StrVector of the configuration group entries returned by Micro-manager.
	 * @param affectedmmprops List of the MMproperties affected by the configuration group.
	 */
	public MMConfigurationGroup(String name, StrVector config, ArrayList<MMProperty> affectedmmprops){
		name_ = name;
		configs_ = config;
		affectedmmprops_ = affectedmmprops;
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
	
	/**
	 * Returns the number of MM properties affected by this configuration group. 
	 * 
	 * @return Number of affected properties.
	 */
	public int getNumberOfMMProperties() {
		return affectedmmprops_.size();
	}
	
	/**
	 * Returns a list of the MMProperties affected by this configuration group.
	 * 
	 * @return List of MMproperties.
	 */
	public  ArrayList<MMProperty> getAffectedProperties(){
		return affectedmmprops_;
	}
}
