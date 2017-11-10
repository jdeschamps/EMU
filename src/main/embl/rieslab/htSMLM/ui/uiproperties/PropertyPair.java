package main.embl.rieslab.htSMLM.ui.uiproperties;

import main.embl.rieslab.htSMLM.micromanager.properties.MMProperty;

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
		uiprop_.setProperty(mmprop_);
		mmprop_.addListener(uiprop_);
	}

	public String getUIPropertyName(){
		return uiprop_.getName();
	}
	
	public String getMMPropertyName(){
		return mmprop_.getHash();
	}
}
