package main.embl.rieslab.mm.uidevint.uiexamples.htsmlm;

import main.embl.rieslab.mm.uidevint.controller.SystemController;
import main.embl.rieslab.mm.uidevint.plugin.UIPlugin;
import main.embl.rieslab.mm.uidevint.ui.PropertyMainFrame;

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
