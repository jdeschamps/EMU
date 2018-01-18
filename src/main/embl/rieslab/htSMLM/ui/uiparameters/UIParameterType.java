package main.embl.rieslab.htSMLM.ui.uiparameters;

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
