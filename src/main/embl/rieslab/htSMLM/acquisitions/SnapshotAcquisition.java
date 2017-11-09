package main.embl.rieslab.htSMLM.acquisitions;

import java.util.HashMap;

import main.embl.rieslab.htSMLM.micromanager.properties.ConfigurationGroup;

public class SnapshotAcquisition extends Acquisition {

	private static String FRIENDLY_NAME = "Snapshot";
	
	public SnapshotAcquisition(AcquisitionType type, String path, String name, ConfigurationGroup group, String configname, HashMap<String, String> propvalues) {
		super(AcquisitionType.SNAPSHOT, path, name, group, configname, 1, 0,propvalues);
	}
	
	@Override
	public void preAcquisition() {
		// Do nothing
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
