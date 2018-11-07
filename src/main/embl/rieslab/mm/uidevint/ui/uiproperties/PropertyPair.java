package main.embl.rieslab.mm.uidevint.ui.uiproperties;

import main.embl.rieslab.mm.uidevint.exceptions.AlreadyAllocatedUIProperty;
import main.embl.rieslab.mm.uidevint.mmproperties.MMProperty;

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
			// TODO Auto-generated catch block
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
