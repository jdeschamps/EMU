package main.java.embl.rieslab.emu.ui.uiparameters;

import main.java.embl.rieslab.emu.ui.ConfigurablePanel;
import main.java.embl.rieslab.emu.ui.uiparameters.UIParameter;

public class BoolUIParameter extends UIParameter<Boolean>{

	public BoolUIParameter(ConfigurablePanel owner, String name, String description, boolean value) {
		super(owner, name, description);
		
		setValue(value);
	}

	@Override
	public UIParameterType getType() {
		return UIParameterType.BOOL;
	}

	@Override
	public boolean isSuitable(String val) {
		if(val.equals("true") || val.equals("false")){
			return true;
		}
		return false;
	}

	@Override
	protected Boolean convertValue(String val) {
		if(val.equals("true")){
			return true;
		}
		return false;
	}

	@Override
	public String getStringValue() {
		return String.valueOf(getValue());
	}


}
