package main.embl.rieslab.htSMLM.ui.uiproperties;

import main.embl.rieslab.htSMLM.ui.PropertyPanel;


public class ToggleUIProperty extends UIProperty{
	
	public static String ON = "On";
	public static String OFF = "Off";
	
	public ToggleUIProperty(PropertyPanel owner, String name, String description) {
		super(owner, name, description);
	}

	@Override
	public boolean isToggle(){
		return true;
	}
}
