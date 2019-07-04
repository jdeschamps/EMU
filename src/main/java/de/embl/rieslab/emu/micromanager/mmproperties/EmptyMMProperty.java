package main.java.de.embl.rieslab.emu.micromanager.mmproperties;

import main.java.de.embl.rieslab.emu.ui.uiproperties.UIProperty;

public abstract class EmptyMMProperty<T> extends MMProperty<T>{

	public EmptyMMProperty(String type, String deviceLabel, String propertyLabel) {
		super(null, type, deviceLabel, propertyLabel, false);
	}
	
	@Override
	public abstract T getValue();
	
	@Override
	public abstract String getStringValue();
	
	@Override
	public abstract void setStringValue(String stringval, UIProperty source);
}
