package main.embl.rieslab.mm.uidevint.mmproperties;

import mmcorej.CMMCore;

public class MMPropertyBuilder {
	
	CMMCore core_;
	
	public MMPropertyBuilder(CMMCore core){
		core_ = core;
	}

	@SuppressWarnings("rawtypes")
	public MMProperty getNewProperty(String deviceLabel, String propertyLabel) {
		MMProperty p = null;
		
		try {		
			String type = (core_.getPropertyType(deviceLabel, propertyLabel)).toString();	
				
			boolean readOnly = core_.isPropertyReadOnly(deviceLabel, propertyLabel);
			boolean hasLimits = core_.hasPropertyLimits(deviceLabel, propertyLabel);
			double upLimit = 0; 
			double downLimit = 0;
			if(hasLimits){
				upLimit = core_.getPropertyUpperLimit(deviceLabel, propertyLabel);
				downLimit = core_.getPropertyLowerLimit(deviceLabel, propertyLabel);
			}
			
			String[] allowedValues = core_.getAllowedPropertyValues(deviceLabel, propertyLabel).toArray();		
			boolean allowedValuesIsNull = true;
			if(allowedValues.length>0){
				if(allowedValues[0] != null){
					allowedValuesIsNull = false;
				}
			} 
			
			if(type.equals("Float")){
				if(hasLimits){
					p = new FloatMMProperty(core_, deviceLabel, propertyLabel, upLimit, downLimit);
				} else if(!allowedValuesIsNull){
					p = new FloatMMProperty(core_, deviceLabel, propertyLabel, allowedValues);
				} else {
					p = new FloatMMProperty(core_, deviceLabel, propertyLabel, readOnly);
				}
			} else if(type.equals("Integer")){
				if(hasLimits){
					p = new IntegerMMProperty(core_, deviceLabel, propertyLabel, upLimit, downLimit);				
				} else if(!allowedValuesIsNull){
					p = new IntegerMMProperty(core_, deviceLabel, propertyLabel, allowedValues);
				} else {
					p = new IntegerMMProperty(core_, deviceLabel, propertyLabel, readOnly);
				}
			} else { // String or Undef	
				if(hasLimits){
					p = new StringMMProperty(core_, deviceLabel, propertyLabel, upLimit, downLimit);
				} else if(!allowedValuesIsNull){
					p = new StringMMProperty(core_, deviceLabel, propertyLabel, allowedValues);
				} else {
					p = new StringMMProperty(core_, deviceLabel, propertyLabel);		
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return p;
	}
}
