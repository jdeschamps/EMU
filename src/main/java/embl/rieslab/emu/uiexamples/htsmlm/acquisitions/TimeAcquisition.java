package main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions;

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

import main.java.embl.rieslab.emu.ui.uiproperties.filters.NoPropertyFilter;
import main.java.embl.rieslab.emu.ui.uiproperties.filters.PropertyFilter;

public class TimeAcquisition extends Acquisition {

	// Convenience constants		
	private final static String PANE_NAME = "Time panel";
	private final static String LABEL_EXPOSURE = "Exposure (ms):";
	private final static String LABEL_PAUSE = "Pause (s):";
	private final static String LABEL_NUMFRAME = "Number of frames:";
	private final static String LABEL_INTERVAL = "Interval (ms):";
	
	public TimeAcquisition(double exposure) {
		super(AcquisitionType.TIME, exposure);
		
		this.setNumberFrames(30000);
	}

	@Override
	public void preAcquisition() {
		// Do nothing
	}

	@Override
	public void postAcquisition() {
		// Do nothing
	}

	@Override
	public boolean stopCriterionReached() {
		return false;
	}

	@Override
	public JPanel getPanel() {
		JPanel pane = new JPanel();
		
		pane.setBorder(BorderFactory.createTitledBorder(null, ACQ_SETTINGS, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, new Color(0,0,0)));
		((TitledBorder) pane.getBorder()).setTitleFont(((TitledBorder) pane.getBorder()).getTitleFont().deriveFont(Font.BOLD, 12));
		
		pane.setName(getPanelName());
		
		JLabel exposurelab, waitinglab, numframelab, intervallab;
		JSpinner exposurespin, waitingspin, numframespin, intervalspin;
		
		exposurelab = new JLabel(LABEL_EXPOSURE);
		waitinglab = new JLabel(LABEL_PAUSE);
		numframelab = new JLabel(LABEL_NUMFRAME);
		intervallab = new JLabel(LABEL_INTERVAL);
		
		exposurespin = new JSpinner(new SpinnerNumberModel(Math.max(this.getExposure(),1), 1, 10000000, 1));
		exposurespin.setName(LABEL_EXPOSURE);
		waitingspin = new JSpinner(new SpinnerNumberModel(this.getWaitingTime(), 0, 10000000, 1)); 
		waitingspin.setName(LABEL_PAUSE);
		numframespin = new JSpinner(new SpinnerNumberModel(this.getNumberFrames(), 1, 10000000, 1)); 
		numframespin.setName(LABEL_NUMFRAME);
		intervalspin = new JSpinner(new SpinnerNumberModel(this.getIntervalMs(), 0, 10000000, 1));
		intervalspin.setName(LABEL_INTERVAL);
		
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
		panelHolder[1][0].add(waitinglab);
		
		panelHolder[0][1].add(exposurespin);
		panelHolder[1][1].add(waitingspin);
		
		panelHolder[0][2].add(numframelab);
		panelHolder[1][2].add(intervallab);
		
		panelHolder[0][3].add(numframespin);
		panelHolder[1][3].add(intervalspin);

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
							}else if(comp[i].getName().equals(LABEL_NUMFRAME) && comp[i] instanceof JSpinner){
								this.setNumberFrames((Integer) ((JSpinner) comp[i]).getValue());
							}else if(comp[i].getName().equals(LABEL_INTERVAL) && comp[i] instanceof JSpinner){
								this.setIntervalMs((Double) ((JSpinner) comp[i]).getValue());
							}
						}
					}
				}
			}	
		}
	}

	@Override
	public PropertyFilter getPropertyFilter() {
		return new NoPropertyFilter();
	}

	@Override
	public String[] getSpecialSettings() {
		String[] s = new String[3];
		s[0] = "Exposure = "+this.getExposure()+" ms";
		s[1] = "Number of frames = "+this.getNumberFrames();
		s[2] = "Interval = "+this.getIntervalMs()+" ms";
		return s;
	}

	@Override
	public String getPanelName() {
		return PANE_NAME;
	}

	@Override
	public String[][] getAdditionalJSONParameters() {
		return null;
	}

}
