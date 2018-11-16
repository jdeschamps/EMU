package main.embl.rieslab.emu.uiexamples.htsmlm.acquisitions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import main.embl.rieslab.emu.controller.SystemController;
import main.embl.rieslab.emu.uiexamples.htsmlm.AcquisitionPanel;
import main.embl.rieslab.emu.uiexamples.htsmlm.ActivationPanel;
import main.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.ui.AcquisitionUI;
import main.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.wrappers.AcquisitionWrapper;
import main.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.wrappers.Experiment;
import main.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.wrappers.ExperimentWrapper;
import main.embl.rieslab.emu.uiexamples.htsmlm.constants.HTSMLMConstants;
import main.embl.rieslab.emu.utils.utils;

public class AcquisitionFactory {
	
	private AcquisitionUI acqpane_;
	private SystemController controller_;
	
	private String[] acqtypelist_;
	
	public AcquisitionFactory(AcquisitionUI acqpane, SystemController controller){
		acqpane_ = acqpane;
		controller_ = controller;
		
		acqtypelist_ = extractAcquisitionTypes();
	}
	
	private String[] extractAcquisitionTypes() {
		String[] temp = AcquisitionType.getList();
		ArrayList<String> finalacq = new ArrayList<String>();
		
		for(int i=0;i<temp.length;i++){
			if(temp[i].equals(AcquisitionType.BFP.getTypeValue())){ // if the acquisition is of type BFP
				if(acqpane_.isPropertyEnabled(AcquisitionPanel.PARAM_BFP)){ // if enabled in the configuration
					finalacq.add(temp[i]); // then add BFP to the list of possible acquisition
				}
			} else if(temp[i].equals(AcquisitionType.BRIGHTFIELD.getTypeValue())){
				if(acqpane_.isPropertyEnabled(AcquisitionPanel.PARAM_BRIGHTFIELD)){ 
					finalacq.add(temp[i]); 
				}
			} else {
				finalacq.add(temp[i]); 
			}
		}
		
		return finalacq.toArray(new String[0]);
	}

	public String[] getAcquisitionTypeList(){
		return acqtypelist_;
	}
	
	public Acquisition getAcquisition(String type){
		if(type.equals(AcquisitionType.BFP.getTypeValue())){
			return new BFPAcquisition(controller_.getExposure(), controller_.getProperty(acqpane_.getUIPropertyName(AcquisitionPanel.PARAM_BFP)));
		} else if(type.equals(AcquisitionType.LOCALIZATION.getTypeValue())){
			return new LocalizationAcquisition(controller_.getTaskHolder(ActivationPanel.TASK_NAME),controller_.getExposure());
		} else if(type.equals(AcquisitionType.TIME.getTypeValue())){
			return new TimeAcquisition(controller_.getExposure());
		} else if(type.equals(AcquisitionType.BRIGHTFIELD.getTypeValue())){
			return new BrightFieldAcquisition(controller_.getExposure(), controller_.getProperty(acqpane_.getUIPropertyName(AcquisitionPanel.PARAM_BRIGHTFIELD)));
		}  else if(type.equals(AcquisitionType.SNAP.getTypeValue())){
			return new SnapAcquisition(controller_.getExposure());
		} else if(type.equals(AcquisitionType.ZSTACK.getTypeValue())){
			return new ZStackAcquisition(controller_.getExposure(), controller_.getProperty(acqpane_.getUIPropertyName(AcquisitionPanel.PARAM_LOCKING)));
		}
			
		return getDefaultAcquisition();
	}
	
	private Acquisition getDefaultAcquisition() {
		return new LocalizationAcquisition(controller_.getTaskHolder(ActivationPanel.TASK_NAME),controller_.getExposure());
	}

	public boolean writeAcquisitionList(Experiment exp, String path){
		ArrayList<Acquisition> acqlist = exp.getAcquisitionList();
		ArrayList<AcquisitionWrapper> aqwlist = new ArrayList<AcquisitionWrapper>();
		for(int i=0;i<acqlist.size();i++){
			aqwlist.add(new AcquisitionWrapper(acqlist.get(i)));
		}
		
		ExperimentWrapper expw = new ExperimentWrapper(exp.getPauseTime(), exp.getNumberPositions(), aqwlist);
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		
		String name;
		if(path.endsWith(HTSMLMConstants.ACQ_EXT)){
			name = path;
		} else if(!path.endsWith("/")){
			name = path+"/"+HTSMLMConstants.ACQ_NAME;
		} else {
			name = path+HTSMLMConstants.ACQ_NAME;
		}
		
		boolean fileExists = true;
		while(fileExists){
			File f = new File(name);
			if(f.exists()) { 
			    name = incrementAcquisitionFileName(name);
			} else {
				fileExists = false;
			}
		}
		
		try {
			objectMapper.writeValue(new FileOutputStream(name), expw);
			
			return true;
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	private String incrementAcquisitionFileName(String name) {
		String newname = name.substring(0, name.length()-HTSMLMConstants.ACQ_EXT.length()-1);
		int ind = 0;
		for(int i=0;i<newname.length();i++){
			if(newname.charAt(i) == '_'){
				ind = i;
			}
		}
		
		if(ind == 0){
			newname = newname+"_1."+HTSMLMConstants.ACQ_EXT;
		} else {
			if(utils.isInteger(newname.substring(ind+1))){
				int num = Integer.valueOf(newname.substring(ind+1))+1;
				newname = newname.substring(0, ind+1)+String.valueOf(num)+"."+HTSMLMConstants.ACQ_EXT;
			} else {
				newname = newname+"_1."+HTSMLMConstants.ACQ_EXT;
			}
		}
		
		return newname;
	}


	public Experiment readAcquisitionList(String path){	
		ArrayList<Acquisition> acqlist = new ArrayList<Acquisition>();
		int waitingtime = 3;
		int numpos = 0;

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		
		try {
			ExperimentWrapper expw = objectMapper.readValue(new FileInputStream(path), ExperimentWrapper.class);			

			ArrayList<AcquisitionWrapper> acqwlist = expw.acqwlist;	

			waitingtime = expw.pausetime;
			numpos = expw.numberpositions;
			
			if(acqwlist != null && !acqwlist.isEmpty()){
				for(int i=0;i<acqwlist.size();i++){
					AcquisitionWrapper acqw = acqwlist.get(i);
					if(acqw.type.equals(AcquisitionType.BFP.getTypeValue())){
						BFPAcquisition acq = (BFPAcquisition) getAcquisition(acqw.type);
						acq.setExposureTime(acqw.exposure);
						acq.setWaitingTime(acqw.waitingTime);
						
						HashMap<String,String> props = new HashMap<String,String>();
						if(acqw.props != null){
							for(int j=0;j<acqw.props.length;j++){
								props.put(acqw.props[j][0], acqw.props[j][1]);
							}
						}
						acq.setProperties(props);
						
						////// add configuration groups
						// TODO
						
						acqlist.add(acq);
						
					} else if(acqw.type.equals(AcquisitionType.BRIGHTFIELD.getTypeValue())){
						BrightFieldAcquisition acq = (BrightFieldAcquisition) getAcquisition(acqw.type);
						acq.setExposureTime(acqw.exposure);
						acq.setWaitingTime(acqw.waitingTime);
						
						HashMap<String,String> props = new HashMap<String,String>();
						if(acqw.props != null){
							for(int j=0;j<acqw.props.length;j++){
								props.put(acqw.props[j][0], acqw.props[j][1]);
							}
						}
						acq.setProperties(props);
						
						acqlist.add(acq);
						
					} else if(acqw.type.equals(AcquisitionType.ZSTACK.getTypeValue())){
						ZStackAcquisition acq = (ZStackAcquisition) getAcquisition(acqw.type);
						acq.setExposureTime(acqw.exposure);
						acq.setWaitingTime(acqw.waitingTime);	
						
						HashMap<String,String> props = new HashMap<String,String>();
						if(acqw.props != null){
							for(int j=0;j<acqw.props.length;j++){
								props.put(acqw.props[j][0], acqw.props[j][1]);
							}
						}
						acq.setProperties(props);
						
						acq.setSlices(Double.parseDouble(acqw.additionalParams[0][1]), Double.parseDouble(acqw.additionalParams[1][1]), Double.parseDouble(acqw.additionalParams[2][1]));
						acq.setZStart(Double.parseDouble(acqw.additionalParams[0][1]));
						acq.setZEnd(Double.parseDouble(acqw.additionalParams[1][1]));
						acq.setZStep(Double.parseDouble(acqw.additionalParams[2][1]));
						
						acq.setNumberFrames(acqw.numFrames);
						acq.setIntervalMs(acqw.interval);
						
						acqlist.add(acq);

					} else if(acqw.type.equals(AcquisitionType.LOCALIZATION.getTypeValue())){
						LocalizationAcquisition acq = (LocalizationAcquisition) getAcquisition(acqw.type);
						acq.setExposureTime(acqw.exposure);
						acq.setWaitingTime(acqw.waitingTime);	
						
						HashMap<String,String> props = new HashMap<String,String>();
						if(acqw.props != null){
							for(int j=0;j<acqw.props.length;j++){
								props.put(acqw.props[j][0], acqw.props[j][1]);
							}
						}
						acq.setProperties(props);
						
						acq.setUseActivation(Boolean.parseBoolean(acqw.additionalParams[0][1]));
						acq.setUseStopOnMaxUV(Boolean.parseBoolean(acqw.additionalParams[1][1]));
						acq.setUseStopOnMaxUVDelay(Integer.parseInt(acqw.additionalParams[2][1]));

						acq.setNumberFrames(acqw.numFrames);
						acq.setIntervalMs(acqw.interval);
						
						acqlist.add(acq);

					} else if(acqw.type.equals(AcquisitionType.TIME.getTypeValue())){
						TimeAcquisition acq = (TimeAcquisition) getAcquisition(acqw.type);
						acq.setExposureTime(acqw.exposure);
						acq.setWaitingTime(acqw.waitingTime);
			
						HashMap<String,String> props = new HashMap<String,String>();
						if(acqw.props != null){
							for(int j=0;j<acqw.props.length;j++){
								props.put(acqw.props[j][0], acqw.props[j][1]);
							}
						}
						acq.setProperties(props);
						
						acq.setNumberFrames(acqw.numFrames);
						acq.setIntervalMs(acqw.interval);		
						
						acqlist.add(acq);
					} else if(acqw.type.equals(AcquisitionType.SNAP.getTypeValue())){
						SnapAcquisition acq = (SnapAcquisition) getAcquisition(acqw.type);
						acq.setExposureTime(acqw.exposure);
						acq.setWaitingTime(acqw.waitingTime);
			
						HashMap<String,String> props = new HashMap<String,String>();
						if(acqw.props != null){
							for(int j=0;j<acqw.props.length;j++){
								props.put(acqw.props[j][0], acqw.props[j][1]);
							}
						}
						acq.setProperties(props);
						
						acq.setNumberFrames(acqw.numFrames);
						acq.setIntervalMs(acqw.interval);		
						
						acqlist.add(acq);
					}
				}
			}
			
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new Experiment(waitingtime, numpos, acqlist);
	}
}
