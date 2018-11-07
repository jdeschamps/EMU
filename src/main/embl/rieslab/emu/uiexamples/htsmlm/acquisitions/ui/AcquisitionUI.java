package main.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.ui;

import main.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.Experiment;

public interface AcquisitionUI {

	public void setExperiment(Experiment experiment);
	public Experiment getExperiment();
	public String getUIPropertyName(String acqtype);
	public boolean isPropertyEnabled(String acqtype);
	
}
