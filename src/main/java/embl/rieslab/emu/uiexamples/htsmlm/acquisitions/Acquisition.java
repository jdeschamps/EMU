package main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions;

import javax.swing.JPanel;

import org.micromanager.Studio;
import org.micromanager.data.Datastore;

import main.java.embl.rieslab.emu.ui.uiproperties.filters.PropertyFilter;

public interface Acquisition {
	
	public abstract GenericAcquisitionParameters getParameters();
	
	public abstract Datastore startAcquisition(Studio studio);
	
	public abstract void stopAcquisition(); 
	
	public abstract boolean isRunning();
	
	public abstract boolean skipPosition();
	
	public abstract JPanel getPanel();
	
	public abstract String getPanelName();
		
	public abstract void readOutParameters(JPanel pane);
	
	public abstract PropertyFilter getPropertyFilter();
	
	public abstract String[] getSpecialSettings();
	
	public abstract String[][] getAdditionalJSONParameters();
}
