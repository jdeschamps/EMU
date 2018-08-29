package main.embl.rieslab.mm.uidevint.ui.uiproperties.filters;

import main.embl.rieslab.mm.uidevint.ui.uiproperties.UIProperty;
import main.embl.rieslab.mm.uidevint.ui.uiproperties.flag.PropertyFlag;

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
		if(property.getFlag().equals(flag_.getPropertyFlag())){
			return true;
		}
		return false;
	}

}
