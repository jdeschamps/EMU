package main.embl.rieslab.emu.configuration.globalsettings;

public class BoolGlobalSettings extends GlobalSettings<Boolean>{

	public BoolGlobalSettings(String name, String description, Boolean default_val) {
		super(name, description, default_val);
	}

	@Override
	protected String getStringValue(Boolean val) {
		return String.valueOf(val);
	}

}
