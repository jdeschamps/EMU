package de.embl.rieslab.emu.ui;

import java.util.ArrayList;
import java.util.HashMap;

import de.embl.rieslab.emu.ui.uiparameters.UIParameter;
import de.embl.rieslab.emu.ui.uiproperties.UIProperty;

/**
 * Interface giving access to a list of {@link ConfigurablePanel}s and maps of UIProperties and UIparameters.
 * 
 * @author Joran Deschamps
 *
 */
public interface ConfigurableFrame {

	public ArrayList<ConfigurablePanel> getConfigurablePanels();
	
	public HashMap<String, UIProperty> getUIProperties();
	
	@SuppressWarnings("rawtypes")
	public HashMap<String, UIParameter> getUIParameters();

}
