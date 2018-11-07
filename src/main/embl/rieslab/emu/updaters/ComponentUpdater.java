package main.embl.rieslab.emu.updaters;

import java.util.List;

import javax.swing.JComponent;
import javax.swing.SwingWorker;

import main.embl.rieslab.emu.ui.uiproperties.UIProperty;

public abstract class ComponentUpdater<T extends JComponent> {

	protected T component_;
	private UIProperty property_;
	private volatile boolean running_ = false;
	private boolean initialised_ = false;
	private UIupdater task_;
	private int idletime_;
	
	public ComponentUpdater(T component, UIProperty prop, int idletime){
		component_ = component;
		property_ = prop;
		
		idletime_ = idletime;
		
		initialised_ = sanityCheck(prop);
	}
	
	public boolean isInitialised(){
		return initialised_;
	}
	
	public boolean isRunning(){
		return running_;
	}
	
	public void startUpdater(){
		if(!running_ && initialised_){
			running_ = true;
			task_ = new UIupdater( );
			task_.execute();
		}
	}
	
	public void stopUpdater(){
		running_ = false;
	}
	
	public void changeIdleTime(int newtime){
		idletime_ = newtime;
	}
	
	public abstract boolean sanityCheck(UIProperty prop);
	public abstract void updateComponent(String val);
		
	private class UIupdater extends SwingWorker<Integer, String> {

		@Override
		protected Integer doInBackground() throws Exception {
			while(running_){
				
				String s = property_.getPropertyValue();
				if(s != null && !s.isEmpty()){
					publish(property_.getPropertyValue());
				}
				Thread.sleep(idletime_);
			}
			return 1;
		}

		@Override
		protected void process(List<String> chunks) {
			for(String result : chunks){
				updateComponent(result);
			}
		}
	}
}