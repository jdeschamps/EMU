package main.java.myUI;

import main.java.embl.rieslab.emu.controller.SystemController;
import main.java.embl.rieslab.emu.plugin.UIPlugin;
import main.java.embl.rieslab.emu.ui.ConfigurableMainFrame;

public class MyPlugin implements UIPlugin {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "MyUI";
	}

	@Override
	public ConfigurableMainFrame getMainFrame(SystemController controller) {
		// TODO Auto-generated method stub
		return new MyFrame(getName(), controller);
	}

}
