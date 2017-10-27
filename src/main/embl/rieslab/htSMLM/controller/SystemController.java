package main.embl.rieslab.htSMLM.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JOptionPane;

import main.embl.rieslab.htSMLM.micromanager.properties.MMProperties;
import main.embl.rieslab.htSMLM.micromanager.properties.MMProperty;
import main.embl.rieslab.htSMLM.ui.MainFrame;
import main.embl.rieslab.htSMLM.ui.PropertyPanel;
import main.embl.rieslab.htSMLM.ui.uiparameters.UIParameter;
import main.embl.rieslab.htSMLM.ui.uiproperties.PropertyPair;
import main.embl.rieslab.htSMLM.ui.uiproperties.SingleValueUIProperty;
import main.embl.rieslab.htSMLM.ui.uiproperties.ToggleUIProperty;
import main.embl.rieslab.htSMLM.ui.uiproperties.UIProperty;
import mmcorej.CMMCore;

public class SystemController {

	private CMMCore core_;
	private MMProperties mmproperties_;
	private Configuration config;
	private MainFrame mainframe_;
	private ArrayList<PropertyPair> pairs_;
	private HashMap<String,UIProperty> uiproperties_;
	@SuppressWarnings("rawtypes")
	private HashMap<String,UIParameter> uiparameters_;
	private ArrayList<String> unallocatedprop_; 
		
	@SuppressWarnings("rawtypes")
	public SystemController(CMMCore core){
		core_ = core;
		pairs_ = new ArrayList<PropertyPair>();
		uiproperties_ = new HashMap<String,UIProperty>();
		uiparameters_ = new HashMap<String,UIParameter>();
		unallocatedprop_ = new ArrayList<String>();
	}
	
	public void start() {
		// extract MM properties
		mmproperties_ = new MMProperties(core_);

		// initiate UI
		mainframe_ = new MainFrame();

		// extract UI properties and parameters
		Iterator<PropertyPanel> it = mainframe_.getPropertyPanels().iterator();
		PropertyPanel pan;
		while (it.hasNext()) {
			pan = it.next();
			uiproperties_.putAll(pan.getUIProperties());
			uiparameters_.putAll(pan.getUIParameters());
		}	

		// read out configuration
		config = new Configuration(this);
		boolean read = config.readConfiguration(uiproperties_, uiparameters_, mmproperties_);

		if(read){ // if read a configuration
			System.out.println("Read out configuration");
			getConfiguration();
		} else { // if failed
			System.out.println("Launch wizard");
			// launch wizard
			config.launchWizard(uiproperties_, uiparameters_, mmproperties_);
		}
	}
	
	public void readProperties(HashMap<String, String> configprop){
		String uiprop;
		unallocatedprop_.clear();
		
		Iterator<String> itstr = configprop.keySet().iterator();
		while (itstr.hasNext()) {
			uiprop = itstr.next();

			// if uiproperty exist
			if (uiproperties_.containsKey(uiprop)) {

				// if exist then link, otherwise add to list of unallocated properties
				if (configprop.get(uiprop).equals(Configuration.KEY_UNALLOCATED)) {
					// register missing allocation
					unallocatedprop_.add(uiprop);
				} else if (mmproperties_.isProperty(configprop.get(uiprop))) {
					// link the properties
					addPair(uiproperties_.get(uiprop),mmproperties_.getProperty(configprop.get(uiprop)));
					
					if(uiproperties_.get(uiprop).isToggle()){ // if toggle property (on/off)
						// extract the on/off values
						ToggleUIProperty t = (ToggleUIProperty) uiproperties_.get(uiprop);
						t.setOnValue(configprop.get(uiprop+ToggleUIProperty.getToggleOnName()));
						t.setOffValue(configprop.get(uiprop+ToggleUIProperty.getToggleOffName()));
					} else if(uiproperties_.get(uiprop).isSingleValue()){ // if constant value property
						// extract the constant value
						SingleValueUIProperty t = (SingleValueUIProperty) uiproperties_.get(uiprop);
						t.setConstantValue(configprop.get(uiprop+SingleValueUIProperty.getValueName()));
					}
				} else {
					// register missing allocation
					unallocatedprop_.add(uiprop);
				}
			} else {
				System.out.println(uiprop+" is not a valid UI property.");
			}
		}
	}
	
	public void readParameters(HashMap<String, String> configparam){
		String uiparam;
		Iterator<String> itstr = configparam.keySet().iterator();
		while (itstr.hasNext()) {
			uiparam = itstr.next();
			
			if (uiparameters_.containsKey(uiparam)) {
				uiparameters_.get(uiparam).setStringValue(configparam.get(uiparam));
			} else {
				System.out.println(uiparam+" is not a valid UI parameter.");
			}
		}
	}

	public void getConfiguration() {
		// Allocate UI properties and parameters
		readProperties(config.getPropertiesConfiguration());
		readParameters(config.getParametersConfiguration());

		// update all properties and parameters
		System.out.println("Update all ui and params");
		mainframe_.updateAll();

		// if unallocated properties show message
		if (unallocatedprop_.size() > 0) {
			showUnallocatedMessage();
		}
	}

	private void showUnallocatedMessage() {
		String title = "Unallocated properties";
		
		String message = "The following properties from the UI have not been allocated: \n\n";
		Iterator<String> it = unallocatedprop_.iterator();
		message = message+it.next();
		while(it.hasNext()){
			message = message+", "+it.next();
		}
		message = message+". \n\n";
		
		message = message+"The UI components related to these properties will not function until these properties are allocated. Lauch the configuration wizard to allocate them.";
		
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}

	@SuppressWarnings("rawtypes")
	private void addPair(UIProperty ui, MMProperty mm){
		pairs_.add(new PropertyPair(ui,mm));
	}
	
	public void shutDown(){
		config.shutDown();
		mainframe_.shutDown();
	}
	
	public double getExposure(){
		try {
			double exp = core_.getExposure();
			return exp;
		} catch (Exception e) {
			// throw error
			return 0;
		}
	}
}
