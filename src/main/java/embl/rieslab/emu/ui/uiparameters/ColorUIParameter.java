package main.java.embl.rieslab.emu.ui.uiparameters;

import java.awt.Color;

import main.java.embl.rieslab.emu.ui.ConfigurablePanel;
import main.java.embl.rieslab.emu.ui.uiparameters.UIParameter;
import main.java.embl.rieslab.emu.utils.ColorRepository;

public class ColorUIParameter extends UIParameter<Color>{
	

	public ColorUIParameter(ConfigurablePanel owner, String name, String description, Color value) {
		super(owner, name, description);
		setValue(value);
	}
	
	@Override
	public void setType() {
		type_ = UIParameterType.COLOUR;
	}

	@Override
	public boolean isSuitable(String val) {
		return true;
	}

	@Override
	protected Color convertValue(String val) {
		return ColorRepository.getColor(val);
	}

	@Override
	public String getStringValue() {
		return ColorRepository.getStringColor(getValue());
	}
	
}
