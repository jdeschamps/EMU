package main.java.embl.rieslab.emu.ui;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import main.java.embl.rieslab.emu.controller.SystemController;
import main.java.embl.rieslab.emu.controller.SystemDialogs;
import main.java.embl.rieslab.emu.ui.ConfigurablePanel;
import main.java.embl.rieslab.emu.ui.internalproperties.IntInternalProperty;
import main.java.embl.rieslab.emu.ui.internalproperties.IntInternalPropertyValue;
import main.java.embl.rieslab.emu.ui.internalproperties.InternalProperty;
import main.java.embl.rieslab.emu.ui.internalproperties.InternalPropertyType;
import main.java.embl.rieslab.emu.ui.uiparameters.UIParameter;
import main.java.embl.rieslab.emu.ui.uiproperties.UIProperty;
import mmcorej.CMMCore;

/**
 * 
 * 
 * @author Joran Deschamps
 *
 */
public abstract class ConfigurableMainFrame extends JFrame implements ConfigurableFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6852560487523093601L;

	private ArrayList<ConfigurablePanel> panels_;
	private SystemController controller_;
    private JMenu switch_plugin, switch_configuration;
	private HashMap<String, UIProperty> properties_;
	@SuppressWarnings("rawtypes")
	private HashMap<String, UIParameter> parameters_;
	
	/**
	 * Constructor. It sets up the JMenu bar containing the menu items for launching the ConfigurationWizard and switching
	 * plugin or configuration. It then calls the abstract method {@link #initComponents()}. Finally, it links the internal
	 * properties together based on their name and retrieves all UIProperties and UIParameters from the ConfigurablePanels
	 * instantiated and registered using {@link #registerConfigurablePanel(ConfigurablePanel)} in {@link #initComponents()}.
	 * 
	 * @param title Title of the frame
	 * @param controller EMU system controller
	 */
	public ConfigurableMainFrame(String title, SystemController controller){
		controller_ = controller;
		
		panels_ = new ArrayList<ConfigurablePanel>();
		
        this.setTitle(title);
		
    	this.addWindowListener(new WindowAdapter() {
    	    @Override
    	    public void windowClosing(WindowEvent e) {
    	    	shutDownAllConfigurablePanels();
    	    }
    	});

        setUpMenu();
		initComponents();
		linkInternalProperties();
		retrieveUIPropertiesAndParameters();
	}

	private void setUpMenu() {
		JMenuBar mb=new JMenuBar(); 
        
		JMenu menu = new JMenu("Menu");
		JMenuItem wiz = new JMenuItem(new AbstractAction("Settings Wizard") {
			private static final long serialVersionUID = -8992610502306964249L;

			public void actionPerformed(ActionEvent e) {
				boolean b = controller_.launchWizard();
				if (!b) {
					JOptionPane.showMessageDialog(null,
							"Configuration wizard already running.",
							"Information", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});

		switch_plugin = new JMenu("Switch plugin");
		switch_configuration = new JMenu("Switch configuration");
		
		JMenuItem about = new JMenuItem(new AbstractAction("About") {
			private static final long serialVersionUID = -5519431309736542210L;

			public void actionPerformed(ActionEvent e) {
				SystemDialogs.showAboutEMU();
			}
		});
		
		menu.add(wiz);
		menu.add(switch_plugin);
		menu.add(switch_configuration);
		menu.add(about);
		mb.add(menu);
        
        this.setJMenuBar(mb); 
	}

	// last time I simplified this part everything broke down. The UIProperties do not contain the name of the panel in their names so collision can happen
	@SuppressWarnings("rawtypes")
	private void retrieveUIPropertiesAndParameters() {
		properties_ = new HashMap<String,UIProperty>();
		parameters_ = new HashMap<String,UIParameter>();
		
		Iterator<ConfigurablePanel> it = this.getConfigurablePanels().iterator();
		ConfigurablePanel pan;
		
		while (it.hasNext()) { // loops over the PropertyPanel contained in the MainFrame
			pan = it.next();
			
			// adds all the UIProperties, since their name contains their parent PropertyPanel name
			// there is no collision <------------ Not true (split personnality comments), this depends on the panel?!
			properties_.putAll(pan.getUIProperties()); 
			 
			// But here the UIParameters don't have the same hash!!!! So what is the point here:
			
			// adds all the UIParameters, in case of collision the first UIParameter has priority
			// and substituted to the second UIParameter in its owner PropertyPanel: "same name" = "same parameter"
			HashMap<String,UIParameter> panparam = pan.getUIParameters();
			Iterator<String> paramit = panparam.keySet().iterator();
			ArrayList<String> subst = new ArrayList<String>();
			while(paramit.hasNext()){
				String param = paramit.next();
				
				if(!parameters_.containsKey(param)){ // if param doesn't exist already, adds it
					parameters_.put(param, panparam.get(param));
				} else if(parameters_.get(param).getType().equals(panparam.get(param).getType())){
					// if it already exists and the new parameter is of the same type than the one
					// previously added to the HashMap, then add to array subst
					subst.add(param);
				} 
				// if it is not of the same type, it is then ignored
			}
			// avoid concurrent modification of the hashmap, by substituting the UIParameter in the 
			// second PropertyPanel
			for(int i=0;i<subst.size();i++){
				pan.substituteParameter(subst.get(i), parameters_.get(subst.get(i)));
			}
		}	
	}
	
	/**
	 * Adds {@code configurablePanel} to the internal List of {@link ConfigurablePanel}s.
	 * 
	 * @param configurablePanel ConfigurablePanel to add
	 */
	protected void registerConfigurablePanel(ConfigurablePanel configurablePanel){
		panels_.add(configurablePanel);
	}
	
	/**
	 * Returns the List of {@link ConfigurablePanel}s.
	 */
	public ArrayList<ConfigurablePanel> getConfigurablePanels(){
		return panels_;
	}
	
	/**
	 * Updates all properties and parameters from each known ConfigurablePanel by calling 
	 * {@link ConfigurablePanel#updateAllProperties()} and {@link ConfigurablePanel#updateAllParameters()}
	 * on each panel.
	 */
	public void updateAllConfigurablePanels(){
		Iterator<ConfigurablePanel> it = panels_.iterator();
		ConfigurablePanel pan;
		while(it.hasNext()){
			pan = it.next();

			pan.updateAllProperties();
			pan.updateAllParameters();
		}
	}
	
	/**
	 * Shuts down all ConfigurablePanels by calling {@link ConfigurablePanel#shutDown()} on
	 * each panel.
	 * 
	 */
	public void shutDownAllConfigurablePanels(){
		Iterator<ConfigurablePanel> it = panels_.iterator();
		ConfigurablePanel pan;
		while(it.hasNext()){
			pan = it.next();
			pan.shutDown();
		}
		this.dispose();
	}

	/**
	 * Provides access the Micro-Manager CMMCore to subclasses.
	 * 
	 * @return Micro-Manager CMMCore.
	 */
	protected CMMCore getCore(){
		return controller_.getCore();
	}
	
	/**
	 * Returns the EMU system controller.
	 * 
	 * @return EMU SystemController.
	 */
	protected SystemController getController(){
		return controller_;
	}
	
	// this has to be reviewed
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void linkInternalProperties(){
		Iterator<ConfigurablePanel> panelsIt = panels_.iterator();
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
			if(!internalproperties.get(firstproperty).isAllocated()){ // should allow more than one to be mapped with it?
				internalpropertiesCopy = (HashMap<String, InternalProperty>) internalproperties.clone(); // seems a bit stupid to always clone a hashmap. 
				// I guess it is to remove the first one and check all the others
				internalpropertiesCopy.remove(firstproperty);
				propIt2 = internalpropertiesCopy.keySet().iterator();
				while(propIt2.hasNext()){
					secondproperty = propIt2.next();
					if(internalproperties.get(secondproperty).getName().equals(internalproperties.get(firstproperty).getName())){ // they are linked if they have the same name. How are they indexed?
						String firstType = internalproperties.get(firstproperty).getType();
						String secondType = internalproperties.get(secondproperty).getType();
						if(firstType.equals(secondType)){
							if(firstType.equals(InternalPropertyType.INTEGER.getTypeValue())){
								IntInternalPropertyValue val = new IntInternalPropertyValue(((IntInternalProperty) internalproperties.get(firstproperty)).getDefaultValue());
								((IntInternalProperty) internalproperties.get(firstproperty)).linkValue(val);
								((IntInternalProperty) internalproperties.get(secondproperty)).linkValue(val);
							} else if(firstType.equals(InternalPropertyType.DOUBLE.getTypeValue())){
								// TODO
							} else if(firstType.equals(InternalPropertyType.STRING.getTypeValue())){
								// TODO
							}
						}
					}
				}
			}
		}
		
	}
	
	/**
	 * Updates the JMenu. In particular, the menu gives access to the other known plugins and configurations 
	 * than the ones currently loaded. This method is called by the {@link main.java.embl.rieslab.emu.controller.SystemController}.
	 */
	public void updateMenu() {
		switch_plugin.removeAll();
		final String[] plugins = controller_.getOtherPluginsList();
		for (int i = 0; i < plugins.length; i++) {
			final int index = i;
			JMenuItem item = new JMenuItem(new AbstractAction(plugins[index]) {
				private static final long serialVersionUID = -4996932088885254278L;

				public void actionPerformed(ActionEvent e) {
					controller_.loadPlugin(plugins[index]);
				}
			});
			switch_plugin.add(item);
		}
		
		switch_configuration.removeAll();
		final String[] confs = controller_.getOtherCompatibleConfigurationsList();
		for (int i = 0; i < confs.length; i++) {
			final int index = i;
			JMenuItem item = new JMenuItem(new AbstractAction(confs[index]) {
				private static final long serialVersionUID = -4996932088885254278L;

				public void actionPerformed(ActionEvent e) {
					controller_.loadConfiguration(confs[index]);
				}
			});
			switch_configuration.add(item);
		}
	}

	/**
	 * Returns the Map of UIProperties indexed by their name.
	 */
	@Override
	public HashMap<String, UIProperty> getUIProperties() {
		return properties_;
	}
	
	/**
	 * Returns the Map of UIParameters indexed by their hash.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public HashMap<String, UIParameter> getUIParameters() {
		return parameters_;
	}

	/**
	 * Sets-up the ConfigurableMainFrame subclass. This method is called in the ConfigurableMainFrame constructor.
	 * In this method, the subclasses should instantiate their ConfigurablePanels and register them using 
	 * {@link #registerConfigurablePanel(ConfigurablePanel)}.
	 */
	protected abstract void initComponents();

}
