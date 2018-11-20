package main.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.wrappers;

import java.util.ArrayList;

public class ExperimentWrapper {

	public int pauseTime;
	public int numberPositions;
	public ArrayList<AcquisitionWrapper> acquisitionList;

	public ExperimentWrapper(){
		// necessary for JSON deserialization
	}
	
	public ExperimentWrapper(int pausetime, int numberpositions, ArrayList<AcquisitionWrapper> acqwlist){
		this.pauseTime = pausetime;
		this.numberPositions = numberpositions;
		this.acquisitionList = acqwlist;
	}
	
	
}
