package de.embl.rieslab.emu.configuration.globalsettings;

/**
 * A {@link GlobalSetting} with boolean value.
 * @see GlobalSetting
 * 
 * @author Joran Deschamps
 *
 */
public class BoolGlobalSetting extends GlobalSetting<Boolean>{

	/**
	 * Constructor.
	 * 
	 * @param name Short name of the setting.
	 * @param description Description as it will appear in the help.
	 * @param default_val Default value for the setting.
	 */
	public BoolGlobalSetting(String name, String description, Boolean default_val) {
		super(name, description, default_val);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getStringValue(Boolean val) {
		return String.valueOf(val);
	}

}
