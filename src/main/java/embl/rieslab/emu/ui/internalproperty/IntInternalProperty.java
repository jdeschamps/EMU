package main.java.embl.rieslab.emu.ui.internalproperty;

import main.java.embl.rieslab.emu.ui.PropertyPanel;

public class IntInternalProperty extends InternalProperty<Integer>{

	public IntInternalProperty(PropertyPanel owner, String name, int defaultvalue) {
		super(owner, name, defaultvalue);
	}

	@Override
	public void setType() {
		type_ = InternalPropertyType.INTEGER;
	}

}
