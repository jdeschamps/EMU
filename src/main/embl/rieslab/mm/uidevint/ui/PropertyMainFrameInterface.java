package main.embl.rieslab.mm.uidevint.ui;

import java.util.ArrayList;
import java.util.HashMap;

import main.embl.rieslab.mm.uidevint.tasks.TaskHolder;
import main.embl.rieslab.mm.uidevint.ui.uiparameters.UIParameter;
import main.embl.rieslab.mm.uidevint.ui.uiproperties.UIProperty;

public class PropertyMainFrameInterface {

	private ArrayList<PropertyPanel> panels_;
	private HashMap<String, UIProperty> uiproperties_;
	@SuppressWarnings("rawtypes")
	private HashMap<String, UIParameter> uiparameters_;
	@SuppressWarnings("rawtypes")
	private HashMap<String, TaskHolder> uitasks_;
	
	@SuppressWarnings("rawtypes")
	public PropertyMainFrameInterface(ArrayList<PropertyPanel> panels, HashMap<String, UIProperty> uiprops,
			HashMap<String, UIParameter> uiparams,
			HashMap<String, TaskHolder> tasks) {
		panels_ = panels;
		uiproperties_ = uiprops;
		uiparameters_ = uiparams;
		uitasks_ = tasks;
	}

	
	public ArrayList<PropertyPanel> getPropertyPanels(){
		return panels_;
	}
	
	public HashMap<String, UIProperty> getUIProperties(){
		return uiproperties_;
	}
	
	@SuppressWarnings("rawtypes")
	public HashMap<String, UIParameter> getUIParameters(){
		return uiparameters_;
	}
	
	@SuppressWarnings("rawtypes")
	public HashMap<String, TaskHolder> getUITaskHolders(){
		return uitasks_;
	}
}
