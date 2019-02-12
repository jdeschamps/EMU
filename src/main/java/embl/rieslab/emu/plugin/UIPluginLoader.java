package main.java.embl.rieslab.emu.plugin;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ServiceLoader;

import main.java.embl.rieslab.emu.controller.SystemConstants;
import main.java.embl.rieslab.emu.controller.SystemController;
import main.java.embl.rieslab.emu.plugin.UIPlugin;
import main.java.embl.rieslab.emu.ui.ConfigurableMainFrame;
import main.java.embl.rieslab.emu.uiexamples.ExamplePlugins;

public class UIPluginLoader {
	
	private SystemController controller_;
	private HashMap<String, UIPlugin> plugins_;
	
	public UIPluginLoader(SystemController controller){
		controller_ = controller;
		
		plugins_ = ExamplePlugins.getExamplePlugins();

        File loc = new File(SystemConstants.HOME);

        File[] flist = loc.listFiles(new FileFilter() {
            public boolean accept(File file) {return file.getPath().toLowerCase().endsWith(".jar");}
        });
        
        URL[] urls = new URL[flist.length];
        for (int i = 0; i < flist.length; i++){
            try {
				urls[i] = flist[i].toURI().toURL();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        URLClassLoader ucl = new URLClassLoader(urls);
		ServiceLoader<UIPlugin> serviceLoader = ServiceLoader.load(UIPlugin.class,ucl);
		for (UIPlugin uiPlugin : serviceLoader) {
			plugins_.put(uiPlugin.getName(), uiPlugin);
		}
		
	}
	
	public int getPluginNumber(){
		return plugins_.size();
	}
	
	public boolean isPluginAvailable(String pluginName){
		return plugins_.containsKey(pluginName);
	}

	public ConfigurableMainFrame loadPlugin(String pluginName){
		return plugins_.get(pluginName).getMainFrame(controller_);
	}
	
	public String[] getPluginList(){
		String[] s =  plugins_.keySet().toArray(new String[0]);
		Arrays.sort(s);
		return s;
	}
}