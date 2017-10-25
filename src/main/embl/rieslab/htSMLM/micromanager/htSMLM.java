package main.embl.rieslab.htSMLM.micromanager;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import main.embl.rieslab.htSMLM.controller.SystemController;
import main.embl.rieslab.htSMLM.micromanager.properties.MMProperties;
import main.embl.rieslab.htSMLM.ui.FocusPanel;
import main.embl.rieslab.htSMLM.ui.MainFrame;
import main.embl.rieslab.htSMLM.ui.PropertyPanel;
import main.embl.rieslab.htSMLM.ui.uiproperties.UIProperty;
import mmcorej.CMMCore;

import org.micromanager.api.MMPlugin;
import org.micromanager.api.ScriptInterface;

public class htSMLM implements MMPlugin {

	   private ScriptInterface gui_;            
	   private CMMCore core_;
	
	@Override
	public String getCopyright() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setApp(ScriptInterface app) {
	      gui_ = app;
	      core_ = gui_.getMMCore();
	}

	@Override
	public void show() {
	      SwingUtilities.invokeLater(new Runnable()								
	       {
	           @Override
	           public void run()
	           {
	       		
	        	   
	           }
	       }); 
	}

}
