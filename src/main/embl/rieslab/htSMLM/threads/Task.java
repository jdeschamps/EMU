package main.embl.rieslab.htSMLM.threads;

public interface Task {

	public String[] getParametersName();
	public void registerHolder(TaskHolder holder);
	public int getSizeOutputs();
	public int getSizeParameters();
	public void startTask();
	public void stopTask();
	public boolean isRunning();
	public TaskOutput getOutput();
}
