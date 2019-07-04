package test.java.de.embl.rieslab.emu.dummyclasses;

import main.java.de.embl.rieslab.emu.micromanager.mmproperties.EmptyMMProperty;
import main.java.de.embl.rieslab.emu.ui.uiproperties.UIProperty;

public class TestMMProperty extends EmptyMMProperty<String> {

	public static final String DEV = "MyDevice";
	public static final String PROP = "MyProperty";
	
	public TestMMProperty() {
		super("String", DEV, PROP);
		this.value = "default";
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
	protected boolean areEqual(String val1, String val2) {
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
	public void setStringValue(String stringval, UIProperty source) {
		value = stringval;
		notifyListeners(source, stringval);
	}
};