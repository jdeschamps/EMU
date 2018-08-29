package main.embl.rieslab.mm.uidevint.ui.uiproperties;

import main.embl.rieslab.mm.uidevint.mmproperties.MMProperty;
import main.embl.rieslab.mm.uidevint.ui.PropertyPanel;

@SuppressWarnings("rawtypes")
public class UIProperty {

	private String name_;
	private String friendlyname_;
	private String description_;
	private PropertyPanel owner_;
	private MMProperty mmproperty_;
	private PropertyFlag flag_;
	private boolean allocated_ = false;
	
	public UIProperty(PropertyPanel owner, String name, String description, PropertyFlag flag){
		this.owner_ = owner;
		this.name_ = name;
		this.description_ = description;
		this.flag_ = flag;
	}
	
	public String getName(){
		return name_;
	}
	
	public String getDescription(){
		return description_;
	}
	
	public void setProperty(MMProperty prop){
		if(!allocated_ && prop != null){
			mmproperty_ = prop;
			allocated_ = true;
		}
	}
	
	public boolean isAllocated(){
		return allocated_;
	}
	
	public void mmPropertyHasChanged(String value){
		owner_.triggerPropertyHasChanged(name_,value);
	}
	
	public String getMMPropertyValue(){
		if(allocated_){
			return mmproperty_.getStringValue();
		}
		return "0";
	}
	
	public String getPropertyValue(){
		return getMMPropertyValue();
	}
	
	public void setPropertyValue(String val){
		if(allocated_){
			mmproperty_.setStringValue(val, this);
		}  
	}
	
	protected MMProperty getMMPoperty(){
		return mmproperty_;
	}
	
	public String getFriendlyName(){
		if(friendlyname_ == null){
			return name_;
		}
		return friendlyname_;
	}
	
	public void setFriendlyName(String s){
		friendlyname_ = s;
	}
	
	public boolean isTwoState(){
		return false;
	}
	
	public boolean isSingleState(){
		return false;
	}	

	public boolean isMultiState(){
		return false;
	}
	
	public boolean isFixedState(){
		return false;
	}
	
	public boolean isMMPropertyReadOnly(){
		if(allocated_){
			return mmproperty_.isReadOnly();
		}
		return true;
	}
	
	public boolean hasMMPropertyAllowedValues(){
		if(allocated_){
			return mmproperty_.hasAllowedValues();
		}
		return false;
	}
	
	public boolean hasMMPropertyLimits(){
		if(allocated_){
			return mmproperty_.hasLimits();
		}
		return false;
	}
	
	public String[] getAllowedValues(){
		if(hasMMPropertyAllowedValues()){
			return mmproperty_.getStringAllowedValues() ;
		}
		return null;
	}
	
	public String[] getLimits(){
		if(hasMMPropertyAllowedValues()){
			String[] lim = {mmproperty_.getStringMin(),mmproperty_.getStringMax()};
			return lim;
		}
		return null;
	}
	
	public String getFlag(){
		return flag_.getDeviceType();
	}
}
