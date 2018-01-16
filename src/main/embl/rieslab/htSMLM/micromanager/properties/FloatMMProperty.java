package main.embl.rieslab.htSMLM.micromanager.properties;

import mmcorej.CMMCore;

public class FloatMMProperty extends MMProperty<Float> {

	public FloatMMProperty(CMMCore core, String deviceLabel, String propertyLabel, boolean readOnly) {
		super(core, deviceLabel, propertyLabel, readOnly);
		
		type_ = MMPropertyType.FLOAT;
	}
	
	public FloatMMProperty(CMMCore core, String deviceLabel, String propertyLabel, double upLimit, double downLimit) {
		super(core, deviceLabel, propertyLabel, upLimit, downLimit);
		
		type_ = MMPropertyType.FLOAT;
	}
	
	public FloatMMProperty(CMMCore core, String deviceLabel, String propertyLabel, String[] allowedValues) {
		super(core, deviceLabel, propertyLabel, allowedValues);
		
		type_ = MMPropertyType.FLOAT;
	}

	@Override
	public Float convertToValue(String s) {
		float val = 0;
		try{
			val = Float.parseFloat(s);
		} catch(Exception e){
			System.out.println("Error parsing float"); 			///// implement log
		}
		return val;
	}

	@Override
	public Float convertToValue(int s) {
		return new Float(s);
	}

	@Override
	public Float convertToValue(double s) {
		return new Float(s);
	}

	@Override
	public Float[] allowedValuesFromString(String[] s) {
		Float[] allowedVal = new Float[s.length];
		for(int i=0;i<s.length;i++){
			allowedVal[i] = convertToValue(s[i]);
		}
		return allowedVal;
	}

	@Override
	public String convertToString(Float val) {
		return val.toString();
	}

	@Override
	public boolean isInRange(Float val) {
		if(hasLimits()){
			if(val>getLowerLimit() & val<getUpperLimit() & val<getMax() & val>getMin()){
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isAllowed(Float val) {
		if(hasAllowedValues()){
			for(int i=0;i<getAllowedValues().length;i++){
				if(areEqual(val, getAllowedValues()[i])){
					return true;
				}
			}
			return false;
		} else if(hasLimits()){
			return isInRange(val);
		}
		return true;
	}

	@Override
	public boolean areEqual(Float val1, Float val2) {
		return val1 == val2;
	}
	
}
