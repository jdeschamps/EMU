package main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.old;

public enum AcquisitionType { 
	TIME("Time"), BFP("BFP"), BRIGHTFIELD("Bright-field"), LOCALIZATION("Localization"), ZSTACK("Z-stack"), SNAP("Snapshot"), CUSTOM("Custom"); 
	
	private String value; 
	
	private AcquisitionType(String value) { 
		this.value = value; 
	}

	public String getTypeValue() {
		return value;
	} 
	
	public static String[] getList(){
		String[] s = {AcquisitionType.LOCALIZATION.getTypeValue(),AcquisitionType.BFP.getTypeValue(),
				AcquisitionType.ZSTACK.getTypeValue(),AcquisitionType.BRIGHTFIELD.getTypeValue(),AcquisitionType.SNAP.getTypeValue(),AcquisitionType.TIME.getTypeValue()};
		return s;
	}
}; 
