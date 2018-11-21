package main.embl.rieslab.emu.uiexamples.focuslock;

import main.embl.rieslab.emu.controller.SystemController;
import main.embl.rieslab.emu.plugin.UIPlugin;
import main.embl.rieslab.emu.ui.PropertyMainFrame;

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