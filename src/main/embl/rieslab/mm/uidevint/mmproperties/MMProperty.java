package main.embl.rieslab.mm.uidevint.mmproperties;

import java.util.ArrayList;

import main.embl.rieslab.mm.uidevint.ui.uiproperties.UIProperty;
import mmcorej.CMMCore;

/**
 * Abstract wrapper for a Micro-manager device property. This class allows retrieving and modifying
 * the value of a device property, within its limits or allowed values as defined by Micro-manager. 
 * 
 * @author Joran Deschamps
 *
 * @param <T>
 */
public abstract class MMProperty<T> {

	private CMMCore core_;
	
	private String label_;
	private String devicelabel_;
	private String hash_;
	
	protected MMPropertyType type_;
	
	private boolean readOnly;							
	private boolean hasLimits;						
	private boolean hasAllowedValues;
	private double upperLimit, lowerLimit;
	private T[] allowedValues;
	private T value;
	private T maxValue, minValue;
	private ArrayList<UIProperty> listeners_;
	
	/**
	 * Builds a MMproperty without limits or allowed values. The property can be set to be read-only.
	 * 
	 * @param core Micro-manager core.
	 * @param deviceLabel Label of the parent device as defined in Micro-manager.
	 * @param propertyLabel Label of the device property as defined in Micro-manager.
	 * @param readOnly True if the device property is read-only, false otherwise.
	 */
	MMProperty(CMMCore core, String deviceLabel, String propertyLabel, boolean readOnly){
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

	/**
	 * Builds a MMProperty with limits. 
	 * 
	 * @param core Micro-manager core.
	 * @param deviceLabel Label of the parent device as defined in Micro-manager.
	 * @param propertyLabel Label of the device property as defined in Micro-manager.
	 * @param upperLimit Upper limit of the device property value.
	 * @param lowerLimit Lower limit of the device property value.
	 */
	MMProperty(CMMCore core, String deviceLabel, String propertyLabel, double upperLimit, double lowerLimit){
		this.core_ = core;
		this.devicelabel_ =  deviceLabel;
		this.label_ =  propertyLabel;
		
		this.readOnly = false;
		this.hasLimits = true;
		this.hasAllowedValues = false;
		
		this.upperLimit = upperLimit;
		this.lowerLimit = lowerLimit;
		this.maxValue = convertToValue(upperLimit);
		this.minValue = convertToValue(lowerLimit);

		hash_ = devicelabel_+"-"+label_;
		
		listeners_ = new ArrayList<UIProperty>();
				
		getValue();
	}
	
	/**
	 * Builds a MMProperty with allowed values.
	 * 
	 * @param core Micro-manager core.
	 * @param deviceLabel Label of the parent device as defined in Micro-manager.
	 * @param propertyLabel Label of the device property as defined in Micro-manager.
	 * @param allowedValues Array of allowed values.
	 */
	MMProperty(CMMCore core, String deviceLabel, String propertyLabel, String[] allowedValues){
		this.core_ = core;
		this.devicelabel_ =  deviceLabel;
		this.label_ =  propertyLabel;
		
		this.allowedValues = arrayFromStrings(allowedValues);

		this.readOnly = false;
		this.hasLimits = false;
		this.hasAllowedValues = true;

		hash_ = devicelabel_+"-"+label_;
		
		listeners_ = new ArrayList<UIProperty>();
				
		getValue();
	}

	/** 
	 * Returns the current value of the device property.
	 * 
	 * @return
	 */
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

	/**
	 * Returns the current value of the device property as a String.
	 * 
	 * @return
	 */
	public String getStringValue() {
		// ask core for value
		String val = "";
		try {
			val = core_.getProperty(devicelabel_, label_);			

			if(val != null && !val.isEmpty()){
				value = convertToValue(val);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return val;
	}

	/**
	 * Sets the value of the device property and updates all parent UIProperties. This method is called by a parent UIProperty, and the source
	 * is excluded from the notification.
	 * 
	 * @param stringval New value.
	 * @param source UIProperty at the origin of the update.
	 */
	public void setStringValue(String stringval, UIProperty source){
		if(!isReadOnly()){
			T val = convertToValue(stringval);
			if(isAllowed(val)){
				try{
					// set value
					value = val;
										
					core_.setProperty(devicelabel_,label_,stringval);
					if(!core_.hasProperty(devicelabel_, label_)){
						System.out.println("Device property ["+devicelabel_+","+label_+"] doesn't exist.");
					} 
					
					notifyListeners(source, stringval);
				} catch (Exception e){
					System.out.println("Error in setting property ["+getHash()+"] to ["+val+"] from ["+stringval+"]");

					e.printStackTrace(); 
				}
			} else {
				System.out.println("VALUE NOT ALLOWED: in ["+getHash()+"], set value ["+val+"] from ["+stringval+"]");
			}
		}
	}
	
	/**
	 * 
	 * @return True if the device property has limits, false otherwise.
	 */
	public boolean hasLimits(){
		return hasLimits;
	}
	
	/**
	 * 
	 * @return True if the device property has allowed values, false otherwise.
	 */
	public boolean hasAllowedValues(){
		return hasAllowedValues;
	}
	
	/**
	 * 
	 * @return True if the device property is read only, false otherwise.
	 */
	public boolean isReadOnly(){
		return readOnly;
	}
	
	/**
	 * Returns the type of the MMproperty, has defined in the MMPropertyType enum.
	 * 
	 * @return 
	 */
	public String getType(){
		return type_.getTypeValue();  
	}

	/**
	 * Returns the array of allowed values.
	 * 
	 * @return
	 */
	public T[] getAllowedValues(){
		if(hasAllowedValues()){
			return allowedValues;
		}
		return null;
	}
	
	/**
	 * Returns the array of allowed values as Strings.
	 * 
	 * @return
	 */
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
	
	/**
	 * Sets a maximum to the value of the device property. The maximum should be smaller than
	 * the upper limit. Maximum value is only used in device property that have limits.
	 * 
	 * @param val
	 */
	public void setMax(T val){
		if(hasLimits() && isInRange(val)){
			maxValue = val;
		}
	}
	
	/**
	 * Sets a minimum to the value of the device property. The minimum should be larger than
	 * the lower limit. Minimum value is only used in device property that have limits.
	 * 
	 * @param val
	 */
	public void setMin(T val){
		if(hasLimits() && isInRange(val)){
			minValue = val;
		}
	}

	/**
	 * Returns the maximum value as a String. If the device property doesn't have limits, then
	 * the method returns null.
	 * 
	 * @return Maximum value, or null if the device property doesn't have limits.
	 */
	public String getStringMax(){
		if(hasLimits()){
			return convertToString(maxValue);
		}
		return null;
	}
	
	/**
	 * Returns the mimimum value has a String. If the device property doesn't have limits, then
	 * the method returns null.
	 * 
	 * @return Minimum value, or null if the device property doesn't have limits.
	 */
	public String getStringMin(){
		if(hasLimits()){
			return convertToString(minValue);
		}
		return null;
	}
	
	/**
	 * Returns the maximum value. If the device property doesn't have limits, then
	 * the method returns null.
	 * 
	 * @return Maximum value, or null if the device property doesn't have limits.
	 */
	public T getMax(){
		if(hasLimits()){
			return maxValue;
		}
		return null;
	}
	
	/**
	 * Returns the minimum value. If the device property doesn't have limits, then
	 * the method returns null.
	 * 
	 * @return Minimum value, or null if the device property doesn't have limits.
	 */
	public T getMin(){
		if(hasLimits()){
			return minValue;
		}
		return null;
	}

	/**
	 * Returns the lower limit. If the device property doesn't have limits, then
	 * the method returns null.
	 * 
	 * @return Lower limit value, or null if the device property doesn't have limits.
	 */
	public Double getLowerLimit(){
		if(hasLimits()){
			return lowerLimit;
		}
		return null;
	}
	
	/**
	 * Returns the upper limit. If the device property doesn't have limits, then
	 * the method returns null.
	 * 
	 * @return Upper limit value, or null if the device property doesn't have limits.
	 */
	public Double getUpperLimit(){
		if(hasLimits()){
			return upperLimit;
		}
		return null;
	}
	
	/**
	 * Returns the parent device label.
	 * @return
	 */
	public String getDeviceLabel(){
		return devicelabel_;
	}

	/**
	 * Returns the device property label.
	 * @return
	 */
	public String getMMPropertyLabel(){
		return label_;
	}
	
	/**
	 * Returns the MMProperty hash, which is "{device label}-{property label}".
	 * @return
	 */
	public String getHash(){
		return hash_;
	}
	
	/**
	 * Tests if the value strval is allowed for the device property.
	 * 
	 * @param strval Value to be tested.
	 * @return True if strval is allowed, false otherwise.
	 */
	protected boolean isAllowed(String strval){
		T val = convertToValue(strval);
		return isAllowed(val);
	}

	/**
	 * Tests if the value strval is in range of the device property limits (if applicable).
	 * 
	 * @param strval
	 * @return True if in range, false otherwise.
	 */
	protected boolean isInRange(String strval){
		T val = convertToValue(strval);
		return isInRange(val);
	}
	
	/**
	 * Adds a UIProperty listener to the list of listeners. The UIProperty is then notified when
	 * the value of the MMProperty is changed.
	 * 
	 * @param listener Parent UIProperty
	 */
	public void addListener(UIProperty listener){
		listeners_.add(listener);
	}
	
	public void clearAllListeners(){
		listeners_.clear();
	}
	
	/**
	 * Notifies the UIProperty parents of a value update, excluding the UIProperty source of the 
	 * update.
	 * @param source UIProperty that triggered the update.
	 * @param value Updated value.
	 */
	private void notifyListeners(UIProperty source, String value){
		for(int i=0;i<listeners_.size();i++){
			if(listeners_.get(i) != source){
				listeners_.get(i).mmPropertyHasChanged(value);
			}
		}
	}
	
	/**
	 * Convert the String s to the MMProperty type.
	 * 
	 * @param s
	 * @return
	 */
	protected abstract T convertToValue(String s);
	/**
	 * Convert the int i to the MMProperty type.
	 * @param s
	 * @return
	 */
	protected abstract T convertToValue(int i);
	/**
	 * Convert the double d to the MMProperty type. 
	 * @param d
	 * @return
	 */
	protected abstract T convertToValue(double d);
	/**
	 * Converts an array of String to the MMProperty type.
	 * @param s
	 * @return
	 */
	protected abstract T[] arrayFromStrings(String[] s);
	
	/**
	 * Converts a value from the MMProperty type to String.
	 * @param val
	 * @return
	 */
	protected abstract String convertToString(T val);
	
	/**
	 * Tests if equal
	 * @param val1
	 * @param val2
	 * @return True if equal, false otherwise. 
	 */
	protected abstract boolean areEqual(T val1, T val2);
	/**
	 * Tests if val in the allowed range of the MMProperty, that is to say is smaller
	 * than the upper limit and larger than the lower limit. If minimum and maximum have 
	 * been set to values different than the upper and lower limit, then val is in range 
	 * if it is contained between the minimum and the maximum. If the property doesn't have
	 * limits, this method should return true.
	 * @param val
	 * @return 
	 */
	protected abstract boolean isInRange(T val);
	/**
	 * Tests if val is allowed. If the property has neither limits nor allowed values,
	 * then the method should return true. If the property has limits, then this method
	 * should be equivalent to isInRange().
	 * @param val
	 * @return
	 */
	protected abstract boolean isAllowed(T val);
	
	public boolean isStringAllowed(String val){
		return isAllowed(convertToValue(val));
	}
	
}
