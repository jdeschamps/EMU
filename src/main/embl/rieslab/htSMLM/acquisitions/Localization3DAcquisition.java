package main.embl.rieslab.htSMLM.acquisitions;

import java.util.HashMap;

import main.embl.rieslab.htSMLM.micromanager.properties.ConfigurationGroup;
import main.embl.rieslab.htSMLM.threads.TaskHolder;
import main.embl.rieslab.htSMLM.ui.uiproperties.TwoStateUIProperty;

public class Localization3DAcquisition extends TimeAcquisition {
	private static String FRIENDLY_NAME = "3D SMLM";
	private TaskHolder<Double> activation_;
	private int waitonmax_;
	private TwoStateUIProperty property3D_;
	private boolean use3D_, useactivation_;

	public Localization3DAcquisition(ConfigurationGroup group, String configname, 
			int numframes, int intervalframes, HashMap<String, String> propvalues, TaskHolder<Double> activation, boolean useactivation, int waitonmax, TwoStateUIProperty property3D, boolean use3D) {
		super(group, configname, numframes, intervalframes,propvalues);
		
		activation_ = activation;
		useactivation_ = useactivation;
		waitonmax_ = waitonmax;
		property3D_ = property3D;
		use3D_ = use3D;
	}
	
	@Override
	public void preAcquisition() {
		if(use3D_){
			property3D_.setPropertyValue(TwoStateUIProperty.ON);
		} else {
			property3D_.setPropertyValue(TwoStateUIProperty.OFF);
		}
		if(useactivation_){			
			activation_.initializeTask();
			activation_.resumeTask();
		}
	}

	@Override
	public void postAcquisition() {
		property3D_.setPropertyValue(TwoStateUIProperty.OFF);
	
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
