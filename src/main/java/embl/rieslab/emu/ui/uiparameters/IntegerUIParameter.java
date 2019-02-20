package main.java.embl.rieslab.emu.ui.uiparameters;

import main.java.embl.rieslab.emu.ui.ConfigurablePanel;
import main.java.embl.rieslab.emu.ui.uiparameters.UIParameter;
import main.java.embl.rieslab.emu.utils.utils;

public class IntegerUIParameter extends UIParameter<Integer>{

	public IntegerUIParameter(ConfigurablePanel owner, String name, String description, int value) {
		super(owner, name, description);
		
		setValue(value);
	}

	@Override
	public UIParameterType getType() {
		return UIParameterType.INTEGER;
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
