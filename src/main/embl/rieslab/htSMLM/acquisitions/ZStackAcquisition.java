package main.embl.rieslab.htSMLM.acquisitions;

import java.util.ArrayList;
import java.util.HashMap;

import main.embl.rieslab.htSMLM.micromanager.properties.ConfigurationGroup;
import main.embl.rieslab.htSMLM.ui.uiproperties.TwoStateUIProperty;

public class ZStackAcquisition extends Acquisition {

	private static String FRIENDLY_NAME = "Zstack";
	
	private TwoStateUIProperty locking_;
	private ArrayList<Double> slices_;
	private double zstart_, zend_, zstep_;
	
	public ZStackAcquisition(AcquisitionType type, String path, String name, ConfigurationGroup group, String configname, 
			HashMap<String, String> propvalues, double zstart, double zend, double zstep, TwoStateUIProperty locking) {
		super(type, path, name, group, configname, 1, 0, propvalues);
		
		locking_ = locking;
		zstart_ = zstart;
		zend_ = zend; 
		zstep_ = zstep;
		
		createSlices();
	}
	
	private void createSlices() {
		slices_ = new ArrayList<Double>();
		
		for(double z=zstart_;z<=zend_;z=z+zstep_){				
			slices_.add(z);
		}
		
		setSlices(slices_);
	}

	@Override
	public void preAcquisition() {
		locking_.setPropertyValue(TwoStateUIProperty.ON);
	}

	@Override
	public boolean stopCriterionReached() {
		return false;
	}

	@Override
	public void postAcquisition() {
		locking_.setPropertyValue(TwoStateUIProperty.OFF);
	}

	@Override
	public String getFriendlyName() {
		return FRIENDLY_NAME;
	}

}
