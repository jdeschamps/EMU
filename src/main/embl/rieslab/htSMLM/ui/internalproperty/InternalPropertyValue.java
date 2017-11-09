package main.embl.rieslab.htSMLM.ui.internalproperty;

public interface InternalPropertyValue<T> {

	public void setValue(T val, InternalProperty<T> source);
	public T getValue();
	public void registerListener(InternalProperty<T> listener);
	public void notifyListeners(InternalProperty<T> source);
	
}
