package main.embl.rieslab.htSMLM.ui.uiproperties;

public enum PropertyFlag { 
	FOCUS("Focus"), LASER("Laser"), TWOSTATE("Two-state device"), FILTERWHEEL("FilterWheel"), Camera ("Camera"), OTHERS("Others"); 
	
	private String value; 
	
	private PropertyFlag(String value) { 
		this.value = value; 
	}

	public String getDeviceType() {
		return value;
	} 
}; 
