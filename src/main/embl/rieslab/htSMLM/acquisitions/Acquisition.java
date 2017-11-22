package main.embl.rieslab.htSMLM.acquisitions;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;

import main.embl.rieslab.htSMLM.ui.uiproperties.filters.PropertyFilter;
import main.embl.rieslab.htSMLM.util.StringSorting;

import org.micromanager.api.SequenceSettings;

public abstract class Acquisition {


	public final static String[] EMPTY = {"Empty"};

	private SequenceSettings settings_;
	private double exposure_;
	private int waitingtime_;
	private AcquisitionType type_;
	private boolean useconfig_ = false;
	private String group_, configname_; 
	private HashMap<String,String> propvalues_;
	private HashMap<String,String[]> configgroups_;
	private String expname_;
	
	public static final String ACQ_SETTINGS = "Acquisition settings";
	
	public Acquisition(AcquisitionType type, double exposure, HashMap<String,String[]> configgroups){
		type_ = type;
		
		expname_ = "";
		
		exposure_ = exposure;
		waitingtime_ = 0;
		
		configgroups_ = configgroups;
		
		configname_ = EMPTY[0];
		group_ = EMPTY[0];
		
		settings_ = new SequenceSettings();
		settings_.save = true;
		settings_.timeFirst = true;
		settings_.usePositionList = false;
	}

	protected void setSlices(ArrayList<Double> slices){
		settings_.slices = slices;
	}	
	
	protected void setSlices(double zstart, double zend, double zstep){
		ArrayList<Double> slices = new ArrayList<Double>();
		for(double z=zstart;z<=zend;z=z+zstep){
			slices.add(z);
		}	
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

	public int getNumberFrames(){
		return settings_.numFrames;
	}

	protected void setIntervalMs(double interval){
		settings_.intervalMs = interval;
	}

	public double getIntervalMs(){
		return settings_.intervalMs;
	}
	
	protected void setExposureTime(double exp){
		exposure_ = exp;
	}	
	
	public double getExposure(){
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
	
	public void setConfigurationGroup(String group, String configname){
		if(group != null && configgroups_.containsKey(group)){
			group_ = group;
			configname_  = configname;
			useconfig_ = true;
		}
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
	
	public String getConfigGroup(){
		if(group_ != null){
			return group_;
		}
		return EMPTY[0];
	}
	
	public String getConfigName(){
		return configname_;
	}
	
	public String[] getConfigGroupList(){
		int size = configgroups_.size();
		String[] s = new String[size+1];
		String[] temp = StringSorting.sort(configgroups_.keySet().toArray(new String[0]));
		s[0] = EMPTY[0];
		for(int i=0;i<size;i++){
			s[i+1] = temp[i];
		}
		return s;
	}
	
	public String[] getConfigGroupNames(String group){
		if(!configgroups_.containsKey(group)){
			return EMPTY;
		}
		
		return StringSorting.sort(configgroups_.get(group));
	}
	
	public abstract void preAcquisition();
	public abstract void postAcquisition();
	public abstract boolean stopCriterionReached();
	public abstract JPanel getPanel();
	public abstract String getPanelName();
	public abstract void readOutParameters(JPanel pane);
	public abstract PropertyFilter getPropertyFilter();
	public abstract String[] getCharacteristicSettings();

}
