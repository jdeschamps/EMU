package main.embl.rieslab.htSMLM.ui.uiparameters;

public class StringUIParameter extends UIParameter<String>{

	public StringUIParameter(String name, String description, String value) {
		super(name, description);
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

}
