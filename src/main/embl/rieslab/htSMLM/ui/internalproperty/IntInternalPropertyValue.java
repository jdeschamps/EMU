package main.embl.rieslab.htSMLM.ui.internalproperty;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class IntInternalPropertyValue implements InternalPropertyValue<Integer>{

	private AtomicInteger value_;
	private ArrayList<InternalProperty<Integer>> listeners_;
	
	public IntInternalPropertyValue(int defaultvalue){
		value_ = new AtomicInteger(defaultvalue);
		listeners_ = new ArrayList<InternalProperty<Integer>>();
	}
	
	@Override
	public void setValue(Integer val, InternalProperty<Integer> source) {		
		System.out.println("New value "+val);

		value_.set(val);		
		System.out.println("New value confirmation "+value_.get());
		notifyListeners(source);
	}

	@Override
	public Integer getValue() {
		return value_.get();
	}

	@Override
	public void registerListener(InternalProperty<Integer> listener) {
		listeners_.add(listener);
	}

	@Override
	public void notifyListeners(InternalProperty<Integer> source) {
		for(int i=0;i<listeners_.size();i++){
			if(listeners_.get(i) != source){
				listeners_.get(i).notifyOwner();
			}
		}
	}

}
