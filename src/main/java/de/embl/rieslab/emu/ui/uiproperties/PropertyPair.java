package main.java.de.embl.rieslab.emu.ui.uiproperties;

import main.java.de.embl.rieslab.emu.exceptions.AlreadyAssignedUIPropertyException;
import main.java.de.embl.rieslab.emu.micromanager.mmproperties.MMProperty;
import main.java.de.embl.rieslab.emu.ui.uiproperties.UIProperty;

/**
 * Class used to pair a UIproperty with a MMProperty.
 * 
 * @author Joran Deschamps
 *
 */
@SuppressWarnings("rawtypes")
public class PropertyPair {
	public static void pair(UIProperty ui, MMProperty mm){
		try {
			ui.assignProperty(mm);
			mm.addListener(ui);
		} catch (AlreadyAssignedUIPropertyException e) {
			e.printStackTrace();
		}
	}
}
