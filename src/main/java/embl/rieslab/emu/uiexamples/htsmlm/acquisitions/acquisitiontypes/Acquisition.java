package main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.acquisitiontypes;

import javax.swing.JPanel;

import org.micromanager.Studio;
import org.micromanager.data.Datastore;

import main.java.embl.rieslab.emu.ui.uiproperties.filters.PropertyFilter;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.AcquisitionFactory.AcquisitionType;

public interface Acquisition {
	
	public abstract GenericAcquisitionParameters getParameters();
	
	public abstract void startAcquisition(Studio studio, Datastore store); // should make sure that the store is empty
	
	public abstract void stopAcquisition(); 
	
	public abstract boolean isRunning();
	
	public abstract boolean skipPosition();
	
	public abstract JPanel getPanel();
	
	public abstract String getPanelName();
		
	public abstract void readOutParameters(JPanel pane);
	
	public abstract PropertyFilter getPropertyFilter();
	
	public abstract String[] getSpecialSettings();
	
	public abstract String[][] getAdditionalJSONParameters();
	
	public abstract String getShortName();

	public abstract AcquisitionType getType();

}
