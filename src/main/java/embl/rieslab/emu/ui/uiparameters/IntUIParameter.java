package main.java.embl.rieslab.emu.ui.uiparameters;

import main.java.embl.rieslab.emu.ui.ConfigurablePanel;
import main.java.embl.rieslab.emu.ui.uiparameters.UIParameter;
import main.java.embl.rieslab.emu.ui.uiparameters.UIParameterType;
import main.java.embl.rieslab.emu.utils.utils;

public class IntUIParameter extends UIParameter<Integer>{

	public IntUIParameter(ConfigurablePanel owner, String name, String description, int value) {
		super(owner, name, description);
		
		setValue(value);
	}

	@Override
	public void setType() {
		type_ = UIParameterType.INTEGER;
	}

	@Override
	public boolean isSuitable(String val) {
		if(utils.isInteger(val)){
			return true;
		}
		return false;
	}

	@Override
	protected Integer convertValue(String val) {
		return Integer.parseInt(val);
	}

	@Override
	public String getStringValue() {
		return String.valueOf(getValue());
	}

}
