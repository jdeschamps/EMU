package main.embl.rieslab.htSMLM.acquisitions;


import javax.swing.JPanel;

import main.embl.rieslab.htSMLM.ui.AcquisitionPanel;

public class AcquisitionFactory {

	private AcquisitionPanel acqpane_;
	
	public AcquisitionFactory(AcquisitionPanel acqpane){
		acqpane_ = acqpane;
	}
	
	public Acquisition getAcquisition(String type){
		if(type.equals(AcquisitionType.BFP.getTypeValue())){
			
		} else if(type.equals(AcquisitionType.LOCALIZATION.getTypeValue())){
			
		} else if(type.equals(AcquisitionType.LOCALIZATION3D.getTypeValue())){
			
		} else if(type.equals(AcquisitionType.SZSTACK.getTypeValue())){
			
		} else if(type.equals(AcquisitionType.TIME.getTypeValue())){
			
		} else if(type.equals(AcquisitionType.WHITELIGHT.getTypeValue())){
			
		} else if(type.equals(AcquisitionType.ZSTACK.getTypeValue())){
			
		}
			
		return getDefaultAcquisition();
	}
	
	public Acquisition getDefaultAcquisition() {
		return null;
	}

}
