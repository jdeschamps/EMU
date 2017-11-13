package main.embl.rieslab.htSMLM.acquisitions;

import java.util.ArrayList;
import java.util.HashMap;

import main.embl.rieslab.htSMLM.micromanager.properties.ConfigurationGroup;

public class ZStackAcquisition extends Acquisition {

	private static String FRIENDLY_NAME = "Zstack";
	
	private ArrayList<Double> slices_;
	private double zstart_, zend_, zstep_;
	
	public ZStackAcquisition(ConfigurationGroup group, String configname, 
			HashMap<String, String> propvalues, double zstart, double zend, double zstep) {
		super(AcquisitionType.ZSTACK, group, configname, 1, 0, propvalues);
		
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
