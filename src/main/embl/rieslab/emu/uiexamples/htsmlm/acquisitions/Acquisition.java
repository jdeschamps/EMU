package main.embl.rieslab.emu.uiexamples.htsmlm.acquisitions;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;

import org.micromanager.acquisition.SequenceSettings;

import main.embl.rieslab.emu.ui.uiproperties.filters.PropertyFilter;
import main.embl.rieslab.emu.utils.utils;

public abstract class Acquisition {


	public final static String[] EMPTY = {"Empty"};

	private double exposure_;
	protected double intervalMs_;
	private int waitingtime_;
	protected int numFrames_;
	private ArrayList<Double> slices_;
	private AcquisitionType type_;
	private String expname_, path_; 
	private HashMap<String,String> propvalues_, mmconfgroups_;
	
	public static final String ACQ_SETTINGS = "Acquisition settings";
	
	public Acquisition(AcquisitionType type, double exposure){
		type_ = type;
		
		expname_ = "";
		
		exposure_ = exposure;
		waitingtime_ = 3;
		intervalMs_ = 0;
		numFrames_ = 1;
		
		path_ = "";
		expname_ = "";

		propvalues_ = new HashMap<String,String>();
		mmconfgroups_ = new HashMap<String,String>();
	}


	public void setSlices(double zstart, double zend, double zstep){
		slices_ = new ArrayList<Double>();
		double z = utils.round(zstart-zstep,2);
		
		while (z<=zend){
			z += zstep;
			slices_.add(utils.round(z,2));
		}
	}
	
	public SequenceSettings getSettings(){
		SequenceSettings settings = new SequenceSettings();
		
		settings.save = true;
		settings.timeFirst = true;
		settings.usePositionList = false;
		
		if(slices_ != null){
			settings.slices = slices_;
		}

		settings.root = path_;
		settings.numFrames = numFrames_;
		settings.intervalMs = intervalMs_;
		
		return settings;
	}
	
	public String getType(){
		return type_.getTypeValue();
	}
	
	public void setPath(String path){
		path_ = path;
	}

	public void setProperties(HashMap<String,String> propvalues){
		propvalues_ = propvalues;
	}
	
	public void setConfigurationGroups(HashMap<String, String> mmconfgroups) {
		mmconfgroups_ = mmconfgroups;
	}
	
	public void setNumberFrames(int numframes){
		numFrames_ = numframes;
	}

	public int getNumberFrames(){
		return numFrames_;
	}

	public void setIntervalMs(double interval){
		intervalMs_ = interval;
	}

	public double getIntervalMs(){
		return intervalMs_;
	}
	
	public void setExposureTime(double exp){
		exposure_ = exp;
	}	
	
	public double getExposure(){
		return exposure_;
	}
	
	public int getWaitingTime(){
		return waitingtime_;
	}
	
	public void setWaitingTime(int waiting){
		waitingtime_ = waiting;
	}
	
	public HashMap<String,String> getPropertyValues(){
		return propvalues_;
	}
	
	public HashMap<String,String> getMMConfGroupValues(){
		return mmconfgroups_;
	}
	
	protected void setType(AcquisitionType type){
		type_ = type;
	}
	
	public String getPath() {
		return path_;
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
	public abstract String getPanelName();
	public abstract void readOutParameters(JPanel pane);
	public abstract PropertyFilter getPropertyFilter();
	public abstract String[] getSpecialSettings();
	public abstract String[][] getAdditionalJSONParameters();
}
