package de.embl.rieslab.emu.exceptions;

public class IncorrectParameterTypeException extends Exception {

	private static final long serialVersionUID = 6111106437134689269L;

	public IncorrectParameterTypeException(String parameterName, String expectedType, String observedType){
		super("The UIParameter [" + parameterName + "] is of type ["
				+ observedType + "], instead of the expected ["+expectedType+"].");
	}
}