package main.embl.rieslab.htSMLM.ui;

import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JPanel;

import main.embl.rieslab.htSMLM.ui.internalproperty.InternalProperty;
import main.embl.rieslab.htSMLM.ui.uiparameters.UIParameter;
import main.embl.rieslab.htSMLM.ui.uiproperties.UIProperty;

@SuppressWarnings("rawtypes")
public abstract class PropertyPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7664471329228929184L;

	private HashMap<String, UIProperty> properties_; 
	private HashMap<String, UIParameter> parameters_;
	private HashMap<String, InternalProperty> internalprops_;

	private String label_;
	
	private boolean propertychange_ = true;
	
	public PropertyPanel(String label){
		label_ = label;
		
		properties_ = new HashMap<String,UIProperty>();
		parameters_ = new HashMap<String,UIParameter>();
		internalprops_ = new HashMap<String, InternalProperty>();
		
		initializeProperties();
		initializeParameters();
		initializeInternalProperties();
		setupPanel();
	}

	public HashMap<String, UIProperty> getUIProperties(){
		return properties_;
	}
	
	public HashMap<String, InternalProperty> getInternalProperties(){
		return internalprops_;
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
	
	public InternalProperty getInternalProperty(String name){
		if(internalprops_.containsKey(getLabel()+" "+name)){
			return internalprops_.get(getLabel()+" "+name);
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
	
	protected void addInternalProperty(InternalProperty p){
		internalprops_.put(p.getHash(),p);
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

	public void substituteParameter(String param, UIParameter uiParameter) {
		parameters_.remove(param);
		parameters_.put(param, uiParameter);
	}
	
	public boolean isPropertyChangeAllowed(){
		return propertychange_;
	}

	public void turnOffPropertyChange(){
		propertychange_ = false;
	}

	public void turnOnPropertyChange(){
		propertychange_ = true;
	}
	
	protected abstract void initializeProperties();
	protected abstract void initializeInternalProperties();
	protected abstract void initializeParameters();
	public abstract void setupPanel();
	protected abstract void changeProperty(String name, String value);
	protected abstract void changeInternalProperty(String name, String value);
	public abstract void propertyhasChanged(String name, String newvalue);
	public abstract void parameterhasChanged(String label);
	public abstract void internalpropertyhasChanged(String label);
	public abstract void shutDown();
	public abstract String getDescription();

}
