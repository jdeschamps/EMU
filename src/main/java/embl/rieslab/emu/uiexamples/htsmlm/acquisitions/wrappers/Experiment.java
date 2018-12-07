package main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.wrappers;

import java.util.ArrayList;

import main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.Acquisition;

public class Experiment {

	private int pausetime;
	private int numberpositions;
	private ArrayList<Acquisition> acqlist;
	
	public Experiment(int pausetime, int numberpositions, ArrayList<Acquisition> acqwlist){
		this.pausetime = pausetime;
		this.numberpositions = numberpositions;
		this.acqlist = acqwlist;
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
}
