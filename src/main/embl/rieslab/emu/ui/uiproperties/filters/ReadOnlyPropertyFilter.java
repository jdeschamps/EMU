package main.embl.rieslab.emu.ui.uiproperties.filters;

import main.embl.rieslab.emu.ui.uiproperties.UIProperty;

public class ReadOnlyPropertyFilter extends PropertyFilter {

	public ReadOnlyPropertyFilter(){
	}

	public ReadOnlyPropertyFilter(PropertyFilter additionalfilter){
		super(additionalfilter);
	}

	
	@Override
	public boolean filterOut(UIProperty property) {
		if(property.isMMPropertyReadOnly()){
			return true;
		}
		return false;
	}

}