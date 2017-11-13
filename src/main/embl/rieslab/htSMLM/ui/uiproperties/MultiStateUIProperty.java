package main.embl.rieslab.htSMLM.ui.uiproperties;

import main.embl.rieslab.htSMLM.ui.PropertyPanel;

public class MultiStateUIProperty extends UIProperty{

	public static String STATE = " state ";
	
	private String[] states_;
	private String[] statesname_;
	
	public MultiStateUIProperty(PropertyPanel owner, String name, String description, PropertyFlag flag, int size) {
		super(owner, name, description, flag);

		states_ = new String[size];
		statesname_ = new String[size];
		for(int i=0;i<size;i++){
			states_[i] = "";
			statesname_[i] = "State"+i;
		}
	}

	/**
	 * Sets values for the states of the UI property. If the array of values is too long, then the supernumerary values are ignored. 
	 * If the array is too short, then the corresponding states are modified while the other ones are left unchanged.
	 * 
	 * @param vals Array of values.
	 */
	public void setStateValues(String[] vals){
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
	}
	
	public void setStatesName(String[] vals){
		if(vals.length == statesname_.length){
			statesname_ = vals;
		} else if (vals.length > statesname_.length){
			for(int i=0; i<statesname_.length;i++){
				statesname_[i] = vals[i];
			}
		} else {
			for(int i=0; i<vals.length;i++){
				statesname_[i] = vals[i];
			}
		}
	}
	
	/**
	 * Sets the value of a specific state.
	 * 
	 * @param state State to modify.
	 * @param value Value to set the state to.
	 */
	public void setStateValue(int state, String value){
		if(state<states_.length){
			states_[state] = value;
		}
	}
	
	public void setStateName(int state, String value){
		if(state<statesname_.length){
			statesname_[state] = value;
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
		if(pos<states_.length){
			return states_[pos];
		}
		return "";
	}
	
	@Override
	public void setPropertyValue(String val) {
		if (isAllocated()) {
			for(int i=0;i<states_.length;i++){
				if(states_[i].equals(val)){
					getMMPoperty().setStringValue(val, this);
				}
			}
		}
	}
	
	@Override
	public boolean isMultiState(){
		return true;
	}
	
	public static String getStateName(int i){
		return STATE+i;
	}
	
	public static String getGenericStateName(){
		return ".*"+STATE+"\\d+";
	}
	
	public String[] getStates(){
		return states_;
	}	
	
	public String[] getStatesName(){
		return statesname_;
	}
}

