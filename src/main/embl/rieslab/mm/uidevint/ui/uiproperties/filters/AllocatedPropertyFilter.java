package main.embl.rieslab.mm.uidevint.ui.uiproperties.filters;

import main.embl.rieslab.mm.uidevint.ui.uiproperties.UIProperty;

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
