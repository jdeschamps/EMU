package main.embl.rieslab.mm.uidevint.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.micromanager.api.ScriptInterface;

import main.embl.rieslab.mm.uidevint.configuration.ConfigurationController;
import main.embl.rieslab.mm.uidevint.configuration.GlobalConfiguration;
import main.embl.rieslab.mm.uidevint.mmproperties.MMConfigurationGroup;
import main.embl.rieslab.mm.uidevint.mmproperties.MMConfigurationGroupsRegistry;
import main.embl.rieslab.mm.uidevint.mmproperties.MMProperties;
import main.embl.rieslab.mm.uidevint.mmproperties.MMProperty;
import main.embl.rieslab.mm.uidevint.plugin.UIPluginLoader;
import main.embl.rieslab.mm.uidevint.tasks.TaskHolder;
import main.embl.rieslab.mm.uidevint.ui.EmptyPropertyMainFrame;
import main.embl.rieslab.mm.uidevint.ui.PropertyMainFrame;
import main.embl.rieslab.mm.uidevint.ui.PropertyMainFrameInterface;
import main.embl.rieslab.mm.uidevint.ui.uiparameters.UIParameter;
import main.embl.rieslab.mm.uidevint.ui.uiproperties.MultiStateUIProperty;
import main.embl.rieslab.mm.uidevint.ui.uiproperties.PropertyPair;
import main.embl.rieslab.mm.uidevint.ui.uiproperties.SingleStateUIProperty;
import main.embl.rieslab.mm.uidevint.ui.uiproperties.TwoStateUIProperty;
import main.embl.rieslab.mm.uidevint.ui.uiproperties.UIProperty;
import mmcorej.CMMCore;

public class SystemController {

	private ScriptInterface script_;
	private CMMCore core_;
	private MMProperties mmproperties_;
	private MMConfigurationGroupsRegistry mmconfiggroups_;
	private ConfigurationController config;
	private PropertyMainFrame mainframe_;
	private PropertyMainFrameInterface interface_;
	private UIPluginLoader pluginloader_;
	private ArrayList<PropertyPair> pairs_;
	private ArrayList<String> unallocatedprop_; 
	
	private String currentPlugin;
		
	public SystemController(ScriptInterface script){
		script_ = script;
		core_ = script_.getMMCore();
		pairs_ = new ArrayList<PropertyPair>();

		unallocatedprop_ = new ArrayList<String>();
		
		currentPlugin = "";
	}
	
	/**
	 * Collects Micro-manager device properties, creates the user interface and loads the configuration. 
	 */
	public void start() {		
		// extracts MM properties
		mmproperties_ = new MMProperties(core_);
		mmconfiggroups_ = new MMConfigurationGroupsRegistry(core_);

		// load plugin list
		pluginloader_ = new UIPluginLoader(this);
		
		// if no plugin is found
		if(pluginloader_.getPluginNumber() == 0){
			// show message: no plugin found, stop here
			SystemDialogs.showNoPluginFound();
			
			// load empty MainFrame
			mainframe_ = new EmptyPropertyMainFrame(this);
		} else {
			// reads out configuration
			config = new ConfigurationController(this);
						
			if(config.readDefaultConfiguration()){ // if a configuration was read
				
				if(pluginloader_.isPluginAvailable(config.getConfiguration().getCurrentPluginName())){ // plugin available
					
					// initiates UI					
					currentPlugin = config.getConfiguration().getCurrentPluginName();
					mainframe_ = pluginloader_.loadPlugin(config.getConfiguration().getCurrentPluginName());
					
					// extracts UI properties and parameters
					interface_ = mainframe_.getInterface();
					
					// sanity check on the configuration to make sure the UIProperties and UIParameters match
					boolean sane = config.sanityCheck(interface_, mmproperties_);
					
					if(sane){
						// apply configuration
						applyConfiguration();
					} else {
						// show dialog
						SystemDialogs.showConfigurationDidNotPassSanityCheck();
						
						// then apply, the user can choose to change the settings or not
						applyConfiguration();
					}
					
				} else { // default UI not found or wrong format
			
					// get list of available plugins
					String[] plugins = pluginloader_.getPluginList();
					if(plugins.length > 1){
						// Let user choose which plugin to load
						// show dialog
						currentPlugin = SystemDialogs.showPluginsChoiceWindow(plugins);
					} else { // just one plugin
						currentPlugin = plugins[0];
					}
					
					// load plugin 
					mainframe_ = pluginloader_.loadPlugin(currentPlugin);
					
					// extracts UI properties and parameters
					interface_ = mainframe_.getInterface();
					
					// get list of configurations corresponding to this plugin
					String[] configs = config.getCompatibleConfigurations(currentPlugin);
					
					if(configs == null || configs.length == 0){
						// launch new wizard
						config.startWizard(currentPlugin, interface_, mmproperties_);
					} else if(configs.length == 1){
						config.setDefaultConfiguration(configs[0]); // set as default
						applyConfiguration();
					} else {
						// if more than one, then let the user decide
						String configuration = SystemDialogs.showPluginConfigurationsChoiceWindow(configs);
						config.setDefaultConfiguration(configuration); // set as default and then launch wizard
						applyConfiguration();
					}					
				}
				
			} else { // no configuration
				
				// show message
				if(config.getDefaultConfigurationFile().exists()){
					SystemDialogs.showConfigurationCouldNotBeParsed();
				}

				// get list of available plugins
				String[] plugins = pluginloader_.getPluginList();
				
				// Let user choose which plugin to load
				currentPlugin = SystemDialogs.showPluginsChoiceWindow(plugins);
				
				if(currentPlugin != null){
					// load plugin 
					mainframe_ = pluginloader_.loadPlugin(currentPlugin);
					
					// extracts UI properties and parameters
					interface_ = mainframe_.getInterface();
					
					// launch a new wizard			
					config.startWizard(currentPlugin, interface_, mmproperties_);
				} else {

					// load empty MainFrame
					mainframe_ = new EmptyPropertyMainFrame(this);
				}
			}
		}
	}


	public void loadConfiguration(String configuration){
		
		// check if the default configuration is compatible with current UI
		if (config.getConfiguration().getPluginConfiguration(configuration).getPluginName().equals(currentPlugin)) {

			pairs_ = new ArrayList<PropertyPair>();
			unallocatedprop_ = new ArrayList<String>();
			
			// close mainframe
			mainframe_.shutDownAllPropertyPanels();
			
			// empty mmproperties listeners
			mmproperties_.clearAllListeners();
			
			// load plugin 
			mainframe_ = pluginloader_.loadPlugin(currentPlugin);
			
			// extracts UI properties and parameters
			interface_ = mainframe_.getInterface();
			
			config.setDefaultConfiguration(configuration); // set as default
			
			// load configuration
			applyConfiguration();

		} else {
			// should throw exception
		}
	}
	
	public void loadPlugin(String newPlugin) {

		if(!pluginloader_.isPluginAvailable(newPlugin)){
			return;
		}
		
		pairs_ = new ArrayList<PropertyPair>();
		unallocatedprop_ = new ArrayList<String>();
		currentPlugin = newPlugin;
		
		// close mainframe
		mainframe_.shutDownAllPropertyPanels();
		
		// empty mmproperties listeners
		mmproperties_.clearAllListeners();
		
		// load plugin 
		mainframe_ = pluginloader_.loadPlugin(currentPlugin);
		
		// extracts UI properties and parameters
		interface_ = mainframe_.getInterface();
		
		// get list of configurations corresponding to this plugin
		String[] configs = config.getCompatibleConfigurations(currentPlugin);
		
		if(configs == null || configs.length == 0){
			// launch new wizard
			config.startWizard(currentPlugin, interface_, mmproperties_);
		} else if(configs.length == 1){
			config.setDefaultConfiguration(configs[0]); // set as default
			applyConfiguration();
		} else {
			// if more than one, then let the user decide
			String configuration = SystemDialogs.showPluginConfigurationsChoiceWindow(configs);
			config.setDefaultConfiguration(configuration); // set as default and then launch wizard
			applyConfiguration();
		}
	}
	
	
	/**
	 * Reads out the properties from the configuration and pairs them to ui properties.
	 * 
	 * @param configprop Mapping of the Micro-manager properties to the UI properties.
	 */
	public void readProperties(Map<String, String> configprop){
		String uiprop;
		unallocatedprop_.clear(); // clear the list of unallocated properties
		
		HashMap<String, UIProperty> uiproperties = interface_.getUIProperties();
		
		Iterator<String> itstr = configprop.keySet().iterator(); // iteration through all the mapped UI properties
		while (itstr.hasNext()) {
			uiprop = itstr.next(); // ui property

			// if ui property exists
			if (uiproperties.containsKey(uiprop)) {

				// if the ui property is not allocated, add to the list of unallocated properties.  
				if (configprop.get(uiprop).equals(GlobalConfiguration.KEY_UNALLOCATED)) {
					// register missing allocation
					unallocatedprop_.add(uiprop);
				} else if (mmproperties_.isProperty(configprop.get(uiprop))) { // if it is allocated to an existing Micro-manager property, link them together
					// link the properties
					addPair(uiproperties.get(uiprop),mmproperties_.getProperty(configprop.get(uiprop)));
					
					// test if the property has finite number of states
					if(uiproperties.get(uiprop).isTwoState()){ // if it is a two-state property
						// extract the on/off values
						TwoStateUIProperty t = (TwoStateUIProperty) uiproperties.get(uiprop);
						t.setOnStateValue(configprop.get(uiprop+TwoStateUIProperty.getOnStateName()));
						t.setOffStateValue(configprop.get(uiprop+TwoStateUIProperty.getOffStateName()));

					} else if(uiproperties.get(uiprop).isSingleState()){ // if single state property
						// extract the state value
						SingleStateUIProperty t = (SingleStateUIProperty) uiproperties.get(uiprop);
						t.setStateValue(configprop.get(uiprop+SingleStateUIProperty.getValueName()));
						
					} else if (uiproperties.get(uiprop).isMultiState()) {// if it is a multistate property
						MultiStateUIProperty t = (MultiStateUIProperty) uiproperties.get(uiprop);
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
	@SuppressWarnings("rawtypes")
	public void readParameters(Map<String, String> configparam){
		String uiparam;
		HashMap<String, UIParameter> uiparameters = interface_.getUIParameters();
		Iterator<String> itstr = configparam.keySet().iterator();
		ArrayList<String> wrg = new ArrayList<String>();
		while (itstr.hasNext()) {
			uiparam = itstr.next();
			
			if (uiparameters.containsKey(uiparam)) {
				try{
					uiparameters.get(uiparam).setStringValue(configparam.get(uiparam));
				} catch (Exception e){
					wrg.add(uiparam);
				}
			} 
		}
		if(wrg.size()>0){
			SystemDialogs.showWrongParameterMessage(wrg);
		}
	}

	/**
	 * Extracts the configuration for the ui properties and parameters, then updates the UI accordingly.
	 * 
	 */
	public void applyConfiguration() {
		// Allocate UI properties and parameters
		readProperties(config.getPropertiesConfiguration());
		readParameters(config.getParametersConfiguration());

		// update all properties and parameters
		mainframe_.updateAllPropertyPanels();
		
		// update menu
		mainframe_.updateMenu();
	}
	

	
	/**
	 * Launches new property Wizard in order to modify the current configuration. 
	 * 
	 * @return False if a Wizard is already running.
	 */
	public boolean launchWizard() {
		return config.startWizard(currentPlugin, interface_, mmproperties_);
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
			mainframe_.shutDownAllPropertyPanels();
		}
	}
	
	/**
	 * Returns a sorted array containing the name of all Micro-manager configuration groups.
	 * 
	 * @return
	 */
	public String[] getMMConfigGroups(){
		String[] groups = mmconfiggroups_.getConfigurationGroups().keySet().toArray(new String[0]);
		Arrays.sort(groups);
		return groups;
	}	
	
	/**
	 * Returns an object wrapping a Micro-manager configuration group.
	 * 
	 * @param groupname Name of the group to return
	 * @return 
	 */
	public MMConfigurationGroup getMMConfigGroup(String groupname){
		if(mmconfiggroups_.getConfigurationGroups().containsKey(groupname)){
			return mmconfiggroups_.getConfigurationGroups().get(groupname);
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
		if(mmconfiggroups_.getConfigurationGroups().containsKey(groupname)){
			return mmconfiggroups_.getConfigurationGroups().get(groupname).getConfigurations().toArray();
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
		HashMap<String, UIProperty> uiproperties = interface_.getUIProperties();
		Iterator<String> it = propvalues.keySet().iterator();
		String s;
		while(it.hasNext()){
			s = it.next();
			if(uiproperties.containsKey(s)){
				uiproperties.get(s).setPropertyValue(propvalues.get(s));
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
		return interface_.getUIProperties().get(name);
	}
	
	/**
	 * Returns a HashMap containing the name of the UIProperties (keys) and the corresponding UIProperties.
	 * 
	 * @return
	 */
	public HashMap<String, UIProperty> getPropertiesMap(){
		return interface_.getUIProperties();
	}
	
	/**
	 * Returns the corresponding TaskHolder.
	 * 
	 * @param taskname name of the task.
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public TaskHolder getTaskHolder(String taskname){
		return interface_.getUITaskHolders().get(taskname);
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public HashMap<String, String[]> getMMConfigurationGroups(){
		return mmconfiggroups_.getConfigurationChannelsMap();
	}
	
	public void refreshProperties(){
		if(mainframe_ != null){
			mainframe_.updateAllPropertyPanels();
		}
	}

	public String[] getPluginsList() {
		if(pluginloader_ == null){
			return null;
		}	
		return pluginloader_.getPluginList();
	}
	
	public String[] getOtherPluginsList() {
		if(pluginloader_ == null){
			return null;
		}	
		
		String[] list = pluginloader_.getPluginList();
		String[] others = new String[list.length-1];
		int curr = 0;
		for(int i=0;i<list.length;i++){
			if(!list[i].equals(currentPlugin)){
				others[curr] = list[i];
				curr++;
			}
		}
		
		return others;
	}
	
	public String[] getOtherCompatibleConfigurationList() {
		if(config == null){
			return null;
		}	
		
		String[] list = config.getCompatibleConfigurations(currentPlugin);
		
		if(list.length==1){
			return new String[0];
		}
		
		String[] others = new String[list.length-1];
		int curr = 0;
		for(int i=0;i<list.length;i++){
			if(!list[i].equals(config.getConfiguration().getCurrentConfigurationName())){
				others[curr] = list[i];
				curr++;
			}
		}
		
		return others;
	}

	
	public String[] getCompatibleConfigurationList() {
		return config.getCompatibleConfigurations(currentPlugin);
	}

}
