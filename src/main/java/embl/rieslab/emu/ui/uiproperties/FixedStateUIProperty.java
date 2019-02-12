package main.java.embl.rieslab.emu.ui.uiproperties;

import java.util.LinkedHashMap;
import java.util.Set;

import main.java.embl.rieslab.emu.ui.ConfigurablePanel;
import main.java.embl.rieslab.emu.ui.uiproperties.UIProperty;
import main.java.embl.rieslab.emu.ui.uiproperties.flag.PropertyFlag;

public class FixedStateUIProperty extends UIProperty {

	public final static String UNKNOWN = "Unknown";
	
	private LinkedHashMap<String,String> states_;
	
	public FixedStateUIProperty(ConfigurablePanel owner, String name, String description, PropertyFlag flag, LinkedHashMap<String,String> states) {
		super(owner, name, description, flag);

		states_ = states;
	}	
	
	public FixedStateUIProperty(ConfigurablePanel owner, String name, String description, LinkedHashMap<String,String> states) {
		super(owner, name, description);

		states_ = states;
	}

	/**
	 * Returns the number of states.
	 * 
	 * @return Number of states.
	 */
	public int getNumberOfStates(){
		return states_.size();
	}
	
	/**
	 * Returns the name of the state given the value.
	 * 
	 * @param value Value of the state.
	 * @return Name of the state.
	 */
	public String getStateName(String value){
		Set<String> keys = states_.keySet();
        for(String k:keys){
            if(states_.get(k).equals(value)){
            	return k;
            }
        }
		return UNKNOWN;
	}
	
	/**
	 * Returns the value corresponding to state name. If the name is not amongst 
	 * the states, returns 0.
	 * 
	 * @param name The state name.
	 * @return The value of the state, or 0 if not found.
	 */
	public String getStateValue(String statename){
		if(states_.containsKey(statename)){
			return states_.get(statename);
		}
		return UNKNOWN;
	}

	public boolean isStateName(String name){
		if(states_.containsKey(name)){
			return true;
		}
		return false;
	}	
	
	public boolean isStateValue(String value){
		Set<String> keys = states_.keySet();
        for(String k:keys){
            if(states_.get(k).equals(value)){
            	return true;
            }
        }
		return false;
	}
	
	@Override
	public void setPropertyValue(String value) {
		if (isAllocated()) {
			if(states_.containsKey(value)){ // if value is the name of one of the state
				System.out.println("Change property: "+getMMPoperty().getMMPropertyLabel()+" to "+states_.get(value));
				getMMPoperty().setStringValue(states_.get(value), this);
			} else if(isStateValue(value)){ // if value corresponds to a state
				System.out.println("Change property: "+getMMPoperty().getMMPropertyLabel()+" to "+value);
				getMMPoperty().setStringValue(value, this);
			}
		}
	}
	
	@Override
	public boolean isFixedState(){
		return true;
	}
	
	public String[] getStatesName(){
		return states_.keySet().toArray(new String[states_.size()]);
	}	
	
}
