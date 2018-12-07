package main.java.embl.rieslab.emu.ui.uiproperties.filters;

import main.java.embl.rieslab.emu.micromanager.mmproperties.ConfigGroupAsMMProperty;
import main.java.embl.rieslab.emu.ui.uiproperties.UIProperty;

public class NoneConfigGroupPropertyFilter extends PropertyFilter {

	public NoneConfigGroupPropertyFilter(PropertyFilter additionalfilter){
		super(additionalfilter);
	}
	
	@Override
	public boolean filterOut(UIProperty property) {
		if(property.getMMProperty() instanceof ConfigGroupAsMMProperty){
			return true;
		}
		return false;
	}

}