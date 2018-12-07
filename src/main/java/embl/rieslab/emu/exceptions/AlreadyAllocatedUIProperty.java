package main.java.embl.rieslab.emu.exceptions;

public class AlreadyAllocatedUIProperty extends Exception {

	private static final long serialVersionUID = 7899587215294640777L;

	public AlreadyAllocatedUIProperty(String property_name){
		super("The property \""+property_name+"\" has already been allocated.");
	}
}
