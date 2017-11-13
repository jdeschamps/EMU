package main.embl.rieslab.htSMLM.acquisitions;

import java.util.HashMap;

import main.embl.rieslab.htSMLM.micromanager.properties.ConfigurationGroup;
import main.embl.rieslab.htSMLM.ui.uiproperties.TwoStateUIProperty;


public class TwoStateAcquisition extends SnapshotAcquisition {

	private String friendlyname_;
	private TwoStateUIProperty prop_;
	
	public TwoStateAcquisition(ConfigurationGroup group, String configname,
			HashMap<String, String> propvalues, String friendlyname, TwoStateUIProperty prop) {
		super(group, configname, propvalues);
		
		friendlyname_ = friendlyname;
		prop_ = prop;
	}

	@Override
	public void preAcquisition() {
		prop_.setPropertyValue(TwoStateUIProperty.ON);
	}
	
	@Override
	public void postAcquisition() {
		prop_.setPropertyValue(TwoStateUIProperty.OFF);
	}
	
	@Override
	public String getFriendlyName(){
		return friendlyname_;
	}
}
