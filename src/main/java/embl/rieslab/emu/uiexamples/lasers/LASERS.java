package main.java.embl.rieslab.emu.uiexamples.lasers;

import main.java.embl.rieslab.emu.controller.SystemController;
import main.java.embl.rieslab.emu.plugin.UIPlugin;
import main.java.embl.rieslab.emu.ui.PropertyMainFrame;

public class LASERS implements UIPlugin{

	@Override
	public PropertyMainFrame getMainFrame(SystemController controller) {
		return new LasersMainFrame("Lasers control center", controller);
	}

	@Override
	public String getName() {
		return "Lasers";
	}

}