package main.java.embl.rieslab.emu.ui.uiparameters;

import main.java.embl.rieslab.emu.ui.PropertyPanel;
import main.java.embl.rieslab.emu.ui.uiparameters.UIParameter;
import main.java.embl.rieslab.emu.ui.uiparameters.UIParameterType;

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
