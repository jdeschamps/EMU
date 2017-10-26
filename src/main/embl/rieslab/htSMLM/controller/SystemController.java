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
		config = new Configuration();
		boolean read = config.readConfiguration(uiproperties_, uiparameters_, mmproperties_);
/*
		// if got a configuration
		if(read){
			// Allocate UI properties
			HashMap<String, String> configprop = config.getPropertiesConfiguration();

			String uiprop;
			Iterator<String> itstr = configprop.keySet().iterator();
			while (itstr.hasNext()) {
				uiprop = itstr.next();

				// if exist link, otherwise add to list of unallocated properties
				if (mmproperties_.isProperty(configprop.get(uiprop))) {
					addPair(uiproperties_.get(uiprop),
							mmproperties_.getProperty(configprop.get(uiprop)));
				} else {
					// register missing allocation
					unallocatedprop_.add(uiprop);
				}
			}

			// Set UI parameters
			HashMap<String, String> configparam = config
					.getPropertiesConfiguration();

			String uiparam;
			itstr = configparam.keySet().iterator();
			while (itstr.hasNext()) {
				uiparam = itstr.next();
				uiparameters_.get(uiparam).setStringValue(
						configparam.get(uiparam));
			}

			// update all
			mainframe_.updateAll();

			// if unallocated properties show message
			if (unallocatedprop_.size() > 0) {
				showUnallocatedMessage();
			}


		} else {
			// launch wizard
		}*/
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
