package de.embl.rieslab.emu.ui.uiproperties;

import de.embl.rieslab.emu.ui.ConfigurablePanel;
import de.embl.rieslab.emu.ui.uiproperties.UIProperty;
import de.embl.rieslab.emu.ui.uiproperties.flag.PropertyFlag;
import de.embl.rieslab.emu.utils.utils;

/**
 * A UIProperty with multiple allowed states, whose values are unknown at compilation time. Upon instantiation
 * the number of states is set, while the user can change the values of each state in the configuration wizard.
 * 
 * @author Joran Deschamps
 *
 */
public class MultiStateUIProperty extends UIProperty{

	public final static String STATE = " state ";
	
	private String[] states_;
	private String[] statenames_;
	
	/**
	 * Constructor with a PropertyFlag.
	 * 
	 * @param owner ConfigurablePanel that instantiated the UIProperty
	 * @param label Name of the UIProperty
	 * @param description Description of the UIProperty
	 * @param flag Flag of the UIProperty
	 * @param size Number of allowed states
	 */
	public MultiStateUIProperty(ConfigurablePanel owner, String label, String description, PropertyFlag flag, int size) {
		super(owner, label, description, flag);

		states_ = new String[size];
		statenames_ = new String[size];
		for(int i=0;i<size;i++){
			states_[i] = "";
			statenames_[i] = STATE+i;
		}
	}	
	/**
	 * Constructor without a PropertyFlag defining the number of states.
	 * 
	 * @param owner ConfigurablePanel that instantiated the UIProperty
	 * @param label Name of the UIProperty
	 * @param description Description of the UIProperty
	 * @param size Number of allowed states
	 */
	public MultiStateUIProperty(ConfigurablePanel owner, String label, String description, int size) {
		super(owner, label, description);

		if(size <= 0) {
			throw new IllegalArgumentException("The number of state of a MultiStateUIProperty cannot be negative or zero.");
		}
		
		states_ = new String[size];
		statenames_ = new String[size];
		for(int i=0;i<size;i++){
			states_[i] = "";
			statenames_[i] = STATE+i;
		}
	}

	/**
	 * Sets values for the states of the UI property. If the array of values is too long, then the supernumerary values are ignored. 
	 * If the array is too short, then the corresponding states are modified while the other ones are left unchanged.
	 * 
	 * @param vals Array of values.
	 */
	public boolean setStateValues(String[] vals){
		if(vals == null){
			return false;
		}
		
		for(int i=0;i<vals.length;i++){
			if(vals[i] == null || !isValueAllowed(vals[i])){
				return false;
			}
		}
		
		if(vals.length == states_.length){
			states_ = vals;
		} else if (vals.length > states_.length){
			for(int i=0; i<states_.length;i++){
				states_[i] = vals[i];
			}
		} else {
			for(int i=0; i<vals.length;i++){
				states_[i] = vals[i];
			}
		}
		return true;
	}
	
	/**
	 * Gives names to the states.
	 * 
	 * @param stateNames State names
	 */
	public boolean setStateNames(String[] stateNames){
		for(String s: stateNames) {
			if(s == null) {
				throw new NullPointerException("State names cannot be null.");
			} else if(s.equals("")) {
				throw new IllegalArgumentException("State names cannot be empty.");
			}
		}
		
		if(stateNames.length == statenames_.length){
			statenames_ = stateNames;
			return true;
		} else if (stateNames.length > statenames_.length){
			for(int i=0; i<statenames_.length;i++){
				statenames_[i] = stateNames[i];
			}
			return true;
		} else {
			for(int i=0; i<stateNames.length;i++){
				statenames_[i] = stateNames[i];
			}
			return true;
		}
	}
	
	/**
	 * Returns the number of states.
	 * 
	 * @return Number of states.
	 */
	public int getNumberOfStates(){
		return states_.length;
	}
	
	/**
	 * Returns the position of the states corresponding to the value val. If the value is not amongst 
	 * the states, returns 0.
	 * 
	 * @param val The state value.
	 * @return The position of value among the states, or 0 if not found.
	 */
	public int getStatePositionNumber(String val){
		for(int i=0;i<states_.length;i++){
			if(states_[i].equals(val)){
				return i;
			}
		}
		return 0;
	}
	
	/**
	 * Returns the value of the state in position pos.
	 * 
	 * @param pos Position of the state.
	 * @return Value of the state.
	 */
	public String getStateValue(int pos){
		if(pos >= 0 && pos<states_.length){
			return states_[pos];
		}
		return "";
	}
	
	/**
	 * Returns the array of values.
	 * 
	 * @return State array.
	 */
	public String[] getStateValues(){
		return states_;
	}
	
	/**
	 * Returns the name of the state in position pos.
	 * 
	 * @param pos Position of the state.
	 * @return Name of the state.
	 */
	public String getStateName(int pos){
		if(pos >= 0 && pos<states_.length){
			return statenames_[pos];
		}
		return "";
	}
	
	/**
	 * Returns the name of the state corresponding to value. The first
	 * state will be returned if the value does not correspond to any state.
	 * 
	 * @param value Value
	 * @return Name of the state, or the first state if value does not correspond to any state.
	 */
	public String getStateNameFromValue(String value){
		for(int i=0;i<states_.length;i++){
			if(states_[i].equals(value)){
				return statenames_[i];
			}
		}	
		return statenames_[0];
	}
	
	
	/**
	 * Sets the value of the MMProperty to {@code val} if {@code val}
	 * equals either one of the state values or one of the state names.
	 * 
	 */
	@Override
	public boolean setPropertyValue(String val) {
		if (isAssigned()) {
			// checks if it corresponds to a valid state
			for (int i = 0; i < states_.length; i++) {
				if (states_[i].equals(val)) {
					return getMMProperty().setValue(val, this);
				}
			}
			// or if it corresponds to a valid state name 
			for (int i = 0; i < statenames_.length; i++) {
				if (statenames_[i].equals(val)) {
					return getMMProperty().setValue(states_[i], this);
				}
			}
			// otherwise, accept indices
			if (utils.isInteger(val)) {
				int v = Integer.parseInt(val);
				if (v >= 0 && v < states_.length) {
					return getMMProperty().setValue(states_[v], this);
				}

			}
		}
		return false;
	}
	
	/**
	 * Returns the generic name for state i.
	 * 
	 * @param i State number
	 * @return Generic name
	 */
	public static String getConfigurationStateName(int i){
		return STATE+i;
	}
	
	/**
	 * Returns the generic state name for String search and comparison: ".*"+STATE+"\\d+".
	 * 
	 * @return generic state name
	 */
	public static String getGenericStateName(){
		return ".*"+STATE+"\\d+";
	}
		
	/**
	 * Returns the names of the states.
	 * 
	 * @return State names
	 */
	public String[] getStatesName(){
		return statenames_;
	}
	
	/**
	 * Returns a String describing the UIPropertyType of the UIProperty.
	 * 
	 * @return UIProperty type
	 */
	public UIPropertyType getType() {
		return UIPropertyType.MULTISTATE;
	}
}

