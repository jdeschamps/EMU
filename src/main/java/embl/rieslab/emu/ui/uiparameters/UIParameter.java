package main.java.embl.rieslab.emu.ui.uiparameters;

import main.java.embl.rieslab.emu.ui.ConfigurablePanel;

public abstract class UIParameter<T> {

	private String name_;
	private String description_;
	protected UIParameterType type_;
	private T value_;
	private String ownername_;
	
	public UIParameter(ConfigurablePanel owner, String name, String description){
		ownername_ = owner.getLabel();
		name_ = name;
		description_ = description;
		
		setType();
	}
	
	public String getType(){
		return type_.getTypeValue();
	}

	public String getHash(){
		return ownername_+" - "+name_;
	}
	
	public String getName(){
		return name_;
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
	
	public enum UIParameterType { 
		INTEGER("Integer"), DOUBLE("Double"), FLOAT("Float"), STRING("String"), COLOUR("Color"), BOOL("Boolean"), COMBO("COMBO"), UIPROPERTY("UIProperty"); 
		
		private String value; 
		
		private UIParameterType(String value) { 
			this.value = value; 
		}

		public String getTypeValue() {
			return value;
		} 
	}; 

}
