package main.embl.rieslab.htSMLM.acquisitions;

import java.util.HashMap;

import main.embl.rieslab.htSMLM.micromanager.properties.ConfigurationGroup;
import main.embl.rieslab.htSMLM.ui.uiproperties.TwoStateUIProperty;

public class BFPAcquisition extends SnapshotAcquisition {

	private static String FRIENDLY_NAME = "BFP";
	private TwoStateUIProperty bfp_;
	
	public BFPAcquisition(AcquisitionType type, String path, String name,ConfigurationGroup group, String configname, 
			HashMap<String, String> propvalues, TwoStateUIProperty bfp) {
		super(type, path, name, group, configname, propvalues);

		bfp_ = bfp;
	}
	
	@Override
	public void preAcquisition() {
		bfp_.setPropertyValue(TwoStateUIProperty.ON);
	}
	
	@Override
	public void postAcquisition() {
		bfp_.setPropertyValue(TwoStateUIProperty.OFF);
	}
	
	@Override
	public String getFriendlyName() {
		return FRIENDLY_NAME;
	}
}