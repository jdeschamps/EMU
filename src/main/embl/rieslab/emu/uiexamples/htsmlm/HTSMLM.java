package main.embl.rieslab.emu.uiexamples.htsmlm;

import main.embl.rieslab.emu.controller.SystemController;
import main.embl.rieslab.emu.plugin.UIPlugin;
import main.embl.rieslab.emu.ui.PropertyMainFrame;

public class HTSMLM implements UIPlugin{

	@Override
	public PropertyMainFrame getMainFrame(SystemController controller) {
		return new MainFrame("ht-SMLM control center", controller);
	}

	@Override
	public String getName() {
		return "ht-SMLM";
	}

}
