package main.java.embl.rieslab.emu.configuration.globalsettings;

public abstract class GlobalSettings<T> {

	private String description_;
	private String name_;
	private T value_;
	
	public GlobalSettings(String name, String description, T default_val){
		name_ = name;
		description_ = description;
		value_ = default_val;
	}

	public String getStringValue(){
		return getStringValue(value_);
	}

	public T getValue(){
		return value_;
	}
	
	public String getName(){
		return name_;
	}	
	
	public String getDescription(){
		return description_;
	}
	
	protected abstract String getStringValue(T val);
}
