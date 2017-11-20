package main.embl.rieslab.htSMLM.ui.uiproperties.filters;

import main.embl.rieslab.htSMLM.ui.uiproperties.UIProperty;

public class AllocatedPropertyFilter extends PropertyFilter {

	@Override
	public boolean filterOut(UIProperty property) {
		if(property.isAllocated()){
			return false;
		}
		return true;
	}

}
