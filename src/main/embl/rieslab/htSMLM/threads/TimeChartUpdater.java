package main.embl.rieslab.htSMLM.threads;


import java.util.List;
import javax.swing.SwingWorker;
import main.embl.rieslab.htSMLM.ui.graph.TimeChart;
import main.embl.rieslab.htSMLM.ui.uiproperties.UIProperty;
import main.embl.rieslab.htSMLM.util.utils;

public class TimeChartUpdater {

	private TimeChart chart_;
	private UIProperty property_;
	private volatile boolean running_ = false;
	private boolean initialised_ = false;
	private UIupdater task_;
	private int idletime_;
	
	public TimeChartUpdater(TimeChart chart, UIProperty prop, int idletime){
		chart_ = chart;
		property_ = prop;
		idletime_ = idletime;
		
		// sanity check
		if(utils.isNumeric(property_.getPropertyValue())){
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
	
	private class UIupdater extends SwingWorker<Integer, Double> {

		@Override
		protected Integer doInBackground() throws Exception {
			Double value;

			while(running_){	
				value = Double.parseDouble(property_.getPropertyValue());
				publish(value);
				
				Thread.sleep(idletime_);
			}
			return 1;
		}

		@Override
		protected void process(List<Double> chunks) {
			for(Double result : chunks){
				chart_.addPoint(result);
			}
		}
	}
}
