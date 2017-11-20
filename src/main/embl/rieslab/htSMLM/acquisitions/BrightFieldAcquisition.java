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

public class BrightFieldAcquisition extends Acquisition {

	public final static String[] EMPTY = {"Empty"};
	
	// Convenience constants		
	private final static String PANE_NAME = "Bright field panel";
	private final static String LABEL_GROUP = "Group:";
	private final static String LABEL_GROUPNAME = "GroupName";
	private final static String LABEL_EXPOSURE = "Exposure (ms):";
	private final static String LABEL_PAUSE = "Pause (s):";
	
	// UI property
	private TwoStateUIProperty bfprop_;

	public BrightFieldAcquisition(SystemController controller, String bfprop) {
		super(AcquisitionType.BRIGHTFIELD, controller);
		
		UIProperty prop = getSystemController().getProperty(bfprop);
		if(prop instanceof TwoStateUIProperty){
			bfprop_ = (TwoStateUIProperty) prop;
		} else {
			bfprop_ = null;
		}
	}

	@Override
	public void preAcquisition() {
		if(bfprop_ != null){
			bfprop_.setPropertyValue(TwoStateUIProperty.ON);
		}
	}

	@Override
	public void postAcquisition() {
		if(bfprop_ != null){
			bfprop_.setPropertyValue(TwoStateUIProperty.OFF);
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

		pane.setBorder(BorderFactory.createTitledBorder(null, ACQ_SETTINGS, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, new Color(0,0,0)));
		((TitledBorder) pane.getBorder()).setTitleFont(((TitledBorder) pane.getBorder()).getTitleFont().deriveFont(Font.BOLD, 12));
				
		JLabel channellab, exposurelab, waitinglab;
		JComboBox channelgroup;
		final JComboBox channelname;
		JSpinner exposurespin, waitingspin;
		
		channellab = new JLabel(LABEL_GROUP);
		exposurelab = new JLabel(LABEL_EXPOSURE);
		waitinglab = new JLabel(LABEL_PAUSE);
		
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

		channelgroup.setPreferredSize(exposurespin.getPreferredSize());
		channelname.setPreferredSize(exposurespin.getPreferredSize());
		
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
		panelHolder[0][2].add(channellab);
		panelHolder[0][3].add(channelgroup);
		panelHolder[1][3].add(channelname);
		
		return pane;
	}

	@Override
	public void readOutParameters(JPanel pane) {
		if(pane.getName().equals(getPanelName())){
			Component[] comp = pane.getComponents();
			String groupname = "", groupmember = "";
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
					}
				}
			}	
			this.setConfigurationGroup(getSystemController().getMMConfigGroup(groupname), groupmember);
		}
	}

	@Override
	public PropertyFilter getPropertyFilter() {
		return new SinglePropertyFilter(bfprop_.getName());
	}

}
