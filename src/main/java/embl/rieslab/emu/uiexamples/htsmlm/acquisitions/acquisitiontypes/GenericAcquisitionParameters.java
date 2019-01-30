package main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.acquisitiontypes;

import java.util.ArrayList;
import java.util.HashMap;

import main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.AcquisitionFactory.AcquisitionType;

public class GenericAcquisitionParameters {

	private AcquisitionType acqtype_;
	private double exposure_;
	private double intervalMs_;
	private int waitingtime_;
	private int numFrames_;
	private ArrayList<Double> zslices_;
	private HashMap<String,String> propvalues_;
	private HashMap<String,String> mmconfgroups_;
	
	public GenericAcquisitionParameters(AcquisitionType type, double exposure, double intervalMs, int waitingtime, int numframes, 
			HashMap<String,String> propvalues, HashMap<String,String> mmconfgroups){
		
		if(propvalues == null || mmconfgroups == null){
			throw new NullPointerException();
		}
		
		acqtype_ = type;
		exposure_ = exposure;
		intervalMs_ = intervalMs;
		waitingtime_ = waitingtime;
		numFrames_ = numframes;
		propvalues_ = propvalues;
		mmconfgroups_ = mmconfgroups;
		
		zslices_ = null;

	}
	
	public GenericAcquisitionParameters(AcquisitionType type, double exposure, double intervalMs, int waitingtime, int numframes, 
			HashMap<String,String> propvalues, HashMap<String,String> mmconfgroups, ArrayList<Double> slices){

		if(propvalues == null || mmconfgroups == null || slices == null){
			throw new NullPointerException();
		}

		acqtype_ = type;
		exposure_ = exposure;
		intervalMs_ = intervalMs;
		waitingtime_ = waitingtime;
		numFrames_ = numframes;
		propvalues_ = propvalues;
		mmconfgroups_ = mmconfgroups;
		
		zslices_ = slices;
	}
	
	public AcquisitionType getAcquisitionType(){
		return acqtype_;
	}
	
	public void setAcquisitionType(AcquisitionType type){
		acqtype_ = type;
	}
	
	public double getExposureTime(){
		return exposure_;
	}
	
	public void setExposureTime(double exposure){
		exposure_ = exposure;
	}
	
	public double getIntervalMs(){
		return intervalMs_;
	}
	
	public void setIntervalMs(double intervalMs){
		intervalMs_ = intervalMs;
	}
	
	public int getWaitingTime(){
		return waitingtime_;
	}

	public void setWaitingTime(int waitingtime){
		waitingtime_ = waitingtime;
	}
	
	public int getNumberFrames(){
		return numFrames_;
	}

	public void setNumberFrames(int numFrames){
		numFrames_ = numFrames;
	}
	
	public ArrayList<Double> getZSlices(){
		return zslices_;
	}
	
	public void setZSlices(ArrayList<Double> zslices){
		zslices_ = zslices;
	}
	
	public HashMap<String,String> getPropertyValues(){
		return propvalues_;
	}
	
	public void setPropertyValues(HashMap<String,String> prop){
		propvalues_ = prop;
	}

	public HashMap<String,String> getMMConfigurationGroupValues(){
		return mmconfgroups_;
	}
	
	public void setMMConfigurationGroupValues(HashMap<String,String> mmconf){
		mmconfgroups_ = mmconf;
	}
}
