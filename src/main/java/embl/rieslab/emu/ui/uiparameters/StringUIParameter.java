package main.java.embl.rieslab.emu.ui.uiparameters;

import main.java.embl.rieslab.emu.ui.ConfigurablePanel;
import main.java.embl.rieslab.emu.ui.uiparameters.UIParameter;

/**
 * UIParameter with String type. It can be used for instance to define the displayed text
 * of JButtons or of titled borders.
 * 
 * @author Joran Deschamps
 *
 */
public class StringUIParameter extends UIParameter<String>{

	public StringUIParameter(ConfigurablePanel owner, String name, String description, String value) {
		super(owner, name, description);
		setValue(value);
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public UIParameterType getType() {
		return UIParameterType.STRING;
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public boolean isSuitable(String val) {
		return true;
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public String convertValue(String val) {
		return val;
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public String getStringValue() {
		return getValue();
	}

}
