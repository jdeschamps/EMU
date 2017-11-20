package main.embl.rieslab.htSMLM.ui.uiparameters;

import main.embl.rieslab.htSMLM.ui.MicroscopeControlUI.PropertyPanel;

public class BoolUIParameter extends UIParameter<Boolean>{

	public BoolUIParameter(PropertyPanel owner, String name, String description, boolean value) {
		super(owner, name, description);
		
		setValue(value);
	}

	@Override
	public void setType() {
		type_ = UIParameterType.BOOL;
	}

	@Override
	public boolean isSuitable(String val) {
		if(val.equals("true") || val.equals("false")){
			return true;
		}
		return false;
	}

	@Override
	protected Boolean convertValue(String val) {
		if(val.equals("true")){
			return true;
		}
		return false;
	}

	@Override
	public String getStringValue() {
		return String.valueOf(getValue());
	}


}
