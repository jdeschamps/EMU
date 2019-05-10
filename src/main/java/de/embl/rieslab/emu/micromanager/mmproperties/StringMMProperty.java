package main.java.de.embl.rieslab.emu.micromanager.mmproperties;

import mmcorej.CMMCore;

/**
 * Wrapper for a Micro-manager device property with String value. The property can be
 * read-only or not or with a set of allowed values.
 * 
 * @author Joran Deschamps
 *
 */
public class StringMMProperty extends MMProperty<String> {

	/**
	 * Builds a String MMProperty without limits or allowed values. The property can be read-only.
	 * @param core Micro-manager core.
	 * @param deviceLabel Label of the parent device as defined in Micro-manager.
	 * @param propertyLabel Label of the device property as defined in Micro-manager.
	 */
	public StringMMProperty(CMMCore core, String deviceLabel, String propertyLabel) {
		super(core, deviceLabel, propertyLabel, true);
	}
	
	/**
	 * Builds a String MMProperty with allowed values.
	 * 
	 * @param core Micro-manager core.
	 * @param deviceLabel Label of the parent device as defined in Micro-manager.
	 * @param propertyLabel Label of the device property as defined in Micro-manager.
	 * @param allowedValues Array of allowed values.
	 */
	StringMMProperty(CMMCore core, String deviceLabel, String propertyLabel, String[] allowedValues) {
		super(core, deviceLabel, propertyLabel, allowedValues);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String convertToValue(String s) {
		return s;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String convertToValue(int s) {
		return String.valueOf(s);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String convertToValue(double s) {
		return String.valueOf(s);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String[] arrayFromStrings(String[] s) {
		return s;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String convertToString(String val) {
		return val.toString();
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public boolean isInRange(String val) {
		if(hasLimits()){
			if(val.compareTo(String.valueOf(getLowerLimit()))>=0 && val.compareTo(String.valueOf(getUpperLimit()))<=0 
					&& val.compareTo(getMin())>=0 && val.compareTo(getMax())<=0 ){
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
	public boolean isAllowed(String val) {
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
	public boolean areEqual(String val1, String val2) {
		return val1.equals(val2);
	}
}
