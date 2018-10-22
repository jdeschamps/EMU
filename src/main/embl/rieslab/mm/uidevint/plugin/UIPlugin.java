package main.embl.rieslab.mm.uidevint.plugin;

import main.embl.rieslab.mm.uidevint.controller.SystemController;
import main.embl.rieslab.mm.uidevint.ui.PropertyMainFrame;

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
	public PropertyMainFrame getMainFrame(SystemController controller);
	
}
