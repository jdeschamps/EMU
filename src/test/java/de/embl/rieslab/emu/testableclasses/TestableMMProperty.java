package de.embl.rieslab.emu.testableclasses;

import de.embl.rieslab.emu.micromanager.mmproperties.EmptyMMProperty;
import de.embl.rieslab.emu.ui.uiproperties.UIProperty;

public class TestableMMProperty extends EmptyMMProperty<String> {

	public static final String DEV = "MyDevice";
	public static final String DEFVAL = "default";
	
	public TestableMMProperty(String propname) {
		super("String", DEV, propname);
		this.value = DEFVAL;
	}

	@Override
	protected String convertToValue(String s) {
		return s;
	}

	@Override
	protected String convertToValue(int i) {
		return String.valueOf(i);
	}

	@Override
	protected String convertToValue(double d) {
		return String.valueOf(d);
	}

	@Override
	protected String[] arrayFromStrings(String[] s) {
		return s;
	}

	@Override
	protected String convertToString(String val) {
		return val;
	}

	@Override
	protected boolean areEquals(String val1, String val2) {
		return val1.equals(val2);
	}

	@Override
	protected boolean isAllowed(String val) {
		return true;
	}

	@Override
	public String getValue() {
		return this.value;
	}

	@Override
	public String getStringValue() {
		return this.value;
	}

	@Override
	public boolean setValue(String stringval, UIProperty source) {
		value = stringval;
		notifyListeners(source, stringval);
		return true;
	}
};