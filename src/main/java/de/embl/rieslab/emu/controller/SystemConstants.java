package de.embl.rieslab.emu.controller;

/**
 * Constants used in the EMU library.
 * 
 * @author Joran Deschamps
 *
 */
public class SystemConstants {

	// Write/read
	/**
	 * Default configuration folder relative path.
	 */
	public final static String HOME = "EMU/"; // home of the default configuration file
	/**
	 * Configuration file's extension.
	 */
	public final static String CONFIG_EXT = "uicfg"; // extension of the configuration file
	/**
	 * Path to the default configuration file.
	 */
	public final static String CONFIG_NAME = HOME+"config."+CONFIG_EXT; // path to the configuration file
	
	public final static double EPSILON = 0.00001; 
}
