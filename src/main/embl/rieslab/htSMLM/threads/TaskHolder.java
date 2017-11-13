package main.embl.rieslab.htSMLM.threads;

public interface TaskHolder<T> {

	public void update(T[] output);
	public T[] retrieveAllParameters();
	public void startTask();
	public void stopTask();
	public boolean isPausable();
	public void pauseTask();
	public void resumeTask();
	public boolean isTaskRunning();
	public String getTaskName();
	public boolean isCriterionReached();
	
	/**
	 * To be called before starting the task.
	 * 
	 */
	public void initializeTask();
	
}
