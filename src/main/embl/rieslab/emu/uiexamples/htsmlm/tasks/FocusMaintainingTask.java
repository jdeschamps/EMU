package main.embl.rieslab.emu.uiexamples.htsmlm.tasks;


import main.embl.rieslab.emu.tasks.Task;
import main.embl.rieslab.emu.tasks.TaskHolder;
import main.embl.rieslab.emu.ui.uiproperties.UIProperty;

public class FocusMaintainingTask implements Task<Double> {
	
	private TaskHolder<Double> holder_;
	private int idletime_;
	private PID worker_;
	private boolean running_;
	private UIProperty signal_, target_;
	
	private double Kp, Ki, Kd, setpoint, scaling_;
	
	public FocusMaintainingTask(TaskHolder<Double> holder, UIProperty signal, UIProperty target, 
			int idle, double Kp, double Ki, double Kd, double setpoint, double scaling){
		running_ = false;
		this.signal_ = signal;
		this.target_ = target;
		idletime_ = idle;
		
		registerHolder(holder);
				
		this.Kp = Kp;
		this.Ki = Ki;
		this.Kd = Kd;
		this.setpoint = setpoint;
	}
	
	@Override
	public void registerHolder(TaskHolder<Double> holder) {
		holder_ = holder;
	}

	@Override
	public void startTask() {
		worker_ = new PID();
		running_ = true;
		worker_.run();
	}

	@Override
	public void stopTask() {
		running_ = false;
	}

	@Override
	public boolean isRunning() {
		return running_;
	}

	public void setIdleTime(int idle){
		idletime_ = idle;
	}
	
	@Override
	public boolean isPausable() {
		return false;
	}

	@Override
	public void pauseTask() {
		// do nothing
	}

	@Override
	public void resumeTask() {
		// do nothing
	}

	@Override
	public void notifyHolder(Double[] outputs) {
		holder_.update(outputs);
	}
	
	private class PID extends Thread {
		
		@Override
		public void run() {
			double prev_err = 0;
			double integ = 0;
			double error, derivative;
			double newpos;
			
			while(running_){ 
				double pos = Double.parseDouble(signal_.getPropertyValue());
				
				error = setpoint - pos;
				integ = integ + error * idletime_ / 1000;
				derivative = 1000*(error - prev_err) / idletime_;
						  
				newpos = (Kp * error + Ki * integ + Kd * derivative - setpoint) / scaling_; // to get -1-1 um for the controller
				prev_err = error;
				
				target_.setPropertyValue(String.valueOf(newpos));
				
				try {
					Thread.sleep(idletime_);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
