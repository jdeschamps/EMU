package main.embl.rieslab.htSMLM.acquisitions;

import java.util.HashMap;

import main.embl.rieslab.htSMLM.micromanager.properties.ConfigurationGroup;
import main.embl.rieslab.htSMLM.threads.TaskHolder;

public class LocalizationAcquisition extends TimeAcquisition {
	private static String FRIENDLY_NAME = "SMLM";
	private TaskHolder<Double> activation_;
	private int waitonmax_;
	private boolean useactivation_;

	public LocalizationAcquisition(ConfigurationGroup group, String configname, 
			int numframes, int intervalframes, HashMap<String, String> propvalues, TaskHolder<Double> activation, boolean useactivation, int waitonmax) {
		super(group, configname, numframes, intervalframes,propvalues);
		
		activation_ = activation;
		useactivation_ = useactivation;
		waitonmax_ = waitonmax;
	}
	
	@Override
	public void preAcquisition() {
		if(useactivation_){			
			activation_.initializeTask();
			activation_.resumeTask();
		}
	}

	@Override
	public void postAcquisition() {
		if(useactivation_){			
			activation_.pauseTask();
		}
	}
	
	@Override
	public boolean stopCriterionReached() {
		if(useactivation_ && activation_.isCriterionReached()){
			try {
				Thread.sleep(waitonmax_);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}
	
	@Override
	public String getFriendlyName(){
		return FRIENDLY_NAME;
	}

}
