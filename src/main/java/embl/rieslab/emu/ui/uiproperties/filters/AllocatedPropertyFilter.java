package main.java.embl.rieslab.emu.ui.uiproperties.filters;

import main.java.embl.rieslab.emu.ui.uiproperties.UIProperty;

public class AllocatedPropertyFilter extends PropertyFilter {
	
	public AllocatedPropertyFilter(PropertyFilter additionalfilter){
		super(additionalfilter);
	}

	
	@Override
	public boolean filterOut(UIProperty property) {
		if(property.isAllocated()){
			return false;
		}
		return true;
	}

}
