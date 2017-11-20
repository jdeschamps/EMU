package main.embl.rieslab.htSMLM.ui.uiproperties.filters;

import main.embl.rieslab.htSMLM.ui.uiproperties.UIProperty;

public class TwoStatePropertyFilter extends PropertyFilter {

	@Override
	public boolean filterOut(UIProperty property) {
		if(property.isTwoState()){
			return false;
		}
		return true;
	}

}
