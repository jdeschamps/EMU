package main.embl.rieslab.emu.ui.uiproperties;

import main.embl.rieslab.emu.ui.PropertyPanel;
import main.embl.rieslab.emu.ui.uiproperties.flag.PropertyFlag;

public class SingleStateUIProperty extends UIProperty{

	public final static String STATE = "state";
	
	private String state_ = "";

	public SingleStateUIProperty(PropertyPanel owner, String name, String description, PropertyFlag flag) {
		super(owner, name, description, flag);
	}

	public SingleStateUIProperty(PropertyPanel owner, String name, String description) {
		super(owner, name, description);
	}

	public boolean setStateValue(String v){
		if(isValueAllowed(v)){
			state_ = v;
			return true;
		}
		return false;
	}
	
	public String getStateValue(){
		return state_;
	}
	
	@Override
	public void setPropertyValue(String val) {
		if (isAllocated()) {
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
