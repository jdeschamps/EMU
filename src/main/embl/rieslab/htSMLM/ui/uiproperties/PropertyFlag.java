package main.embl.rieslab.htSMLM.ui.uiproperties;

public enum PropertyFlag { 
	FOCUSSTAB("FocusStabilization"), LASER("Laser"),  FOCUSLOCK("Focus-lock laser"), TWOSTATE("Two-state device"), FILTERWHEEL("FilterWheel"), CAMERAEXP("CameraExposure"), OTHERS("Others"); 
	
	private String value; 
	
	private PropertyFlag(String value) { 
		this.value = value; 
	}

	public String getDeviceType() {
		return value;
	} 
}; 
