package main.embl.rieslab.htSMLM.acquisitions;

import java.util.ArrayList;

public class ExperimentWrapper {

	public int pausetime;
	public ArrayList<AcquisitionWrapper> acqwlist;
	
	public ExperimentWrapper(){
		
	}
	
	public ExperimentWrapper(int pausetime, ArrayList<AcquisitionWrapper> acqwlist){
		this.pausetime = pausetime;
		this.acqwlist = acqwlist;
	}
	
	
}
