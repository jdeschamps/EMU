package main.embl.rieslab.mm.uidevint.plugin;

import java.util.ServiceLoader;

import main.embl.rieslab.mm.uidevint.controller.SystemController;
import main.embl.rieslab.mm.uidevint.ui.EmptyPropertyMainFrame;
import main.embl.rieslab.mm.uidevint.ui.PropertyMainFrame;

public class UIPluginLoader {
	
	public static PropertyMainFrame loadPlugin(SystemController controller) {
		ServiceLoader<UIPlugin> serviceLoader = ServiceLoader.load(UIPlugin.class);
		for (UIPlugin uiPlugin : serviceLoader) {
			if((controller.getPluginName()).equals(uiPlugin.getName())){
				return uiPlugin.getMainFrame(controller);
			}
		}
		return new EmptyPropertyMainFrame(controller);
	}
}