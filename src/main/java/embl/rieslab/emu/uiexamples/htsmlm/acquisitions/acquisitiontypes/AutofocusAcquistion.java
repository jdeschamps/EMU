package main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.acquisitiontypes;

import java.io.IOException;
import java.util.HashMap;

import javax.swing.JPanel;

import main.java.embl.rieslab.emu.ui.uiproperties.TwoStateUIProperty;
import main.java.embl.rieslab.emu.ui.uiproperties.UIProperty;
import main.java.embl.rieslab.emu.ui.uiproperties.filters.PropertyFilter;

import org.micromanager.Studio;
import org.micromanager.data.Coords;
import org.micromanager.data.Datastore;
import org.micromanager.data.Image;
import org.micromanager.data.internal.DefaultCoords;

public class AutofocusAcquistion implements Acquisition{


	private GenericAcquisitionParameters params_;
	private UIProperty stagepos_;
	private TwoStateUIProperty stabprop_;
	private boolean zstab_;
	private volatile boolean stopAcq_, running_;
	
	public AutofocusAcquistion(double exposure, UIProperty zposition, TwoStateUIProperty stabprop){
		if(zposition == null){
			throw new NullPointerException();
		}
		stagepos_ = zposition;
		
		stabprop_ = stabprop;
		if(stabprop_ == null){
			zstab_ = false;
		} else {
			zstab_ = true;
		}
		
		stopAcq_ = false;
		running_ = false;
	
		params_ = new GenericAcquisitionParameters(exposure, 0, 3, 0, new HashMap<String,String>(), new HashMap<String,String>());
	}
	
	@Override
	public GenericAcquisitionParameters getParameters() {
		return params_;
	}

	@Override
	public Datastore startAcquisition(Studio studio) {
		stopAcq_ = false;
		running_ = true;
		
		if(zstab_){
			stabprop_.setPropertyValue(TwoStateUIProperty.getOffStateName());
		}
		
		Datastore store = studio.data().createRAMDatastore();
		studio.displays().createDisplay(store);
		
		Image image;
		Coords.CoordsBuilder builder = new DefaultCoords.Builder();
		builder.z(0).channel(0).stagePosition(0);
		
		try{
			double z0 = Double.parseDouble(stagepos_.getMMPropertyValue());
			double metrics = 0;
			for(int i=0;i<params_.getZSlices().size();i++){
				
				
				builder = builder.time(i);
				image = studio.live().snap(false).get(0);
				image = image.copyAtCoords(builder.build());
				
				try {
					store.putImage(image);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				// calculate metrics
				
				// calculate next position and move
				double pos = z0;
				stagepos_.setPropertyValue(String.valueOf(pos));
				
				// check if exit
				if(stopAcq_){
					break;
				}
			
			}
			running_ = false;
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		if(zstab_){
			stabprop_.setPropertyValue(TwoStateUIProperty.getOnStateName());
		}
		
		return store; 
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPanelName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void readOutParameters(JPanel pane) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PropertyFilter getPropertyFilter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getSpecialSettings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[][] getAdditionalJSONParameters() {
		// TODO Auto-generated method stub
		return null;
	}

}
