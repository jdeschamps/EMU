package main.java.embl.rieslab.emu.ui.uiproperties.filters;

import main.java.embl.rieslab.emu.ui.uiproperties.UIProperty;

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
