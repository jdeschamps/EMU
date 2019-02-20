package main.java.embl.rieslab.emu.ui.internalproperties;

import java.util.ArrayList;

import main.java.embl.rieslab.emu.ui.ConfigurablePanel;

/**
 * An InternalProperty is aimed at passing information between {@link main.java.embl.rieslab.emu.ui.ConfigurablePanel}s
 * using an atomic variable of type T. In the ConfigurablePanel, the value can then be retrieved as a wrapper for one of
 * the corresponding primitive type. An InternalProperty is instantiated with a {@code name}. InternalProperties with the
 * same name will be shared between the different ConfigurablePanels that created them, as long as they are of the same InternalPropertyType.
 * <pre>
 * Upon modification of its value, through a call to {@link #setInternalPropertyValue(Object, ConfigurablePanel)}, the
 * value of the atomic variable is updated and a call to all other listeners except the source of the call notifies them
 * of the change.
 * 
 * @author Joran Deschamps
 *
 * @param <T> Should be an Atomic type to keep the synchronization between threads.
 * @param <V> Wrapper for a primitive type the Atomic type can be easily converted to.
 */
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