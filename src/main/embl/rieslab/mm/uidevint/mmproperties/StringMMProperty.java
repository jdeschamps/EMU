package main.embl.rieslab.mm.uidevint.mmproperties;

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
	 * @param readOnly True if the device property is read-only, false otherwise.
	 */
	StringMMProperty(CMMCore core, String deviceLabel, String propertyLabel) {
		super(core, deviceLabel, propertyLabel, true);
		
		type_ = MMPropertyType.STRING;
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
		
		type_ = MMPropertyType.STRING;
	}

	@Override
	public String convertToValue(String s) {
		return s;
	}

	@Override
	public String convertToValue(int s) {
		return String.valueOf(s);
	}

	@Override
	public String convertToValue(double s) {
		return String.valueOf(s);
	}

	@Override
	public String[] arrayFromStrings(String[] s) {
		return s;
	}

	@Override
	public String convertToString(String val) {
		return val.toString();
	}

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

	@Override
	public boolean isAllowed(String val) {
		if(hasAllowedValues()){
			for(int i=0;i<getAllowedValues().length;i++){
				if(areEqual(val, getAllowedValues()[i])){
					return true;
				}
			}
		} else if(hasLimits()){
			return isInRange(val);
		}
		return true;
	}

	@Override
	public boolean areEqual(String val1, String val2) {
		return val1.equals(val2);
	}
}
