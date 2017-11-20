package main.embl.rieslab.htSMLM.ui.uiproperties.filters;

import main.embl.rieslab.htSMLM.ui.uiproperties.UIProperty;

public class TwoStatePropertyFilter extends PropertyFilter {

	public TwoStatePropertyFilter(){
	}

	public TwoStatePropertyFilter(PropertyFilter additionalfilter){
		super(additionalfilter);
	}
	
	@Override
	public boolean filterOut(UIProperty property) {
		if(property.isTwoState()){
			return false;
		}
		return true;
	}

}
