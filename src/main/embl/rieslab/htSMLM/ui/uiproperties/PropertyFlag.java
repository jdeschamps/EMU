package main.embl.rieslab.htSMLM.ui.uiproperties;

public enum PropertyFlag { 
	FOCUSSTAB("FocusStabilization"), LASER("Laser"), TWOSTATE("Two-state device"), FILTERWHEEL("FilterWheel"), CAMERA("Camera"), OTHERS("Others"); 
	
	private String value; 
	
	private PropertyFlag(String value) { 
		this.value = value; 
	}

	public String getDeviceType() {
		return value;
	} 
}; 
