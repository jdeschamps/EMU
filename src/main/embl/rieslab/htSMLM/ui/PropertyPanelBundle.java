package main.embl.rieslab.htSMLM.ui;

import java.util.ArrayList;
import javax.swing.JPanel;


public abstract class PropertyPanelBundle extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8286579225244576665L;
	
	private ArrayList<PropertyPanel> panels_;
	
	public PropertyPanelBundle(){
		panels_ =  new ArrayList<PropertyPanel>();
		
		initComponents();
		registerPropertyPanels();
	}
	
	protected void registerPanel(PropertyPanel pan){
		panels_.add(pan);
	}
	
	protected void registerBundle(PropertyPanelBundle bun){
		panels_.addAll(bun.getPanels());
	}
	
	public ArrayList<PropertyPanel> getPanels(){
		return panels_;
	}

	protected abstract void initComponents();
	protected abstract void registerPropertyPanels();
}
