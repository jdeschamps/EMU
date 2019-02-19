package main.java.embl.rieslab.emu.ui;

import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import main.java.embl.rieslab.emu.ui.internalproperties.InternalProperty;
import main.java.embl.rieslab.emu.ui.uiparameters.UIParameter;
import main.java.embl.rieslab.emu.ui.uiproperties.UIProperty;

/**
 * Building block of EMU, this abstract class extends a JPanel. It holds a map of {@link main.java.embl.rieslab.emu.ui.uiproperties.UIProperty},
 * {@link main.java.embl.rieslab.emu.ui.uiparameters.UIParameter} and {@link main.java.embl.rieslab.emu.ui.internalproperties.InternalProperty}.
 * ConfigurablePanel subclasses should be instantiated within a {@link ConfigurableMainFrame}. The latter will collect their UIProperties, 
 * UIParameters and InternalProperties, and pass them on to the SystemController. 
 * <p> 
 * Subclasses of ConfigurablePanel must implements few methods called in the ConfigurablePanel constructor in order to instantiate the
 * UIProperties ({@link #initializeProperties()}), UIParameters ({@link #initializeParameters()}) and InternalProperties ({@link #initializeInternalProperties()}), 
 * as well as setting up the JComponents in the JPanel (itself). All JComponent instantiations should happen in {@link #setupPanel()}. Note 
 * that this method is the last one to be called in the ConfigurablePanel constructor, therefore the JPanel set up can also take place in 
 * the subclass constructor equivalently.
 * <p> 
 * UIProperties are aimed at linking the state of a MMProperty with the state of one or multiple JComponenents. InternalProperties are made to allow
 * shared values between ConfigurablePanels, such that a modification to one panel can trigger a change in the other panel. Finally, UIProperties
 * should only be used for user settings, such as changing the colors of JLabels or JButtons (to describe a filter or a laser) or the text of a header. 
 * All UIProperties and UIParameters appear in the {@link main.java.embl.rieslab.emu.configuration.ConfigurationWizard}. The user can then map the 
 * UIProperties with a MMProperty and change the value of a UIParameter. 
 * <p> 
 * Modifications to the state of UIProperties and InternalProperties should not be done explicitly in the subclasses, but should be done through the 
 * abstraction methods: {@link #changeProperty(String, String)} and {@link #changeInternalProperty(String, String)}. UIParameters should not be modified
 * within the subclasses. Modifications of the JComponents based on UIProperties, UIParameters and InternalProperties changes take place in the subclasses 
 * implementation of {@link #propertyhasChanged(String, String)}, {@link #parameterhasChanged(String)} and {@link #internalpropertyhasChanged(String)} respectively.  
 *  <p> 
 * For instance, a JToggleButton can be designed to turn on and off a laser. After declaration of the JToggleButton and addition to the panel in {@link #setupPanel()}, 
 * an eventListener can be added to the JToggleButton. The eventListener should then call {@link #changeProperty(String, String)} to modify the corresponding
 * UIProperty with a new value being on when the JToggleButton is selected, and an off value when the JToggleButton is unselected. More details can be found in
 * tutorials and the javadocs of the different UIProperties implementations.
 * <p> 
 * Upon start up of the {@link main.java.embl.rieslab.emu.plugin.UIPlugin}, the {@link main.java.embl.rieslab.emu.controller.SystemController} will
 * pair up the UIProperties with {@link main.java.embl.rieslab.emu.micromanager.mmproperties.MMProperty} and the latter's values will be propagated 
 * to the ConfigurablePanel via {@link #propertyhasChanged(String, String)}. Later on, changes to a MMProperty (for instance by another ConfigurablePanel)
 * will trigger the same method. The same mechanism is at play for the InternalProperties and the UIParameters. Note that the UIParameters are only changed
 * upon start up and when the user modifies the configuration through the {@link main.java.embl.rieslab.emu.configuration.ConfigurationWizard}. 
 * <p> 
 * In addition, to avoid triggering {@link #changeProperty(String, String)} through the eventListener when modifying the state of a JComponent (as it might happen
 * depending on the eventListener type), {@link #turnOffComponentTriggering()} can be called in the beginning of the subclasses implementation of 
 * {@link #propertyhasChanged(String, String)} and {@link #turnOnComponentTriggering()} at the end. These methods change the state of an internal member
 * boolean variable. In the eventListener, the method {@link #isComponentTriggeringOff()} can query the state of the boolean variable and decide to not
 * call {@link #changeProperty(String, String)}. This mechanism is optional.
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
	 * Constructor, calls the abstract methods {@link #initializeProperties()}, {@link #initializeParameters()}, 
	 * {@link #initializeInternalProperties()} and finally {@link #setupPanel()} (in that order).
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
		setupPanel();
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
	 * Returns a hash map of the panel's internal properties indexed by their hash ({panel's name}-{property's name}).
	 *
	 * @see main.java.embl.rieslab.emu.ui.internalproperties.InternalProperty
	 * 
	 * @return HashMap with the InternalProperty indexed by their hash. 
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
	 * Returns the {@link main.java.embl.rieslab.emu.ui.internalproperties.InternalProperty} named {@code propertyName}.
	 * Since the internal properties are stored in a HashMap with their hashes as keys, this method takes the name of 
	 * the internal property and searches the HashMap for the hash {this panel's label}-{propertyName}. 
	 * 
	 * @param propertyName Name of the internal property.
	 * @return Corresponding InternalProperty, null if it doesn't exist.
	 */
	protected InternalProperty getInternalProperty(String propertyName){
		if(internalprops_.containsKey(getLabel()+" "+propertyName)){
			return internalprops_.get(getLabel()+" "+propertyName);
		}
		return null;
	}	
	
	/**
	 * Returns the {@link main.java.embl.rieslab.emu.ui.uiparameters.UIParameter} named {@code parameterName}.
	 * Since the UI parameters are stored in a HashMap with their hashes as keys, this method takes the name of 
	 * the parameter and searches the HashMap for the hash {this panel's label}-{parameterName}. 
	 * 
	 * @param parameterName Name of the UIParameter
	 * @return Corresponding UIParameter, null if it doesn't exist.
	 */
	protected UIParameter getUIParameter(String parameterName){
		if(parameters_.containsKey(getLabel()+" "+parameterName)){
			return parameters_.get(getLabel()+" "+parameterName);
		}
		return null;
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
	 * Adds a {@link main.java.embl.rieslab.emu.ui.uiproperties.UIProperty} to the internal HashMap
	 * using the UI property's name.
	 * 
	 * @param uiproperty UIProperty to add
	 */
	// Shouldn't UI property not be added by unique hash instead of collision prone name?
	protected void addUIProperty(UIProperty uiproperty){
		properties_.put(uiproperty.getName(),uiproperty);
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
	 * using the InternalProperty hash.
	 * 
	 * @param internalproperty InternalProperty to add
	 */
	protected void addInternalProperty(InternalProperty internalproperty){
		internalprops_.put(internalproperty.getHash(),internalproperty);
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
			triggerParameterHasChanged(parameters_.get(it.next()).getName());
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

	// TODO
	protected void substituteParameter(String param, UIParameter uiParameter) {
		System.out.println("Call to substitute Parameter: how often does this happen?");
		// UIParamters have a "unique" hash, so they should not collide, so this method is useless. Is it the case with htSMLM?
		parameters_.remove(param);
		parameters_.put(param, uiParameter);
	}
	
	/**
	 * Checks if the component triggering is enabled. The change in permission is done through {@link #turnOffComponentTriggering()}
	 * and {@link #turnOnComponentTriggering()}. This is just indicative and only provides a mechanism to avoid triggering 
	 * the components eventListeners upon calling {@link #propertyhasChanged(String, String)}. For this mechanism to work, 
	 * {@link #changeProperty(String, String)} needs to be called in the eventListeners ONLY if this method returns true. For instance,
	 * this can be useful upon loading the UI, as {@link #propertyhasChanged(String, String)} will be called for each UIProperty,
	 * then in turn {@link #propertyhasChanged(String, String)} will be called and might change the state of a JComponent. Depending
	 * on the type of eventListeners, this might trigger {@link #changeProperty(String, String)}.
	 * 
	 * @return true if the component triggering is on, false if it is off.
	 */
	protected boolean isComponentTriggeringOff(){
		return componentTriggering_;
	}

	/**
	 * Turns off component triggering. See {@link #isComponentTriggeringOff()}.
	 */
	protected void turnOffComponentTriggering(){
		componentTriggering_ = false;
	}
	
	/**
	 * Turns on component triggering. See {@link #isComponentTriggeringOff()}.
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
	 * Method called when an internal property's value has been changed. This allows the ConfigurablePanel
	 * subclasses to react to the change of vale.
	 * 
	 * @param propertyName Name of the internal property
	 */
	public abstract void internalpropertyhasChanged(String propertyName);
	
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
	 * Method called in the constructor of a ConfigurablePanel after {@link #addUIProperty(UIProperty)}, {@link #addUIParameter(UIParameter)} 
	 * and {@link #addInternalProperty(InternalProperty)}. In this method, the subclasses must instantiate their 
	 * JComponents and build the JPanel (itself) as in any user interface. In particular, the default values of the
	 * UIParameters can be used.
	 */
	protected abstract void setupPanel();
	
	/**
	 * Method called to change the value of a UIProperty after user interaction with the ConfigurablePanel. For instance, this
	 * method should be called from the eventListeners of a JComponent.
	 * 
	 * @param propertyName UIProperty to change
	 * @param value New value of the UIproperty
	 */
	protected abstract void changeProperty(String propertyName, String value);
	
	/**
	 * Method called to change the value of an InternalProperty after user interaction with the ConfigurablePanel. For instance, this
	 * method should be called from the eventListeners of a JComponent.
	 * 
	 * @param propertyName InternalProperty to change
	 * @param value New value of the InternalProperty
	 */
	protected abstract void changeInternalProperty(String propertyName, String value);
	
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
