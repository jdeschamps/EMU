package main.embl.rieslab.htSMLM.acquisitions;

import java.util.HashMap;

import main.embl.rieslab.htSMLM.micromanager.properties.ConfigurationGroup;

public class TimeAcquisition extends Acquisition {

	private static String FRIENDLY_NAME = "Time stack";
	
	public TimeAcquisition(ConfigurationGroup group, String configname, 
			int numframes, int intervalframes, HashMap<String, String> propvalues) {
		super(AcquisitionType.TIME, group, configname, numframes, intervalframes,propvalues);
	}

	@Override
	public void preAcquisition() {
		// do nothing
	}

	@Override
	public boolean stopCriterionReached() {
		return false;
	}

	@Override
	public void postAcquisition() {
		// Do nothing
	}

	@Override
	public String getFriendlyName() {
		return FRIENDLY_NAME;
	}


}
