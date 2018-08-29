package main.embl.rieslab.mm.uidevint.ui.uiparameters;

import main.embl.rieslab.mm.uidevint.ui.PropertyPanel;
import main.embl.rieslab.mm.uidevint.utils.utils;

public class FloatUIParameter extends UIParameter<Float>{

	public FloatUIParameter(PropertyPanel owner, String name, String description, Float val) {
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