package main.java.de.embl.rieslab.emu.micromanager.mmproperties;

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
	public IntegerMMProperty(CMMCore core, String deviceLabel, String propertyLabel, boolean readOnly) {
		super(core, deviceLabel, propertyLabel, readOnly);
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
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public Integer convertToValue(String s) {
		try{
			Integer val = Integer.parseInt(s);
			return val;
		} catch(Exception e){
			System.out.println(getDeviceLabel()+" - "+getMMPropertyLabel()+": error parsing int "+s); 
		}
		return null;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public Integer convertToValue(int s) {
		return s;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public Integer convertToValue(double s) {
		Double val = new Double(s);
		return val.intValue();
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public Integer[] arrayFromStrings(String[] s) {
		Integer[] allowedVal = new Integer[s.length];
		for(int i=0;i<s.length;i++){
			allowedVal[i] = convertToValue(s[i]);
		}
		return allowedVal;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String convertToString(Integer val) {
		return val.toString();
	}

	/**
	 * @inheritDoc
	 */
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

	/**
	 * @inheritDoc
	 */
	@Override
	public boolean isAllowed(Integer val) {
		if(val == null){
			return false;
		}
		
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

	/**
	 * @inheritDoc
	 */
	@Override
	public boolean areEqual(Integer val1, Integer val2) {
		return val1 == val2;
	}

}
