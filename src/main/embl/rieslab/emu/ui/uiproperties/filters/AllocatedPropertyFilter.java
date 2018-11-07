package main.embl.rieslab.emu.ui.uiproperties.filters;

import main.embl.rieslab.emu.ui.uiproperties.UIProperty;

public class AllocatedPropertyFilter extends PropertyFilter {


	public AllocatedPropertyFilter(){
	}

	
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
