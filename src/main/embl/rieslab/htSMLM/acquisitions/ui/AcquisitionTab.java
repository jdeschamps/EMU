package main.embl.rieslab.htSMLM.acquisitions.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import main.embl.rieslab.htSMLM.acquisitions.Acquisition;
import main.embl.rieslab.htSMLM.acquisitions.AcquisitionFactory;
import main.embl.rieslab.htSMLM.acquisitions.AcquisitionType;
import main.embl.rieslab.htSMLM.controller.SystemController;
import main.embl.rieslab.htSMLM.ui.AcquisitionPanel;

public class AcquisitionTab extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7966565586677957738L;

	public static final String DEFAULT_NAME = AcquisitionType.LOCALIZATION.getTypeValue();

	private SystemController controller_;
	private Acquisition acq_;
	private AcquisitionPanel owner_;
	private AcquisitionFactory factory_;
	private JPanel pane_;
	private JComboBox acqtype_;
	
	public AcquisitionTab(SystemController controller, AcquisitionPanel owner) {
		controller_ = controller;
		owner_ = owner;
		
		factory_ = new AcquisitionFactory(owner_);
		
		acq_ = factory_.getDefaultAcquisition(); 
		pane_ = acq_.getPanel();
		
		acqtype_ = new JComboBox(AcquisitionType.getList());
		acqtype_.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	            setTabName();
	            changeAcquisition((String) acqtype_.getSelectedItem());
	    	}
	    });
		setUpPanel();
	}


	private void setUpPanel() {
		
	}

    public void setTabName(){
    	String name = (String) acqtype_.getSelectedItem();
    	if(name != null){
    		this.setName(name);
    	} else {
    		this.setName(AcquisitionType.LOCALIZATION.getTypeValue());
    	}
    }
	
	private HashMap<String, String> getProperties(){
		HashMap<String, String> props = new HashMap<String, String>();
		
		// read out
		
		return props;
	}

	private void changeAcquisition(String type){
		acq_ = factory_.getAcquisition(type);
		pane_ = acq_.getPanel();
		this.repaint();
	}
	
	public Acquisition getAcquisition() {
		acq_.setProperties(getProperties());
		acq_.readOutParameters(pane_);
		return acq_;
	}


}
