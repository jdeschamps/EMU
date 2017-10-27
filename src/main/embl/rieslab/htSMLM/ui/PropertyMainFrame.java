package main.embl.rieslab.htSMLM.ui;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;


public abstract class PropertyMainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6852560487523093601L;

	private ArrayList<PropertyPanel> panels_;
	
	public PropertyMainFrame(){
		panels_ = new ArrayList<PropertyPanel>();
		
		initComponents();
		registerPropertyPanels();
	}
	
	protected void registerBundle(PropertyPanelBundle bun){
		panels_.addAll(bun.getPanels());
	}
	
	protected void registerPropertyPanel(PropertyPanel pan){
		panels_.add(pan);
	}
	
	public ArrayList<PropertyPanel> getPropertyPanels(){
		return panels_;
	}
	
	public void updateAll(){
		Iterator<PropertyPanel> it = panels_.iterator();
		PropertyPanel pan;
		while(it.hasNext()){
			pan = it.next();
			System.out.println(pan.getLabel());
			pan.updateAllProperties();
			pan.updateAllParameters();
		}
	}
	
	public void shutDown(){
		Iterator<PropertyPanel> it = panels_.iterator();
		PropertyPanel pan;
		while(it.hasNext()){
			pan = it.next();
			pan.shutDown();
		}
		this.dispose();
	}
	
	protected abstract void initComponents();
	protected abstract void registerPropertyPanels();
}
