package main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.old;

import main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.AcquisitionFactory.AcquisitionType;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.wrappers.Experiment;

public interface AcquisitionUIOld {

	public void setExperiment(Experiment experiment);
	public Experiment getExperiment();
	public String getUIPropertyName(String acqtype);
	public boolean isPropertyEnabled(AcquisitionType type);
	
}
