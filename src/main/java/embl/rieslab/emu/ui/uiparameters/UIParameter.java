package main.java.embl.rieslab.emu.ui.uiparameters;

import main.java.embl.rieslab.emu.ui.ConfigurablePanel;

/**
 * 
 * 
 * @author Joran Deschamps
 *
 * @param <T> Type of the parameter
 */
public abstract class UIParameter<T> {

	private String name_;
	private String description_;
	private T value_;
	private String ownername_;
	
	/**
	 * Constructor.
	 * 
	 * @param owner ConfigurablePanel that instantiated the UIParameter.
	 * @param name Name of the UIParameter.
	 * @param description Description of the UIParameters, used in the configuration wizard help.
	 */
	public UIParameter(ConfigurablePanel owner, String name, String description){
		ownername_ = owner.getLabel();
		name_ = name;
		description_ = description;
	}
	
	/**
	 * Returns the UIParameter's hash: {ConfigurablePanel's name}-{UIParameter's name}.
	 * 
	 * @return UIParameter's hash.
	 */
	public String getHash(){
		return ownername_+" - "+name_;
	}
	
	/**
	 * Returns the name of the UIParameter.
	 * 
	 * @return UIParameter's name
	 */
	public String getName(){
		return name_;
	}

	/**
	 * Returns the description of the UIParameter.
	 * 
	 * @return UIparameter's description
	 */
	public String getDescription(){
		return description_;
	}
	
	/**
	 * Returns the value of the UIParameter.
	 * 
	 * @return UIParameter's value
	 */
	public T getValue(){
		return value_;
	}
	
	/**
	 * Sets the value of the UIParameter.
	 * 
	 * @param val New value
	 */
	protected void setValue(T val){
		value_ = val;
	}
	
	/**
	 * Sets the value of the UIParameter from a String.
	 * 
	 * @param val New value as a String
	 */
	public void setStringValue(String val){
		if(isSuitable(val)){
			value_ = convertValue(val);
		}
	}
	
	/**
	 * Returns the type of the UIParameter. This method is used to check the compatibility of UIParameters.
	 * 
	 * @return The UIParameter's type
	 */
	public abstract UIParameterType getType();
	
	/**
	 * Checks if the String {@code val} is suitable for this UIParameter.
	 * 
	 * @param val String value.
	 * @return True if {@code val} is suitable, false otherwise.
	 */
	public abstract boolean isSuitable(String val);
	
	/**
	 * Converts the String {@code val} to the UIParameter's type.
	 * 
	 * @param val String to convert
	 * @return {@code} in the type of the UIParameter
	 */
	protected abstract T convertValue(String val);
	
	/**
	 * Returns the value of the UIParameter as a String.
	 * 
	 * @return String value.
	 */
	public abstract String getStringValue();
	
	/**
	 * UIParameter type.
	 * 
	 * @author Joran Deschamps
	 *
	 */
	public enum UIParameterType { 
		INTEGER("Integer"), DOUBLE("Double"), FLOAT("Float"), STRING("String"), COLOR("Color"), BOOL("Boolean"), COMBO("COMBO"), UIPROPERTY("UIProperty"); 
		
		private String value; 
		
		private UIParameterType(String value) { 
			this.value = value; 
		}

		public String getTypeValue() {
			return value;
		} 
	}; 

}
