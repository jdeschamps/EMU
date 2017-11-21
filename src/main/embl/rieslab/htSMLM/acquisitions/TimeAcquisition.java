package main.embl.rieslab.htSMLM.acquisitions;


import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;

import main.embl.rieslab.htSMLM.configuration.SystemController;
import main.embl.rieslab.htSMLM.ui.uiproperties.filters.NoPropertyFilter;
import main.embl.rieslab.htSMLM.ui.uiproperties.filters.PropertyFilter;

public class TimeAcquisition extends Acquisition {
	
	// Convenience constants		
	private final static String PANE_NAME = "Time panel";
	private final static String LABEL_GROUP = "Group:";
	private final static String LABEL_EXPOSURE = "Exposure (ms):";
	private final static String LABEL_PAUSE = "Pause (s):";
	private final static String LABEL_NUMFRAME = "Number of frames:";
	private final static String LABEL_GROUPNAME = "GroupName";
	private final static String LABEL_INTERVAL = "Interval (ms):";
	
	public TimeAcquisition(SystemController controller) {
		super(AcquisitionType.TIME, controller);

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
		
		pane.setName(PANE_NAME);
		
		JLabel channellab, exposurelab, waitinglab, numframelab, intervallab;
		JComboBox channelgroup;
		final JComboBox channelname;
		JSpinner exposurespin, waitingspin, numframespin, intervalspin;
		
		channellab = new JLabel(LABEL_GROUP);
		exposurelab = new JLabel(LABEL_EXPOSURE);
		waitinglab = new JLabel(LABEL_PAUSE);
		numframelab = new JLabel(LABEL_NUMFRAME);
		intervallab = new JLabel(LABEL_INTERVAL);
		
		String[] s = getSystemController().getMMConfigGroups();
		String[] s2 = new String[s.length+1];
		String[] s3 = new String[1];
		s2[0] = EMPTY[0];
		s3[0] = s2[0];
		int ind=0;
		for(int i=0;i<s.length;i++){
			s2[i+1] = s[i];
			if(this.getConfigGroup() != null && s[i].equals(this.getConfigGroup())){
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
		

		channelgroup.setPreferredSize(intervalspin.getPreferredSize());
		channelname.setPreferredSize(intervalspin.getPreferredSize());
		
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
		

		return pane;
	}

	@Override
	public void readOutParameters(JPanel pane) {
		if(pane.getName().equals(PANE_NAME)){
			Component[] comp = pane.getComponents();
			String groupname="", groupmember="";
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
		String[] s = new String[3];
		s[0] = "Exposure = "+this.getExposure()+" ms";
		s[1] = "Number of frames = "+this.getNumberFrames();
		s[2] = "Interval = "+this.getIntervalMs()+" ms";
		return s;
	}

}
