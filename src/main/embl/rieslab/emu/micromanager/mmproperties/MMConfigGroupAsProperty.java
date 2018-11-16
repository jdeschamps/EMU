package main.embl.rieslab.emu.micromanager.mmproperties;

import java.util.ArrayList;

import main.embl.rieslab.emu.ui.uiproperties.UIProperty;
import mmcorej.CMMCore;

public class MMConfigGroupAsProperty extends MMProperty<String> {

	@SuppressWarnings("rawtypes")
	private ArrayList<MMProperty> affectedmmprops_;
	
	@SuppressWarnings("rawtypes")
	MMConfigGroupAsProperty(CMMCore core, String deviceLabel, String propertyLabel, String[] allowedValues, ArrayList<MMProperty> affectedmmprops) {
		super(core, deviceLabel, propertyLabel, allowedValues);

		type_ = MMPropertyType.MMCONF;
		affectedmmprops_ = affectedmmprops;
	}
	
	@Override
	public String getValue() {
		// ask core for value
		String val;
		try {
			val = convertToValue(getCore().getCurrentConfig(getMMPropertyLabel()));
			value = val;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	@Override
	public String getStringValue() {
		return getValue();
	}

	@Override
	public void setStringValue(String stringval, UIProperty source){
		if(!isReadOnly()){
			if(isAllowed(stringval)){
				try{
					// set value
					value = stringval;

					getCore().setConfig(getMMPropertyLabel(), stringval);
					notifyListeners(source, stringval);
					notifyMMProperties();
					
				} catch (Exception e){
					System.out.println("Error in setting configuration ["+getHash()+"] to ["+stringval+"] from ["+value+"]");

					e.printStackTrace(); 
				}
			} else {
				System.out.println("VALUE NOT ALLOWED: in ["+getHash()+"], set value ["+stringval+"] from ["+value+"]");
			}
		}
	}

	private void notifyMMProperties() {
		for(int i=0; i<affectedmmprops_.size();i++){
			// notify its listeners
			affectedmmprops_.get(i).updateMMProperty();
		}
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
		// doesn't have a range
		return true;
	}

	@Override
	public boolean isAllowed(String val) { // always have allowed values, so no checking here
		for(int i=0;i<getAllowedValues().length;i++){
			if(areEqual(val, getAllowedValues()[i])){
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean areEqual(String val1, String val2) {
		return val1.equals(val2);
	}

}
