package main.embl.rieslab.htSMLM.micromanager;


import javax.swing.SwingUtilities;

import main.embl.rieslab.htSMLM.controller.SystemController;

import mmcorej.CMMCore;

import org.micromanager.api.MMPlugin;
import org.micromanager.api.ScriptInterface;

public class htSMLM implements MMPlugin {

	private ScriptInterface gui_;
	private CMMCore core_;
	private SystemController controller_;

	private static String copyright = "LGPL";
	private static String description = "Plugin for the control of a high-throughput SMLM microscope.";
	private static String info = "Written by Joran Deschamps, Ries lab, EMBL.";
	private static String version = "v1-alpha";

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
