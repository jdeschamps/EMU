package main.java.embl.rieslab.emu.ui.internalproperties;

import java.util.ArrayList;

import main.java.embl.rieslab.emu.ui.ConfigurablePanel;

public abstract class InternalProperty<T, V> {
	private String name_;
	private T value_;
	private ArrayList<ConfigurablePanel> listeners_;
	
	public InternalProperty(ConfigurablePanel owner, String name, V defaultvalue){
		this.name_ = name;

		value_ = initializeDefault(defaultvalue);
		listeners_ = new ArrayList<ConfigurablePanel>();
		listeners_.add(owner);
	}
	
	public String getName(){
		return name_;
	}
	
	public boolean hasListeners(){
		return listeners_.size() > 0;
	}
	
	public V getInternalPropertyValue(){
		return convertValue(value_);
	}
	
	protected T getAtomicValue(){
		return value_;
	}
	
	public void setInternalPropertyValue(V val, ConfigurablePanel source) {
		setAtomicValue(val);
		notifyListeners(source);
	}

	public void registerListener(ConfigurablePanel listener) {
		if(listener.getInternalPropertyType(name_).compareTo(this.getType()) == 0) {
			listeners_.add(listener);
			listener.substituteInternalProperty(this);
		}
	}

	public void notifyListeners(ConfigurablePanel source) {
		for(int i=0;i<listeners_.size();i++){
			if(listeners_.get(i) != source){
				listeners_.get(i).internalpropertyhasChanged(name_);
			}
		}
	}
	
	public enum InternalPropertyType{
		INTEGER("Integer"), DOUBLE("Double"), BOOLEAN("Boolean"), NONE("None"); 

		private String value; 
		
		private InternalPropertyType(String value) { 
			this.value = value; 
		}
		
		public String getTypeValue() {
			return value;
		} 
	}; 	
	
	public abstract InternalPropertyType getType();
	protected abstract T initializeDefault(V defaultval);
	protected abstract V convertValue(T val);
	protected abstract void setAtomicValue(V val);
}