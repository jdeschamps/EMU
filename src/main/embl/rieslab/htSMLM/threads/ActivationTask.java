package main.embl.rieslab.htSMLM.threads;

import java.util.List;

import javax.swing.SwingWorker;

import mmcorej.CMMCore;

public class ActivationTask implements Task {

	public static int PARAM_SDCOEFF = 0;
	public static int PARAM_FEEDBACK = 1;
	public static int PARAM_CUTOFF = 2;
	public static int PARAM_AUTOCUTOFF = 3;
	public static int PARAM_PULSE = 4;
	public static int PARAM_MAXPULSE = 5;
	private static String[] PARAMS = {"Sd coeff","Feedback","Cut-off","Auto cut-off","Pulse","Maximum pulse"};
	
	private CMMCore core_;
	private TaskHolder holder_;
	private int idletime_;
	private AutomatedActivation worker_;
	private boolean running_ = false;
	private ActivationOutput output_;
	
	public ActivationTask(TaskHolder holder, CMMCore core, int idle){
		core_ = core;
		idletime_ = idle;
		registerHolder(holder);
		output_ = new ActivationOutput();
		
	}
	
	@Override
	public String[] getParametersName() {
		return PARAMS;
	}

	@Override
	public void registerHolder(TaskHolder holder) {
		holder_ = holder;
	}

	@Override
	public int getSizeOutputs() {
		return PARAMS.length;
	}

	@Override
	public int getSizeParameters() {
		return PARAMS.length;
	}

	@Override
	public void startTask() {
		worker_ = new AutomatedActivation();
		worker_.execute();
		running_ = true;
	}

	@Override
	public void stopTask() {
		running_ = false;
	}

	@Override
	public boolean isRunning() {
		return running_;
	}
	
	private class AutomatedActivation extends SwingWorker<Integer, String> {

		@Override
		protected Integer doInBackground() throws Exception {
			String[] params;
			
			while(running_){
				params = holder_.retrieveAllParameters();
				
				
				
				publish(property_.getPropertyValue());
				
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
