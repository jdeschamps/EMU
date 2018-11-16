package main.embl.rieslab.emu.micromanager.mmproperties;


import mmcorej.CMMCore;

/**
 * Wrapper for a Micro-manager device property with float value. The property can be
 * read-only or not, with limits or with a set of allowed values.
 * 
 * @author Joran Deschamps
 *
 */
public class FloatMMProperty extends MMProperty<Float> {

	/**
	 * Builds a float MMProperty without limits or allowed values. The property can be read-only.
	 * @param core Micro-manager core.
	 * @param deviceLabel Label of the parent device as defined in Micro-manager.
	 * @param propertyLabel Label of the device property as defined in Micro-manager.
	 * @param readOnly True if the device property is read-only, false otherwise.
	 */
	public FloatMMProperty(CMMCore core, String deviceLabel, String propertyLabel, boolean readOnly) {
		super(core, deviceLabel, propertyLabel, readOnly);
	}
	
	/**
	 * Builds a float MMProperty with limits. 
	 * 
	 * @param core Micro-manager core.
	 * @param deviceLabel Label of the parent device as defined in Micro-manager.
	 * @param propertyLabel Label of the device property as defined in Micro-manager.
	 * @param upperLimit Upper limit of the device property value.
	 * @param lowerLimit Lower limit of the device property value.
	 */
	FloatMMProperty(CMMCore core, String deviceLabel, String propertyLabel, double upLimit, double downLimit) {
		super(core, deviceLabel, propertyLabel, upLimit, downLimit);
	}
	
	/**
	 * Builds a float MMProperty with allowed values.
	 * 
	 * @param core Micro-manager core.
	 * @param deviceLabel Label of the parent device as defined in Micro-manager.
	 * @param propertyLabel Label of the device property as defined in Micro-manager.
	 * @param allowedValues Array of allowed values.
	 */
	FloatMMProperty(CMMCore core, String deviceLabel, String propertyLabel, String[] allowedValues) {
		super(core, deviceLabel, propertyLabel, allowedValues);
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
	public Float[] arrayFromStrings(String[] s) {
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
			if(val.compareTo(new Float(getLowerLimit()))>=0 && val.compareTo(new Float(getUpperLimit()))<=0 
					&& val.compareTo(getMax())<=0 && val.compareTo(getMin())>=0){
				return true;
			}
			return false;
		}
		return true;
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
