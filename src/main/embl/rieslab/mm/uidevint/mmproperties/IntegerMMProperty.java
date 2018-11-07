package main.embl.rieslab.mm.uidevint.mmproperties;

import mmcorej.CMMCore;

/**
 * Wrapper for a Micro-manager device property with integer value. The property can be
 * read-only or not, with limits or with a set of allowed values.
 * 
 * @author Joran Deschamps
 *
 */
public class IntegerMMProperty extends MMProperty<Integer> {

	/**
	 * Builds an integer MMProperty without limits or allowed values. The property can be read-only.
	 * @param core Micro-manager core.
	 * @param deviceLabel Label of the parent device as defined in Micro-manager.
	 * @param propertyLabel Label of the device property as defined in Micro-manager.
	 * @param readOnly True if the device property is read-only, false otherwise.
	 */
	IntegerMMProperty(CMMCore core, String deviceLabel, String propertyLabel, boolean readOnly) {
		super(core, deviceLabel, propertyLabel, readOnly);
		
		type_ = MMPropertyType.INTEGER;
	}
	
	/**
	 * Builds an integer MMProperty with limits. 
	 * 
	 * @param core Micro-manager core.
	 * @param deviceLabel Label of the parent device as defined in Micro-manager.
	 * @param propertyLabel Label of the device property as defined in Micro-manager.
	 * @param upperLimit Upper limit of the device property value.
	 * @param lowerLimit Lower limit of the device property value.
	 */
	IntegerMMProperty(CMMCore core, String deviceLabel, String propertyLabel, double upLimit, double downLimit) {
		super(core, deviceLabel, propertyLabel, upLimit, downLimit);
		
		type_ = MMPropertyType.INTEGER;
	}
	
	/**
	 * Builds an integer MMProperty with allowed values.
	 * 
	 * @param core Micro-manager core.
	 * @param deviceLabel Label of the parent device as defined in Micro-manager.
	 * @param propertyLabel Label of the device property as defined in Micro-manager.
	 * @param allowedValues Array of allowed values.
	 */
	IntegerMMProperty(CMMCore core, String deviceLabel, String propertyLabel, String[] allowedValues) {
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
	public Integer[] arrayFromStrings(String[] s) {
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
			if(val>=getLowerLimit() && val<=getUpperLimit() && val<=getMax() && val>=getMin()){
				return true;
			}
			return false;
		}
		return true;
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
