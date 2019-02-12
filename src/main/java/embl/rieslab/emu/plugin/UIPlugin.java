package main.java.embl.rieslab.emu.plugin;

import main.java.embl.rieslab.emu.controller.SystemController;
import main.java.embl.rieslab.emu.ui.ConfigurableMainFrame;

public interface UIPlugin {

	/**
	 * Returns the name of the plugin, used to identify it and load if set in the configuration.
	 * 
	 * @return
	 */
	public String getName();
	
	
	/**
	 * Returns a PropertyMainFrame.
	 * 
	 * @param controller
	 * @return
	 */
	public ConfigurableMainFrame getMainFrame(SystemController controller);
	
}
