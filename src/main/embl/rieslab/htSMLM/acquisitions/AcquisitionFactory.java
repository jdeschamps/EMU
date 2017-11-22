package main.embl.rieslab.htSMLM.acquisitions;

import main.embl.rieslab.htSMLM.acquisitions.ui.AcquisitionUI;
import main.embl.rieslab.htSMLM.configuration.SystemController;
import main.embl.rieslab.htSMLM.ui.MicroscopeControlUI.AcquisitionPanel;
import main.embl.rieslab.htSMLM.ui.MicroscopeControlUI.ActivationPanel;

public class AcquisitionFactory {

	private AcquisitionUI acqpane_;
	private SystemController controller_;
	
	public AcquisitionFactory(AcquisitionUI acqpane, SystemController controller){
		acqpane_ = acqpane;
		controller_ = controller;
	}
	
	public Acquisition getAcquisition(String type){
		if(type.equals(AcquisitionType.BFP.getTypeValue())){
			return new BFPAcquisition(controller_.getExposure(), controller_.getConfigurationGroups(), controller_.getProperty(acqpane_.getUIPropertyName(AcquisitionPanel.PARAM_BFP)));
		} else if(type.equals(AcquisitionType.LOCALIZATION.getTypeValue())){
			return new LocalizationAcquisition(controller_.getTaskHolder(ActivationPanel.TASK_NAME),controller_.getExposure(), controller_.getConfigurationGroups());
		} else if(type.equals(AcquisitionType.TIME.getTypeValue())){
			return new TimeAcquisition(controller_.getExposure(), controller_.getConfigurationGroups());
		} else if(type.equals(AcquisitionType.BRIGHTFIELD.getTypeValue())){
			return new BrightFieldAcquisition(controller_.getExposure(), controller_.getConfigurationGroups(), controller_.getProperty(acqpane_.getUIPropertyName(AcquisitionPanel.PARAM_BRIGHTFIELD)));
		} else if(type.equals(AcquisitionType.ZSTACK.getTypeValue())){
			return new ZStackAcquisition(controller_.getExposure(), controller_.getConfigurationGroups(), controller_.getProperty(acqpane_.getUIPropertyName(AcquisitionPanel.PARAM_LOCKING)));
		}
			
		return getDefaultAcquisition();
	}
	
	public Acquisition getDefaultAcquisition() {
		return new LocalizationAcquisition(controller_.getTaskHolder(ActivationPanel.TASK_NAME),controller_.getExposure(), controller_.getConfigurationGroups());
	}

}
