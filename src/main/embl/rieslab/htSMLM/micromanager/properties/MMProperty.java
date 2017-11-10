package main.embl.rieslab.htSMLM.micromanager.properties;

import java.util.ArrayList;

import main.embl.rieslab.htSMLM.ui.uiproperties.UIProperty;
import mmcorej.CMMCore;

public abstract class MMProperty<T> {

	private CMMCore core_;
	
	private String label_;
	private String devicelabel_;
	private String hash_;
	
	protected MMPropertyType type_;
	
	private boolean readOnly;							
	private boolean hasLimits;						
	private boolean hasAllowedValues;
	protected double upLimit, downLimit;
	protected T[] allowedValues;
	protected T value;
	protected T maxValue, minValue;
	private ArrayList<UIProperty> listeners_;
	
	public MMProperty(CMMCore core, String deviceLabel, String propertyLabel, boolean readOnly){
		this.core_ = core;
		this.devicelabel_ =  deviceLabel;
		this.label_ =  propertyLabel;
		
		this.readOnly = readOnly;
		this.hasLimits = false;
		this.hasAllowedValues = false;

		hash_ = devicelabel_+"-"+label_;
		
		listeners_ = new ArrayList<UIProperty>();
		
		getValue();
	}


	public MMProperty(CMMCore core, String deviceLabel, String propertyLabel, double upLimit, double downLimit){
		this.core_ = core;
		this.devicelabel_ =  deviceLabel;
		this.label_ =  propertyLabel;
		
		this.readOnly = false;
		this.hasLimits = true;
		this.hasAllowedValues = false;
		
		this.upLimit = upLimit;
		this.downLimit = downLimit;
		this.maxValue = convertToValue(upLimit);
		this.minValue = convertToValue(downLimit);

		hash_ = devicelabel_+"-"+label_;
		
		listeners_ = new ArrayList<UIProperty>();
				
		getValue();
	}
	
	public MMProperty(CMMCore core, String deviceLabel, String propertyLabel, String[] allowedValues){
		this.core_ = core;
		this.devicelabel_ =  deviceLabel;
		this.label_ =  propertyLabel;
		
		this.allowedValues = allowedValuesFromString(allowedValues);

		this.readOnly = false;
		this.hasLimits = false;
		this.hasAllowedValues = true;

		hash_ = devicelabel_+"-"+label_;
		
		listeners_ = new ArrayList<UIProperty>();
				
		getValue();
	}

	public T getValue() {
		// ask core for value
		T val;
		try {
			val = convertToValue(core_.getProperty(devicelabel_, label_));
			value = val;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	public String getStringValue() {
		// ask core for value
		String val = "";
		try {
			val = core_.getProperty(devicelabel_, label_);
			value = convertToValue(val);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return val;
	}

	public void setStringValue(String stringval, UIProperty source){
		if(!isReadOnly()){
			T val = convertToValue(stringval);
			if(isAllowed(val)){
				try{
					// set value
					value = val;
					core_.setProperty(devicelabel_,label_,stringval);
					notifyListeners(source, stringval);
				} catch (Exception e){
					e.printStackTrace(); 
				}
			}
		}
	}
	
	public boolean hasLimits(){
		return hasLimits;
	}
	
	public boolean hasAllowedValues(){
		return hasAllowedValues;
	}
	
	public boolean isReadOnly(){
		return readOnly;
	}
	
	public String getType(){
		return type_.getTypeValue();  
	}

	public T[] getAllowedValues(){
		if(hasAllowedValues){
			return allowedValues;
		}
		return null;
	}
	
	public String[] getStringAllowedValues(){
		if(hasAllowedValues()){
			String[] s = new String[allowedValues.length];
			for(int i=0;i<allowedValues.length;i++){
				s[i]=convertToString(allowedValues[i]);
			}
			return s;
		}
		return null;
	}
	
	public void setMax(T val){
		if(hasLimits()){
			maxValue = val;
		}
	}
	
	public void setMin(T val){
		if(hasLimits()){
			minValue = val;
		}
	}
	
	public String getMax(){
		return convertToString(maxValue);
	}
	
	public String getMin(){
		return convertToString(minValue);
	}

	public String getDeviceName(){
		return devicelabel_;
	}

	public String getPropertyName(){
		return label_;
	}
	
	public String getHash(){
		return hash_;
	}


	protected boolean isAllowed(String strval){
		T val = convertToValue(strval);
		return isAllowed(val);
	}

	protected boolean isInRange(String strval){
		T val = convertToValue(strval);
		return isInRange(val);
	}
	
	public void addListener(UIProperty listener){
		listeners_.add(listener);
	}
	
	private void notifyListeners(UIProperty source, String value){
		for(int i=0;i<listeners_.size();i++){
			if(listeners_.get(i) != source){
				listeners_.get(i).mmPropertyHasChanged(value);
			}
		}
	}
	
	protected abstract T convertToValue(String s);
	protected abstract T convertToValue(int s);
	protected abstract T convertToValue(double s);
	protected abstract T[] allowedValuesFromString(String[] s);
	protected abstract String convertToString(T val);
	protected abstract boolean areEqual(T val1, T val2);
	protected abstract boolean isInRange(T val);
	protected abstract boolean isAllowed(T val);
	
}
