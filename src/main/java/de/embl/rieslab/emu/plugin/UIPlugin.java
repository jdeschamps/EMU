package main.java.de.embl.rieslab.emu.plugin;

import main.java.de.embl.rieslab.emu.controller.SystemController;
import main.java.de.embl.rieslab.emu.ui.ConfigurableMainFrame;

/**
 * Interface for an EMU plugin. The plugin main class should implement this interface and
 * the jar should contain META-INF/services/main.java.embl.rieslab.emu.plugin.EMUPlugin file
 * containing the package location of the plugin (example: "main.java.myui.myplugin").
 * 
 * @author Joran Deschamps
 *
 */
public interface UIPlugin {

	/**
	 * Returns the name of the plugin, used to identify it and load if set in the configuration.
	 * 
	 * @return Plugin's name.
	 */
	public String getName();
	
	
	/**
	 * Returns a PropertyMainFrame.
	 * 
	 * @param controller EMU system controller.
	 * @return ConfigurableMainFrame of the plugin
	 */
	public ConfigurableMainFrame getMainFrame(SystemController controller);
	
}
