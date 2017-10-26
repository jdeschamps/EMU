package main.embl.rieslab.htSMLM.ui.uiparameters;

import main.embl.rieslab.htSMLM.ui.PropertyPanel;

public abstract class UIParameter<T> {

	private String name_;
	private String description_;
	protected UIParameterType type_;
	private T value_;
	private PropertyPanel owner_;
	
	public UIParameter(PropertyPanel owner, String name, String description){
		owner_ = owner;
		name_ = name;
		description_ = description;
		
		setType();
	}
	
	public UIParameterType getType(){
		return type_;
	}

	public String getName(){
		return owner_.getLabel()+" - "+name_;
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
		
	public abstract void setType();
	public abstract boolean isSuitable(String val);
	protected abstract T convertValue(String val);
}
