package main.embl.rieslab.htSMLM.ui.internalproperty;

import main.embl.rieslab.htSMLM.ui.PropertyPanel;

public class IntInternalProperty extends InternalProperty<Integer>{

	public IntInternalProperty(PropertyPanel owner, String name, int defaultvalue) {
		super(owner, name, defaultvalue);
	}

	@Override
	public void setType() {
		type_ = InternalPropertyType.INTEGER;
	}

}
