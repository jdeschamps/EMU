package main.java.embl.rieslab.emu.ui.internalproperties;

import main.java.embl.rieslab.emu.ui.ConfigurablePanel;

public abstract class InternalProperty<T> {
	private String name_;
	private ConfigurablePanel owner_;
	private boolean allocated_ = false;
	private InternalPropertyValue<T> value_;
	protected InternalPropertyType type_;
	private T defaultvalue;
	
	public InternalProperty(ConfigurablePanel owner, String name, T defaultvalue){
		this.owner_ = owner;
		this.name_ = name;
		this.defaultvalue = defaultvalue;
		
		setType();
	}
	
	public String getName(){
		return name_;
	}
	
	public String getHash(){
		return owner_.getLabel()+" "+name_;
	}
	
	public void linkValue(InternalPropertyValue<T> prop){
		if(!allocated_ && prop != null){
			value_ = prop;
			value_.registerListener(this);
			allocated_ = true;
		}
	}
	
	public boolean isAllocated(){
		return allocated_;
	}
	
	public T getPropertyValue(){
		if(allocated_){
			return value_.getValue();
		}
		return null;
	}
	
	public void setPropertyValue(T val){		
		if(allocated_){
			value_.setValue(val, this);
		}  
	}
	
	protected ConfigurablePanel getOwner(){
		return owner_;
	}
	
	public void notifyOwner(){
		owner_.internalpropertyhasChanged(name_);
	}
	
	public String getType(){
		return type_.getTypeValue();
	}
	
	public T getDefaultValue(){
		return defaultvalue;
	}
	
	public abstract void setType();
}