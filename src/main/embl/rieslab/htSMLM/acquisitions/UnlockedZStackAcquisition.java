package main.embl.rieslab.htSMLM.acquisitions;

import java.util.HashMap;

import main.embl.rieslab.htSMLM.micromanager.properties.ConfigurationGroup;
import main.embl.rieslab.htSMLM.ui.uiproperties.TwoStateUIProperty;

public class UnlockedZStackAcquisition extends ZStackAcquisition {

	private static String FRIENDLY_NAME = "Unlocked Zstack";
	
	private TwoStateUIProperty locking_;
	
	public UnlockedZStackAcquisition(String name, ConfigurationGroup group, String configname, 
			HashMap<String, String> propvalues, double zstart, double zend, double zstep, TwoStateUIProperty locking) {
		super(group, configname, propvalues, zstart, zend, zstep);
		
		locking_ = locking;
	}

	@Override
	public void preAcquisition() {
		locking_.setPropertyValue(TwoStateUIProperty.OFF);
	}


	@Override
	public void postAcquisition() {
		locking_.setPropertyValue(TwoStateUIProperty.ON);
	}

	@Override
	public String getFriendlyName() {
		return FRIENDLY_NAME;
	}

}
