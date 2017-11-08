package main.embl.rieslab.htSMLM.acquisitions;

public enum AcquisitionType { 
	TIME("Time"), SNAPSHOT("Snapshot"), ZSTACK("Zstack"), CUSTOM("Custom"); 
	
	private String value; 
	
	private AcquisitionType(String value) { 
		this.value = value; 
	}

	public String getTypeValue() {
		return value;
	} 
}; 
