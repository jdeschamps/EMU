package main.embl.rieslab.htSMLM.acquisitions;

import java.util.HashMap;

import main.embl.rieslab.htSMLM.micromanager.properties.ConfigurationGroup;
import main.embl.rieslab.htSMLM.ui.uiproperties.TwoStateUIProperty;


public class TransmissionAcquisition extends SnapshotAcquisition {

	private static String FRIENDLY_NAME = "White light snap";
	private TwoStateUIProperty whitelight_;
	
	public TransmissionAcquisition(AcquisitionType type, String path,String name, ConfigurationGroup group, String configname,
			HashMap<String, String> propvalues, TwoStateUIProperty whitelight) {
		super(type, path, name, group, configname, propvalues);
		
		whitelight_ = whitelight;
	}

	@Override
	public void preAcquisition() {
		whitelight_.setPropertyValue(TwoStateUIProperty.ON);
	}
	
	@Override
	public void postAcquisition() {
		whitelight_.setPropertyValue(TwoStateUIProperty.OFF);
	}
	
	@Override
	public String getFriendlyName(){
		return FRIENDLY_NAME;
	}
}
