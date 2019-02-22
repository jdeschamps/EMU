package main.java.embl.rieslab.emu.uiexamples.htsmlm.filters;

import main.java.embl.rieslab.emu.ui.uiproperties.UIProperty;

public class NonConfigGroupPropertyFilter extends PropertyFilter {

	public NonConfigGroupPropertyFilter(PropertyFilter additionalfilter){
		super(additionalfilter);
	}
	
	@Override
	public boolean filterOut(UIProperty property) {
		if(property.isConfigGroupMMProperty()){
			return true;
		}
		return false;
	}

}