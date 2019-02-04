package main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.acquisitiontypes;

import java.awt.Component;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import main.java.embl.rieslab.emu.ui.uiproperties.TwoStateUIProperty;
import main.java.embl.rieslab.emu.ui.uiproperties.filters.NoPropertyFilter;
import main.java.embl.rieslab.emu.ui.uiproperties.filters.PropertyFilter;
import main.java.embl.rieslab.emu.ui.uiproperties.filters.SinglePropertyFilter;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.AcquisitionFactory.AcquisitionType;
import main.java.embl.rieslab.emu.utils.utils;

import org.micromanager.Studio;
import org.micromanager.data.Coords;
import org.micromanager.data.Datastore;
import org.micromanager.data.Image;
import org.micromanager.data.internal.DefaultCoords;

public class ZStackAcquisition implements Acquisition {
	
	// Convenience constants		
	private final static String PANE_NAME = "Autofocus panel";
	private final static String LABEL_EXPOSURE = "Exposure (ms):";
	private final static String LABEL_PAUSE = "Pause (s):";
	private final static String LABEL_ZSTART = "Z start:";
	private final static String LABEL_ZEND = "Z end (um):";
	private final static String LABEL_ZSTEP = "Z step (um):";
	private final static String LABEL_ZDEVICE = "Z stage:";
	private final static String LABEL_CHECK = "Focus-lock";
		
	public final static String KEY_ZSTART = "Z start";
	public final static String KEY_ZEND = "Z end";
	public final static String KEY_ZSTEP = "Z step";
	public final static String KEY_ZDEVICE = "Z stage";
	public final static String KEY_USEFL = "Use focus-lock";
	
	// UI property
	private TwoStateUIProperty stabprop_;

	private String zdevice_;
	private String[] zdevices_;
	private double zstart, zend, zstep;
	private boolean zstab_, zstabuse_; 
	private GenericAcquisitionParameters params_;
	private volatile boolean stopAcq_, running_;
	
	public ZStackAcquisition(double exposure, String[] zdevices, String defaultzdevice, TwoStateUIProperty twoStateUIProperty) {

		if(twoStateUIProperty.isAllocated()){
			stabprop_ = twoStateUIProperty;
		}
		
		if(stabprop_ == null){
			zstab_ = false;
			zstabuse_ = false;
		} else {
			zstab_ = true;
			zstabuse_ = true;
		}
		
		zstart=-2;
		zend=2;
		zstep=0.05;

		zdevice_ = defaultzdevice;
		zdevices_ = zdevices;
		
		stopAcq_ = false;
		running_ = false;
		
		params_ = new GenericAcquisitionParameters(AcquisitionType.ZSTACK, 
				exposure, 0, 3, 1, new HashMap<String,String>(), new HashMap<String,String>(), getSlices(zstart, zend, zstep));
	}
	
	public ArrayList<Double> getSlices(double zstart, double zend, double zstep){
		ArrayList<Double> slices = new ArrayList<Double>();
		double z = utils.round(zstart-zstep,2);
		
		while (z<=zend){
			z += zstep;
			slices.add(utils.round(z,2));
		}

		return slices;
	}
	
	private void setSlices(double zstart, double zend, double zstep){
		params_.setZSlices(getSlices(zstart,zend,zstep));
	}
	
	@Override
	public GenericAcquisitionParameters getParameters() {
		return params_;
	}

	@Override
	public void startAcquisition(Studio studio, Datastore store) {
		stopAcq_ = false;
		running_ = true;
		
		if(zstab_ && zstabuse_){
			stabprop_.setPropertyValue(TwoStateUIProperty.getOffStateName());
		}
		
		studio.displays().createDisplay(store);
		
		Image image;
		Coords.CoordsBuilder builder = new DefaultCoords.Builder();
		builder.time(0).channel(0).stagePosition(0);
		
		try{
			double z0 = studio.getCMMCore().getPosition(zdevice_);
			
			for(int i=0;i<params_.getZSlices().size();i++){
				
				// set stage position
				double pos = z0 + params_.getZSlices().get(i);
				studio.getCMMCore().setPosition(zdevice_, pos);
				
				builder = builder.z(i);
				image = studio.live().snap(false).get(0);
				image = image.copyAtCoords(builder.build());
				
				try {
					store.putImage(image);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				// check if exit
				if(stopAcq_){
					break;
				}
			
			}
			running_ = false;
			
			// close display
			studio.displays().closeDisplaysFor(store);
			
			// go back to original position
			studio.getCMMCore().setPosition(zdevice_, z0);
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		if(zstab_ && zstabuse_){
			stabprop_.setPropertyValue(TwoStateUIProperty.getOnStateName());
		}
	}

	@Override
	public void stopAcquisition() {
		stopAcq_ = true;
	}

	@Override
	public boolean isRunning() {
		return running_;
	}

	@Override
	public boolean skipPosition() {
		return false;
	}

	@Override
	public JPanel getPanel() {
		JPanel pane = new JPanel();
		
		pane.setName(getPanelName());
		
		JLabel exposurelab, waitinglab, zstartlab, zdevicelabel;
		JSpinner exposurespin, waitingspin, zstartspin, zendspin, zstepspin;
		
		JCheckBox usefocuslocking = new JCheckBox(LABEL_CHECK);
		usefocuslocking.setName(LABEL_CHECK);
		usefocuslocking.setEnabled(zstab_);
		
		exposurelab = new JLabel(LABEL_EXPOSURE);
		waitinglab = new JLabel(LABEL_PAUSE);
		zstartlab = new JLabel(LABEL_ZSTART);
		zdevicelabel = new JLabel(LABEL_ZDEVICE);
		
		exposurespin = new JSpinner(new SpinnerNumberModel(Math.max(params_.getExposureTime(),1), 1, 10000, 1));
		exposurespin.setName(LABEL_EXPOSURE);
		waitingspin = new JSpinner(new SpinnerNumberModel(params_.getWaitingTime(), 0, 10000, 1)); 
		waitingspin.setName(LABEL_PAUSE);
		zstartspin = new JSpinner(new SpinnerNumberModel(zstart, -1000, 1000, 0.05)); 
		zstartspin.setName(LABEL_ZSTART);
		zendspin = new JSpinner(new SpinnerNumberModel(zend, -1000, 1000, 1)); 
		zendspin.setName(LABEL_ZEND);
		zstepspin = new JSpinner(new SpinnerNumberModel(zstep, -1000, 1000, 0.01));
		zstepspin.setName(LABEL_ZSTEP);
		
		JComboBox<String> zdevices = new JComboBox<String>(zdevices_);
		zdevices.setSelectedItem(zdevice_);
		zdevices.setName(LABEL_ZDEVICE);

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

		panelHolder[0][0].add(zdevicelabel);
		panelHolder[0][1].add(zdevices);
		panelHolder[0][2].add(usefocuslocking);
		
		panelHolder[1][0].add(exposurelab);
		panelHolder[2][0].add(zstartlab);
		
		panelHolder[1][1].add(exposurespin);
		panelHolder[2][1].add(zstartspin);
		
		panelHolder[1][2].add(waitinglab);
		panelHolder[2][2].add(zendspin);
		
		panelHolder[1][3].add(waitingspin);
		panelHolder[2][3].add(zstepspin);

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
							} else if(comp[i].getName().equals(LABEL_PAUSE) && comp[i] instanceof JSpinner){
								params_.setWaitingTime((Integer) ((JSpinner) comp[i]).getValue());
							} else if(comp[i].getName().equals(LABEL_ZSTART) && comp[i] instanceof JSpinner){
								zstart = ((Double) ((JSpinner) comp[i]).getValue());
							} else if(comp[i].getName().equals(LABEL_ZEND) && comp[i] instanceof JSpinner){
								zend = ((Double) ((JSpinner) comp[i]).getValue());
							} else if(comp[i].getName().equals(LABEL_ZSTEP) && comp[i] instanceof JSpinner){
								zstep = ((Double) ((JSpinner) comp[i]).getValue());
							} else if(comp[i].getName().equals(LABEL_ZDEVICE) && comp[i] instanceof JComboBox){
								zdevice_ = ((String) ((JComboBox) comp[i]).getSelectedItem());
							}else if(comp[i].getName().equals(LABEL_CHECK) && comp[i] instanceof JCheckBox){
								zstabuse_ = ((JCheckBox) comp[i]).isSelected();
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
		String[] s = new String[6];
		s[0] = "Exposure = "+params_.getExposureTime()+" ms";
		s[1] = "Stage = "+zdevice_;
		s[2] = "Zstart = "+zstart+" um";
		s[3] = "Zend = "+zend+" um";
		s[4] = "Zstep = "+zstep+" um";
		s[5] = "Use focus-lock = "+String.valueOf(zstabuse_);
		return s;
	}

	@Override
	public String getPanelName() {
		return PANE_NAME;
	}
	
	@Override
	public String[][] getAdditionalJSONParameters() {
		String[][] s = new String[5][2];

		s[0][0] = KEY_ZSTART;
		s[0][1] = String.valueOf(zstart);
		s[1][0] = KEY_ZEND;
		s[1][1] = String.valueOf(zend);
		s[2][0] = KEY_ZSTEP;
		s[2][1] = String.valueOf(zstep);
		s[3][0] = KEY_ZDEVICE;
		s[3][1] = zdevice_;
		s[4][0] = KEY_USEFL;
		s[4][1] = String.valueOf(zstabuse_);
		
		return s;
	}

	public void setZRange(double zstart, double zend, double zstep){
		this.zstart = zstart;
		this.zend = zend;
		this.zstep = zstep;
		
		this.setSlices(zstart,zend,zstep);
	}

	public void setZDevice(String zdevice){
		zdevice_ = zdevice;
	}

	public void setUseFocusLock(boolean zstabuse){
		zstabuse_ = zstabuse;
	}
	
	@Override
	public AcquisitionType getType() {
		return AcquisitionType.ZSTACK;
	}

	@Override
	public String getShortName() {
		return "Z";
	}
}
