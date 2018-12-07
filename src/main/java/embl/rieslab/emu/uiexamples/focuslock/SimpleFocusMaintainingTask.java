package main.java.embl.rieslab.emu.uiexamples.focuslock;


import javax.swing.SwingUtilities;

import main.java.embl.rieslab.emu.tasks.Task;
import main.java.embl.rieslab.emu.tasks.TaskHolder;
import main.java.embl.rieslab.emu.ui.uiproperties.UIProperty;

public class SimpleFocusMaintainingTask implements Task<Double> {
	
	private TaskHolder<Double> holder_;
	private int idletime_;
	private PI worker_;
	private boolean running_;
	private UIProperty signal_, target_;
	
	private double Kp, Ki, setpoint, scaling_;
	
	public SimpleFocusMaintainingTask(TaskHolder<Double> holder, UIProperty signal, UIProperty target, 
			int idle, double Kp, double Ki, double setpoint, double scaling){
		running_ = false;
		this.signal_ = signal;
		this.target_ = target;
		idletime_ = idle;
		
		registerHolder(holder);
				
		this.Kp = Kp;
		this.Ki = Ki;
		this.setpoint = setpoint;
	}
	
	@Override
	public void registerHolder(TaskHolder<Double> holder) {
		holder_ = holder;
	}

	@Override
	public void startTask() {
		worker_ = new PI();
		running_ = true;
		worker_.start();
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
	
	private class PI extends Thread {
		
		@Override
		public void run() {
			double integ = 0;
			double error;
			double newpos;
			
			while(running_){ 
				System.out.println("tadaaa "+SwingUtilities.isEventDispatchThread());
				double pos = Double.parseDouble(signal_.getPropertyValue());
				
				error = setpoint - pos;
				integ = integ + error; // dt = 1
				newpos = (Kp * error + Ki * integ - setpoint) / scaling_; // to get -1-1 um for the controller
				
				double stagepos = Double.valueOf(target_.getPropertyValue()) + newpos;
				target_.setPropertyValue(String.valueOf(stagepos));
				
				try {
					Thread.sleep(idletime_);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
