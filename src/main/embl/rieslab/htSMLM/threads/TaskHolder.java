package main.embl.rieslab.htSMLM.threads;

public interface TaskHolder {

	public void update(Double[] output);
	public double[] retrieveAllParameters();
	
}
