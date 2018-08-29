package main.embl.rieslab.mm.uidevint.updaters;

import java.util.List;

import javax.swing.SwingWorker;

import main.embl.rieslab.mm.uidevint.ui.graph.Chart;
import main.embl.rieslab.mm.uidevint.ui.uiproperties.UIProperty;
import main.embl.rieslab.mm.uidevint.utils.utils;

public class ChartUpdater {

	private Chart chart_;
	private UIProperty propertyX_, propertyY_;
	private volatile boolean running_ = false;
	private boolean initialised_ = false;
	private UIupdater task_;
	private int idletime_;
	
	public ChartUpdater(Chart chart, UIProperty xprop, UIProperty yprop, int idletime){
		chart_ = chart;
		propertyX_ = xprop;
		propertyY_ = yprop;
		
		idletime_ = idletime;
		
		// sanity check
		if(utils.isNumeric(propertyX_.getPropertyValue()) && utils.isNumeric(propertyY_.getPropertyValue())){
			initialised_ = true;
		}
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

	public void changeChart(Chart newchart){
		chart_ = newchart;
	}
	
	private class UIupdater extends SwingWorker<Integer, Double[]> {

		@Override
		protected Integer doInBackground() throws Exception {
			Double[] value = new Double[2];
			while(running_){

				value[0] = Double.parseDouble(propertyX_.getPropertyValue());
				value[1] = Double.parseDouble(propertyY_.getPropertyValue());
				publish(value);
				
				Thread.sleep(idletime_);
			}
			return 1;
		}

		@Override
		protected void process(List<Double[]> chunks) {
			for(Double[] result : chunks){
				chart_.addPoint(result[0],result[1]);
			}
		}
	}
}