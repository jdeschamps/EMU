package main.embl.rieslab.htSMLM.ui;

import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JPanel;

import main.embl.rieslab.htSMLM.ui.uiparameters.UIParameter;
import main.embl.rieslab.htSMLM.ui.uiproperties.UIProperty;

@SuppressWarnings("rawtypes")
public abstract class PropertyPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7664471329228929184L;

	private HashMap<String, UIProperty> properties_; // find a faster way to access the props by name?
	private HashMap<String, UIParameter> parameters_;
	
	private String label_;
	
	public PropertyPanel(String label){
		label_ = label;
		
		properties_ = new HashMap<String,UIProperty>();
		parameters_ = new HashMap<String,UIParameter>();
		
		initializeProperties();
		initializeParameters();
		setupPanel();
	}

	public HashMap<String, UIProperty> getUIProperties(){
		return properties_;
	}
	public HashMap<String,UIParameter> getUIParameters(){
		return parameters_;
	}
	
	public UIProperty getUIProperty(String name){
		if(properties_.containsKey(name)){
			return properties_.get(name);
		}
		return null;
	}	
	
	public String getUIPropertyValue(String name){
		return getUIProperty(name).getPropertyValue();
	}
	
	public UIParameter getUIParameter(String name){
		Iterator<String> it = parameters_.keySet().iterator();
		UIParameter param;
		while(it.hasNext()){
			param = parameters_.get(it.next());
			if(param.getLabel().equals(name)){
				return param;
			}
		}
		return null;
	}
	
	protected void addUIProperty(UIProperty p){
		properties_.put(p.getName(),p);
	}	
	
	protected void addUIParameter(UIParameter p){
		parameters_.put(p.getHash(),p);
	}
	
	public void updateAllProperties(){
		Iterator<String> it = properties_.keySet().iterator();
		String prop;
		while(it.hasNext()){
			prop = it.next();
			propertyhasChanged(prop,properties_.get(prop).getPropertyValue());
		}
	}	
	
	public void updateAllParameters(){
		Iterator<String> it = parameters_.keySet().iterator();
		while(it.hasNext()){
			parameterhasChanged(parameters_.get(it.next()).getLabel());
		}
	}
	
	public String getLabel(){
		return label_;
	}

	protected abstract void initializeProperties();
	protected abstract void initializeParameters();
	public abstract void setupPanel();
	protected abstract void changeProperty(String name, String value);
	public abstract void propertyhasChanged(String name, String newvalue);
	public abstract void parameterhasChanged(String label);
	public abstract void shutDown();
	
}
