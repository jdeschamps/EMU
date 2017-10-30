package main.embl.rieslab.htSMLM.ui.uiproperties;

import main.embl.rieslab.htSMLM.ui.PropertyPanel;

public class MultipleValuesUIProperty extends UIProperty{

	public static String VALUE = "Values";
	
	private String[] value_;
	
	public MultipleValuesUIProperty(PropertyPanel owner, String name, String description, int size) {
		super(owner, name, description);
		
		value_ = new String[size];
		for(int i=0;i<size;i++){
			value_[i] = "";
		}
	}

	
	public void setConstantValues(String[] vals){
		if(vals.length == value_.length){
			value_ = vals;
		} else if (vals.length > value_.length){
			for(int i=0; i<value_.length;i++){
				value_[i] = vals[i];
			}
		} else {
			for(int i=0; i<vals.length;i++){
				value_[i] = vals[i];
			}
		}
	}
	
	@Override
	public void setPropertyValue(String val) {
		if (isInitialised()) {
			for(int i=0;i<value_.length;i++){
				if(value_[i].equals(val)){
					getMMPoperty().setStringValue(val);
				}
			}
		}
	}
	
	@Override
	public boolean isMultipleValues(){
		return true;
	}
	
	public static String getValueName(){
		return " "+VALUE;
	}
}

