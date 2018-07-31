package main.embl.rieslab.htSMLM.threads;

public interface Task<T> {

	public void registerHolder(TaskHolder<T> holder);
	public void startTask();
	public void stopTask();
	public boolean isRunning();
	public boolean isPausable();
	public void pauseTask();
	public void resumeTask();
	public void setSaveData(boolean savedata);
}
