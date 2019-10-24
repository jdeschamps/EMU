package de.embl.rieslab.emu.configuration.settings;

import de.embl.rieslab.emu.utils.utils;

/**
 * A {@link Setting} with boolean value.
 * @see Setting
 * 
 * @author Joran Deschamps
 *
 */
public class BoolSetting extends Setting<Boolean>{

	/**
	 * Constructor.
	 * 
	 * @param name Short name of the setting.
	 * @param description Description as it will appear in the help.
	 * @param default_val Default value for the setting.
	 */
	public BoolSetting(String name, String description, Boolean default_val) {
		super(name, description, Setting.SettingType.BOOL, default_val);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getStringValue(Boolean val) {
		return String.valueOf(val);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValueCompatible(String val) {
		return utils.isBool(val);
	}

	@Override
	protected Boolean getTypedValue(String val) {
		// The superclass already checks for compatibility
		return utils.convertStringToBool(val);
	}

}
