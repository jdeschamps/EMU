package main.java.embl.rieslab.emu.uiexamples.htsmlm.filters;

import main.java.embl.rieslab.emu.ui.uiproperties.UIProperty;

public class AllocatedPropertyFilter extends PropertyFilter {
	
	public AllocatedPropertyFilter(PropertyFilter additionalfilter){
		super(additionalfilter);
	}

	
	@Override
	public boolean filterOut(UIProperty property) {
		if(property.isAssigned()){
			return false;
		}
		return true;
	}

}
