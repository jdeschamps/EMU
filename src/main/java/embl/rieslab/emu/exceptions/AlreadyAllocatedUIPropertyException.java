package main.java.embl.rieslab.emu.exceptions;

/**
 * Exception thrown when a process attempts to allocate a UIProperty that has already been allocated.
 * 
 * @author Joran Deschamps
 *
 */
public class AlreadyAllocatedUIPropertyException extends Exception {

	private static final long serialVersionUID = 7899587215294640777L;

	public AlreadyAllocatedUIPropertyException(String property_name){
		super("The property \""+property_name+"\" has already been allocated.");
	}
}
