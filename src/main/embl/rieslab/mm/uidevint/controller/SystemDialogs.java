package main.embl.rieslab.mm.uidevint.controller;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class SystemDialogs {

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
	
	public static void showNoPluginFound(){
		String title = "No plugin";
		
		String message = "No plugin were found.";
		
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void showConfigurationDidNotPassSanityCheck(){
		String title = "Failed sanity check";
		
		String message = "Some entries in the configuration did not match expectations.\n "
				+ "As a result, the configuration has been automatically edited.\n"
				+ "Please, check the settings and save the modifications.";
		
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Pops-up a message indicating that a parameter has been wrongly set.
	 * @param wrongvals
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
	 * @param unallocated
	 */
	public static void showUnallocatedMessage(ArrayList<String> unallocated) {
		String title = "Unallocated properties";
		
		String message = "The following properties from the UI have not been allocated: \n\n";
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
		
		message = message+"The UI components related to these properties will not function until these properties are allocated. \nCreate or load configuration to allocate them.";
		
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
}
