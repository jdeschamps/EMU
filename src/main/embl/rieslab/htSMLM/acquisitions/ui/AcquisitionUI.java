package main.embl.rieslab.htSMLM.acquisitions.ui;

import java.util.ArrayList;

import main.embl.rieslab.htSMLM.acquisitions.Acquisition;

public interface AcquisitionUI {

	public void setAcquisitionList(ArrayList<Acquisition> acquisitionList, int waitingTime);
	public String getUIPropertyName(String acqtype);
	
}
