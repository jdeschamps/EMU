package main.java.embl.rieslab.emu.ui;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import main.java.embl.rieslab.emu.ui.internalproperties.BoolInternalProperty;
import main.java.embl.rieslab.emu.ui.internalproperties.DoubleInternalProperty;
import main.java.embl.rieslab.emu.ui.internalproperties.IntegerInternalProperty;
import main.java.embl.rieslab.emu.ui.internalproperties.InternalProperty;
import main.java.embl.rieslab.emu.ui.internalproperties.InternalProperty.InternalPropertyType;
import main.java.embl.rieslab.emu.ui.uiparameters.BoolUIParameter;
import main.java.embl.rieslab.emu.ui.uiparameters.ColorUIParameter;
import main.java.embl.rieslab.emu.ui.uiparameters.ComboUIParameter;
import main.java.embl.rieslab.emu.ui.uiparameters.DoubleUIParameter;
import main.java.embl.rieslab.emu.ui.uiparameters.IntegerUIParameter;
import main.java.embl.rieslab.emu.ui.uiparameters.StringUIParameter;
import main.java.embl.rieslab.emu.ui.uiparameters.UIParameter;
import main.java.embl.rieslab.emu.ui.uiparameters.UIParameter.UIParameterType;
import main.java.embl.rieslab.emu.ui.uiparameters.UIPropertyParameter;
import main.java.embl.rieslab.emu.ui.uiproperties.UIProperty;

/**
 * Building block of EMU, this abstract class extends a JPanel. It holds a map of {@link main.java.embl.rieslab.emu.ui.uiproperties.UIProperty},
 * {@link main.java.embl.rieslab.emu.ui.uiparameters.UIParameter} and {@link main.java.embl.rieslab.emu.ui.internalproperties.InternalProperty}.
 * ConfigurablePanel subclasses should be instantiated within a {@link ConfigurableMainFrame}. The latter will collect their UIProperties, 
 * UIParameters and InternalProperties, and pass them on to the SystemController. 
 * <p> 
 * Subclasses of ConfigurablePanel must implements few methods called in the ConfigurablePanel constructor in order to instantiate the
 * UIProperties ({@link #initializeProperties()}), UIParameters ({@link #initializeParameters()}) and InternalProperties ({@link #initializeInternalProperties()}), 
 * as well as setting up the JComponents in the JPanel (itself). All JComponent instantiations should happen in the constructor. 
 * <p> 
 * UIProperties are aimed at linking the state of a MMProperty with the state of one or multiple JComponenents. InternalProperties are made to allow
 * shared values between ConfigurablePanels, such that a modification to one panel can trigger a change in the other panel. Finally, UIProperties
 * should only be used for user settings, such as changing the colors of JLabels or JButtons (to describe a filter or a laser) or the text of a header. 
 * All UIProperties and UIParameters appear in the {@link main.java.embl.rieslab.emu.configuration.ConfigurationWizard}. The user can then map the 
 * UIProperties with a MMProperty and change the value of a UIParameter. 
 * <p>
 * In order to be added to the internal HashMap representation, UIproperties, UIParameters and InternalProperties need to be added using the following methods
 * respectively: {@link #addUIProperty(UIProperty)}, {@link #addUIParameter(UIParameter)} and {@link #addInternalProperty(InternalProperty)}. 
 * <p>
 * Modifications to the state of UIProperties and InternalProperties should not be done explicitly in the subclasses, but should be done through the 
 * abstraction methods: {@link #setUIPropertyValue(String, String)} and setInternalPropertyValue(String, ?). UIParameters should not be modified within the subclasses. 
 * Modifications of the JComponents based on UIProperties, UIParameters and InternalProperties changes take place in the subclasses 
 * implementation of {@link #propertyhasChanged(String, String)}, {@link #parameterhasChanged(String)} and {@link #internalpropertyhasChanged(String)} respectively.
 * To query the value of a UIParameter or an InternalProperty, use respectively the methods {@code get{type of the UIParameter}UIParamterValue()} and
 * {@code get{type of the InternalProperty}InternalPropertyValue()}.
 *  <p> 
 * For instance, a JToggleButton can be designed to turn on and off a laser. After declaration of the JToggleButton and addition to the panel in the constructor, 
 * an eventListener can be added to the JToggleButton. The eventListener should then call {@link #setUIPropertyValue(String, String)} to modify the corresponding
 * UIProperty with a new value being on when the JToggleButton is selected, and an off value when the JToggleButton is unselected. More details can be found in
 * tutorials and the javadocs of the different UIProperties implementations.
 * <p> 
 * Upon start up of the {@link main.java.embl.rieslab.emu.plugin.UIPlugin}, the {@link main.java.embl.rieslab.emu.controller.SystemController} will
 * pair up the UIProperties with {@link main.java.embl.rieslab.emu.micromanager.mmproperties.MMProperty} and the latter's values will be propagated 
 * to the ConfigurablePanel via {@link #propertyhasChanged(String, String)}. Later on, changes to a MMProperty (for instance by another ConfigurablePanel)
 * will trigger the same method. The same mechanism is at play for the InternalProperties and the UIParameters. Note that the UIParameters are only changed
 * upon start up and when the user modifies the configuration through the {@link main.java.embl.rieslab.emu.configuration.ConfigurationWizard}. 
 * <p> 
 * In addition, to avoid triggering {@link #setUIPropertyValue(String, String)} through the eventListener when modifying the state of a JComponent (as it might happen
 * depending on the eventListener type), {@link #turnOffComponentTriggering()} can be called in the beginning of the subclasses implementation of 
 * {@link #propertyhasChanged(String, String)} and {@link #turnOnComponentTriggering()} at the end. These methods change the state of an internal member
 * boolean variable. In the eventListener, the method {@link #isComponentTriggeringEnabled()} can query the state of the boolean variable and decide to not
 * call {@link #setUIPropertyValue(String, String)}. This mechanism is optional.
 * 
 * @see main.java.embl.rieslab.emu.ui.uiproperties.UIProperty
 * @see main.java.embl.rieslab.emu.ui.uiparameters.UIParameter
 * @see main.java.embl.rieslab.emu.ui.internalproperties.InternalProperty
 * @see ConfigurableMainFrame
 * 
 * @author Joran Deschamps
 *
 */
@SuppressWarnings("rawtypes")
public abstract class ConfigurablePanel extends JPanel{

	private static final long serialVersionUID = 7664471329228929184L;

	private HashMap<String, UIProperty> properties_; 
	private HashMap<String, UIParameter> parameters_;
	private HashMap<String, InternalProperty> internalprops_;

	private String label_;
	
	private boolean componentTriggering_ = true;
	
	/**
	 * Constructor, calls the abstract methods {@link #initializeProperties()} and {@link #initializeParameters()}, 
	 * {@link #initializeInternalProperties()}.
	 * 
	 * @param label Label of the panel.
	 */
	public ConfigurablePanel(String label){
		label_ = label;
		
		properties_ = new HashMap<String,UIProperty>();
		parameters_ = new HashMap<String,UIParameter>();
		internalprops_ = new HashMap<String, InternalProperty>();
		
		initializeProperties();
		initializeParameters();
		initializeInternalProperties();
	}

	/**
	 * Returns a hash map of the panel's UI properties indexed by their name.
	 *
	 * @see main.java.embl.rieslab.emu.ui.uiproperties.UIProperty
	 * 
	 * @return HashMap with the UIProperty indexed by their name. 
	 */
	protected HashMap<String, UIProperty> getUIProperties(){
		return properties_;
	}
	
	/**
	 * Returns a hash map of the panel's internal properties indexed by their names.
	 *
	 * @see main.java.embl.rieslab.emu.ui.internalproperties.InternalProperty
	 * 
	 * @return HashMap with the InternalProperty indexed by their names. 
	 */
	protected HashMap<String, InternalProperty> getInternalProperties(){
		return internalprops_;
	}
	
	/**
	 * Returns a hash map of the panel's UI parameters indexed by their hash (({panel's name}-{parameter's name})).
	 *
	 * @see main.java.embl.rieslab.emu.ui.uiparameters.UIParameter
	 * 
	 * @return HashMap with the UIParameter indexed by their hash. 
	 */
	protected HashMap<String,UIParameter> getUIParameters(){
		return parameters_;
	}	
		
	/**
	 * Returns the {@link main.java.embl.rieslab.emu.ui.uiproperties.UIProperty} named {@code propertyName}.
	 * 
	 * @param propertyName Name of the UIProperty
	 * @return Corresponding UIProperty, null if it doesn't exist.
	 */
	// maybe this should not be exposed to avoid modifying the property value on the EDT
	protected UIProperty getUIProperty(String propertyName){
		if(properties_.containsKey(propertyName)){
			return properties_.get(propertyName);
		}
		return null;
	}
	
	/**
	 * Sets the UIProperty {@code propertyName} friendly name to {@code friendlyName}.
	 * 
	 * @param propertyName Property name
	 * @param friendlyName New friendly name
	 */
	protected void setUIPropertyFriendlyName(String propertyName, String friendlyName){
		if(properties_.containsKey(propertyName)){
			properties_.get(propertyName).setFriendlyName(friendlyName);
		}
	}
	
	/**
	 * Returns the current value of the UIProperty called {@code propertyName}.
	 * 
	 * @param propertyName Name of the property
	 * @return String value of the property, null if the property doesn't exist.
	 */
	protected String getUIPropertyValue(String propertyName){
		if(properties_.containsKey(propertyName)){
			return properties_.get(propertyName).getPropertyValue();
		}
		return null;
	}
	
	/**
	 * Sets the UIProperty {@code propertyName}'s value to {@code newValue}. This method calls the 
	 * UIProperty's method to set the value, which will in turn call the corresponding MMProperty's
	 * method. Since the change will be notified to all the UIProperties listening to the MMProperty 
	 * (through {@link #triggerPropertyHasChanged(String, String)}), this method runs on an independent
	 * thread (that is, not on the EDT).
	 *  
	 * @param propertyName UIProperty's name
	 * @param newValue New value
	 */
	protected void setUIPropertyValue(String propertyName, String newValue){
		// makes sure the call does NOT run on EDT
		Thread t = new Thread("Property change: " + propertyName) {
			public void run() {
				if (properties_.containsKey(propertyName)) {
					properties_.get(propertyName).setPropertyValue(newValue);
				}
			}
		};
		t.start();
	}
	
	/**
	 * Sets the value of the IntegerInternalProperty called {@code propertyName} to {@code newValue}.
	 * If {@code propertyName} doesn't exist or is not an IntegerInternalProperty, nothing happens.
	 *  
	 * @param propertyName Name of the InternalProperty
	 * @param newValue New value
	 */
	protected void setInternalPropertyValue(String propertyName, int newValue){
		// runs on EDT, is this a problem?
		if (internalprops_.containsKey(propertyName)
				&& internalprops_.get(propertyName).getType().equals(InternalPropertyType.INTEGER)) {
			((IntegerInternalProperty) internalprops_.get(propertyName)).setInternalPropertyValue(newValue, this);
		}
	}
	
	/**
	 * Sets the value of the BoolInternalProperty called {@code propertyName} to {@code newValue}.
	 * If {@code propertyName} doesn't exist or is not an BoolInternalProperty, nothing happens.
	 *  
	 * @param propertyName Name of the InternalProperty
	 * @param newValue New value
	 */
	protected void setInternalPropertyValue(String propertyName, boolean newValue){
		// runs on EDT, is this a problem?
		if (internalprops_.containsKey(propertyName)
				&& internalprops_.get(propertyName).getType().equals(InternalPropertyType.BOOLEAN)) {
			((BoolInternalProperty) internalprops_.get(propertyName)).setInternalPropertyValue(newValue, this);
		}
	}
	
	/**
	 * Sets the value of the DoubleInternalProperty called {@code propertyName} to {@code newValue}.
	 * If {@code propertyName} doesn't exist or is not an DoubleInternalProperty, nothing happens.
	 *  
	 * @param propertyName name of the InternalProperty
	 * @param newValue New value
	 */
	protected void setInternalPropertyValue(String propertyName, double newValue){
		// runs on EDT, is this a problem?
		if (internalprops_.containsKey(propertyName)
				&& internalprops_.get(propertyName).getType().equals(InternalPropertyType.DOUBLE)) {
			((DoubleInternalProperty) internalprops_.get(propertyName)).setInternalPropertyValue(newValue, this);
		}
	}

	/**
	 * Returns the value of the IntegerInternalProperty called {@code propertyName}.
	 * If {@code propertyName} doesn't exist or is not an IntegerInternalProperty, returns 0.
	 * 
	 * @param propertyName Name of the property
	 * @return Value of the InternalProperty, or 0 if it doesn't exists.
	 */
	protected int getIntegerInternalPropertyValue(String propertyName) {
		if(internalprops_.containsKey(propertyName)
				&& internalprops_.get(propertyName).getType().equals(InternalPropertyType.INTEGER)) {
			return ((IntegerInternalProperty) internalprops_.get(propertyName)).getInternalPropertyValue();
		}
		return 0;
	}
	
	/**
	 * Returns the value of the BoolInternalProperty called {@code propertyName}.
	 * If {@code propertyName} doesn't exist or is not an BoolInternalProperty, returns false.
	 * 
	 * @param propertyName Name of the property
	 * @return Value of the InternalProperty, or false if it doesn't exists.
	 */
	protected boolean getBoolInternalPropertyValue(String propertyName) {
		if(internalprops_.containsKey(propertyName)
				&& internalprops_.get(propertyName).getType().equals(InternalPropertyType.BOOLEAN)) {
			return ((BoolInternalProperty) internalprops_.get(propertyName)).getInternalPropertyValue();
		}
		return false;
	}
	
	/**
	 * Returns the value of the DoubleInternalProperty called {@code propertyName}.
	 * If {@code propertyName} doesn't exist or is not an DoubleInternalProperty, returns 0.
	 * 
	 * @param propertyName Name of the property
	 * @return Value of the InternalProperty, or 0 if it doesn't exists.
	 */
	protected double getDoubleInternalPropertyValue(String propertyName) {
		if(internalprops_.containsKey(propertyName)
				&& internalprops_.get(propertyName).getType().equals(InternalPropertyType.DOUBLE)) {
			return ((DoubleInternalProperty) internalprops_.get(propertyName)).getInternalPropertyValue();
		}
		return 0.;
	}
	
	/**
	 * Returns the value of the DoubleUIParameter called {@code parameterName}. If no such UIParameter exists
	 * then returns 0.
	 * 
	 * @param parameterName Name of the parameter
	 * @return Value of the UIParameter, or 0 if it doesn't exist.
	 */
	protected double getDoubleUIParameterValue(String parameterName) {
		if(parameters_.containsKey(getLabel()+" - "+parameterName)
				&& parameters_.get(getLabel()+" - "+parameterName).getType().equals(UIParameterType.DOUBLE)) {
			return ((DoubleUIParameter) parameters_.get(getLabel()+" - "+parameterName)).getValue();
		}
		return 0.;
	}

	
	/**
	 * Returns the value of the BoolUIParameter called {@code parameterName}. If no such UIParameter exists
	 * then returns false.
	 * 
	 * @param parameterName Name of the parameter
	 * @return Value of the UIParameter, or false if it doesn't exist.
	 */
	protected boolean getBoolUIParameterValue(String parameterName) {
		if(parameters_.containsKey(getLabel()+" - "+parameterName)
				&& parameters_.get(getLabel()+" - "+parameterName).getType().equals(UIParameterType.BOOL)) {
			return ((BoolUIParameter) parameters_.get(getLabel()+" - "+parameterName)).getValue();
		}
		return false;
	}

	/**
	 * Returns the value of the ColorUIParameter called {@code parameterName}. If no such UIParameter exists
	 * then returns black.
	 * 
	 * @param parameterName Name of the parameter
	 * @return Value of the UIParameter, or black if it doesn't exist.
	 */
	protected Color getColorUIParameterValue(String parameterName) {
		if(parameters_.containsKey(getLabel()+" - "+parameterName)
				&& parameters_.get(getLabel()+" - "+parameterName).getType().equals(UIParameterType.COLOR)) {
			return ((ColorUIParameter) parameters_.get(getLabel()+" - "+parameterName)).getValue();
		}
		return Color.black;
	}

	/**
	 * Returns the value of the ComboUIParameter called {@code parameterName}. If no such UIParameter exists
	 * then returns an empty String.
	 * 
	 * @param parameterName Name of the parameter
	 * @return Value of the UIParameter, or an empty String if it doesn't exist.
	 */
	protected String getComboUIParameterValue(String parameterName) {
		if(parameters_.containsKey(getLabel()+" - "+parameterName)
				&& parameters_.get(getLabel()+" - "+parameterName).getType().equals(UIParameterType.COMBO)) {
			return ((ComboUIParameter) parameters_.get(getLabel()+" - "+parameterName)).getValue();
		}
		return "";
	}

	/**
	 * Returns the value of the IntegerUIParameter called {@code parameterName}. If no such UIParameter exists
	 * then returns 0.
	 * 
	 * @param parameterName Name of the parameter
	 * @return Value of the UIParameter, or 0 if it doesn't exist.
	 */
	protected int getIntegerUIParameterValue(String parameterName) {		
		if(parameters_.containsKey(getLabel()+" - "+parameterName)
				&& parameters_.get(getLabel()+" - "+parameterName).getType().equals(UIParameterType.INTEGER)) {
			return ((IntegerUIParameter) parameters_.get(getLabel()+" - "+parameterName)).getValue();
		}
		return 0;
	}

	/**
	 * Returns the value of the StringUIParameter called {@code parameterName}. If no such UIParameter exists
	 * then returns an empty String.
	 * 
	 * @param parameterName Name of the parameter
	 * @return Value of the UIParameter, or an empty String if it doesn't exist.
	 */
	protected String getStringUIParameterValue(String parameterName) {
		if(parameters_.containsKey(getLabel()+" - "+parameterName)
				&& parameters_.get(getLabel()+" - "+parameterName).getType().equals(UIParameterType.STRING)) {
			return ((StringUIParameter) parameters_.get(getLabel()+" - "+parameterName)).getValue();
		}
		return "";
	}

	/**
	 * Returns the value of the UIPropertyParameter called {@code parameterName}. If no such UIParameter exists
	 * then returns 0.
	 * 
	 * @param parameterName Name of the parameter
	 * @return Value of the UIParameter, or 0 if it doesn't exist.
	 */
	protected String getUIPropertyParameterValue(String parameterName) {
		if(parameters_.containsKey(getLabel()+" - "+parameterName)
				&& parameters_.get(getLabel()+" - "+parameterName).getType().equals(UIParameterType.UIPROPERTY)) {
			return ((UIPropertyParameter) parameters_.get(getLabel()+" - "+parameterName)).getValue();
		}
		return UIPropertyParameter.NO_PROPERTY;
	}
	

	/**
	 * Adds a {@link main.java.embl.rieslab.emu.ui.uiproperties.UIProperty} to the internal HashMap
	 * using the UI property's name.
	 * 
	 * @param uiproperty UIProperty to add
	 */
	protected void addUIProperty(UIProperty uiproperty){
		properties_.put(uiproperty.getLabel(),uiproperty);
	}	

	/**
	 * Adds a {@link main.java.embl.rieslab.emu.ui.uiparameters.UIParameter} to the internal HashMap
	 * using the UIParameter's hash.
	 * 
	 * @param uiparameter UIParameter to add
	 */
	protected void addUIParameter(UIParameter uiparameter){
		parameters_.put(uiparameter.getHash(),uiparameter);
	}
	
	/**
	 * Adds a {@link main.java.embl.rieslab.emu.ui.internalproperties.InternalProperty} to the internal HashMap
	 * using the InternalProperty name.
	 * 
	 * @param internalproperty InternalProperty to add
	 */
	protected void addInternalProperty(InternalProperty internalproperty){
		internalprops_.put(internalproperty.getLabel(),internalproperty);
	}
	
	/**
	 * Updates the ConfigurablePanel for all UI properties by calling {@link #triggerPropertyHasChanged(String, String)} 
	 * for each UIProperty.
	 */
	protected void updateAllProperties(){
		Iterator<String> it = properties_.keySet().iterator();
		String prop;
		while(it.hasNext()){
			prop = it.next();
			triggerPropertyHasChanged(prop,properties_.get(prop).getPropertyValue());
		}
	}	
	
	/**
	 * Updates the ConfigurablePanel for all UI parameters by calling {@link #triggerParameterHasChanged(String)} 
	 * for each UIParameter.
	 */
	protected void updateAllParameters(){
		Iterator<String> it = parameters_.keySet().iterator();
		while(it.hasNext()){
			triggerParameterHasChanged(parameters_.get(it.next()).getLabel());
		}
	}
	
	/**
	 * Returns this ConfigurablePanel's label.
	 * 
	 * @return This panel's label.
	 */
	public String getLabel(){
		return label_;
	}

	/**
	 * Substitute the parameter indexed by {@code paramHash} with {@code uiParameter}. This is used to resolve collisions
	 * between two parameters with same hash: their respective ConfigurablePanel owners then share the same UIParameter.
	 * 
	 * @param paramHash
	 * @param uiParameter
	 */
	public void substituteParameter(String paramHash, UIParameter uiParameter) {
		parameters_.put(paramHash, uiParameter);
	}
	
	/**
	 * Substitutes the InternalProperty with same name and type than {@code internalProperty} with it. If no such 
	 * InternalProperty exists, then this method does nothing.  
	 * 
	 * @param internalProperty InternalProperty to substitute with an existing one.
	 */
	public void substituteInternalProperty(InternalProperty internalProperty) {
		if(internalprops_.containsKey(internalProperty.getLabel())) {
			if(getInternalPropertyType(internalProperty.getLabel()).compareTo(internalProperty.getType()) == 0) {
				internalprops_.put(internalProperty.getLabel(), internalProperty);
			}
		}
	}
	
	/**
	 * Returns the InternalPropertyType of the InternalProperty called {@code propertyName}. If there is no such
	 * InternalProperty, returns InternalPropertyType.NONE.
	 * 
	 * @param propertyName Name of the InternalProperty
	 * @return The corresponding InternalPropertyType, InternalPropertyType.NONE if there is no such InternalProperty.
	 */
	public InternalPropertyType getInternalPropertyType(String propertyName) {
		if(internalprops_.containsKey(propertyName)) {
			return internalprops_.get(propertyName).getType();
		}
		return InternalPropertyType.NONE;
	}
	
	/**
	 * Checks if the component triggering is enabled. The change in permission is done through {@link #turnOffComponentTriggering()}
	 * and {@link #turnOnComponentTriggering()}. This is just indicative and only provides a mechanism to avoid triggering 
	 * the components eventListeners upon calling {@link #propertyhasChanged(String, String)}. For this mechanism to work, 
	 * {@link #setUIPropertyValue(String, String)} needs to be called in the eventListeners ONLY if this method returns true. For instance,
	 * this can be useful upon loading the UI, as {@link #propertyhasChanged(String, String)} will be called for each UIProperty,
	 * then in turn {@link #propertyhasChanged(String, String)} will be called and might change the state of a JComponent. Depending
	 * on the type of eventListeners, this might trigger {@link #setUIPropertyValue(String, String)}.
	 * 
	 * @return true if the component triggering is on, false if it is off.
	 */
	protected boolean isComponentTriggeringEnabled(){
		return componentTriggering_;
	}

	/**
	 * Turns off component triggering. See {@link #isComponentTriggeringEnabled()}.
	 */
	protected void turnOffComponentTriggering(){
		componentTriggering_ = false;
	}
	
	/**
	 * Turns on component triggering. See {@link #isComponentTriggeringEnabled()}.
	 */
	protected void turnOnComponentTriggering(){
		componentTriggering_ = true;
	}

	/**
	 * Calls {@link #propertyhasChanged(String, String)} on the EDT. This allows the extension classes of
	 * ConfigurablePanel to change the state of a JComponent depending on the value {@code newValue} of 
	 * the UIProperty {@code propertyName} (itself linked to a MMProperty). Since a UIProperty does not hold the value of the corresponding
	 * MMProperty, it is passed as a parameter.
	 * 
	 * @param propertyName Name of the property
	 * @param newValue New value of the property
	 */
	public void triggerPropertyHasChanged(final String propertyName, final String newValue){
		// Makes sure that the updating runs on EDT
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				propertyhasChanged(propertyName, newValue);
			}
		});
	}
	
	/**
	 * Calls {@link #parameterhasChanged(String)} on the EDT. This allows the subclasses of
	 * ConfigurablePanel to adjust its components based on the value of the UIParameter {@code parameterName}.
	 * 
	 * @param parameterName Name of the parameter
	 */
	public void triggerParameterHasChanged(final String parameterName){
		// Makes sure that the updating runs on EDT
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				parameterhasChanged(parameterName);
			}
		});
	}

	/**
	 * Method called in the constructor of a ConfigurablePanel. In this method, the subclasses must create
	 * their UIproperty and add them to the map of properties using {@link #addUIProperty(UIProperty)}.
	 */
	protected abstract void initializeProperties();
	
	/**
	 * Method called in the constructor of a ConfigurablePanel. In this method, the subclasses must create
	 * their InternalProperties and add them to the map of internal properties using {@link #addInternalProperty(InternalProperty)}.
	 */
	protected abstract void initializeInternalProperties();
	
	/**
	 * Method called in the constructor of a ConfigurablePanel. In this method, the subclasses must create
	 * their UIparameter and add them to the map of properties using {@link #addUIParameter(UIParameter)}.
	 */
	protected abstract void initializeParameters();
	
	/**
	 * Method called when an internal property's value has been changed. This allows the ConfigurablePanel
	 * subclasses to react to the change of vale.
	 * 
	 * @param propertyName Name of the internal property
	 */
	public abstract void internalpropertyhasChanged(String propertyName);
	
	/**
	 * Notifies the ConfigurablePanel subclass that the MMProperty linked to its UIProperty {@code propertyName}
	 * has changed in value. This function is called on the EDT and allows the subclass to change the states of
	 * its JComponents based on the new value of the UIProperty.
	 * 
	 * @param propertyName Name of the Property whose value has changed.
	 * @param newvalue New value of the UIProperty
	 */
	protected abstract void propertyhasChanged(String propertyName, String newvalue);
	
	/**
	 * Notifies the ConfigurablePanel subclass that the UIParameter {@code parameterName} value has changed.
	 * This method is called upon loading of the UI and whenever the user changes the configuration.
	 * 
	 * @param parameterName Name of the UIParameter
	 */
	protected abstract void parameterhasChanged(String parameterName);
	
	/**
	 * Allows the ConfigurablePanel subclass to shut down all processes, e.g. SwingWorkers. This method is called 
	 * when EMU is closing.
	 */
	public abstract void shutDown();
	
	/**
	 * Returns the description of the ConfigurablePanel. 
	 * 
	 * @return Description of the ConfigurablePanel.
	 */
	public abstract String getDescription();

}
