package main.embl.rieslab.htSMLM.ui.uiproperties.filters;

import main.embl.rieslab.htSMLM.ui.uiproperties.UIProperty;

public class NoPropertyFilter extends PropertyFilter {

	public NoPropertyFilter(){
	}

	public NoPropertyFilter(PropertyFilter additionalfilter){
		super(additionalfilter);
	}
	
	@Override
	protected boolean filterOut(UIProperty property) {
		return false;
	}

}
