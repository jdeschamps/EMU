package main.embl.rieslab.emu.ui.uiparameters;

import main.embl.rieslab.emu.ui.PropertyPanel;

public abstract class UIParameter<T> {

	private String label_;
	private String description_;
	protected UIParameterType type_;
	private T value_;
	private String ownername_;
	
	public UIParameter(PropertyPanel owner, String label, String description){
		ownername_ = owner.getLabel();
		label_ = label;
		description_ = description;
		
		setType();
	}
	
	public String getType(){
		return type_.getTypeValue();
	}

	public String getHash(){
		return ownername_+" - "+label_;
	}
	
	public String getLabel(){
		return label_;
	}

	public String getDescription(){
		return description_;
	}
	
	public T getValue(){
		return value_;
	}
	
	protected void setValue(T val){
		value_ = val;
	}
	
	public void setStringValue(String val){
		if(isSuitable(val)){
			value_ = convertValue(val);
		}
	}
		
	public boolean isCommaSeparated(){
		return false;
	}
	
	public int getDepth(){
		return 1;
	}
	
	public abstract void setType();
	public abstract boolean isSuitable(String val);
	protected abstract T convertValue(String val);
	public abstract String getStringValue();
}
