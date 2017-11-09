package main.embl.rieslab.htSMLM.threads;

public interface Task {

	public void registerHolder(TaskHolder holder);
	public void startTask();
	public void stopTask();
	public boolean isRunning();
	public void notifyHolder(Double[] outputs);
	public boolean isPausable();
	public void pauseTask();
	public void resumeTask();
}
