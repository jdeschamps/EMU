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
import main.embl.rieslab.htSMLM.ui.uiproperties.TwoStateUIProperty;
import main.embl.rieslab.htSMLM.ui.uiproperties.UIProperty;
import main.embl.rieslab.htSMLM.ui.uiproperties.filters.PropertyFilter;
import main.embl.rieslab.htSMLM.ui.uiproperties.filters.SinglePropertyFilter;

public class ZStackAcquisition extends Acquisition {

	public final static String[] EMPTY = {"Empty"};
	
	// Convenience constants		
	private final static String PANE_NAME = "Zstack panel";
	private final static String LABEL_GROUP = "Group:";
	private final static String LABEL_EXPOSURE = "Exposure (ms):";
	private final static String LABEL_PAUSE = "Pause (s):";
	private final static String LABEL_ZSTART = "Z start/end/step (um):";
	private final static String LABEL_ZEND = "Z end (um):";
	private final static String LABEL_ZSTEP = "Z step (um):";
	private final static String LABEL_GROUPNAME = "GroupName";
	
	// UI property
	private TwoStateUIProperty stabprop_;
	
	public ZStackAcquisition(SystemController controller, String stabprop) {
		super(AcquisitionType.ZSTACK, controller);

		UIProperty prop = getSystemController().getProperty(stabprop);
		if(prop instanceof TwoStateUIProperty){
			stabprop_ = (TwoStateUIProperty) prop;
		} else {
			stabprop_ = null;
		}
	}

	@Override
	public void preAcquisition() {
		if(stabprop_ != null){
			stabprop_.setPropertyValue(TwoStateUIProperty.OFF);
		}
	}

	@Override
	public void postAcquisition() {
		if(stabprop_ != null){
			stabprop_.setPropertyValue(TwoStateUIProperty.ON);
		}
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
		
		JLabel channellab, exposurelab, waitinglab, zstartlab;
		JComboBox channelgroup;
		final JComboBox channelname;
		JSpinner exposurespin, waitingspin, zstartspin, zendspin, zstepspin;
		
		channellab = new JLabel(LABEL_GROUP);
		exposurelab = new JLabel(LABEL_EXPOSURE);
		waitinglab = new JLabel(LABEL_PAUSE);
		zstartlab = new JLabel(LABEL_ZSTART);
		
		String[] s = getSystemController().getMMConfigGroups();
		String[] s2 = new String[s.length+1];
		String[] s3 = new String[1];
		s2[0] = EMPTY[0];
		s3[0] = s2[0];
		for(int i=0;i<s.length;i++){
			s2[i+1] = s[i];
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
		
		exposurespin = new JSpinner(new SpinnerNumberModel(10, 1, 10000000, 1));
		exposurespin.setName(LABEL_EXPOSURE);
		waitingspin = new JSpinner(new SpinnerNumberModel(0, 0, 10000000, 0.5)); 
		waitingspin.setName(LABEL_PAUSE);
		zstartspin = new JSpinner(new SpinnerNumberModel(-2, -1000, 1000, 0.05)); 
		zstartspin.setName(LABEL_ZSTART);
		zendspin = new JSpinner(new SpinnerNumberModel(2, -1000, 1000, 1)); 
		zendspin.setName(LABEL_ZEND);
		zstepspin = new JSpinner(new SpinnerNumberModel(0.02, -1000, 1000, 0.01));
		zstepspin.setName(LABEL_ZSTEP);
		

		channelgroup.setPreferredSize(zstepspin.getPreferredSize());
		channelname.setPreferredSize(zstepspin.getPreferredSize());
		
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
		panelHolder[2][0].add(zstartlab);
		panelHolder[0][1].add(exposurespin);
		panelHolder[1][1].add(waitingspin);
		panelHolder[2][1].add(zstartspin);
		panelHolder[0][2].add(channellab);
		panelHolder[2][2].add(zendspin);
		panelHolder[0][3].add(channelgroup);
		panelHolder[1][3].add(channelname);
		panelHolder[2][3].add(zstepspin);
		

		return pane;
	}

	@Override
	public void readOutParameters(JPanel pane) {
		if(pane.getName().equals(PANE_NAME)){
			Component[] comp = pane.getComponents();
			String groupname = "", groupmember = "";
			double zstart=0, zend=0, zstep=0;
			for(int i=0;i<comp.length;i++){
				if(!(comp[i] instanceof JLabel)){
					if(comp[i].getName().equals(LABEL_GROUP) && comp[i] instanceof JComboBox){
						groupname = (String) ((JComboBox) comp[i]).getSelectedItem();
					}else if(comp[i].getName().equals(LABEL_GROUPNAME) && comp[i] instanceof JComboBox){
						groupmember = (String) ((JComboBox) comp[i]).getSelectedItem();
					}else if(comp[i].getName().equals(LABEL_EXPOSURE) && comp[i] instanceof JSpinner){
						this.setExposureTime((Integer) ((JSpinner) comp[i]).getValue());
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
			this.setSlices(zstart, zend, zstep);
			this.setConfigurationGroup(getSystemController().getMMConfigGroup(groupname), groupmember);
		}
	}

	@Override
	public PropertyFilter getPropertyFilter() {
		return new SinglePropertyFilter(stabprop_.getName());
	}

}
