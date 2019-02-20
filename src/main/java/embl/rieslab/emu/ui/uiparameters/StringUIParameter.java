package main.java.embl.rieslab.emu.ui.uiparameters;

import main.java.embl.rieslab.emu.ui.ConfigurablePanel;
import main.java.embl.rieslab.emu.ui.uiparameters.UIParameter;

public class StringUIParameter extends UIParameter<String>{

	public StringUIParameter(ConfigurablePanel owner, String name, String description, String value) {
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
