package test.java.de.embl.rieslab.emu.testableclasses;

import main.java.de.embl.rieslab.emu.ui.ConfigurableMainFrame;
import mmcorej.CMMCore;

public abstract class TestableConfigurableMainFrame extends ConfigurableMainFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5515001170950109376L;

	public TestableConfigurableMainFrame() {
		super("", null);
	}
	
	@Override
	protected CMMCore getCore(){
		return null;
	}
	
	@Override
	public void updateMenu() {
		// Do nothing
	}
	
	@Override
	protected void setUpMenu() {
		// Do nothing
	}
}
