package main.java.de.embl.rieslab.emu.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Reads/Writes a {@link GlobalConfigurationWrapper} from/to a file using jackson ObjectMapper.
 * 
 * @author Joran Deschamps
 *
 */
public class ConfigurationIO {

	/**
	 * Reads a {@link GlobalConfigurationWrapper} object from the file {@code fileToReadFrom}. It then
	 * instantiates and returns a @link GlobalConfiguration}.
	 * 
	 * @see GlobalConfiguration
	 * 
	 * @param fileToReadFrom File to read the GlobalConfiguration from.
	 * @return GlobalConfiguration from the file. null if no GlobalConfiguration could be read.
	 */
	public static GlobalConfiguration read(File fileToReadFrom) {
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		
		try {
			GlobalConfiguration config = new GlobalConfiguration(objectMapper.readValue(new FileInputStream(fileToReadFrom), GlobalConfigurationWrapper.class));
			return config;
			
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		return null;
	}

	/**
	 * Writes a {@link GlobalConfigurationWrapper} object to the file {@code fileToWriteTo}.
	 * 
	 * @param fileToWriteTo File in which to save the {@code configuration}.
	 * @param configuration GlobalConfiguration to be saved.
	 * @return True if the save was successful, false otherwise.
	 */
	public static boolean write(File fileToWriteTo, GlobalConfiguration configuration) {

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		
		try {
			// should write a GlobalConfigurationWrapper, not a GlobalConfiguration
			objectMapper.writeValue(new FileOutputStream(fileToWriteTo), configuration.getGlobalConfigurationWrapper()); 
			return true;
			
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

}
