package main.embl.rieslab.mm.uidevint.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JOptionPane;

import org.micromanager.api.ScriptInterface;

import main.embl.rieslab.mm.uidevint.configuration.Configuration;
import main.embl.rieslab.mm.uidevint.mmproperties.ConfigurationGroup;
import main.embl.rieslab.mm.uidevint.mmproperties.ConfigurationGroupsRegistry;
import main.embl.rieslab.mm.uidevint.mmproperties.MMProperties;
import main.embl.rieslab.mm.uidevint.mmproperties.MMProperty;
import main.embl.rieslab.mm.uidevint.tasks.TaskHolder;
import main.embl.rieslab.mm.uidevint.ui.PropertyPanel;
import main.embl.rieslab.mm.uidevint.ui.uiparameters.UIParameter;
import main.embl.rieslab.mm.uidevint.ui.uiproperties.MultiStateUIProperty;
import main.embl.rieslab.mm.uidevint.ui.uiproperties.PropertyPair;
import main.embl.rieslab.mm.uidevint.ui.uiproperties.SingleStateUIProperty;
import main.embl.rieslab.mm.uidevint.ui.uiproperties.TwoStateUIProperty;
import main.embl.rieslab.mm.uidevint.ui.uiproperties.UIProperty;
import main.embl.rieslab.mm.uidevint.uiexamples.htsmlm.MainFrame;
import mmcorej.CMMCore;

public class SystemController {

	private ScriptInterface script_;
	private CMMCore core_;
	private MMProperties mmproperties_;
	private ConfigurationGroupsRegistry configgroups_;
	private Configuration config;
	private MainFrame mainframe_;
	private ArrayList<PropertyPair> pairs_;
	private HashMap<String,UIProperty> uiproperties_;
	@SuppressWarnings("rawtypes")
	private HashMap<String,UIParameter> uiparameters_;
	@SuppressWarnings("rawtypes")
	private HashMap<String,TaskHolder> tasks_;
	private ArrayList<String> unallocatedprop_; 
	private boolean start_;
		
	@SuppressWarnings("rawtypes")
	public SystemController(ScriptInterface script){
		script_ = script;
		core_ = script_.getMMCore();
		pairs_ = new ArrayList<PropertyPair>();
		uiproperties_ = new HashMap<String,UIProperty>();
		uiparameters_ = new HashMap<String,UIParameter>();
		tasks_ = new HashMap<String,TaskHolder>();
		unallocatedprop_ = new ArrayList<String>();
	}
	
	/**
	 * Collects Micro-manager device properties, creates the user interface and loads the configuration. 
	 */
	public void start() {
		start_ = true;
		
		// extracts MM properties
		mmproperties_ = new MMProperties(core_);
		configgroups_ = new ConfigurationGroupsRegistry(core_);

		// initiates UI
		mainframe_ = new MainFrame("ht-SMLM control center", this);

		// extracts UI properties and parameters
		extractPropertiesAndParameters();
			
		// reads out configuration
		config = new Configuration(this);
		boolean read = config.readDefaultConfiguration(uiproperties_, uiparameters_, mmproperties_);

		if(read){ // if read a configuration
			setConfiguration();
			start_ = false;
		} else { // if failed
			start_ = false;
			// launches a new wizard
			config.launchNewWizard(uiproperties_, uiparameters_, mmproperties_);
		}
	}
	
	// Extract the UIProperty and UIParameter from the UI.
	@SuppressWarnings("rawtypes")
	private void extractPropertiesAndParameters() {
		Iterator<PropertyPanel> it = mainframe_.getPropertyPanels().iterator();
		PropertyPanel pan;
		while (it.hasNext()) { // loops over the PropertyPanel contained in the MainFrame
			pan = it.next();
			
			// adds all the UIProperties, since their name contains their parent PropertyPanel name
			// there is no collision
			uiproperties_.putAll(pan.getUIProperties()); 
			
			
			////////////////////////////////////////////////////////////////////////////////////////////////
			//
			// I am not really certain any more of the purpose of theses lines
			// TODO
			
			// Loops over all parameters and make sure that they are unique
			HashMap<String,UIParameter> panparam = pan.getUIParameters();
			Iterator<String> paramit = panparam.keySet().iterator();
			ArrayList<String> subst = new ArrayList<String>();
			while(paramit.hasNext()){
				String param = paramit.next();
				
				if(!uiparameters_.containsKey(param)){ // if param doesn't exist already, adds it
					uiparameters_.put(param, panparam.get(param));
				} else if(uiparameters_.get(param).getType().equals(panparam.get(param).getType())){
					// if it already exists and the new parameter is of the same type than the one
					// previously added to the HashMap, then add to array subst
					subst.add(param);
				} 
			}
			// avoid concurrent modification of the hashmap, by substituting the parameters while keeping the same value
			for(int i=0;i<subst.size();i++){
				pan.substituteParameter(subst.get(i), uiparameters_.get(subst.get(i)));
			}
			
			//
			///////////////////////////////////////////////////////////////////////////////////////////////
			
			// gets tasks
			if(pan instanceof TaskHolder){
				tasks_.put(((TaskHolder) pan).getTaskName(), (TaskHolder) pan);
			}
		}	
	}

	/**
	 * Loads new configuration.
	 * 
	 * @param f Configuration to be read
	 * @return True if reading successful
	 */
	public boolean loadConfiguration(File f){
		boolean read = config.readConfiguration(uiproperties_, uiparameters_, mmproperties_, f);
		
		if(read){ // if read a configuration, then update.
			setConfiguration();
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Reads out the properties from the configuration and pairs them to ui properties.
	 * 
	 * @param configprop Mapping of the Micro-manager properties to the UI properties.
	 */
	public void readProperties(HashMap<String, String> configprop){
		String uiprop;
		unallocatedprop_.clear(); // clear the list of unallocated properties
		
		Iterator<String> itstr = configprop.keySet().iterator(); // iteration through all the mapped UI properties
		while (itstr.hasNext()) {
			uiprop = itstr.next(); // ui property

			// if ui property exists
			if (uiproperties_.containsKey(uiprop)) {

				// if the ui property is not allocated, add to the list of unallocated properties.  
				if (configprop.get(uiprop).equals(Configuration.KEY_UNALLOCATED)) {
					// register missing allocation
					unallocatedprop_.add(uiprop);
				} else if (mmproperties_.isProperty(configprop.get(uiprop))) { // if it is allocated to an existing Micro-manager property, link them together
					// link the properties
					addPair(uiproperties_.get(uiprop),mmproperties_.getProperty(configprop.get(uiprop)));
					
					// test if the property has finite number of states
					if(uiproperties_.get(uiprop).isTwoState()){ // if it is a two-state property
						// extract the on/off values
						TwoStateUIProperty t = (TwoStateUIProperty) uiproperties_.get(uiprop);
						t.setOnStateValue(configprop.get(uiprop+TwoStateUIProperty.getOnStateName()));
						t.setOffStateValue(configprop.get(uiprop+TwoStateUIProperty.getOffStateName()));

					} else if(uiproperties_.get(uiprop).isSingleState()){ // if single state property
						// extract the state value
						SingleStateUIProperty t = (SingleStateUIProperty) uiproperties_.get(uiprop);
						t.setStateValue(configprop.get(uiprop+SingleStateUIProperty.getValueName()));
					} else if (uiproperties_.get(uiprop).isMultiState()) {// if it is a multistate property
						MultiStateUIProperty t = (MultiStateUIProperty) uiproperties_.get(uiprop);
						int numpos = t.getNumberOfStates();
						for(int j=0;j<numpos;j++){
							t.setStateValue(j, configprop.get(uiprop+MultiStateUIProperty.getStateName(j)));
						}
					}
				} else {
					// register missing allocation
					unallocatedprop_.add(uiprop);
				}
			} 
		}
	}
	
	/**
	 * Reads out the ui parameters from the configuration.
	 * 
	 * @param configparam Values set by the user mapped to their corresponding ui parameter.
	 */
	public void readParameters(HashMap<String, String> configparam){
		String uiparam;
		Iterator<String> itstr = configparam.keySet().iterator();
		ArrayList<String> wrg = new ArrayList<String>();
		while (itstr.hasNext()) {
			uiparam = itstr.next();
			
			if (uiparameters_.containsKey(uiparam)) {
				try{
					uiparameters_.get(uiparam).setStringValue(configparam.get(uiparam));
				} catch (Exception e){
					wrg.add(uiparam);
				}
			} 
		}
		if(wrg.size()>0){
			showWrongParameterMessage(wrg);
		}
	}

	/**
	 * Extracts the configuration for the ui properties and parameters, then updates the UI accordingly. If
	 * there are some unallocated properties, then pops-up a message.
	 * 
	 */
	public void setConfiguration() {
		// Allocate UI properties and parameters
		readProperties(config.getPropertiesConfiguration());
		readParameters(config.getParametersConfiguration());

		// update all properties and parameters
		mainframe_.updateAll();

		// if unallocated properties show message
		if (!start_ && unallocatedprop_.size() > 0) {
			showUnallocatedMessage();
		}
	}
	

	
	/**
	 * Launches new property Wizard in order to modify the current configuration. 
	 * 
	 * @return False if a Wizard is already running.
	 */
	public boolean launchWizard() {
		return config.launchWizard(uiproperties_, uiparameters_, mmproperties_);
	}
	
	// Pops-up a message indicating that a parameter has been wrongly set.
	private void showWrongParameterMessage(ArrayList<String> wrongvals) {
		String title = "Unallocated properties";
		
		String message = "The following parameters have been set to a wrong value: \n\n";
		Iterator<String> it = wrongvals.iterator();
		message = message+it.next();
		int count = 1;
		while(it.hasNext()){
			if(count % 5 == 0){
				message = message+", \n"+it.next();
			} else {
				message = message+", "+it.next();
			}
			count ++;
		}
		message = message+". \n\n";
		
		message = message+"The value from these parameters will be ignored.";
		
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	// Pops-up a message indicating the unallocated ui properties.
	private void showUnallocatedMessage() {
		String title = "Unallocated properties";
		
		String message = "The following properties from the UI have not been allocated: \n\n";
		Iterator<String> it = unallocatedprop_.iterator();
		message = message+it.next();
		int count = 1;
		while(it.hasNext()){
			if(count % 5 == 0){
				message = message+", \n"+it.next();
			} else {
				message = message+", "+it.next();
			}
			count ++;
		}
		message = message+". \n\n";
		
		message = message+"The UI components related to these properties will not function until these properties are allocated. \nCreate or load configuration to allocate them.";
		
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}

	// Pairs a ui property and a Micro-manager property together.
	@SuppressWarnings("rawtypes")
	private void addPair(UIProperty ui, MMProperty mm){
		pairs_.add(new PropertyPair(ui,mm));
	}
	
	/**
	 * Shutdowns the UI.
	 * 
	 */
	public void shutDown(){
		if(config != null){
			config.shutDown();
		}
		if(mainframe_ != null){
			mainframe_.shutDown();
		}
	}
	
	/**
	 * Returns a sorted array containing the name of all Micro-manager configuration groups.
	 * 
	 * @return
	 */
	public String[] getMMConfigGroups(){
		String[] groups = configgroups_.getConfigurationGroups().keySet().toArray(new String[0]);
		Arrays.sort(groups);
		return groups;
	}	
	
	/**
	 * Returns an object wrapping a Micro-manager configuration group.
	 * 
	 * @param groupname Name of the group to return
	 * @return 
	 */
	public ConfigurationGroup getMMConfigGroup(String groupname){
		if(configgroups_.getConfigurationGroups().containsKey(groupname)){
			return configgroups_.getConfigurationGroups().get(groupname);
		}		
		return null;
	}
	
	/**
	 * Returns an array containing the names of each state of the specified Micro-manager configuration group.
	 * 
	 * @param groupname
	 * @return
	 */
	public String[] getMMConfigNames(String groupname){
		if(configgroups_.getConfigurationGroups().containsKey(groupname)){
			return configgroups_.getConfigurationGroups().get(groupname).getConfigurations().toArray();
		} 
		String[] s = {"Empty"}; 
		return s;
	}
	
	/**
	 * Sets the state of the UIProperties used as keys in propvalues to the corresponding value.
	 * 
	 * @param propvalues Map of the UIProperties (keys) and the values they should be set to.
	 */
	public void setUpSystem(HashMap<String, String> propvalues){
		Iterator<String> it = propvalues.keySet().iterator();
		String s;
		while(it.hasNext()){
			s = it.next();
			if(uiproperties_.containsKey(s)){
				uiproperties_.get(s).setPropertyValue(propvalues.get(s));
			}
		}
	}

	/**
	 * Returns the camera exposure. If the exposure failed to be retrieved from Micro-manager, returns 0.
	 * 
	 * @return Camera exposure in ms, or 0 if failed to retrieve it from Micro-manager.
	 */
	public double getExposure(){
		double i = 0;
		try {
			i = core_.getExposure();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Could not get exposure from core.");
			e.printStackTrace();
		}
		return i;
	}
	
	/** 
	 * Returns Micro-manager CMMCore.
	 * 
	 * @return
	 */
	public CMMCore getCore(){
		return core_;
	}

	/**
	 * Returns Micro-Maanager ScriptInterface.
	 * 
	 * @return
	 */
	public ScriptInterface getScriptInterface(){
		return script_;
	}
	
	/**
	 * Returns the corresponding UIProperty.
	 * 
	 * @param name name of the UIProperty
	 * @return
	 */
	public UIProperty getProperty(String name){ 
		return uiproperties_.get(name);
	}
	
	/**
	 * Returns a HashMap containing the name of the UIProperties (keys) and the corresponding UIProperties.
	 * 
	 * @return
	 */
	public HashMap<String, UIProperty> getPropertiesMap(){
		return uiproperties_;
	}
	
	/**
	 * Returns the corresponding TaskHolder.
	 * 
	 * @param taskname name of the task.
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public TaskHolder getTaskHolder(String taskname){
		return tasks_.get(taskname);
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public HashMap<String, String[]> getConfigurationGroups(){
		return configgroups_.getConfigurationChannelsMap();
	}
	
	public void refreshProperties(){
		if(mainframe_ != null){
			mainframe_.updateAll();
		}
	}

}
