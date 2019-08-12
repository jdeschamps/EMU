package de.embl.rieslab.emu.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.micromanager.Studio;

import de.embl.rieslab.emu.configuration.ConfigurationController;
import de.embl.rieslab.emu.configuration.GlobalConfiguration;
import de.embl.rieslab.emu.controller.SystemDialogs;
import de.embl.rieslab.emu.exceptions.AlreadyAssignedUIPropertyException;
import de.embl.rieslab.emu.exceptions.IncompatiblePluginConfigurationException;
import de.embl.rieslab.emu.micromanager.MMRegistry;
import de.embl.rieslab.emu.micromanager.configgroups.MMConfigurationGroupsRegistry;
import de.embl.rieslab.emu.micromanager.mmproperties.MMProperty;
import de.embl.rieslab.emu.plugin.UIPluginLoader;
import de.embl.rieslab.emu.ui.ConfigurableMainFrame;
import de.embl.rieslab.emu.ui.EmptyPropertyMainFrame;
import de.embl.rieslab.emu.ui.uiparameters.UIParameter;
import de.embl.rieslab.emu.ui.uiproperties.MultiStateUIProperty;
import de.embl.rieslab.emu.ui.uiproperties.PropertyPair;
import de.embl.rieslab.emu.ui.uiproperties.SingleStateUIProperty;
import de.embl.rieslab.emu.ui.uiproperties.TwoStateUIProperty;
import de.embl.rieslab.emu.ui.uiproperties.UIProperty;
import mmcorej.CMMCore;

/**
 * EMU controller class, bridging Micro-manager and the EMU UIPlugins using a configuration controller. 
 * Upon starting, it extracts the Micro-Manager device properties and configuration groups using an 
 * instance of {@link de.embl.rieslab.emu.micromanager.MMRegistry}. It then checks the available 
 * {@link de.embl.rieslab.emu.plugin.UIPlugin} through an {@link de.embl.rieslab.emu.plugin.UIPluginLoader}. It also makes use of a
 * {@link de.embl.rieslab.emu.configuration.ConfigurationController} to read the plugin configurations.
 * Finally, it extracts the UIProperties and UIParameters from the loaded plugin and pair them to the corresponding
 * MMProperties or state values. During running time, it can update the properties and change the current plugin or 
 * configuration.
 * 
 * @author Joran Deschamps
 *
 */
public class SystemController {

	private static Studio studio_; // MM studio
	private MMRegistry mmregistry_; // holds the properties and configuration groups from Micro-manager
	private ConfigurationController config; // Configuration controller, with all known plugin configurations
	private ConfigurableMainFrame mainframe_; // Main frame of the current plugin
	private UIPluginLoader pluginloader_; // loader for EMU plugins
	private ArrayList<String> unallocatedprop_;  // list of unallocated properties
	private ArrayList<String> forbiddenValuesProp_; // forbidden properties value
	private String currentPlugin; // reference to the current loaded plugin
		
	/**
	 * Constructor, solely instantiates the member variables.
	 * 
	 * @param studio Micro-manager studio instance.
	 */
	public SystemController(Studio studio){
		studio_ = studio;
	
		unallocatedprop_ = new ArrayList<String>();
		forbiddenValuesProp_ = new ArrayList<String>();
		
		currentPlugin = "";
	}
	
	/**
	 * Collects Micro-manager device properties and configuration groups, instantiates the default user interface
	 * plugin and loads the configurations. 
	 */
	public void start() {		
		// extracts MM properties, configuration groups and register configurations groups as properties
		mmregistry_ = new MMRegistry(studio_);
		
		// loads plugin list
		pluginloader_ = new UIPluginLoader(this);
		
		// if no plugin is found
		if(pluginloader_.getPluginNumber() == 0){
			// shows message: no plugin found
			SystemDialogs.showNoPluginFound();
			
			// loads empty MainFrame and stops here
			mainframe_ = new EmptyPropertyMainFrame(this);
			
		} else { // there are plugins
			// reads out configuration
			config = new ConfigurationController(this);
						
			if(config.readDefaultConfiguration()){ // if a configuration was read
				
				if(pluginloader_.isPluginAvailable(config.getConfiguration().getCurrentPluginName())){ // if plugin available
					
					// retrieves the plugin name and initiates UI					
					currentPlugin = config.getConfiguration().getCurrentPluginName();
					mainframe_ = pluginloader_.loadPlugin(currentPlugin);
										
					applyConfiguration();
					
				} else { // default configuration corresponds to an unknown plugin
			
					// gets list of available plugins
					String[] plugins = pluginloader_.getPluginList();
					if(plugins.length > 1){
						// Lets user choose which plugin to load
						currentPlugin = SystemDialogs.showPluginsChoiceWindow(plugins);
					} else if(plugins.length == 1) { // there is just one plugin, therefore loads it
						currentPlugin = plugins[0];
					} 
					
					// loads plugin 
					if(currentPlugin != null){
						// loads plugin 
						mainframe_ = pluginloader_.loadPlugin(currentPlugin);
						
						// gets list of configurations corresponding to this plugin
						String[] configs = config.getCompatibleConfigurations(currentPlugin);
						
						if(configs.length == 0){ // if there is no compatible configuration
							// launches new wizard
							config.startWizard(currentPlugin, mainframe_, mmregistry_.getMMPropertiesRegistry());
						} else if(configs.length == 1){ // there is one so applies it
							config.setDefaultConfiguration(configs[0]); // sets as default
							applyConfiguration();
						} else {
							// if more than one, then lets the user decide
							String configuration = SystemDialogs.showPluginConfigurationsChoiceWindow(configs);
							config.setDefaultConfiguration(configuration); // sets as default and then launch wizard
							applyConfiguration();
						}		
					} else {
						// loads empty MainFrame
						mainframe_ = new EmptyPropertyMainFrame(this);
					}
				}
				
			} else { // no configuration was loaded
				
				// shows message
				if(config.getDefaultConfigurationFile().exists()){
					SystemDialogs.showConfigurationCouldNotBeParsed();
				}

				// gets list of available plugins
				String[] plugins = pluginloader_.getPluginList();
				
				// Lets user choose which plugin to load
				currentPlugin = SystemDialogs.showPluginsChoiceWindow(plugins);
				
				if(currentPlugin != null){
					// loads plugin 
					mainframe_ = pluginloader_.loadPlugin(currentPlugin);
					
					// launches a new wizard			
					config.startWizard(currentPlugin, mainframe_, mmregistry_.getMMPropertiesRegistry());
				} else {
					// loads empty MainFrame
					mainframe_ = new EmptyPropertyMainFrame(this);
				}
			}
		}
	}

	/**
	 * Reloads the UI with the {@code newConfiguration}.
	 * 
	 * @param newConfiguration Configuration to reload the system with.
	 */
	public void loadConfiguration(String newConfiguration){
		try {
			reloadSystem(currentPlugin, newConfiguration);
			
			// apply configuration
			applyConfiguration();
			
		} catch (IncompatiblePluginConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public void loadPlugin(String newPlugin) {

		if(!pluginloader_.isPluginAvailable(newPlugin)){
			throw new IllegalArgumentException();
		}

		// get list of configurations corresponding to this plugin
		String[] configs = config.getCompatibleConfigurations(newPlugin);

		if(configs.length == 0){ // no configuration corresponding to the plugin
			currentPlugin = newPlugin;
			
			// close mainframe
			mainframe_.shutDownAllConfigurablePanels();
			
			mainframe_ = pluginloader_.loadPlugin(newPlugin);
			
			// launch new wizard
			config.startWizard(newPlugin, mainframe_, mmregistry_.getMMPropertiesRegistry());
			
		} else if(configs.length == 1){ // a single compatible configuration
			
			try {
				reloadSystem(newPlugin, configs[0]);
				applyConfiguration();
			} catch (IncompatiblePluginConfigurationException e) {
				e.printStackTrace();
			}
			
		} else { // more than one configuration
			
			// let the user decide
			String configuration = SystemDialogs.showPluginConfigurationsChoiceWindow(configs);
			
			if(configuration != null){
				// then loads the system
				try {
					reloadSystem(newPlugin, configuration);
					applyConfiguration();
				} catch (IncompatiblePluginConfigurationException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void reloadSystem(String pluginName, String configName) throws IncompatiblePluginConfigurationException {
		
		// if the plugin is not available or the configuration unknown
		if(!pluginloader_.isPluginAvailable(pluginName) || !config.doesConfigurationExist(configName)){ 
			throw new IllegalArgumentException();
		}
		
		// if the configuration is not compatible to the plugin
		if(!config.getConfiguration().getPluginConfiguration(configName).getPluginName().equals(pluginName)){
			throw new IncompatiblePluginConfigurationException(pluginName, configName);
		}
		
		// if the current plugin is not the requested plugin, then changes the current plugin
		if(!currentPlugin.equals(pluginName)){
			currentPlugin = pluginName;
		}
		
		// if the current configuration is not the requested configuration, then updates it
		if(!config.getDefaultConfiguration().equals(configName)){
			config.setDefaultConfiguration(configName);
		}
		
		config.writeConfiguration(); // to set the default configuration in the configuration file.
					
		// closes mainframe
		mainframe_.shutDownAllConfigurablePanels();
		
		// empties mmproperties listeners
		mmregistry_.getMMPropertiesRegistry().clearAllListeners();
		
		// reloads plugin 
		mainframe_ = pluginloader_.loadPlugin(pluginName);
	}

	
	/**
	 * Reads out the properties from the configuration and pairs them to ui properties.
	 * 
	 * @param configprop Mapping of the Micro-manager properties to the UI properties.
	 */
	public void readProperties(Map<String, String> configprop){
		String uiprop;
		unallocatedprop_.clear(); // clear the list of unallocated properties, props with forbidden values
		forbiddenValuesProp_.clear();
		
		HashMap<String, UIProperty> uiproperties = mainframe_.getUIProperties();
		
		Iterator<String> itstr = configprop.keySet().iterator(); // iteration through all the mapped UI properties
		while (itstr.hasNext()) {
			uiprop = itstr.next(); // ui property

			// if ui property exists
			if (uiproperties.containsKey(uiprop)) {

				// if the ui property is not allocated, add to the list of unallocated properties.  
				if (configprop.get(uiprop).equals(GlobalConfiguration.KEY_UNALLOCATED)) {
					// registers missing allocation
					unallocatedprop_.add(uiprop);
					
				} else if (mmregistry_.getMMPropertiesRegistry().isProperty(configprop.get(uiprop))) { // if it is allocated to an existing Micro-manager property, link them together
					// links the properties
					try {
						addPair(uiproperties.get(uiprop),mmregistry_.getMMPropertiesRegistry().getProperty(configprop.get(uiprop)));
					} catch (AlreadyAssignedUIPropertyException e) {
						e.printStackTrace();
					}
					
					// tests if the property has finite number of states
					if(uiproperties.get(uiprop) instanceof TwoStateUIProperty){ // if it is a two-state property
						// extracts the on/off values
						TwoStateUIProperty t = (TwoStateUIProperty) uiproperties.get(uiprop);
						String offval = configprop.get(uiprop+TwoStateUIProperty.getOffStateName());
						String onval = configprop.get(uiprop+TwoStateUIProperty.getOnStateName());
						
						if(!t.setOnStateValue(onval) || !t.setOffStateValue(offval)){
							forbiddenValuesProp_.add(uiprop);
						}

					} else if(uiproperties.get(uiprop) instanceof SingleStateUIProperty){ // if single state property
						// extracts the state value
						SingleStateUIProperty t = (SingleStateUIProperty) uiproperties.get(uiprop);
						String value = configprop.get(uiprop+SingleStateUIProperty.getValueName());
						
						if(!t.setStateValue(value)){
							forbiddenValuesProp_.add(uiprop);
						}
						
					} else if (uiproperties.get(uiprop) instanceof MultiStateUIProperty) {// if it is a multistate property
						MultiStateUIProperty t = (MultiStateUIProperty) uiproperties.get(uiprop);
						int numpos = t.getNumberOfStates();
						String[] val = new String[numpos];
						for(int j=0;j<numpos;j++){								
							val[j] =  configprop.get(uiprop+MultiStateUIProperty.getConfigurationStateName(j));
						}

						if(!t.setStateValues(val)){
							forbiddenValuesProp_.add(uiprop);
						}
					}
				} else {
					// registers missing allocation
					unallocatedprop_.add(uiprop);
				}
			} 
		}

		if(!forbiddenValuesProp_.isEmpty()){
			SystemDialogs.showForbiddenValuesMessage(forbiddenValuesProp_);
		}
		
		if(config.getEnableUnallocatedWarnings().getValue() && !unallocatedprop_.isEmpty()){
			SystemDialogs.showUnallocatedMessage(unallocatedprop_);
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
		HashMap<String, UIParameter> uiparameters = mainframe_.getUIParameters();
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


	private void applyConfiguration() {
		/*
		 * This method is called on the EDT, which makes the loading of the UI very slow.
		 * On the other hand, this prevents the UI to be shown until everything has been
		 * set-up, preventing user interaction.
		 */
		
		// sanity check
		boolean sane = config.sanityCheck(mainframe_);
		
		if(!sane){
			// shows dialog
			SystemDialogs.showConfigurationDidNotPassSanityCheck();
		} 
		
		// Allocates UI properties and parameters
		readProperties(config.getPropertiesConfiguration());
		readParameters(config.getParametersConfiguration());

		// updates all properties and parameters
		mainframe_.updateAllConfigurablePanels(); // this is slow as it calls CMMCore for every UIProperty
		
		// adds all action listeners
		mainframe_.addAllListeners();
				
		// updates menu
		mainframe_.updateMenu();
	}

	
	/**
	 * Launches new property Wizard in order to modify the current configuration. 
	 * 
	 * @return False if a Wizard is already running.
	 */
	public boolean launchWizard() {
		if(config != null) {
			return config.startWizard(currentPlugin, mainframe_, mmregistry_.getMMPropertiesRegistry());
		}
		return false;
	}
	
	// Pairs a ui property and a Micro-manager property together.
	@SuppressWarnings("rawtypes")
	private void addPair(UIProperty ui, MMProperty mm) throws AlreadyAssignedUIPropertyException{
		PropertyPair.pair(ui,mm);
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
			mainframe_.shutDownAllConfigurablePanels();
		}
	}
	
	/**
	 * Returns the Micro-manager configuration groups.
	 * 
	 * @return {@link de.embl.rieslab.emu.micromanager.configgroups.MMConfigurationGroupsRegistry}
	 */
	public MMConfigurationGroupsRegistry getMMConfigGroupRegistry(){
		return mmregistry_.getMMConfigurationGroupsRegistry();
	}	

	/**
	 * Returns the camera exposure. If the exposure failed to be retrieved from Micro-manager, returns 0.
	 * 
	 * @return Camera exposure in ms, or 0 if failed to retrieve it from Micro-manager.
	 */
	public double getExposure(){
		double i = 0;
		try {
			i = studio_.getCMMCore().getExposure();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	
	/** 
	 * Returns Micro-manager CMMCore.
	 * 
	 * @return Micro-manager CMMCore
	 */
	public CMMCore getCore(){
		return studio_.getCMMCore();
	}

	/**
	 * Returns Micro-Manager Studio.
	 * 
	 * @return Micro-manager Studio
	 */
	public Studio getStudio(){
		return studio_;
	}
	
	/**
	 * Returns the corresponding UIProperty.
	 * 
	 * @param name name of the UIProperty
	 * @return Corresponding UIProperty 
	 */
	public UIProperty getProperty(String name){ 
		return mainframe_.getUIProperties().get(name);
	}
	
	/**
	 * Returns a HashMap containing the name of the UIProperties (keys) and the corresponding UIProperties.
	 * 
	 * @return HashMap of the UIProperties
	 */
	public HashMap<String, UIProperty> getPropertiesMap(){
		return mainframe_.getUIProperties();
	}
	
	/**
	 * Updates all properties and parameters by calling {@link de.embl.rieslab.emu.ui.ConfigurableMainFrame#updateAllConfigurablePanels()}
	 * 
	 */
	public void forceUpdate(){
		if(mainframe_ != null){
			mainframe_.updateAllConfigurablePanels();
		}
	}

	/**
	 * Returnsan array of known plugins.
	 * 
	 * @return Array of plugins name.
	 */
	public String[] getPluginsList() {
		if(pluginloader_ == null){
			return new String[0];
		} else {	
			return pluginloader_.getPluginList();
		}
	}
	
	/**
	 * Returns an array of known plugins, excluding the currently loaded plugin.
	 * 
	 * @return Array of plugins name.
	 */
	public String[] getOtherPluginsList() {
		if(pluginloader_ == null){
			return new String[0];
		} else {	
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
	}
	
	/**
	 * Returns an array of known compatible configurations for the current loaded Plugin, 
	 * excluding the current configuration. 
	 * 
	 * @return Array of compatible configurations name.
	 */
	public String[] getOtherCompatibleConfigurationsList() {
		if(config == null){
			return new String[0];
		}	
		
		String[] list = config.getCompatibleConfigurations(currentPlugin);
		
		if(list.length==1){ // if length is 1 then the only configuration is the current one
			return new String[0];
		} else { // else remove the current one
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
	}

}
