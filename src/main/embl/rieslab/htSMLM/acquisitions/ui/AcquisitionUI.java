package main.embl.rieslab.htSMLM.acquisitions.ui;

import main.embl.rieslab.htSMLM.acquisitions.Experiment;

public interface AcquisitionUI {

	public void setExperiment(Experiment experiment);
	public Experiment getExperiment();
	public String getUIPropertyName(String acqtype);
	
}
