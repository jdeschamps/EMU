package main.embl.rieslab.htSMLM.acquisitions;

import org.micromanager.api.SequenceSettings;

public abstract class Acquisition {

	private SequenceSettings settings_;
	private AcquisitionType type_;
	private String path_;
	private int numframes_;
	private int intervalms_;
	
	public Acquisition(AcquisitionType type, int numframes, int intervalms, String path){
		type_ = type;
		path_ = path;
		numframes_ = numframes;
		intervalms_ = intervalms;
		
		setSequenceSettings();
	}
	
	
	private void setSequenceSettings(){
		settings_ = new SequenceSettings();
		
		settings_.numFrames = numframes_;
		settings_.intervalMs = intervalms_;
		settings_.root = path_;
		settings_.save = true;
		settings_.timeFirst = true;
		settings_.usePositionList = false;
	}
	
	
}
