package main.embl.rieslab.mm.uidevint.configuration.globalsettings;

public class StringGlobalSettings extends GlobalSettings<String>{

	public StringGlobalSettings(String name, String description, String default_val) {
		super(name, description, default_val);
	}

	@Override
	protected String getStringValue(String val) {
		return val;
	}

}
