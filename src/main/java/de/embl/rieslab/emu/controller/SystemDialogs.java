package de.embl.rieslab.emu.controller;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
	 * know which {@link de.embl.rieslab.emu.configuration.PluginConfiguration} to instantiate. It lets the user decide
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
	 * Displays the list of UIProperties states set to a forbidden value (msimatch with MM property limits).
	 * 
	 * @param forbiddenvals
	 */
	public static void showForbiddenValuesMessage(ArrayList<String> forbiddenvals) {
		String title = "Forbidden values";
		
		String message = "The state values of the following UIProperties are forbidden: \n\n";
		Iterator<String> it = forbiddenvals.iterator();
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
	 * About EMU window.
	 * 
	 */
	public static void showAboutEMU(){
		String title = "Easier Micro-manager User interfaces";
		
		String message = "Easier Micro-manager User interfaces (EMU).\nThis Micro-manager plugin provides a way to quickly interface a GUI with "
				     + "\nthe device properties of Micro-manager. Build you own GUI using drag'n drop "
				     + "\nsoftwares, follow the tutorials on how to implement properly EMU classes "
				     + "\nand, finally, load your GUI in this plugin. You will then be able to intuitively "
				     + "\nmap your devices properties with the actions of your UI. \n\n"
				     + "Find the tutorials and the wiki on GitHub (jdeschamps/EMU).\n\n"
				     + "This plugin was developped by Joran Deschamps, EMBL (2019).";
		
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
}
