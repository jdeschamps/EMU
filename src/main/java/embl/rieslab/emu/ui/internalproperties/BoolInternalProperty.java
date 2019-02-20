package main.java.embl.rieslab.emu.ui.internalproperties;

import java.util.concurrent.atomic.AtomicBoolean;

import main.java.embl.rieslab.emu.ui.ConfigurablePanel;

public class BoolInternalProperty extends InternalProperty<AtomicBoolean, Boolean>{

	public BoolInternalProperty(ConfigurablePanel owner, String name, Boolean defaultvalue) {
		super(owner, name, defaultvalue);
	}

	@Override
	public InternalPropertyType getType() {
		return InternalPropertyType.BOOLEAN;
	}

	@Override
	protected Boolean convertValue(AtomicBoolean val) {
		return val.get();
	}

	@Override
	protected void setAtomicValue(Boolean val) {
		getAtomicValue().set(val);
	}

	@Override
	protected AtomicBoolean initializeDefault(Boolean defaultval) {
		return new AtomicBoolean(defaultval);
	}

}
