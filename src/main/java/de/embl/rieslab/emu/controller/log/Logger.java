package de.embl.rieslab.emu.controller.log;

import org.micromanager.LogManager;

public class Logger {

	LogManager logManager_;
	
	public Logger(LogManager logManager) {
		logManager_ = logManager;
	}
	
	public void logDebugMessage(String message) {
		logManager_.logDebugMessage("[EMU] "+message);
	}
	
	public void logError(Exception e) {
		logManager_.logError(e);
	}
	
	public void logError(String message) {
		logManager_.logError("[EMU] "+message);
	}
	
	public void logMessage(String message) {
		logManager_.logMessage("[EMU] "+message);
	}
}
