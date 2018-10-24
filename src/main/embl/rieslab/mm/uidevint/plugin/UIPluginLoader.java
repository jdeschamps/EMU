package main.embl.rieslab.mm.uidevint.plugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.ServiceLoader;

import main.embl.rieslab.mm.uidevint.controller.SystemController;
import main.embl.rieslab.mm.uidevint.ui.PropertyMainFrame;

public class UIPluginLoader {
	
	private SystemController controller_;
	private HashMap<String, UIPlugin> plugins_;
	
	public UIPluginLoader(SystemController controller){
		controller_ = controller;
		
		plugins_ = new HashMap<String, UIPlugin>();
		ServiceLoader<UIPlugin> serviceLoader = ServiceLoader.load(UIPlugin.class);
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

	public PropertyMainFrame loadPlugin(String pluginName){
		return plugins_.get(pluginName).getMainFrame(controller_);
	}
	
	public String[] getPluginList(){
		String[] s =  plugins_.keySet().toArray(new String[0]);
		Arrays.sort(s);
		return s;
	}
}