package main.embl.rieslab.htSMLM.acquisitions;

import java.util.Arrays;
import java.util.HashMap;

public class AcquisitionWrapper {

	public String type, configGroup, configName;
	public double exposure, interval;
	public int numFrames, waitingTime;
	public String[][] props;
	public String[][] additionalParams;
	
	public AcquisitionWrapper(){
		
	}
	
	public AcquisitionWrapper(Acquisition acq){
        type = acq.getType();
        configGroup= acq.getConfigGroup();
        configName= acq.getConfigName();
        exposure=acq.getExposure();
        interval= acq.getIntervalMs();
        numFrames=acq.getNumberFrames();
        waitingTime= acq.getWaitingTime();
        
        HashMap<String,String> prop = acq.getPropertyValues();
        String[] propkeys = prop.keySet().toArray(new String[0]);
        Arrays.sort(propkeys);
        props = new String[propkeys.length][2];
       	for(int j=0;j<propkeys.length;j++){
          	props[j][0] = propkeys[j];
          	props[j][1] = prop.get(propkeys[j]);
       	}
       	
       	additionalParams = acq.getAdditionalJSONParameters();
	}
}
