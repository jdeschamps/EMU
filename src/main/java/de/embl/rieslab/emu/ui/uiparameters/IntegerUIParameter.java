package main.java.de.embl.rieslab.emu.ui.uiparameters;

import main.java.de.embl.rieslab.emu.ui.ConfigurablePanel;
import main.java.de.embl.rieslab.emu.ui.uiparameters.UIParameter;
import main.java.de.embl.rieslab.emu.utils.utils;

/**
 * UIParameter with integer value.
 * 
 * @author Joran Deschamps
 *
 */
public class IntegerUIParameter extends UIParameter<Integer>{

	public IntegerUIParameter(ConfigurablePanel owner, String label, String description, int value) {
		super(owner, label, description);
		
		setValue(value);
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public UIParameterType getType() {
		return UIParameterType.INTEGER;
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public boolean isSuitable(String val) {
		if(utils.isInteger(val)){
			return true;
		}
		return false;
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	protected Integer convertValue(String val) {
		return Integer.parseInt(val);
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public String getStringValue() {
		return String.valueOf(getValue());
	}

}
