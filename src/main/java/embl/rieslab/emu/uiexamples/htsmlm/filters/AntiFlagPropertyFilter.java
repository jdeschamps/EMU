package main.java.embl.rieslab.emu.uiexamples.htsmlm.filters;

import main.java.embl.rieslab.emu.ui.uiproperties.UIProperty;
import main.java.embl.rieslab.emu.ui.uiproperties.flag.PropertyFlag;

public class AntiFlagPropertyFilter extends PropertyFilter {

	private PropertyFlag flag_;
	
	public AntiFlagPropertyFilter(PropertyFlag flag){
		flag_ = flag;
	}

	public AntiFlagPropertyFilter(PropertyFlag flag, PropertyFilter additionalfilter){
		super(additionalfilter);
		
		flag_ = flag;
	}
	
	@Override
	public boolean filterOut(UIProperty property) {
		if(property.getFlag().equals(flag_)){
			return true;
		}
		return false;
	}

}
