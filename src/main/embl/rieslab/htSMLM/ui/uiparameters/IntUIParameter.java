package main.embl.rieslab.htSMLM.ui.uiparameters;

import main.embl.rieslab.htSMLM.util.utils;

public class IntUIParameter extends UIParameter<Integer>{

	public IntUIParameter(String name, String description, int value) {
		super(name, description);
		
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

}
