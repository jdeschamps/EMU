package main.embl.rieslab.htSMLM.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.micromanager.api.ScriptInterface;

import main.embl.rieslab.htSMLM.acquisitions.ui.PropertySettingsTable;
import main.embl.rieslab.htSMLM.micromanager.properties.ConfigurationGroupsRegistry;
import main.embl.rieslab.htSMLM.micromanager.properties.MMProperties;
import main.embl.rieslab.htSMLM.micromanager.properties.MMProperty;
import main.embl.rieslab.htSMLM.threads.TaskHolder;
import main.embl.rieslab.htSMLM.ui.MainFrame;
import main.embl.rieslab.htSMLM.ui.PropertyPanel;
import main.embl.rieslab.htSMLM.ui.uiparameters.UIParameter;
import main.embl.rieslab.htSMLM.ui.uiproperties.MultiStateUIProperty;
import main.embl.rieslab.htSMLM.ui.uiproperties.PropertyPair;
import main.embl.rieslab.htSMLM.ui.uiproperties.SingleStateUIProperty;
import main.embl.rieslab.htSMLM.ui.uiproperties.TwoStateUIProperty;
import main.embl.rieslab.htSMLM.ui.uiproperties.UIProperty;
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
		
		// extract MM properties
		mmproperties_ = new MMProperties(core_);
		configgroups_ = new ConfigurationGroupsRegistry(core_);

		// initiate UI
		mainframe_ = new MainFrame("ht-SMLM control center", this);

		// extract UI properties and parameters
		extractPropertiesAndParameters();
			
		// read out configuration
		config = new Configuration(this);
		boolean read = config.readDefaultConfiguration(uiproperties_, uiparameters_, mmproperties_);

		if(read){ // if read a configuration
			getConfiguration();
			start_ = false;
		} else { // if failed
			start_ = false;
			// launch a new wizard
			config.launchNewWizard(uiproperties_, uiparameters_, mmproperties_);
		}
		
		// test
	/*	if(uiproperties_.size()>0){
			JFrame test = new JFrame();
			PropertySettingsTable pst = new PropertySettingsTable(uiproperties_);
			test.add(pst);
			test.pack();
			test.setVisible(true);
		}*/
	}
	
	@SuppressWarnings("rawtypes")
	private void extractPropertiesAndParameters() {
		Iterator<PropertyPanel> it = mainframe_.getPropertyPanels().iterator();
		PropertyPanel pan;
		while (it.hasNext()) {
			pan = it.next();
			
			uiproperties_.putAll(pan.getUIProperties());
			
			HashMap<String,UIParameter> panparam = pan.getUIParameters();
			Iterator<String> paramit = panparam.keySet().iterator();
			ArrayList<String> subst = new ArrayList<String>();
			while(paramit.hasNext()){
				String param = paramit.next();
				
				if(!uiparameters_.containsKey(param)){
					uiparameters_.put(param, panparam.get(param));
				} else if(uiparameters_.get(param).getType().equals(panparam.get(param).getType())){
					subst.add(param);
				} 
			}
			// avoid concurrent modification of the hashmap
			for(int i=0;i<subst.size();i++){
				pan.substituteParameter(subst.get(i), uiparameters_.get(subst.get(i)));
			}
			//uiparameters_.putAll(pan.getUIParameters());
			
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
			getConfiguration();
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Reads out the properties from the configuration and link them to ui properties.
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
	public void getConfiguration() {
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
	


	/**
	 * Pops-up a message indicating that a parameter has been wrongly set.
	 */
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
	
	/**	 
	 * Pops-up a message indicating the unallocated ui properties.
	 */
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

	/**
	 * Links a ui property and a Micro-manager property together.
	 * 
	 * @param ui UI property
	 * @param mm Micro-manager property
	 */
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
	
	public ConfigurationGroupsRegistry getConfigurationGroupsRegistry(){
		return configgroups_;
	}
	
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

	public CMMCore getCore(){
		return core_;
	}

	public ScriptInterface getScriptInterface(){
		return script_;
	}

}
