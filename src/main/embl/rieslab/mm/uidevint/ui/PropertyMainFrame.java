package main.embl.rieslab.mm.uidevint.ui;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import main.embl.rieslab.mm.uidevint.configuration.SystemConstants;
import main.embl.rieslab.mm.uidevint.controller.SystemController;
import main.embl.rieslab.mm.uidevint.ui.internalproperty.IntInternalProperty;
import main.embl.rieslab.mm.uidevint.ui.internalproperty.IntInternalPropertyValue;
import main.embl.rieslab.mm.uidevint.ui.internalproperty.InternalProperty;
import main.embl.rieslab.mm.uidevint.ui.internalproperty.InternalPropertyType;
import mmcorej.CMMCore;


public abstract class PropertyMainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6852560487523093601L;

	private ArrayList<PropertyPanel> panels_;
	private SystemController controller_;
	
	public PropertyMainFrame(String title, SystemController controller){
		controller_ = controller;
		
		panels_ = new ArrayList<PropertyPanel>();
		
        this.setTitle(title);
		
    	this.addWindowListener(new WindowAdapter() {
    	    @Override
    	    public void windowClosing(WindowEvent e) {
    	    	shutDown();
    	    }
    	});
        
    	setUpMenuBar();
		initComponents();
		linkInternalProperties();
	}
	
	private void setUpMenuBar(){
        JMenu menu;
        JMenuItem newcfg, loadcfg;  
        JMenuBar mb=new JMenuBar();  
        
        menu=new JMenu("Menu");  
        newcfg=new JMenuItem(new AbstractAction("Settings") {
            /**
			 * 
			 */
			private static final long serialVersionUID = -8992610502306964249L;

			public void actionPerformed(ActionEvent e) {
				boolean b = controller_.launchWizard();
				if(!b){
			        JOptionPane.showMessageDialog(null, "Configuration wizard already running.", "Information", JOptionPane.INFORMATION_MESSAGE);
				}
            }
        });
        
        loadcfg=new JMenuItem(new AbstractAction("Load settings") {
            /**
			 * 
			 */
			private static final long serialVersionUID = -8992610502306964249L;

			public void actionPerformed(ActionEvent e) {
				boolean b = getConfigurationFile();
				if(!b){
			        JOptionPane.showMessageDialog(null, "Not a valid configuration.", "Information", JOptionPane.INFORMATION_MESSAGE);
				}
            }
        }); 
        menu.add(newcfg); 
        menu.add(loadcfg); 
        mb.add(menu);  
        
        this.setJMenuBar(mb); 
	}
	
	private boolean getConfigurationFile(){
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("UI Configuration", SystemConstants.CONFIG_EXT);
		fileChooser.setFileFilter(filter);
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
		    File selectedFile = fileChooser.getSelectedFile();
		    return controller_.loadConfiguration(selectedFile);
		}
		return true;
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

	public CMMCore getCore(){
		return controller_.getCore();
	}
	
	public SystemController getController(){
		return controller_;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void linkInternalProperties(){
		Iterator<PropertyPanel> panelsIt = panels_.iterator();
		HashMap<String, InternalProperty> internalproperties = new HashMap<String, InternalProperty>();
		HashMap<String, InternalProperty> internalpropertiesCopy;
		while(panelsIt.hasNext()){
			internalproperties.putAll(panelsIt.next().getInternalProperties());
		}
		
		Iterator<String> propIt1 = internalproperties.keySet().iterator();
		Iterator<String> propIt2;
		String firstproperty, secondproperty;
		while(propIt1.hasNext()){
			firstproperty = propIt1.next();
			if(!internalproperties.get(firstproperty).isAllocated()){
				internalpropertiesCopy = (HashMap<String, InternalProperty>) internalproperties.clone();
				internalpropertiesCopy.remove(firstproperty);
				propIt2 = internalpropertiesCopy.keySet().iterator();
				while(propIt2.hasNext()){
					secondproperty = propIt2.next();
					if(internalproperties.get(secondproperty).getName().equals(internalproperties.get(firstproperty).getName())){
						String firstType = internalproperties.get(firstproperty).getType();
						String secondType = internalproperties.get(secondproperty).getType();
						if(firstType.equals(secondType)){
							if(firstType.equals(InternalPropertyType.INTEGER.getTypeValue())){
								IntInternalPropertyValue val = new IntInternalPropertyValue(((IntInternalProperty) internalproperties.get(firstproperty)).getDefaultValue());
								((IntInternalProperty) internalproperties.get(firstproperty)).linkValue(val);
								((IntInternalProperty) internalproperties.get(secondproperty)).linkValue(val);
							} else if(firstType.equals(InternalPropertyType.DOUBLE)){
								// implement
							} else if(firstType.equals(InternalPropertyType.STRING)){
								// implement
							}
						}
					}
				}
			}
		}
		
	}
	
	protected abstract void initComponents();
}
