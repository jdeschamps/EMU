package main.java.de.embl.rieslab.emu.ui.uiproperties;

import java.util.LinkedHashMap;
import java.util.Set;

import main.java.de.embl.rieslab.emu.ui.ConfigurablePanel;
import main.java.de.embl.rieslab.emu.ui.uiproperties.UIProperty;
import main.java.de.embl.rieslab.emu.ui.uiproperties.flag.PropertyFlag;

/**
 * UIProperty with a fixed number of ordered states whose values are known at compilation time.
 * 
 * @author Joran Deschamps
 *
 */
public class ImmutableMultiStateUIProperty extends UIProperty {

	public final static String UNKNOWN = "Unknown";
	
	private LinkedHashMap<String,String> states_;
	
	/**
	 * Constructor with PropertyFlag/
	 * 
	 * @param owner ConfigurablePanel that instantiated the UIProperty
	 * @param label Name of the UIProperty
	 * @param description Description of the UIProperty
	 * @param flag Flag of the UIProperty
	 * @param states Allowed states
	 */
	public ImmutableMultiStateUIProperty(ConfigurablePanel owner, String label, String description, PropertyFlag flag, LinkedHashMap<String,String> states) {
		super(owner, label, description, flag);

		states_ = states;
	}	
	/**
	 * Constructor without PropertyFlag/
	 * 
	 * @param owner ConfigurablePanel that instantiated the UIProperty
	 * @param label Name of the UIProperty
	 * @param description Description of the UIProperty
	 * @param states Allowed states
	 */
	public ImmutableMultiStateUIProperty(ConfigurablePanel owner, String label, String description, LinkedHashMap<String,String> states) {
		super(owner, label, description);

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
	 * Returns the value corresponding to state name. If {@code statename} is not amongst 
	 * the states, returns 0.
	 * 
	 * @param statename The state name.
	 * @return The value of the state, or 0 if not found.
	 */
	public String getStateValue(String statename){
		if(states_.containsKey(statename)){
			return states_.get(statename);
		}
		return UNKNOWN;
	}

	/**
	 * Checks if {@code stateName} is a known state name.
	 *  
	 * @param stateName State name to check
	 * @return True if it is, false otherwise
	 */
	public boolean isStateName(String stateName){
		if(states_.containsKey(stateName)){
			return true;
		}
		return false;
	}	
	
	/**
	 * Checks if {@code stateValue} is a known state value.
	 *  
	 * @param stateValue State value to check
	 * @return True if it is, false otherwise
	 */
	public boolean isStateValue(String stateValue){
		Set<String> keys = states_.keySet();
        for(String k:keys){
            if(states_.get(k).equals(stateValue)){
            	return true;
            }
        }
		return false;
	}
	
	/**
	 * Sets the value of the MMProperty to {@code value}, only if {@code value} is equal to
	 * either a valid state name or a valid state value.
	 * 
	 */
	@Override
	public void setPropertyValue(String value) {
		if (isAssigned()) {
			if(states_.containsKey(value)){ // if value is the name of one of the state
				getMMProperty().setStringValue(states_.get(value), this);
			} else if(isStateValue(value)){ // if value corresponds to a state
				getMMProperty().setStringValue(value, this);
			}
		}
	}

	/**
	 * Returns the state names.
	 * 
	 * @return State names
	 */
	public String[] getStatesName(){
		return states_.keySet().toArray(new String[states_.size()]);
	}	
	
}
