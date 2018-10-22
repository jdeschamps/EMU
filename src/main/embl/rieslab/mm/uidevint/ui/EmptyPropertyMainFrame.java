package main.embl.rieslab.mm.uidevint.ui;

import main.embl.rieslab.mm.uidevint.controller.SystemController;

public class EmptyPropertyMainFrame extends PropertyMainFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1840416249969324418L;

	public EmptyPropertyMainFrame(SystemController controller) {
		super("Empty frame", controller);
	}

	@Override
	protected void initComponents() {
		// Do nothing
	}

}
