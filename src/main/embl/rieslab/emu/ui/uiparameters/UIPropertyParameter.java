package main.embl.rieslab.emu.ui.uiparameters;

import main.embl.rieslab.emu.ui.PropertyPanel;

public class UIPropertyParameter extends UIParameter<String>{
	
	public static String NO_PROPERTY = "None";
	
	private String propertyflag_;

	public UIPropertyParameter(PropertyPanel owner, String name, String description, String propertyflag) {
		super(owner, name, description);
		
		propertyflag_ = propertyflag;
		
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
	
	public String getPropertyFlag(){
		return propertyflag_;
	}
}
