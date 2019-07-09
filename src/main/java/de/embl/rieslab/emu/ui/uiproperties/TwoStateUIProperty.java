package de.embl.rieslab.emu.ui.uiproperties;

import de.embl.rieslab.emu.ui.ConfigurablePanel;
import de.embl.rieslab.emu.ui.uiproperties.UIProperty;
import de.embl.rieslab.emu.ui.uiproperties.flag.PropertyFlag;

/**
 * UIProperty that only accepts two states: ON or OFF. The value of these states are not known at compilation time and can be changed in
 * the configuration wizard.
 * 
 * @author Joran Deschamps
 *
 */
public class TwoStateUIProperty extends UIProperty{
	
	private final static String ON = " - On value";
	private final static String OFF = " - Off value";
	private String onstate_;
	private String offstate_;
	
	/**
	 * Constructor with a PropertyFlag.
	 * 
	 * @param owner ConfigurablePanel that instantiated the UIProperty
	 * @param label Name of the UIProperty
	 * @param description Description of the UIProperty
	 * @param flag Flag of the UIProperty
	 */
	public TwoStateUIProperty(ConfigurablePanel owner, String label, String description, PropertyFlag flag) {
		super(owner, label, description, flag);
	}	
	
	/**
	 * Constructor without PropertyFlag, the flag being set to NoFlag.
	 * 
	 * @param owner ConfigurablePanel that instantiated the UIProperty
	 * @param label Name of the UIProperty
	 * @param description Description of the UIProperty
	 */
	public TwoStateUIProperty(ConfigurablePanel owner, String label, String description) {
		super(owner, label, description);
	}
	
	/**
	 * Returns the static String used to refer to the TwoStateUIProperty's ON state.
	 * 
	 * @return ON state's name
	 */
	public static String getOnStateName(){
		return ON;
	}
	
	/**
	 * Returns the static String used to refer to the TwoStateUIProperty's OFF state.
	 * 
	 * @return OFF state's name
	 */
	public static String getOffStateName(){
		return OFF;
	}

	/**
	 * Sets the value of the ON state.
	 * 
	 * @param newOnValue New value of the ON state
	 * @return True if the value was correctly set, false otherwise (for instance if the value is not allowed)
	 */
	public boolean setOnStateValue(String newOnValue){
		if(isValueAllowed(newOnValue)){
			onstate_ = newOnValue;	
			return true;
		}
		return false;
	}
	
	/**
	 * Sets the value of the OFF state.
	 * 
	 * @param newOffvalue New value of the OFF state
	 * @return True if the value was correctly set, false otherwise (for instance if the value is not allowed)
	 */
	public boolean setOffStateValue(String newOffvalue){
		if(isValueAllowed(newOffvalue)){
			offstate_ = newOffvalue;
			return true;
		}
		return false;
	}

	/**
	 * Returns the value of the ON state.
	 * 
	 * @return ON state value
	 */
	public String getOnStateValue(){
		return onstate_;
	}
	
	/**
	 * Returns the value of the OFF state.
	 * 
	 * @return OFF state value
	 */
	public String getOffStateValue(){
		return offstate_;
	}
	
	/**
	 * Sets the value of the UIproperty to {@code newValue} if {@code newValue}
	 * is either equal to the ON state or to the OFF state, or their respective value.
	 */
	@Override
	public void setPropertyValue(String newValue) {
		if(isAssigned()) {
			if (newValue.equals(getOnStateName()) || newValue.equals(getOnStateValue())) {
				getMMProperty().setStringValue(getOnStateValue(), this);
			} else if(newValue.equals(getOffStateName()) || newValue.equals(getOffStateValue())) {
				getMMProperty().setStringValue(getOffStateValue(), this);
			}
		}
	}
}
