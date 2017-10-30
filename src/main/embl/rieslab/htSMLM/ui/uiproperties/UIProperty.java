package main.embl.rieslab.htSMLM.ui.uiproperties;

import main.embl.rieslab.htSMLM.micromanager.properties.MMProperty;
import main.embl.rieslab.htSMLM.ui.PropertyPanel;

@SuppressWarnings("rawtypes")
public class UIProperty {

	private String name_;
	private String description_;
	private PropertyPanel owner_;
	private MMProperty mmproperty_;
	private boolean initialised_ = false;
	
	public UIProperty(PropertyPanel owner, String name, String description){
		this.owner_ = owner;
		this.name_ = name;
		this.description_ = description;
	}
	
	public String getName(){
		return name_;
	}
	
	public String getDescription(){
		return description_;
	}
	
	public void setProperty(MMProperty prop){
		if(!initialised_ && prop != null){
			mmproperty_ = prop;
			initialised_ = true;
		}
	}
	
	public boolean isInitialised(){
		return initialised_;
	}
	
	public void mmPropertyHasChanged(String value){
		owner_.propertyhasChanged(name_,value);
	}
	
	public String getPropertyValue(){
		if(initialised_){
			return mmproperty_.getStringValue();
		}
		return "0";
	}
	
	public void setPropertyValue(String val){
		if(initialised_){
			mmproperty_.setStringValue(val);
		}  
	}
	
	protected MMProperty getMMPoperty(){
		return mmproperty_;
	}
	
	public boolean isToggle(){
		return false;
	}
	
	public boolean isSingleValue(){
		return false;
	}	
	
	public boolean isMultipleValues(){
		return false;
	}
}
