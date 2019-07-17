package de.embl.rieslab.emu.ui.uiproperties;

import de.embl.rieslab.emu.exceptions.AlreadyAssignedUIPropertyException;
import de.embl.rieslab.emu.micromanager.mmproperties.MMProperty;
import de.embl.rieslab.emu.ui.uiproperties.UIProperty;

/**
 * Class used to pair a UIproperty with a MMProperty.
 * 
 * @author Joran Deschamps
 *
 */
@SuppressWarnings("rawtypes")
public class PropertyPair {
	public static void pair(UIProperty ui, MMProperty mm) throws AlreadyAssignedUIPropertyException{
		if(ui == null) {
			throw new NullPointerException("Cannot pair a null UIProperty.");
		}
		
		if(mm == null) {
			throw new NullPointerException("Cannot pair a null MMProperty.");
		}
		
		ui.assignProperty(mm);
		mm.addListener(ui);
	}
}
