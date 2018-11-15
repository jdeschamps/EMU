package main.embl.rieslab.emu.micromanager.mmproperties;

public enum MMPropertyType { 
	INTEGER("Integer"), FLOAT("Float"), STRING("String"), UNDEF("Undef"); 
	
	private String value; 
	
	private MMPropertyType(String value) { 
		this.value = value; 
	}

	public String getTypeValue() {
		return value;
	} 
}; 
