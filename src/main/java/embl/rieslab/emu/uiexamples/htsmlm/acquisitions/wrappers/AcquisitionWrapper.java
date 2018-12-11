package main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.wrappers;

import java.util.Arrays;
import java.util.HashMap;

import main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.old.Acquisition;

public class AcquisitionWrapper {

	public String type;
	public double exposure, interval;
	public int numFrames, waitingTime;
	public String[][] configurations;
	public String[][] properties;
	public String[][] additionalParameters;
	
	public AcquisitionWrapper(){
		// necessary for JSON deserialization
	}
	
	public AcquisitionWrapper(Acquisition acq){
        type = acq.getType();
        exposure=acq.getExposureTime();
        interval= acq.getIntervalMs();
        numFrames=acq.getNumberFrames();
        waitingTime= acq.getWaitingTime();
        
        
        HashMap<String,String> conf = acq.getMMConfigurationGroups();
        String[] confkeys = conf.keySet().toArray(new String[0]);
        Arrays.sort(confkeys);
        configurations = new String[confkeys.length][2];
       	for(int j=0;j<confkeys.length;j++){
       		configurations[j][0] = confkeys[j];
       		configurations[j][1] = conf.get(confkeys[j]);
       	}

        
        HashMap<String,String> prop = acq.getPropertyValues();
        String[] propkeys = prop.keySet().toArray(new String[0]);
        Arrays.sort(propkeys);
        properties = new String[propkeys.length][2];
       	for(int j=0;j<propkeys.length;j++){
          	properties[j][0] = propkeys[j];
          	properties[j][1] = prop.get(propkeys[j]);
       	}
       	
       	additionalParameters = acq.getAdditionalJSONParameters();
	}
}
