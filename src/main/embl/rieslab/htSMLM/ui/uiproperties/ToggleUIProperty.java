package main.embl.rieslab.htSMLM.ui.uiproperties;

import main.embl.rieslab.htSMLM.ui.PropertyPanel;


public class ToggleUIProperty extends UIProperty{
	
	public static String ON = "On";
	public static String OFF = "Off";
	private String onval_ = "";
	private String offval_ = "";
	
	public ToggleUIProperty(PropertyPanel owner, String name, String description) {
		super(owner, name, description);
	}

	@Override
	public boolean isToggle(){
		return true;
	}
	
	public static String getToggleOnName(){
		return " "+ON;
	}
	
	public static String getToggleOffName(){
		return " "+OFF;
	}
	
	public void setOnValue(String on){
		onval_ = on;
	}
	
	public void setOffValue(String off){
		offval_ = off;
	}
	
	@Override
	public void setPropertyValue(String val) {
		if (isInitialised()) {
			if (val.equals(ON)) {
				getMMPoperty().setStringValue(onval_);
			} else {
				getMMPoperty().setStringValue(offval_);
			}
		}
	}
}
