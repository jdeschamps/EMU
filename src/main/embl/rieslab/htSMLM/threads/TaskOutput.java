package main.embl.rieslab.htSMLM.threads;

public interface TaskOutput<T> {

	public int getNumberOutputs();
	public String[] getOutputsName();
	public void setOutputsName(String[] names);
	public void setOutput(String name, T value);
	public T getOutput(String name);
	
}
