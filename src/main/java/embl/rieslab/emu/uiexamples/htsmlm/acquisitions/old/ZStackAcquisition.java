package main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.old;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import main.java.embl.rieslab.emu.ui.uiproperties.TwoStateUIProperty;
import main.java.embl.rieslab.emu.ui.uiproperties.UIProperty;
import main.java.embl.rieslab.emu.ui.uiproperties.filters.NoPropertyFilter;
import main.java.embl.rieslab.emu.ui.uiproperties.filters.PropertyFilter;
import main.java.embl.rieslab.emu.ui.uiproperties.filters.SinglePropertyFilter;

public class ZStackAcquisition extends Acquisition {
	
	
	// Convenience constants		
	private final static String PANE_NAME = "Zstack panel";
	private final static String LABEL_EXPOSURE = "Exposure (ms):";
	private final static String LABEL_PAUSE = "Pause (s):";
	private final static String LABEL_ZSTART = "Z start/end/step (um):";
	private final static String LABEL_ZEND = "Z end (um):";
	private final static String LABEL_ZSTEP = "Z step (um):";
		
	public final static String KEY_ZSTART = "Z start";
	public final static String KEY_ZEND = "Z end";
	public final static String KEY_ZSTEP = "Z step";
	
	// UI property
	private TwoStateUIProperty stabprop_;
	
	private double zstart, zend, zstep;
	
	public ZStackAcquisition(double exposure, UIProperty stabprop) {
		super(AcquisitionType.ZSTACK, exposure);

		if(stabprop instanceof TwoStateUIProperty){
			stabprop_ = (TwoStateUIProperty) stabprop;
		} else {
			stabprop_ = null;
		}
		
		this.setNumberFrames(1);
		this.setIntervalMs(0);
		
		zstart=-2;
		zend=2;
		zstep=0.05;
	}

	@Override
	public void preAcquisition() {
		if(stabprop_ != null){
			stabprop_.setPropertyValue(TwoStateUIProperty.getOffStateName());
		}
	}

	@Override
	public void postAcquisition() {
		if(stabprop_ != null){
			stabprop_.setPropertyValue(TwoStateUIProperty.getOnStateName());
		}
	}

	@Override
	public boolean stopCriterionReached() {
		return false;
	}

	@Override
	public JPanel getPanel() {
		JPanel pane = new JPanel();
		
		pane.setName(getPanelName());
		
		JLabel exposurelab, waitinglab, zstartlab;
		JSpinner exposurespin, waitingspin, zstartspin, zendspin, zstepspin;
		
		exposurelab = new JLabel(LABEL_EXPOSURE);
		waitinglab = new JLabel(LABEL_PAUSE);
		zstartlab = new JLabel(LABEL_ZSTART);
		
		exposurespin = new JSpinner(new SpinnerNumberModel(Math.max(this.getExposureTime(),1), 1, 10000000, 1));
		exposurespin.setName(LABEL_EXPOSURE);
		waitingspin = new JSpinner(new SpinnerNumberModel(this.getWaitingTime(), 0, 10000000, 1)); 
		waitingspin.setName(LABEL_PAUSE);
		zstartspin = new JSpinner(new SpinnerNumberModel(zstart, -1000, 1000, 0.05)); 
		zstartspin.setName(LABEL_ZSTART);
		zendspin = new JSpinner(new SpinnerNumberModel(zend, -1000, 1000, 1)); 
		zendspin.setName(LABEL_ZEND);
		zstepspin = new JSpinner(new SpinnerNumberModel(zstep, -1000, 1000, 0.01));
		zstepspin.setName(LABEL_ZSTEP);
		

		int nrow = 2;
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
		panelHolder[1][0].add(zstartlab);
		
		panelHolder[0][1].add(exposurespin);
		panelHolder[1][1].add(zstartspin);
		
		panelHolder[0][2].add(waitinglab);
		panelHolder[1][2].add(zendspin);
		
		panelHolder[0][3].add(waitingspin);
		panelHolder[1][3].add(zstepspin);

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
								this.setExposureTime((Double) ((JSpinner) comp[i]).getValue());
							}else if(comp[i].getName().equals(LABEL_PAUSE) && comp[i] instanceof JSpinner){
								this.setWaitingTime((Integer) ((JSpinner) comp[i]).getValue());
							}else if(comp[i].getName().equals(LABEL_ZSTART) && comp[i] instanceof JSpinner){
								zstart = ((Double) ((JSpinner) comp[i]).getValue());
							}else if(comp[i].getName().equals(LABEL_ZEND) && comp[i] instanceof JSpinner){
								zend = ((Double) ((JSpinner) comp[i]).getValue());
							}else if(comp[i].getName().equals(LABEL_ZSTEP) && comp[i] instanceof JSpinner){
								zstep = ((Double) ((JSpinner) comp[i]).getValue());
							}
						}
					}
				}
			}	
			this.setSlices(zstart, zend, zstep);
		}
	}

	@Override
	public PropertyFilter getPropertyFilter() {
		if(stabprop_ == null){
			return new NoPropertyFilter();
		}
		return new SinglePropertyFilter(stabprop_.getName());
	}

	@Override
	public String[] getSpecialSettings() {
		String[] s = new String[4];
		s[0] = "Exposure = "+this.getExposureTime()+" ms";
		s[1] = "Zstart = "+zstart+" um";
		s[2] = "Zend = "+zend+" um";
		s[3] = "Zstep = "+zstep+" um";
		return s;
	}

	@Override
	public String getPanelName() {
		return PANE_NAME;
	}

	
	public void setZStart(double val){
		zstart = val;
	}
	
	public void setZEnd(double val){
		zend = val;
	}
	
	public void setZStep(double val){
		zstep = val;
	}
	
	@Override
	public String[][] getAdditionalJSONParameters() {
		String[][] s = new String[3][2];

		s[0][0] = KEY_ZSTART;
		s[0][1] = String.valueOf(zstart);
		s[1][0] = KEY_ZEND;
		s[1][1] = String.valueOf(zend);
		s[2][0] = KEY_ZSTEP;
		s[2][1] = String.valueOf(zstep);
		
		return s;
	}
}
