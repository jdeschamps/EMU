package main.embl.rieslab.htSMLM.ui.uiparameters;

public enum UIParameterType { 
	INTEGER("Integer"), DOUBLE("double"), FLOAT("Float"), STRING("String"), COLOUR("Color"); 
	
	private String value; 
	
	private UIParameterType(String value) { 
		this.value = value; 
	}

	public String getTypeValue() {
		return value;
	} 
}; 
