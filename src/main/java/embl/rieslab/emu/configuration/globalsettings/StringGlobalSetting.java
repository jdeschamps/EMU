package main.java.embl.rieslab.emu.configuration.globalsettings;

/**
 * A {@link GlobalSetting} with String value.
 * 
 * @author Joran Deschamps
 *
 */
public class StringGlobalSetting extends GlobalSetting<String>{

	/**
	 * Constructor.
	 * 
	 * @param name Short name of the setting.
	 * @param description Description as it will appear in the help.
	 * @param default_val Default value for the setting.
	 */
	public StringGlobalSetting(String name, String description, String default_val) {
		super(name, description, default_val);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getStringValue(String val) {
		return val;
	}

}
