package main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.wrappers;

import java.util.ArrayList;

import main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.Acquisition;

public class Experiment {

	private String name;
	private String path;
	private int pausetime;
	private int numberpositions;
	private ArrayList<Acquisition> acqlist;
	
	public Experiment(int pausetime, int numberpositions, ArrayList<Acquisition> acqwlist){
		this.name = "";
		this.path = "";
		this.pausetime = pausetime;
		this.numberpositions = numberpositions;
		this.acqlist = acqwlist;
	}	
	
	public Experiment(String name, String path, Experiment e){
		this.name = name;
		this.path = path;
		this.pausetime = e.getPauseTime();
		this.numberpositions = e.getNumberPositions();
		this.acqlist = e.getAcquisitionList();
	}

	public Experiment(String name, String path, int pausetime, int numberpositions, ArrayList<Acquisition> acqwlist) {
		this.name = name;
		this.path = path;
		this.pausetime = pausetime;
		this.numberpositions = numberpositions;
		this.acqlist = acqwlist;
	}

	public String getName(){
		return name;
	}	
	
	public void setName(String s){
		name = s;
	}
	
	public void setPath(String s){
		path = s;
	}

	public String getPath(){
		return path;
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
