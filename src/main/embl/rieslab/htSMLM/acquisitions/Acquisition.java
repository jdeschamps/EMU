package main.embl.rieslab.htSMLM.acquisitions;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;

import main.embl.rieslab.htSMLM.configuration.SystemController;
import main.embl.rieslab.htSMLM.micromanager.properties.ConfigurationGroup;

import org.micromanager.api.SequenceSettings;

public abstract class Acquisition {

	private SequenceSettings settings_;
	private int exposure_, waitingtime_;
	private AcquisitionType type_;
	private boolean useconfig_ = false;
	private ConfigurationGroup group_;
	private String configname_; 
	private HashMap<String,String> propvalues_;
	private String expname_;
	private SystemController controller_;
	
	public static final String ACQ_SETTINGS = "Acquisition settings";
	
	public Acquisition(AcquisitionType type, SystemController controller){
		type_ = type;
		
		expname_ = "";
		
		exposure_ = 10;
		waitingtime_ = 0;
		
		controller_ = controller;
		
		settings_ = new SequenceSettings();
		settings_.save = true;
		settings_.timeFirst = true;
		settings_.usePositionList = false;
	}
	
	protected SystemController getSystemController(){
		return controller_;
	}
	
	protected void setSlices(ArrayList<Double> slices){
		settings_.slices = slices;
	}
	
	public SequenceSettings getSettings(){
		return settings_;
	}
	
	public String getType(){
		return type_.getTypeValue();
	}
	
	public void setPath(String path){
		settings_.root = path;
	}

	public void setProperties(HashMap<String,String> propvalues){
		propvalues_ = propvalues;
	}
	
	protected void setNumberFrames(int numframes){
		settings_.numFrames = numframes;
	}
	
	protected void setIntervalMs(int interval){
		settings_.intervalMs = interval;
	}
	
	protected void setExposureTime(int exp){
		exposure_ = exp;
	}	
	
	public int getExposure(){
		return exposure_;
	}
	
	public int getWaitingTime(){
		return waitingtime_;
	}
	
	protected void setWaitingTime(int waiting){
		waitingtime_ = waiting;
	}
	
	public boolean useConfig(){
		return useconfig_;
	}
	
	public void setConfigurationGroup(ConfigurationGroup group, String configname){
		if(group != null && group.hasConfiguration(configname)){
			group_ = group;
			configname_  = configname;
			useconfig_ = true;
		}
	}
	
	public String getConfigurationGroup(){
		if(useconfig_){
			return group_.getName();
		}
		return null;
	}
	
	public String getConfigurationName(){
		if(useconfig_){
			return configname_;
		}
		return null;
	}
	
	public HashMap<String,String> getPropertyValues(){
		return propvalues_;
	}
	
	protected void setType(AcquisitionType type){
		type_ = type;
	}
	public String getPath() {
		return settings_.root;
	}

	public String getExperimentName() {
		return expname_;
	}	
	
	public void setName(String name) {
		expname_ = name;
	}
	
	public abstract void preAcquisition();
	public abstract void postAcquisition();
	public abstract boolean stopCriterionReached();
	public abstract JPanel getPanel();
	public abstract void readOutParameters(JPanel pane);


}
