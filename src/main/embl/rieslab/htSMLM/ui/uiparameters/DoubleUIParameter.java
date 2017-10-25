package main.embl.rieslab.htSMLM.ui.uiparameters;

import main.embl.rieslab.htSMLM.util.utils;

public class DoubleUIParameter extends UIParameter<Double> {

	public DoubleUIParameter(String name, String description, double val) {
		super(name, description);

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

}
