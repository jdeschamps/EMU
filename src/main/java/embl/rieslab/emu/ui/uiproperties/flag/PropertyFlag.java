package main.java.embl.rieslab.emu.ui.uiproperties.flag;

public abstract class PropertyFlag implements Comparable<PropertyFlag>{ 	
	private final String value; 
	
	public PropertyFlag(String value) { 
		this.value = value; 
	}

	public String getPropertyFlag() {
		return value;
	}

	@Override
	public int compareTo(PropertyFlag arg0) {
		return getPropertyFlag().compareTo(arg0.getPropertyFlag());
	} 
}; 
