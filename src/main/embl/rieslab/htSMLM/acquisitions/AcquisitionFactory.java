package main.embl.rieslab.htSMLM.acquisitions;

import main.embl.rieslab.htSMLM.acquisitions.ui.AcquisitionUI;
import main.embl.rieslab.htSMLM.controller.SystemController;
import main.embl.rieslab.htSMLM.ui.AcquisitionPanel;

public class AcquisitionFactory {

	private AcquisitionUI acqpane_;
	private SystemController controller_;
	
	public AcquisitionFactory(AcquisitionUI acqpane, SystemController controller){
		acqpane_ = acqpane;
		controller_ = controller;
	}
	
	public Acquisition getAcquisition(String type){
		if(type.equals(AcquisitionType.BFP.getTypeValue())){
			return new BFPAcquisition(controller_, acqpane_.getUIPropertyName(AcquisitionPanel.PARAM_BFP));
		} else if(type.equals(AcquisitionType.LOCALIZATION.getTypeValue())){
			
		} else if(type.equals(AcquisitionType.LOCALIZATION3D.getTypeValue())){
			
		} else if(type.equals(AcquisitionType.SZSTACK.getTypeValue())){
			
		} else if(type.equals(AcquisitionType.TIME.getTypeValue())){
			return new TimeAcquisition(controller_);
		} else if(type.equals(AcquisitionType.BRIGHTFIELD.getTypeValue())){
			
		} else if(type.equals(AcquisitionType.ZSTACK.getTypeValue())){
			
		}
			
		return getDefaultAcquisition();
	}
	
	public Acquisition getDefaultAcquisition() {
		return new TimeAcquisition(controller_);
	}

}
