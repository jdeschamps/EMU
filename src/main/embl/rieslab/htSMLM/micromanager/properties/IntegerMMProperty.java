package main.embl.rieslab.htSMLM.micromanager.properties;

import mmcorej.CMMCore;

public class IntegerMMProperty extends MMProperty<Integer> {

	public IntegerMMProperty(CMMCore core, String deviceLabel, String propertyLabel, boolean readOnly) {
		super(core, deviceLabel, propertyLabel, readOnly);
		
		type_ = MMPropertyType.INTEGER;
	}
	
	public IntegerMMProperty(CMMCore core, String deviceLabel, String propertyLabel, double upLimit, double downLimit) {
		super(core, deviceLabel, propertyLabel, upLimit, downLimit);
		
		type_ = MMPropertyType.INTEGER;
	}
	
	public IntegerMMProperty(CMMCore core, String deviceLabel, String propertyLabel, String[] allowedValues) {
		super(core, deviceLabel, propertyLabel, allowedValues);
		
		type_ = MMPropertyType.INTEGER;
	}

	@Override
	public Integer convertToValue(String s) {
		Double val = (double) 0;
		try{
			val = Double.parseDouble(s);
		} catch(Exception e){
			System.out.println("IntergerMMProperty: Error parsing int ["+s+"]"); 			///// implement log
		}
		return val.intValue();
	}

	@Override
	public Integer convertToValue(int s) {
		return s;
	}

	@Override
	public Integer convertToValue(double s) {
		Double val = new Double(s);
		return val.intValue();
	}

	@Override
	public Integer[] allowedValuesFromString(String[] s) {
		Integer[] allowedVal = new Integer[s.length];
		for(int i=0;i<s.length;i++){
			allowedVal[i] = convertToValue(s[i]);
		}
		return allowedVal;
	}

	@Override
	public String convertToString(Integer val) {
		return val.toString();
	}

	@Override
	public boolean isInRange(Integer val) {
		if(hasLimits()){
			if(val>=getLowerLimit() & val<=getUpperLimit() & val<=getMax() & val>=getMin()){
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isAllowed(Integer val) {
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
	public boolean areEqual(Integer val1, Integer val2) {
		return val1 == val2;
	}

}
