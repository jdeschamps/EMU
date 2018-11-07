package main.embl.rieslab.emu.ui.uiproperties;

import main.embl.rieslab.emu.ui.PropertyPanel;
import main.embl.rieslab.emu.ui.uiproperties.flag.PropertyFlag;


public class TwoStateUIProperty extends UIProperty{
	
	private final static String ON = " - On value";
	private final static String OFF = " - Off value";
	private String onstate_;
	private String offstate_;
	
	public TwoStateUIProperty(PropertyPanel owner, String name, String description, PropertyFlag flag) {
		super(owner, name, description, flag);
	}	
	
	public TwoStateUIProperty(PropertyPanel owner, String name, String description) {
		super(owner, name, description);
	}

	@Override
	public boolean isTwoState(){
		return true;
	}
	
	public static String getOnStateName(){
		return ON;
	}
	
	public static String getOffStateName(){
		return OFF;
	}

	public void setOnStateValue(String on){
		onstate_ = on;
	}
	
	public void setOffStateValue(String off){
		offstate_ = off;
	}

	public String getOnStateValue(){
		return onstate_;
	}
	
	public String getOffStateValue(){
		return offstate_;
	}
	
	@Override
	public void setPropertyValue(String val) {
		if(isAllocated()) {
			if (val.equals(getOnStateName())) {
				String t = getOnStateValue();
				getMMPoperty().setStringValue(t, this);
			} else {
				String t = getOffStateValue();
				getMMPoperty().setStringValue(t, this);
			}
		}
	}
	
}
