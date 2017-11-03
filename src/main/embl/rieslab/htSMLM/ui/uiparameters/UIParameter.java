package main.embl.rieslab.htSMLM.ui.uiparameters;

import main.embl.rieslab.htSMLM.ui.PropertyPanel;

public abstract class UIParameter<T> {

	private String label_;
	private String description_;
	protected UIParameterType type_;
	private T value_;
	private PropertyPanel owner_;
	
	public UIParameter(PropertyPanel owner, String label, String description){
		owner_ = owner;
		label_ = label;
		description_ = description;
		
		setType();
	}
	
	public UIParameterType getType(){
		return type_;
	}

	public String getHash(){
		return owner_.getLabel()+" - "+label_;
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
