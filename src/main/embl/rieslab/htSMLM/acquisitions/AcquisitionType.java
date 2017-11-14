package main.embl.rieslab.htSMLM.acquisitions;

public enum AcquisitionType { 
	TIME("Time"), BFP("BFP"), WHITELIGHT("WhiteLight"), LOCALIZATION("Localization"), LOCALIZATION3D("3D Localization"), SZSTACK("Simple Zstack"), ZSTACK("Zstack"), CUSTOM("Custom"); 
	
	private String value; 
	
	private AcquisitionType(String value) { 
		this.value = value; 
	}

	public String getTypeValue() {
		return value;
	} 
	
	public static String[] getList(){
		String[] s = {AcquisitionType.LOCALIZATION.getTypeValue(),AcquisitionType.LOCALIZATION3D.getTypeValue(),AcquisitionType.BFP.getTypeValue(),
				AcquisitionType.ZSTACK.getTypeValue(),AcquisitionType.WHITELIGHT.getTypeValue(),AcquisitionType.TIME.getTypeValue(),AcquisitionType.SZSTACK.getTypeValue()};
		return s;
	}
}; 
