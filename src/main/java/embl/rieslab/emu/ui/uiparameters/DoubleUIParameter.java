package main.java.embl.rieslab.emu.ui.uiparameters;

import main.java.embl.rieslab.emu.ui.ConfigurablePanel;
import main.java.embl.rieslab.emu.ui.uiparameters.UIParameter;
import main.java.embl.rieslab.emu.utils.utils;

public class DoubleUIParameter extends UIParameter<Double> {

	public DoubleUIParameter(ConfigurablePanel owner, String name, String description, double val) {
		super(owner, name, description);

		setValue(val);
	}

	@Override
	public void setType() {
		type_ = UIParameterType.DOUBLE;
	}

	@Override
	public boolean isSuitable(String val) {
		if(utils.isNumeric(val)){
			return true;
		}
		return false;
	}

	@Override
	protected Double convertValue(String val) {
		return Double.parseDouble(val);
	}

	@Override
	public String getStringValue() {
		return String.valueOf(getValue());
	}

}
