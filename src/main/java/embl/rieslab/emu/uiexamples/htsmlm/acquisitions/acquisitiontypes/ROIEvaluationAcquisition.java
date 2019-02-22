package main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.acquisitiontypes;

import javax.swing.JPanel;

import main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.AcquisitionFactory.AcquisitionType;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.filters.PropertyFilter;

import org.micromanager.Studio;
import org.micromanager.data.Datastore;

public class ROIEvaluationAcquisition implements Acquisition {

	@Override
	public GenericAcquisitionParameters getParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startAcquisition(Studio studio, Datastore store) {
		// TODO Auto-generated method stub
	}

	@Override
	public void stopAcquisition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isRunning() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean skipPosition() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public JPanel getPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPanelName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void readOutParameters(JPanel pane) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PropertyFilter getPropertyFilter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getSpecialSettings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[][] getAdditionalJSONParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getShortName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AcquisitionType getType() {
		// TODO Auto-generated method stub
		return null;
	}

}
