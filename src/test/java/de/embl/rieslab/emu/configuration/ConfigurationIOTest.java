package test.java.de.embl.rieslab.emu.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

import main.java.de.embl.rieslab.emu.configuration.ConfigurationIO;
import main.java.de.embl.rieslab.emu.configuration.GlobalConfiguration;
import main.java.de.embl.rieslab.emu.configuration.GlobalConfigurationWrapper;
import main.java.de.embl.rieslab.emu.configuration.PluginConfiguration;
import main.java.de.embl.rieslab.emu.controller.SystemConstants;

public class ConfigurationIOTest {

	@Test 
	public void testWriteReadConfiguration() {
		HashMap<String, String> properties = new HashMap<String,String>();
		HashMap<String, String> parameters = new HashMap<String,String>();

		final String prop1 = "Prop1";
		final String prop2 = "Prop2";
		final String prop3 = "Prop3";
		final String mmprop1 = "MM Prop1";
		final String mmprop2 = "MM Prop2";
		final String mmprop3 = "MM Prop3";
		properties.put(prop1, mmprop1);
		properties.put(prop2, mmprop2);
		properties.put(prop3, mmprop3);
		
		final String param1 = "Param1";
		final String param2 = "Param2";
		final String paramVal1 = "Param Val1";
		final String paramVal2 = "Param Val2";
		parameters.put(param1, paramVal1);
		parameters.put(param2, paramVal2);

		final String plugin = "MyPlugin";
		final String config = "MyConfig";
		
		PluginConfiguration mypluginconfig = new PluginConfiguration();
		mypluginconfig.configure(config, plugin, properties, parameters);
		
		final boolean b = true;
				
		GlobalConfigurationWrapper globconfig = new GlobalConfigurationWrapper();
		globconfig.setDefaultConfigurationName(config);
		globconfig.setEnableUnallocatedWarnings(b);
		
		ArrayList<PluginConfiguration> list = new ArrayList<PluginConfiguration>();
		list.add(mypluginconfig);
		globconfig.setPluginConfigurations(list);
		
		// Test writing
		final String path = "TestConfigIO."+SystemConstants.CONFIG_EXT;
		boolean write = ConfigurationIO.write(new File(path), new GlobalConfiguration(globconfig));
		assertTrue(write);
		
		// Test reading
		GlobalConfiguration readconfig = ConfigurationIO.read(new File(path));
		
		// tests if the configurations match
		assertTrue(readconfig.doesConfigurationExist(config));
		assertEquals(config, readconfig.getCurrentConfigurationName());
		assertTrue(readconfig.getEnableUnallocatedWarningsSetting().getValue() == b);
		
		ArrayList<PluginConfiguration> newlist = readconfig.getPluginConfigurations();
		assertEquals(list.size(), newlist.size());
		
		PluginConfiguration newpluginconfig = newlist.get(0);
		assertEquals(config, newpluginconfig.getConfigurationName());	
		assertEquals(plugin, newpluginconfig.getPluginName());
		
		Map<String,String> mapprop = newpluginconfig.getProperties();	
		assertEquals(properties.size(), mapprop.size());
		
		Iterator<String> it = properties.keySet().iterator();
		while(it.hasNext()) {
			String s = it.next();
			assertTrue(mapprop.containsKey(s));
			assertEquals(properties.get(s), mapprop.get(s));
		}
		
		Map<String,String> mapparam = newpluginconfig.getParameters();	
		assertEquals(parameters.size(), mapparam.size());
		
		it = parameters.keySet().iterator();
		while(it.hasNext()) {
			String s = it.next();
			assertTrue(mapparam.containsKey(s));
			assertEquals(parameters.get(s), mapparam.get(s));
		}
	}
}












