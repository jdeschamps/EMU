package main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.old;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;

import main.java.embl.rieslab.emu.tasks.TaskHolder;
import main.java.embl.rieslab.emu.ui.uiproperties.filters.NoPropertyFilter;
import main.java.embl.rieslab.emu.ui.uiproperties.filters.PropertyFilter;

public class LocalizationAcquisition extends Acquisition {


	private final static String PANE_NAME = "Localization panel";
	private final static String LABEL_EXPOSURE = "Exposure (ms):";
	private final static String LABEL_PAUSE = "Pause (s):";
	private final static String LABEL_NUMFRAME = "Number of frames:";
	private final static String LABEL_INTERVAL = "Interval (ms):";
	private final static String LABEL_USEACTIVATION = "Use activation";
	private final static String LABEL_USESTOPONMAXUV = "Stop on max";
	private final static String LABEL_MAXUVTIME = "Stop on max delay (s):";
	
	public final static String KEY_USEACT = "Use activation?";
	public final static String KEY_STOPONMAX = "Stop on max?";
	public final static String KEY_STOPDELAY = "Stop on max delay";
	
	@SuppressWarnings("rawtypes")
	private TaskHolder activation_;
	private boolean useactivation_, stoponmax_, activationtask_;
	private int stoponmaxdelay_;
	
	@SuppressWarnings("rawtypes")
	public LocalizationAcquisition(TaskHolder activationtask, double exposure) {
		super(AcquisitionType.LOCALIZATION, exposure);

		activation_ = activationtask;
		if(activation_ != null){
			activationtask_ = true;
		} else {
			activationtask_ = false;
		}
		
		useactivation_ = true;
		stoponmax_ = true;
		stoponmaxdelay_ = 5;
		this.setNumberFrames(30000);
	}

	@Override
	public void preAcquisition() {
		if(activationtask_ && useactivation_){			
			activation_.initializeTask();
			activation_.resumeTask();
		}
	}

	@Override
	public void postAcquisition() {
		if(activationtask_ && useactivation_){			
			activation_.pauseTask();
		}
	}

	@Override
	public boolean stopCriterionReached() {
		if(activationtask_ && useactivation_ && stoponmax_ && activation_.isCriterionReached()){
			try {
				Thread.sleep(stoponmaxdelay_*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	@Override
	public JPanel getPanel() {
		JPanel pane = new JPanel();
		
		pane.setName(getPanelName());
		
		JLabel exposurelab, waitinglab, numframelab, intervallab,waitonmaxlab;
		JSpinner exposurespin, waitingspin, numframespin, intervalspin, waitonmaxspin;
		JCheckBox activatecheck, stoponmaxcheck;
		
		exposurelab = new JLabel(LABEL_EXPOSURE);
		waitinglab = new JLabel(LABEL_PAUSE);
		numframelab = new JLabel(LABEL_NUMFRAME);
		intervallab = new JLabel(LABEL_INTERVAL);
		waitonmaxlab = new JLabel(LABEL_MAXUVTIME);
		
		exposurespin = new JSpinner(new SpinnerNumberModel(Math.max(this.getExposureTime(),1), 1, 10000000, 1));
		exposurespin.setName(LABEL_EXPOSURE);
		waitingspin = new JSpinner(new SpinnerNumberModel(this.getWaitingTime(), 0, 10000000, 1)); 
		waitingspin.setName(LABEL_PAUSE);
		numframespin = new JSpinner(new SpinnerNumberModel(this.getNumberFrames(), 1, 10000000, 1)); 
		numframespin.setName(LABEL_NUMFRAME);
		intervalspin = new JSpinner(new SpinnerNumberModel(this.getIntervalMs(), 0, 10000000, 1));
		intervalspin.setName(LABEL_INTERVAL);
		waitonmaxspin = new JSpinner(new SpinnerNumberModel(stoponmaxdelay_, 0, 10000, 1));
		waitonmaxspin.setName(LABEL_MAXUVTIME);

		activatecheck = new JCheckBox(LABEL_USEACTIVATION);
		activatecheck.setSelected(useactivation_);
		stoponmaxcheck = new JCheckBox(LABEL_USESTOPONMAXUV);
		stoponmaxcheck.setSelected(stoponmax_);
		activatecheck.setName(LABEL_USEACTIVATION);
		stoponmaxcheck.setName(LABEL_USESTOPONMAXUV);
		
		int nrow = 3;
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
		panelHolder[2][0].add(waitonmaxlab);

		panelHolder[0][1].add(exposurespin);
		panelHolder[1][1].add(waitingspin);
		panelHolder[2][1].add(waitonmaxspin);

		panelHolder[0][2].add(numframelab);
		panelHolder[1][2].add(intervallab);
		panelHolder[2][2].add(stoponmaxcheck);

		panelHolder[0][3].add(numframespin);
		panelHolder[1][3].add(intervalspin);
		panelHolder[2][3].add(activatecheck);
	
		return pane;
	}

	public void setUseActivation(boolean b){
		useactivation_  = b;
	}

	public void setUseStopOnMaxUV(boolean b){
		stoponmax_ = b;
	}
	
	public void setUseStopOnMaxUVDelay(int delay){
		stoponmaxdelay_ = delay;
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
							}else if(comp[i].getName().equals(LABEL_USEACTIVATION) && comp[i] instanceof JCheckBox){
								this.setUseActivation(((JCheckBox) comp[i]).isSelected());
							}else if(comp[i].getName().equals(LABEL_USESTOPONMAXUV) && comp[i] instanceof JCheckBox){
								this.setUseStopOnMaxUV(((JCheckBox) comp[i]).isSelected());
							}else if(comp[i].getName().equals(LABEL_MAXUVTIME) && comp[i] instanceof JSpinner){
								this.setUseStopOnMaxUVDelay((Integer) ((JSpinner) comp[i]).getValue());
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
		String[] s = new String[6];
		s[0] = "Exposure = "+this.getExposureTime()+" ms";
		s[1] = "Interval = "+this.getIntervalMs()+" ms";
		s[2] = "Number of frames = "+this.getNumberFrames();
		s[3] = "Use activation = "+useactivation_;
		s[4] = "Stop on max UV = "+stoponmax_;
		s[5] = "Stop on max delay = "+stoponmaxdelay_+" s";
		return s;
	}

	@Override
	public String getPanelName() {
		return PANE_NAME;
	}
	
	@Override
	public String[][] getAdditionalJSONParameters() {
		String[][] s = new String[3][2];

		s[0][0] = KEY_USEACT;
		s[0][1] = String.valueOf(useactivation_);
		s[1][0] = KEY_STOPONMAX;
		s[1][1] = String.valueOf(stoponmax_);
		s[2][0] = KEY_STOPDELAY;
		s[2][1] = String.valueOf(stoponmaxdelay_);
		
		return s;
	}

}