package main.java.embl.rieslab.emu.uiexamples.focuslock;

import main.java.embl.rieslab.emu.controller.SystemController;
import main.java.embl.rieslab.emu.plugin.UIPlugin;
import main.java.embl.rieslab.emu.ui.PropertyMainFrame;

public class FOCUSLOCK  implements UIPlugin{

	@Override
	public PropertyMainFrame getMainFrame(SystemController controller) {
		return new FocusLockMainFrame("PI focus lock", controller);
	}

	@Override
	public String getName() {
		return "PI focus lock";
	}

}