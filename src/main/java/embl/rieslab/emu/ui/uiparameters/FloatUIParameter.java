package main.java.embl.rieslab.emu.ui.uiparameters;

import main.java.embl.rieslab.emu.ui.ConfigurablePanel;
import main.java.embl.rieslab.emu.ui.uiparameters.UIParameter;
import main.java.embl.rieslab.emu.utils.utils;

public class FloatUIParameter extends UIParameter<Float>{

	public FloatUIParameter(ConfigurablePanel owner, String name, String description, Float val) {
		super(owner, name, description);
		setValue(val);
	}

	@Override
	public void setType() {
		type_ = UIParameterType.FLOAT;
	}

	@Override
	public boolean isSuitable(String val) {
		if(utils.isFloat(val)){
			return true;
		}
		return false;
	}

	@Override
	protected Float convertValue(String val) {
		return Float.parseFloat(val);
	}

	@Override
	public String getStringValue() {
		return String.valueOf(getValue());
	}

}
