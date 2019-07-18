package de.embl.rieslab.emu.exceptions;

public class UnknownUIParameterException extends Exception{

	private static final long serialVersionUID = -7752429733361340084L;

	public UnknownUIParameterException(String label){
		super("The UIProperty ["+label+"] is unknown.");
	}
}
