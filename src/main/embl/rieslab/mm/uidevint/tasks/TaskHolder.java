package main.embl.rieslab.mm.uidevint.tasks;

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
	@SuppressWarnings("rawtypes")
	public Task getTask();
	public void taskDone();
	
	/**
	 * To be called before starting the task.
	 * 
	 */
	public void initializeTask();
	
}
