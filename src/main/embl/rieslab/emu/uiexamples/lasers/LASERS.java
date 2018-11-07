package main.embl.rieslab.emu.uiexamples.lasers;

import main.embl.rieslab.emu.controller.SystemController;
import main.embl.rieslab.emu.plugin.UIPlugin;
import main.embl.rieslab.emu.ui.PropertyMainFrame;

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