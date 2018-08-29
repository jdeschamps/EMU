package main.embl.rieslab.mm.uidevint.mmproperties;

import mmcorej.CMMCore;

public class StringMMProperty extends MMProperty<String> {

	public StringMMProperty(CMMCore core, String deviceLabel, String propertyLabel) {
		super(core, deviceLabel, propertyLabel, true);
		
		type_ = MMPropertyType.STRING;
	}
	
	public StringMMProperty(CMMCore core, String deviceLabel, String propertyLabel, double upLimit, double downLimit) {
		super(core, deviceLabel, propertyLabel, upLimit, downLimit);
		
		type_ = MMPropertyType.STRING;
	}
	
	public StringMMProperty(CMMCore core, String deviceLabel, String propertyLabel, String[] allowedValues) {
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
	public String[] allowedValuesFromString(String[] s) {
		return s;
	}

	@Override
	public String convertToString(String val) {
		return val.toString();
	}

	@Override
	public boolean isInRange(String val) {
		return false;
	}

	@Override
	public boolean isAllowed(String val) {
		if(hasAllowedValues()){
			for(int i=0;i<getAllowedValues().length;i++){
				if(areEqual(val, getAllowedValues()[i])){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean areEqual(String val1, String val2) {
		return val1.equals(val2);
	}
}
