package main.java.embl.rieslab.emu.ui.uiproperties;

import main.java.embl.rieslab.emu.exceptions.AlreadyAssignedUIPropertyException;
import main.java.embl.rieslab.emu.micromanager.mmproperties.MMProperty;
import main.java.embl.rieslab.emu.ui.uiproperties.UIProperty;

/**
 * Class used to pair a UIproperty with a MMProperty.
 * 
 * @author Joran Deschamps
 *
 */
@SuppressWarnings("rawtypes")
public class PropertyPair {

	private UIProperty uiprop_;
	private MMProperty mmprop_;
	
	public PropertyPair(UIProperty ui, MMProperty mm){
		uiprop_ = ui;
		mmprop_ = mm;
		
		pair();
	}
	
	public void pair(){
		try {
			uiprop_.assignProperty(mmprop_);
			mmprop_.addListener(uiprop_);
		} catch (AlreadyAssignedUIPropertyException e) {
			e.printStackTrace();
		}
	}
}
