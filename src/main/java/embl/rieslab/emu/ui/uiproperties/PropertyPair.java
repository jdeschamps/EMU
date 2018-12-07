package main.java.embl.rieslab.emu.ui.uiproperties;

import main.java.embl.rieslab.emu.exceptions.AlreadyAllocatedUIProperty;
import main.java.embl.rieslab.emu.micromanager.mmproperties.MMProperty;
import main.java.embl.rieslab.emu.ui.uiproperties.UIProperty;

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
			uiprop_.setProperty(mmprop_);
			mmprop_.addListener(uiprop_);
		} catch (AlreadyAllocatedUIProperty e) {
			e.printStackTrace();
		}
	}

	public String getUIPropertyName(){
		return uiprop_.getName();
	}
	
	public String getMMPropertyName(){
		return mmprop_.getHash();
	}
}
