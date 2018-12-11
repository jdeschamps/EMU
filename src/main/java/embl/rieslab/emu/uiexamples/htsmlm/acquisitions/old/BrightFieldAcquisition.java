package main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.old;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;

import main.java.embl.rieslab.emu.ui.uiproperties.TwoStateUIProperty;
import main.java.embl.rieslab.emu.ui.uiproperties.UIProperty;
import main.java.embl.rieslab.emu.ui.uiproperties.filters.AntiFlagPropertyFilter;
import main.java.embl.rieslab.emu.ui.uiproperties.filters.PropertyFilter;
import main.java.embl.rieslab.emu.ui.uiproperties.filters.SinglePropertyFilter;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.flags.LaserFlag;

public class BrightFieldAcquisition extends Acquisition {
		
	// Convenience constants		
	private final static String PANE_NAME = "Bright field panel";
	private final static String LABEL_EXPOSURE = "Exposure (ms):";
	private final static String LABEL_PAUSE = "Pause (s):";
		
	// UI property
	private TwoStateUIProperty bfprop_; 
	
	public BrightFieldAcquisition(double exposure, UIProperty bfprop) {
		super(AcquisitionType.BRIGHTFIELD, exposure);
		
		if(bfprop instanceof TwoStateUIProperty){
			bfprop_ = (TwoStateUIProperty) bfprop;
		} else {
			bfprop_ = null;
		}
		
		this.setNumberFrames(1);
		this.setIntervalMs(0);
	}

	@Override
	public void preAcquisition() {
		if(bfprop_ != null){
			bfprop_.setPropertyValue(TwoStateUIProperty.getOnStateName());
		}
	}

	@Override
	public void postAcquisition() {
		if(bfprop_ != null){
			bfprop_.setPropertyValue(TwoStateUIProperty.getOffStateName());
		}
	}

	@Override
	public boolean stopCriterionReached() {
		return false;
	}

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
		
		exposurespin = new JSpinner(new SpinnerNumberModel(Math.max(this.getExposureTime(),1), 1, 10000000, 1));
		exposurespin.setName(LABEL_EXPOSURE);
		waitingspin = new JSpinner(new SpinnerNumberModel(this.getWaitingTime(), 0, 10000000, 1)); 
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
				Component[] comp = ((JPanel) pancomp[j]).getComponents();
				for(int i=0;i<comp.length;i++){
					if(!(comp[i] instanceof JLabel) && comp[i].getName() != null){
						if(comp[i].getName().equals(LABEL_EXPOSURE) && comp[i] instanceof JSpinner){
							this.setExposureTime((Double) ((JSpinner) comp[i]).getValue());
						}else if(comp[i].getName().equals(LABEL_PAUSE) && comp[i] instanceof JSpinner){
							this.setWaitingTime((Integer) ((JSpinner) comp[i]).getValue());
						}
					}
				}
			}	
		}
	}

	@Override
	public PropertyFilter getPropertyFilter() {
		if(bfprop_ == null){
			return null;
		}
		return new SinglePropertyFilter(bfprop_.getName(), new AntiFlagPropertyFilter(new LaserFlag()));
	}

	@Override
	public String[] getSpecialSettings() {
		String[] s = new String[1];
		s[0] = "Exposure = "+this.getExposureTime()+" ms";
		return s;
	}


	@Override
	public String[][] getAdditionalJSONParameters() {
		return null;
	}


	@Override
	public void setNumberFrames(int numframes){
		numFrames_ = 1;
	}

	@Override
	public void setIntervalMs(double interval){
		intervalMs_ = 0;
	}
}
