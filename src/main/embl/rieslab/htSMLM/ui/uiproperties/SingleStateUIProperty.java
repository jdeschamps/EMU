package main.embl.rieslab.htSMLM.ui.uiproperties;

import main.embl.rieslab.htSMLM.ui.PropertyPanel;

public class SingleStateUIProperty extends UIProperty{

	public final static String STATE = "state";
	
	private String state_ = "";
	
	public SingleStateUIProperty(PropertyPanel owner, String name, String description, PropertyFlag flag) {
		super(owner, name, description, flag);
	}

	public void setStateValue(String v){
		state_ = v;
	}
	
	public String getStateValue(){
		return state_;
	}
	
	@Override
	public void setPropertyValue(String val) {
		if (isAllocated()) {
			System.out.println("Change property: "+getMMPoperty().getPropertyName()+" to "+val);
			getMMPoperty().setStringValue(state_, this);
		}
	}
	
	@Override
	public boolean isSingleState(){
		return true;
	}
	
	public static String getValueName(){
		return " "+STATE;
	}
}
