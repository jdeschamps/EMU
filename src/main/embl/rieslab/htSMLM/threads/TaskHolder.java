package main.embl.rieslab.htSMLM.threads;

public interface TaskHolder {

	public void update(Double[] output);
	public double[] retrieveAllParameters();
	public void startTask();
	public void stopTask();
	public boolean isPausable();
	public void pauseTask();
	public void resumeTask();
	public boolean isTaskRunning();
	
}
