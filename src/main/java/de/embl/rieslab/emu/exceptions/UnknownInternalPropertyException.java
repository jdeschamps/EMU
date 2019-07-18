package de.embl.rieslab.emu.exceptions;

public class UnknownInternalPropertyException extends Exception{
	private static final long serialVersionUID = -5225065640056140913L;
	
	public UnknownInternalPropertyException(String label){
		super("The InternalProperty ["+label+"] is unknown.");
	}
}
