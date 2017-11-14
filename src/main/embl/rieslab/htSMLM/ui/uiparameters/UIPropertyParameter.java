package main.embl.rieslab.htSMLM.ui.uiparameters;

import main.embl.rieslab.htSMLM.ui.PropertyPanel;

public class UIPropertyParameter extends UIParameter<String>{
	
	public static String NO_PROPERTY = "None";

	public UIPropertyParameter(PropertyPanel owner, String name, String description) {
		super(owner, name, description);
		setValue(NO_PROPERTY);
	}
	
	@Override
	public void setType() {
		type_ = UIParameterType.UIPROPERTY;
	}

	@Override
	public boolean isSuitable(String val) {
		return true;
	}

	@Override
	protected String convertValue(String val) {
		return val;
	}

	@Override
	public String getStringValue() {
		return getValue();
	}
	
}
