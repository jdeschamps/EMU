package main.java.embl.rieslab.emu.ui.uiparameters;

import main.java.embl.rieslab.emu.ui.ConfigurablePanel;
import main.java.embl.rieslab.emu.ui.uiparameters.UIParameter;

public class UIPropertyParameter extends UIParameter<String>{
	
	public static String NO_PROPERTY = "None";
	
	private String propertyflag_;

	public UIPropertyParameter(ConfigurablePanel owner, String name, String description, String propertyflag) {
		super(owner, name, description);
		
		propertyflag_ = propertyflag;
		
		setValue(NO_PROPERTY);
	}
	
	@Override
	public UIParameterType getType() {
		return UIParameterType.UIPROPERTY;
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
