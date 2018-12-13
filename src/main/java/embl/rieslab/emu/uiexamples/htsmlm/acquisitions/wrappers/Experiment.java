package main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.wrappers;

import java.util.ArrayList;

import main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.acquisitiontypes.Acquisition;

public class Experiment {

	private int pausetime;
	private int numberpositions;
	private ArrayList<Acquisition> acqlist;
	
	public Experiment(int pausetime, int numberpositions, ArrayList<Acquisition> acqlist){
		
		if(acqlist == null){
			throw new NullPointerException();
		}
		
		this.pausetime = pausetime;
		this.numberpositions = numberpositions;
		this.acqlist = acqlist;
	}	
	
	public int getPauseTime(){
		return pausetime;
	}
	
	public int getNumberPositions(){
		return numberpositions;
	}
	
	public ArrayList<Acquisition> getAcquisitionList(){
		return acqlist;
	}

	public ArrayList<AcquisitionWrapper> getAcquisitionWrapperList() {
		ArrayList<AcquisitionWrapper> acqwlist = new ArrayList<AcquisitionWrapper>();
	
		for (int i=0;i<acqlist.size();i++){
			acqwlist.add(new AcquisitionWrapper(acqlist.get(i)));
		}
		
		return acqwlist;
	}
}
