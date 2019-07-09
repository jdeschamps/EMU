package de.embl.rieslab.emu.exceptions;

/**
 * Exception thrown when the {@link de.embl.rieslab.emu.controller.SystemController} is asked
 * to reload the system with a UIPlugin and PluginConfiguration that are not compatible.
 * 
 * @author Joran Deschamps
 *
 */
public class IncompatiblePluginConfigurationException extends Exception {

	static final long serialVersionUID = 4685765568955853230L;

	public IncompatiblePluginConfigurationException(String plugin_name, String configuration_name){
		super("The configuration \""+configuration_name+"\" is incompatible with the "+plugin_name+" plugin.");
	}
}
