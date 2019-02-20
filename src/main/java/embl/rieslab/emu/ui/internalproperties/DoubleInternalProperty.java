package main.java.embl.rieslab.emu.ui.internalproperties;

import java.util.concurrent.atomic.AtomicLong;

import main.java.embl.rieslab.emu.ui.ConfigurablePanel;

public class DoubleInternalProperty extends InternalProperty<AtomicLong, Double>{

	public DoubleInternalProperty(ConfigurablePanel owner, String name, Double defaultvalue) {
		super(owner, name, defaultvalue);
	}

	@Override
	public InternalPropertyType getType() {
		return InternalPropertyType.DOUBLE;
	}

	@Override
	protected Double convertValue(AtomicLong val) {
		return Double.longBitsToDouble(val.get());
	}

	@Override
	protected void setAtomicValue(Double val) {
		getAtomicValue().set(Double.doubleToLongBits(val));
	}

	@Override
	protected AtomicLong initializeDefault(Double defaultval) {
		return new AtomicLong(Double.doubleToLongBits(defaultval));
	}

}
