package main.java.embl.rieslab.emu.uiexamples.htsmlm.tasks;

public interface Task<T> {

	public void registerHolder(TaskHolder<T> holder);
	public void notifyHolder(T[] outputs);
	public void startTask();
	public void stopTask();
	public boolean isRunning();
	public boolean isPausable();
	public void pauseTask();
	public void resumeTask();
}
