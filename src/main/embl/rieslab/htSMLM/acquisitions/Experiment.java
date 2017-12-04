package main.embl.rieslab.htSMLM.acquisitions;

import java.util.ArrayList;

public class Experiment {

	private int pausetime;
	private ArrayList<Acquisition> acqlist;
	
	public Experiment(int pausetime, ArrayList<Acquisition> acqwlist){
		this.pausetime = pausetime;
		this.acqlist = acqwlist;
	}
	
	public int getPauseTime(){
		return pausetime;
	}
	
	public ArrayList<Acquisition> getAcquisitionList(){
		return acqlist;
	}
}
