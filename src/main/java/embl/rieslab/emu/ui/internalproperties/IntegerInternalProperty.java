package main.java.embl.rieslab.emu.ui.internalproperties;

import java.util.concurrent.atomic.AtomicInteger;

import main.java.embl.rieslab.emu.ui.ConfigurablePanel;

public class IntegerInternalProperty extends InternalProperty<AtomicInteger, Integer> {

	public IntegerInternalProperty(ConfigurablePanel owner, String name, Integer defaultvalue) {
		super(owner, name, defaultvalue);
	}

	@Override
	public InternalPropertyType getType() {
		return InternalPropertyType.INTEGER;
	}

	@Override
	protected Integer convertValue(AtomicInteger val) {
		return val.get();
	}

	@Override
	protected void setAtomicValue(Integer val) {
		getAtomicValue().set(val);
	}

	@Override
	protected AtomicInteger initializeDefault(Integer defaultval) {
		return new AtomicInteger(defaultval);
	}

}
