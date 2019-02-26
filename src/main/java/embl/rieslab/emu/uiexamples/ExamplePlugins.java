package main.java.embl.rieslab.emu.uiexamples;

import java.util.HashMap;

import main.java.embl.rieslab.emu.plugin.UIPlugin;

public class ExamplePlugins {

	public static HashMap<String, UIPlugin> getExamplePlugins(){
		HashMap<String, UIPlugin> plugins = new HashMap<String, UIPlugin>();
		
		/*UIPlugin plugin = new FOCUSLOCK();
		plugins.put(plugin.getName(), plugin);
		
		plugin = new LASERS();
		plugins.put(plugin.getName(), plugin);
		*/
		return plugins;
	}
}
