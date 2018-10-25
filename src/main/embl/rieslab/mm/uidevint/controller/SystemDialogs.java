package main.embl.rieslab.mm.uidevint.controller;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;

public class SystemDialogs {

	public static void showPluginsChoiceWindow(SystemController controller, String[] plugins) {
		// TODO
	}
	
	public static void showPluginConfigurationsChoiceWindow(SystemController controller, String[] pluginConfigurations) {
		// TODO
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
