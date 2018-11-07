package main.embl.rieslab.emu.exceptions;

public class IncompatiblePluginConfigurationException extends Exception {

	static final long serialVersionUID = 4685765568955853230L;

	public IncompatiblePluginConfigurationException(String plugin_name, String configuration_name){
		super("The configuration \""+configuration_name+"\" is incompatible with the "+plugin_name+" plugin.");
	}
}
