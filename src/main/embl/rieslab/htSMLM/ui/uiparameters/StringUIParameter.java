package main.embl.rieslab.htSMLM.ui.uiparameters;

import main.embl.rieslab.htSMLM.ui.MicroscopeControlUI.PropertyPanel;

public class StringUIParameter extends UIParameter<String>{

	public StringUIParameter(PropertyPanel owner, String name, String description, String value) {
		super(owner, name, description);
		setValue(value);
	}

	@Override
	public void setType() {
		type_ = UIParameterType.STRING;
	}

	@Override
	public boolean isSuitable(String val) {
		return true;
	}

	@Override
	public String convertValue(String val) {
		return val;
	}

	@Override
	public String getStringValue() {
		return getValue();
	}

}
