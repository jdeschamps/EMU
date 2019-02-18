package main.java.embl.rieslab.emu.ui.internalproperties;

import main.java.embl.rieslab.emu.ui.ConfigurablePanel;

public class IntInternalProperty extends InternalProperty<Integer>{

	public IntInternalProperty(ConfigurablePanel owner, String name, int defaultvalue) {
		super(owner, name, defaultvalue);
	}

	@Override
	public void setType() {
		type_ = InternalPropertyType.INTEGER;
	}

}
