package main.embl.rieslab.htSMLM.ui.uiparameters;

import java.awt.Color;

import main.embl.rieslab.htSMLM.ui.PropertyPanel;

public class ColorUIParameter extends UIParameter<Color>{
	

	public ColorUIParameter(PropertyPanel owner, String name, String description, Color value) {
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
		return Color.getColor(val);
	}
	
}
