package main.java.embl.rieslab.emu.uiexamples.htsmlm.filters;

import main.java.embl.rieslab.emu.ui.uiproperties.UIProperty;

public class SinglePropertyFilter extends PropertyFilter{

	private String excludedprop_;
	
	public SinglePropertyFilter(String excludedproperty){
		excludedprop_ = excludedproperty;
	}
	
	public SinglePropertyFilter(String excludedproperty, PropertyFilter additionalfilter){
		super(additionalfilter);
		
		excludedprop_ = excludedproperty;
	}

	
	@Override
	public boolean filterOut(UIProperty property) {
		if(property.getLabel().equals(excludedprop_)){
			return true;
		}
		return false;
	}

}
