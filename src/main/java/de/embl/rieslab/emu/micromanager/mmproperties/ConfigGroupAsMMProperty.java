package de.embl.rieslab.emu.micromanager.mmproperties;

import java.util.ArrayList;

import org.micromanager.Application;

import de.embl.rieslab.emu.ui.uiproperties.UIProperty;
import mmcorej.CMMCore;

/**
 * Instantiates a MMProperty that wraps a configuration group from Mico-manager. This is useful to 
 * map a UIProperty to multiple MMProperties as defined in Micro-Manager by the user (e.g. channels).
 * All configuration groups are assigned to the same fictitious device. The groups are then considered 
 * as a property with allowed values being the channels.
 * 
 * @author Joran Deschamps
 *
 */
public class ConfigGroupAsMMProperty extends MMProperty<String> {

	/**
	 * Fictitious device label all configuration groups are assigned to.
	 */
	public final static String KEY_MMCONFDEVICE = "Configurations";
	
	@SuppressWarnings("rawtypes")
	private ArrayList<MMProperty> affectedmmprops_;
	private Application app_;
	
	/**
	 * Constructor. Instantiate the MMProperty has a property belonging to the device {@code KEY_MMCONFDEVICE}, with 
	 * allowed values being the different channel names.
	 * 
	 * @param app Micro-Manager application instance
	 * @param core Micro-Manager CMMCore
	 * @param groupName Name of the configuration group
	 * @param groupChannelNames Array of the channel names
	 * @param affectedMMProps MMproperties affected by the group
	 */
	@SuppressWarnings("rawtypes")
	public ConfigGroupAsMMProperty(Application app, CMMCore core, String groupName, String[] groupChannelNames, ArrayList<MMProperty> affectedMMProps) {
		super(core, MMProperty.TYPE_CONFIG, KEY_MMCONFDEVICE, groupName, groupChannelNames);
		app_ = app;
		affectedmmprops_ = affectedMMProps;
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public String getValue() {
		// ask core for value
		String val;
		try {
			val = convertToValue(getCore().getCurrentConfig(getMMPropertyLabel()));
			value = val;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String getStringValue() {
		return getValue();
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void setValue(String stringval, UIProperty source){
		if(!isReadOnly()){
			if(isAllowed(stringval)){
				try{
					// set value
					value = stringval;

					getCore().setConfig(getMMPropertyLabel(), stringval);
					notifyListeners(source, stringval);
					notifyMMProperties();
					
					app_.refreshGUI();
					
				} catch (Exception e){
					System.out.println("Error in setting configuration ["+getHash()+"] to ["+stringval+"] from ["+value+"]");

					e.printStackTrace(); 
				}
			} else {
				System.out.println("VALUE NOT ALLOWED: in ["+getHash()+"], set value ["+stringval+"] from ["+value+"]");
			}
		}
	}

	/**
	 * Notify all the affected properties of a value change, should trickle down to all the listeners UI properties.
	 */
	private void notifyMMProperties() {
		for(int i=0; i<affectedmmprops_.size();i++){
			// notify its listeners
			affectedmmprops_.get(i).updateMMProperty();
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String convertToValue(String s) {
		return s;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String convertToValue(int s) {
		return String.valueOf(s);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String convertToValue(double s) {
		return String.valueOf(s);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String[] arrayFromStrings(String[] s) {
		return s;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String convertToString(String val) {
		return val.toString();
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public boolean isAllowed(String val) { // always has allowed values, so no "hasAllowedValues" to prevent null object
		for(int i=0;i<getAllowedValues().length;i++){
			if(areEquals(val, getAllowedValues()[i])){
				return true;
			}
		}
		return false;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public boolean areEquals(String val1, String val2) {
		return val1.equals(val2);
	}

}
