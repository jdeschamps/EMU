package main.embl.rieslab.mm.uidevint.micromanager;

import javax.swing.SwingUtilities;

import main.embl.rieslab.mm.uidevint.controller.SystemController;

import org.micromanager.api.MMPlugin;
import org.micromanager.api.ScriptInterface;

public class EMU implements MMPlugin {

	private ScriptInterface gui_;
	private SystemController controller_;

	private static String copyright = "LGPL";
	private static String description = "Interface UIs with Micro-manager device properties.";
	private static String info = "Joran Deschamps, EMBL, 2016-2019.";
	private static String version = "v1";

	@Override
	public String getCopyright() {
		return copyright;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getInfo() {
		return info;
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public void dispose() {
		controller_.shutDown();
	}

	@Override
	public void setApp(ScriptInterface app) {
	      gui_ = app;
	}

	@Override
	public void show() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				controller_ = new SystemController(gui_);
				controller_.start();
			}
		});
	}

}
