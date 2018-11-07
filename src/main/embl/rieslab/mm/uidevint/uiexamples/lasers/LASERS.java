package main.embl.rieslab.mm.uidevint.uiexamples.lasers;

import main.embl.rieslab.mm.uidevint.controller.SystemController;
import main.embl.rieslab.mm.uidevint.plugin.UIPlugin;
import main.embl.rieslab.mm.uidevint.ui.PropertyMainFrame;

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