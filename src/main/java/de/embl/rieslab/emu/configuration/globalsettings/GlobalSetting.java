package de.embl.rieslab.emu.configuration.globalsettings;

/**
 * Defines a setting valid for the whole of EMU, regardless of which UI is loaded. 
 * 
 * @author Joran Deschamps
 *
 * @param <T> Setting's value type.
 */
public abstract class GlobalSetting<T> {

	private String description_;
	private String name_;
	private T value_;
	
	/**
	 * Constructor.
	 * 
	 * @param name Short name of the setting.
	 * @param description Description as it will appear in the help.
	 * @param default_val Default value for the setting.
	 */
	public GlobalSetting(String name, String description, T default_val){
		name_ = name;
		description_ = description;
		value_ = default_val;
	}

	/**
	 * Returns the setting value as a String. Used to display the setting in the {@link de.embl.rieslab.emu.configuration.ui.GlobalSettingsTable GlobalSettingsTable}.
	 * @see de.embl.rieslab.emu.configuration.ui.GlobalSettingsTable
	 * 
	 * @return Setting's string value.
	 */
	public String getStringValue(){
		return getStringValue(value_);
	}

	/**
	 * Returns the setting value.
	 * 
	 * @return Setting's value.
	 */
	public T getValue(){
		return value_;
	}
	
	/**
	 * Returns the name of the setting.
	 * 
	 * @return Setting's name.
	 */
	public String getName(){
		return name_;
	}	
	
	/**
	 * Returns the description of the setting.
	 * 
	 * @return Setting's description.
	 */
	public String getDescription(){
		return description_;
	}
	
	/**
	 * Convert the setting's value to a String. Used in {@link #getStringValue() getStringValue}
	 * 
	 * @param val Setting's value.
	 * @return Setting's value as a String.
	 */
	protected abstract String getStringValue(T val);
}
