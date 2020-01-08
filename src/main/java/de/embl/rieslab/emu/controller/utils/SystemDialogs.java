package de.embl.rieslab.emu.controller.utils;

import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import de.embl.rieslab.emu.controller.SystemController;

/**
 * Dialogs prompted to the user.
 * 
 * @author Joran Deschamps
 *
 */
public class SystemDialogs {

	/**
	 * This dialog pops up when the {@link de.embl.rieslab.emu.controller.SystemController} does not
	 * know which {@link de.embl.rieslab.emu.plugin.UIPlugin} to instantiate. It lets the user decide
	 * from an array of known plugin names.
	 * 
	 * @param plugins Array of plugin names.
	 * @return Name of the selected plugin.
	 */
	public static String showPluginsChoiceWindow(String[] plugins) {
        JFrame frame = new JFrame("Select a plugin");
        String selected_plugin = (String) JOptionPane.showInputDialog(frame, 
            "Select a plugin",
            "Select a plugin:",
            JOptionPane.QUESTION_MESSAGE, 
            null, 
            plugins, 
            plugins[0]);
        
        return selected_plugin;
	}
	
	/**
	 * This dialog pops up when the {@link de.embl.rieslab.emu.controller.SystemController} does not
	 * know which {@link de.embl.rieslab.emu.configuration.data.PluginConfiguration} to instantiate. It lets the user decide
	 * from an array of known configuration names.
	 *  
	 * @param configs Array of configuration names.
	 * @return Name of the selected configuration.
	 */
	public static String showPluginConfigurationsChoiceWindow(String[] configs) {
        JFrame frame = new JFrame("Select a configuration");
        String selected_conf = (String) JOptionPane.showInputDialog(frame, 
            "Select a configuration",
            "Select a configuration:",
            JOptionPane.QUESTION_MESSAGE, 
            null, 
            configs, 
            configs[0]);
        
        return selected_conf;
	}

	/**
	 * Dialog displayed when the {@link de.embl.rieslab.emu.controller.SystemController} does not find any {@link de.embl.rieslab.emu.plugin.UIPlugin}.
	 * 
	 */
	public static void showNoPluginFound(){
		String title = "No plugin";
		
		String message = "No plugin was found.";
		
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	
	/**
	 * Used by the {@link de.embl.rieslab.emu.controller.SystemController} to signify the user of missing UIParameters or UIProperties
	 * in the configuration.
	 * 
	 */
	public static void showConfigurationDidNotPassSanityCheck(){
		String title = "Failed sanity check";
		
		String message = "Some entries in the configuration did not match expectations.\n"
				+ "As a result, some aspects of the UI might not work properly. \nThis could be due to "
				+ "missing or unknown properties in the configuration.\n"
				+ "Please, check the settings.";
		
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Displayed when reading of the configuration file fails.
	 * 
	 */
	public static void showConfigurationCouldNotBeParsed(){
		String title = "Improper configuration file";
		
		String message = "The default configuration could not be read, check for JSON errors in the file.\n";
		
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Pops-up a message indicating that a parameter has been wrongly set.
	 * 
	 * @param wrongvals List of wrong parameters.
	 */
	public static void showWrongParameterMessage(ArrayList<String> wrongvals) {
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
	 * 
	 * @param unallocated List of unallocated properties.
	 */
	public static void showUnallocatedMessage(ArrayList<String> unallocated) {
		String title = "Unallocated properties";
		
		String message = "The following UI properties have not been allocated: \n\n";
		Iterator<String> it = unallocated.iterator();
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
		
		message = message+"The corresponding UI components will not function until these properties are allocated. \nUse the Settings Wizard to allocate them.";
		
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Displays the list of plugin settings states set to a wrong value.
	 * 
	 * @param wrongvals List of wrongly set settings.
	 */
	public static void showWrongPluginSettings(ArrayList<String> wrongvals) {
		String title = "Wrong plugin setting values";
		
		String message = "The values of the following plugin settings were ignored due to type mismatch: \n\n";
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
		
		message = message+"Please check make sure you enter correct values.\n";
		
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Displays the list of global settings states set to a wrong value.
	 * 
	 * @param wrongvals List of wrongly set settings.
	 */
	public static void showWrongGlobalSettings(ArrayList<String> wrongvals) {
		String title = "Wrong global setting values";
		
		String message = "The values of the following global settings were ignored due to type mismatch: \n\n";
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
		
		message = message+"Please check make sure you enter correct values.\n";
		
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Displays the list of UIProperties states set to a forbidden value (mismatch with MM property limits).
	 * 
	 * @param forbiddenvalProps List of UIProperties set to forbidden values. 
	 */
	public static void showForbiddenValuesMessage(ArrayList<String> forbiddenvalProps) {
		String title = "Forbidden values";
		
		String message = "The state values of the following UIProperties are forbidden: \n\n";
		Iterator<String> it = forbiddenvalProps.iterator();
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
		
		message = message+"Please check the device property browser of Micro-Manager \nto infer allowed values and correct the state values in the Settings Wizard.\n";
		
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Displays the list of UIProperties allocated to incompatible MMProperties.
	 * 
	 * @param incompatibleProps List of incompatible UIProperties.
	 */
	public static void showIncompatiblePropertiesMessage(ArrayList<String> incompatibleProps) {
		String title = "Incompatible properties";
		
		String message = "The following UIProperties were paired with incompatible MMProperties: \n\n";
		Iterator<String> it = incompatibleProps.iterator();
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
		message = message+". \n";
		
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * "About EMU" window.
	 */
	public static void showAboutEMU(){
		String title = "Easier Micro-manager User interfaces";
		
		// From: https://stackoverflow.com/questions/8348063/clickable-links-in-joptionpane
		// for copying style
	    JLabel label = new JLabel();
	    Font font = label.getFont();

	    // create some css from the label's font
	    StringBuffer style = new StringBuffer("font-family:" + font.getFamily() + ";");
	    style.append("font-weight:" + (font.isBold() ? "bold" : "normal") + ";");
	    style.append("font-size:" + font.getSize() + "pt;");
		
		String message = "Easier Micro-manager User interfaces (EMU).<br />"
						+ "version "+SystemController.EMU_VERSION+" <br />"
						+ "EMU is a Micro-Manager plugin that provides an easy and intuitive way to interface a user <br />"
						+ "interface (UI) with the device properties of Micro-manager. Build you own UI using drag and<br />"
						+ "drop softwares and EMU classes and load your UI in EMU. Finally, configure it by mapping <br />"
						+ "device properties to the UI properties and set the UI parameter values using the configuration <br />"
						+ "wizard.<br /><br />"
						+ "<a href=\"https://jdeschamps.github.io/EMU-guide/\">The EMU guide</a> is available online.<br />"
						+ "<a href=\"https://github.com/jdeschamps/EMU\">Find the source code on Github</a>.<br /><br />"
						+ "This plugin was developped by Joran Deschamps, EMBL (2019).";
	   
		// html content
	    JEditorPane ep = new JEditorPane("text/html", "<html><body style=\"" + style + "\">" //
	            + message //
	            + "</body></html>");

	    // handle link events
	    ep.addHyperlinkListener(new HyperlinkListener()
	    {
	        @Override
	        public void hyperlinkUpdate(HyperlinkEvent e)
	        {
	            if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
					try {
						java.awt.Desktop.getDesktop().browse(java.net.URI.create(e.getURL().toString()));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
	            }
	        }
	    });
	    ep.setEditable(false);
	    ep.setBackground(label.getBackground());
		
	    // icon		
	    ImageIcon ic;
		try {
			ic = new ImageIcon(ImageIO.read(label.getClass().getResource( "/images/logo64.png" )));
	        JOptionPane.showMessageDialog(null, ep, title, JOptionPane.INFORMATION_MESSAGE, ic);

		} catch (IOException e1) {
	        JOptionPane.showMessageDialog(null, ep, title, JOptionPane.INFORMATION_MESSAGE);

			e1.printStackTrace();
		}
	}
	
	/**
	 * Prompted when the Configuration Wizard is already running.
	 */
	public static void showWizardRunningMessage() {
		JOptionPane.showMessageDialog(null,
				"Configuration wizard already running.",
				"Information", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Prompted when the Configuration Manager is already running.
	 */
	public static void showManagerRunningMessage() {
		JOptionPane.showMessageDialog(null,
				"Configuration manager already running or they are no configurations to manage.",
				"Information", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Prompted when the Configuration Manager is asked to delete the current configuration. 
	 */
	public static void showCannotDeleteCurrentConfiguration() {
		JOptionPane.showMessageDialog(null,
				"The current configuration cannot be deleted.",
				"Information", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Prompted when a new configuration is created in the ConfigurationWizard. 
	 */
	public static void showWillCreateNewconfiguration() {
		JOptionPane.showMessageDialog(null,
				"Modifying the name of the configuration will create a new configuration (use the configuration manager to delete configurations).",
				"Information", JOptionPane.INFORMATION_MESSAGE);
	}
}
