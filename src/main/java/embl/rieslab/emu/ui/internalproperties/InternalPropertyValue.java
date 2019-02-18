package main.java.embl.rieslab.emu.ui.internalproperties;

public interface InternalPropertyValue<T> {

	public void setValue(T val, InternalProperty<T> source);
	public T getValue();
	public void registerListener(InternalProperty<T> listener);
	public void notifyListeners(InternalProperty<T> source);
	
}
