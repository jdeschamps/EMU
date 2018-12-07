package main.java.embl.rieslab.emu.uiexamples.htsmlm;

import main.java.embl.rieslab.emu.controller.SystemController;
import main.java.embl.rieslab.emu.plugin.UIPlugin;
import main.java.embl.rieslab.emu.ui.PropertyMainFrame;

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
