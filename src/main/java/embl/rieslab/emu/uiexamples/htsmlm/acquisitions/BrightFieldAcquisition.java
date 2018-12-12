package main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions;

import java.awt.Component;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import main.java.embl.rieslab.emu.ui.uiproperties.TwoStateUIProperty;
import main.java.embl.rieslab.emu.ui.uiproperties.filters.PropertyFilter;
import main.java.embl.rieslab.emu.ui.uiproperties.filters.SinglePropertyFilter;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.AcquisitionFactory.AcquisitionType;

import org.micromanager.Studio;
import org.micromanager.data.Coords;
import org.micromanager.data.Datastore;
import org.micromanager.data.Image;
import org.micromanager.data.internal.DefaultCoords;

public class BrightFieldAcquisition implements Acquisition{
	
	private GenericAcquisitionParameters params_;
	
	private final static String PANE_NAME = "Bright-field panel";
	private final static String LABEL_EXPOSURE = "Exposure (ms):";
	private final static String LABEL_PAUSE = "Pause (s):";
	
	private TwoStateUIProperty bfprop_;
		
	public BrightFieldAcquisition(double exposure, TwoStateUIProperty bfprop) {
		if(bfprop == null){
			throw new NullPointerException();
		}
		bfprop_ = bfprop;
		
		params_ = new GenericAcquisitionParameters(AcquisitionType.BF, 
				exposure, 0, 3, 1, new HashMap<String,String>(), new HashMap<String,String>());
	}

	@Override
	public String getPanelName(){
		return PANE_NAME;
	}
	
	@Override
	public JPanel getPanel() {
		JPanel pane = new JPanel();
		pane.setName(getPanelName());
				
		JLabel exposurelab, waitinglab;
		JSpinner exposurespin, waitingspin;
		
		exposurelab = new JLabel(LABEL_EXPOSURE);
		waitinglab = new JLabel(LABEL_PAUSE);

		exposurespin = new JSpinner(new SpinnerNumberModel(Math.max(params_.getExposureTime(),1), 1, 10000000, 1));
		exposurespin.setName(LABEL_EXPOSURE);
		waitingspin = new JSpinner(new SpinnerNumberModel(params_.getWaitingTime(), 0, 10000000, 1)); 
		waitingspin.setName(LABEL_PAUSE);
		
		int nrow = 1;
		int ncol = 4;
		JPanel[][] panelHolder = new JPanel[nrow][ncol];    
		pane.setLayout(new GridLayout(nrow,ncol));

		for(int m = 0; m < nrow; m++) {
		   for(int n = 0; n < ncol; n++) {
		      panelHolder[m][n] = new JPanel();
		      pane.add(panelHolder[m][n]);
		   }
		} 

		panelHolder[0][0].add(exposurelab);
		panelHolder[0][2].add(waitinglab);
		panelHolder[0][1].add(exposurespin);
		panelHolder[0][3].add(waitingspin);
		
		return pane;
	}

	@Override
	public void readOutParameters(JPanel pane) {
		if(pane.getName().equals(getPanelName())){
			Component[] pancomp = pane.getComponents();

			for(int j=0;j<pancomp.length;j++){
				if(pancomp[j] instanceof JPanel){
					Component[] comp = ((JPanel) pancomp[j]).getComponents();
					for(int i=0;i<comp.length;i++){
						if(!(comp[i] instanceof JLabel) && comp[i].getName() != null){
							if(comp[i].getName().equals(LABEL_EXPOSURE) && comp[i] instanceof JSpinner){
								params_.setExposureTime((Double) ((JSpinner) comp[i]).getValue());
							}else if(comp[i].getName().equals(LABEL_PAUSE) && comp[i] instanceof JSpinner){
								params_.setWaitingTime((Integer) ((JSpinner) comp[i]).getValue());
							}
						}
					}
				}
			}	
		}
	}

	@Override
	public PropertyFilter getPropertyFilter() {
		return new SinglePropertyFilter(bfprop_.getName());
	}

	@Override
	public String[] getSpecialSettings() {
		String[] s = new String[1];
		s[0] = "Exposure = "+params_.getExposureTime()+" ms";
		return s;
	}

	@Override
	public String[][] getAdditionalJSONParameters() {
		return null;
	}

	@Override
	public GenericAcquisitionParameters getParameters() {
		return params_;
	}

	@Override
	public Datastore startAcquisition(Studio studio) {
		// turn on BF
		bfprop_.setPropertyValue(TwoStateUIProperty.getOnStateName());

		
		Datastore store = studio.data().createRAMDatastore();
		studio.displays().createDisplay(store);
		
		Image image;
		Coords.CoordsBuilder builder = new DefaultCoords.Builder();
		builder.time(0).channel(0).z(0).stagePosition(0);
		
			
		image = studio.live().snap(false).get(0);
		image = image.copyAtCoords(builder.build());
			
		try {
			store.putImage(image);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// close display
		studio.displays().closeDisplaysFor(store);
		
		// turn off BF
		bfprop_.setPropertyValue(TwoStateUIProperty.getOffStateName());
		
		return store; 
	}


	@Override
	public void stopAcquisition() {
		// do nothing
	}

	@Override
	public boolean isRunning() {
		return false;
	}

	@Override
	public boolean skipPosition() {
		return false;
	}

	@Override
	public AcquisitionType getType() {
		return AcquisitionType.BF;
	}
}