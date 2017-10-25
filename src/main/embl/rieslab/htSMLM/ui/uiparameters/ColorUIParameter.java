package main.embl.rieslab.htSMLM.ui.uiparameters;

import java.awt.Color;

public class ColorUIParameter extends UIParameter<Color>{
	

	public ColorUIParameter(String name, String description, Color value) {
		super(name, description);
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
