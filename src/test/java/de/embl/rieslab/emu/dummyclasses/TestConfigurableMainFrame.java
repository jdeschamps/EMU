package test.java.de.embl.rieslab.emu.dummyclasses;

import main.java.de.embl.rieslab.emu.ui.ConfigurableMainFrame;
import mmcorej.CMMCore;

public abstract class TestConfigurableMainFrame extends ConfigurableMainFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5515001170950109376L;

	public TestConfigurableMainFrame() {
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
