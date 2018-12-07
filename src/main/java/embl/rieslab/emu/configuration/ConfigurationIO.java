package main.java.embl.rieslab.emu.configuration;

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

public class ConfigurationIO {

	public static GlobalConfiguration read(File file) {
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		
		try {
			GlobalConfiguration config = new GlobalConfiguration(objectMapper.readValue(new FileInputStream(file), GlobalConfigurationWrapper.class));
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

	public static boolean write(File file, GlobalConfigurationWrapper configuration) {

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		
		try {
			objectMapper.writeValue(new FileOutputStream(file), configuration);
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
