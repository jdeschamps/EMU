package main.embl.rieslab.htSMLM.acquisitions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import main.embl.rieslab.htSMLM.acquisitions.ui.AcquisitionUI;
import main.embl.rieslab.htSMLM.configuration.SystemConstants;
import main.embl.rieslab.htSMLM.configuration.SystemController;
import main.embl.rieslab.htSMLM.ui.MicroscopeControlUI.AcquisitionPanel;
import main.embl.rieslab.htSMLM.ui.MicroscopeControlUI.ActivationPanel;

public class AcquisitionFactory {
	
	private AcquisitionUI acqpane_;
	private SystemController controller_;
	
	public AcquisitionFactory(AcquisitionUI acqpane, SystemController controller){
		acqpane_ = acqpane;
		controller_ = controller;
	}
	
	public Acquisition getAcquisition(String type){
		if(type.equals(AcquisitionType.BFP.getTypeValue())){
			return new BFPAcquisition(controller_.getExposure(), controller_.getConfigurationGroups(), controller_.getProperty(acqpane_.getUIPropertyName(AcquisitionPanel.PARAM_BFP)));
		} else if(type.equals(AcquisitionType.LOCALIZATION.getTypeValue())){
			return new LocalizationAcquisition(controller_.getTaskHolder(ActivationPanel.TASK_NAME),controller_.getExposure(), controller_.getConfigurationGroups());
		} else if(type.equals(AcquisitionType.TIME.getTypeValue())){
			return new TimeAcquisition(controller_.getExposure(), controller_.getConfigurationGroups());
		} else if(type.equals(AcquisitionType.BRIGHTFIELD.getTypeValue())){
			return new BrightFieldAcquisition(controller_.getExposure(), controller_.getConfigurationGroups(), controller_.getProperty(acqpane_.getUIPropertyName(AcquisitionPanel.PARAM_BRIGHTFIELD)));
		} else if(type.equals(AcquisitionType.ZSTACK.getTypeValue())){
			return new ZStackAcquisition(controller_.getExposure(), controller_.getConfigurationGroups(), controller_.getProperty(acqpane_.getUIPropertyName(AcquisitionPanel.PARAM_LOCKING)));
		}
			
		return getDefaultAcquisition();
	}
	
	public Acquisition getDefaultAcquisition() {
		return new LocalizationAcquisition(controller_.getTaskHolder(ActivationPanel.TASK_NAME),controller_.getExposure(), controller_.getConfigurationGroups());
	}

	public boolean writeAcquisitionList(ArrayList<Acquisition> acqlist, String path){
		try {
			String filepath = path;
			
			if(!filepath.endsWith("/")){
				filepath += "/";
			}

			FileOutputStream f = new FileOutputStream(new File(filepath+SystemConstants.ACQ_NAME));
			ObjectOutputStream o = new ObjectOutputStream(f);

			// Write objects to file
			o.writeObject(acqlist);

			o.close();
			f.close();
			
			return true;
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error initializing stream");
			e.printStackTrace();
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Acquisition> readAcquisitionList(String path){	
		ArrayList<Acquisition> acqlist = new ArrayList<Acquisition>();
		String filepath = path;
		
		if(!filepath.endsWith("/")){
			filepath += "/";
		}
		
		try {
			FileInputStream fi = new FileInputStream(new File(filepath));
			ObjectInputStream oi = new ObjectInputStream(fi);

			// Read objects
			acqlist = (ArrayList<Acquisition>) oi.readObject();

			oi.close();
			fi.close();

		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error initializing stream");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return acqlist;
	}
}
