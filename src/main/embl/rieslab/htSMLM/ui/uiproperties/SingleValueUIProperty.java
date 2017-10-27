package main.embl.rieslab.htSMLM.ui.uiproperties;

import main.embl.rieslab.htSMLM.ui.PropertyPanel;

public class SingleValueUIProperty extends UIProperty{

	public static String VALUE = "Value";
	
	private String value_ = "";
	
	public SingleValueUIProperty(PropertyPanel owner, String name, String description) {
		super(owner, name, description);
	}

	
	public void setConstantValue(String v){
		value_ = v;
	}
	
	@Override
	public void setPropertyValue(String val) {
		if (isInitialised()) {
			getMMPoperty().setStringValue(value_);
		}
	}
	
	@Override
	public boolean isSingleValue(){
		return true;
	}
	
	public static String getValueName(){
		return " "+VALUE;
	}
}
