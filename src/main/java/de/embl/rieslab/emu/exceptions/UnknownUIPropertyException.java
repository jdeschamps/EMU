package de.embl.rieslab.emu.exceptions;

public class UnknownUIPropertyException extends Exception{

	private static final long serialVersionUID = -2582718773998398031L;
	
	public UnknownUIPropertyException(String label){
		super("The UIProperty ["+label+"] is unknown.");
	}
}
