package main.embl.rieslab.htSMLM.acquisitions;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;

import main.embl.rieslab.htSMLM.configuration.SystemController;
import main.embl.rieslab.htSMLM.threads.TaskHolder;
import main.embl.rieslab.htSMLM.ui.uiproperties.filters.NoPropertyFilter;
import main.embl.rieslab.htSMLM.ui.uiproperties.filters.PropertyFilter;

public class LocalizationAcquisition extends Acquisition {

	public final static String TASK_NAME = "Activation task";
	
	private final static String PANE_NAME = "Localization panel";
	private final static String LABEL_GROUP = "Group:";
	private final static String LABEL_EXPOSURE = "Exposure (ms):";
	private final static String LABEL_PAUSE = "Pause (s):";
	private final static String LABEL_NUMFRAME = "Number of frames:";
	private final static String LABEL_GROUPNAME = "GroupName";
	private final static String LABEL_INTERVAL = "Interval (ms):";
	private final static String LABEL_USEACTIVATION = "Use activation";
	private final static String LABEL_USESTOPONMAXUV = "Stop on max";
	private final static String LABEL_MAXUVTIME = "Stop on max delay (s):";
	
	@SuppressWarnings("rawtypes")
	private TaskHolder activation_;
	private boolean useactivation_, stoponmax_, activationtask_;
	private int stoponmaxdelay_;
	
	public LocalizationAcquisition(SystemController controller) {
		super(AcquisitionType.LOCALIZATION, controller);

		activation_ = controller.getTaskHolder(TASK_NAME);
		if(activation_ != null){
			activationtask_ = true;
		} else {
			activationtask_ = false;
		}
		
		useactivation_ = true;
		stoponmax_ = true;
		stoponmaxdelay_ = 5;
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
		
		pane.setBorder(BorderFactory.createTitledBorder(null, ACQ_SETTINGS, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, new Color(0,0,0)));
		((TitledBorder) pane.getBorder()).setTitleFont(((TitledBorder) pane.getBorder()).getTitleFont().deriveFont(Font.BOLD, 12));
		
		pane.setName(PANE_NAME);
		
		JLabel channellab, exposurelab, waitinglab, numframelab, intervallab,waitonmaxlab;
		JComboBox channelgroup;
		final JComboBox channelname;
		JSpinner exposurespin, waitingspin, numframespin, intervalspin, waitonmaxspin;
		JCheckBox activatecheck, stoponmaxcheck;
		
		channellab = new JLabel(LABEL_GROUP);
		exposurelab = new JLabel(LABEL_EXPOSURE);
		waitinglab = new JLabel(LABEL_PAUSE);
		numframelab = new JLabel(LABEL_NUMFRAME);
		intervallab = new JLabel(LABEL_INTERVAL);
		waitonmaxlab = new JLabel(LABEL_MAXUVTIME);
		
		
		String[] s = getSystemController().getMMConfigGroups();
		String[] s2 = new String[s.length+1];
		String[] s3 = new String[1];
		s2[0] = EMPTY[0];
		s3[0] = s2[0];
		int ind=0;
		for(int i=0;i<s.length;i++){
			s2[i+1] = s[i];
			if(this.getConfigGroup()!=null && s[i].equals(this.getConfigGroup())){
				ind = i;
			}
		}
		channelgroup = new JComboBox(s2);
		channelgroup.setName(LABEL_GROUP);
		channelname = new JComboBox(s3);
		channelname.setName(LABEL_GROUPNAME);
		channelgroup.addActionListener(
	            new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
	                    JComboBox combo = (JComboBox)e.getSource();
	                    String current = (String)combo.getSelectedItem();
	
	                    DefaultComboBoxModel model = new DefaultComboBoxModel(getSystemController().getMMConfigNames(current));
	                    channelname.setModel( model );
					}
	            }            
	    );
		
		channelgroup.setSelectedIndex(ind);
		if(ind != 0){
			DefaultComboBoxModel model = new DefaultComboBoxModel(getSystemController().getMMConfigNames(this.getConfigGroup()));
	        channelname.setModel(model);
	        
	        int ind2  = model.getIndexOf(this.getConfigName());
	        channelname.setSelectedIndex(ind2);
		}
		
		
		exposurespin = new JSpinner(new SpinnerNumberModel(this.getExposure(), 1, 10000000, 1));
		exposurespin.setName(LABEL_EXPOSURE);
		waitingspin = new JSpinner(new SpinnerNumberModel(this.getWaitingTime(), 0, 10000000, 0.5)); 
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
	
		channelgroup.setPreferredSize(intervalspin.getPreferredSize());
		channelname.setPreferredSize(intervalspin.getPreferredSize());
		
		int nrow = 4;
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
		panelHolder[1][0].add(numframelab);
		panelHolder[2][0].add(waitinglab);
		panelHolder[0][1].add(exposurespin);
		panelHolder[1][1].add(numframespin);
		panelHolder[2][1].add(waitingspin);
		panelHolder[0][2].add(channellab);
		panelHolder[2][2].add(intervallab);
		panelHolder[0][3].add(channelgroup);
		panelHolder[1][3].add(channelname);
		panelHolder[2][3].add(intervalspin);
		
		panelHolder[3][3].add(activatecheck);
		panelHolder[3][2].add(stoponmaxcheck);
		panelHolder[3][0].add(waitonmaxlab);
		panelHolder[3][1].add(waitonmaxspin);
	
		return pane;
	}

	private void setUseActivation(boolean b){
		useactivation_  = b;
	}

	private void setUseStopOnMaxUV(boolean b){
		stoponmax_ = b;
	}
	
	private void setUseStopOnMaxUVDelay(int delay){
		stoponmaxdelay_ = delay;
	}
	
	@Override
	public void readOutParameters(JPanel pane) {
		if(pane.getName().equals(PANE_NAME)){
			Component[] comp = pane.getComponents();
			String groupname = "", groupmember = "";
			for(int i=0;i<comp.length;i++){
				if(!(comp[i] instanceof JLabel) && comp[i].getName() != null){
					if(comp[i].getName().equals(LABEL_GROUP) && comp[i] instanceof JComboBox){
						groupname = (String) ((JComboBox) comp[i]).getSelectedItem();
					}else if(comp[i].getName().equals(LABEL_GROUPNAME) && comp[i] instanceof JComboBox){
						groupmember = (String) ((JComboBox) comp[i]).getSelectedItem();
					}else if(comp[i].getName().equals(LABEL_EXPOSURE) && comp[i] instanceof JSpinner){
						this.setExposureTime((Integer) ((JSpinner) comp[i]).getValue());
					}else if(comp[i].getName().equals(LABEL_PAUSE) && comp[i] instanceof JSpinner){
						this.setWaitingTime((Integer) ((JSpinner) comp[i]).getValue());
					}else if(comp[i].getName().equals(LABEL_NUMFRAME) && comp[i] instanceof JSpinner){
						this.setNumberFrames((Integer) ((JSpinner) comp[i]).getValue());
					}else if(comp[i].getName().equals(LABEL_INTERVAL) && comp[i] instanceof JSpinner){
						this.setIntervalMs((Integer) ((JSpinner) comp[i]).getValue());
					}else if(comp[i].getName().equals(LABEL_USEACTIVATION) && comp[i] instanceof JCheckBox){
						this.setUseActivation(((JCheckBox) comp[i]).isSelected());
					}else if(comp[i].getName().equals(LABEL_USESTOPONMAXUV) && comp[i] instanceof JCheckBox){
						this.setUseStopOnMaxUV(((JCheckBox) comp[i]).isSelected());
					}else if(comp[i].getName().equals(LABEL_MAXUVTIME) && comp[i] instanceof JSpinner){
						this.setUseStopOnMaxUVDelay((Integer) ((JSpinner) comp[i]).getValue());
					}
				}
			}	
			this.setConfigurationGroup(getSystemController().getMMConfigGroup(groupname), groupmember);
		}
	}

	@Override
	public PropertyFilter getPropertyFilter() {
		return new NoPropertyFilter();
	}

	@Override
	public String[] getCharacteristicSettings() {
		String[] s = new String[5];
		s[0] = "Exposure = "+this.getExposure()+" ms";
		s[1] = "Number of frames = "+this.getNumberFrames();
		s[2] = "Use activation = "+useactivation_;
		s[3] = "Stop on max UV = "+stoponmax_;
		s[4] = "Stop on max delay = "+stoponmaxdelay_+" s";
		return s;
	}

}
