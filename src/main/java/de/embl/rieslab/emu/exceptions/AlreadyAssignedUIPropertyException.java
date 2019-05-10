package main.java.de.embl.rieslab.emu.exceptions;

/**
 * Exception thrown when a process attempts to assign a UIProperty that has already been assigned.
 * 
 * @author Joran Deschamps
 *
 */
public class AlreadyAssignedUIPropertyException extends Exception {

	private static final long serialVersionUID = 7899587215294640777L;

	public AlreadyAssignedUIPropertyException(String property_name){
		super("The property \""+property_name+"\" has already been assigned.");
	}
}
