package main.embl.rieslab.htSMLM.threads;

public interface TaskHolder {

	public void notifyOutput(TaskOutput output);
	public String retrieveParameter(String parameterName);
	public String[] retrieveAllParameters();
	
}
