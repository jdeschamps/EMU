package main.java.de.embl.rieslab.emu.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.GraphicsEnvironment;
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

import main.java.de.embl.rieslab.emu.controller.SystemController;
import main.java.de.embl.rieslab.emu.controller.SystemDialogs;
import main.java.de.embl.rieslab.emu.ui.ConfigurablePanel;
import main.java.de.embl.rieslab.emu.ui.internalproperties.InternalProperty;
import main.java.de.embl.rieslab.emu.ui.uiparameters.UIParameter;
import main.java.de.embl.rieslab.emu.ui.uiproperties.UIProperty;
import mmcorej.CMMCore;

/**
 * Class representing the main JFrame of a {@link main.java.de.embl.rieslab.emu.plugin.UIPlugin}. Subclasses must
 * implement the {@link #initComponents()} method, in which the {@link ConfigurablePanel}s must be instantiated
 * and added the same way a JPanel is added to a JFrame.
 * <p>
 * The ConfigurableMainFrame aggregates the UIParameters and the UIProperties, as well as linking together the
 * InternalProperties. If two UIProperties have the same name, then the last added UIproperty will replace the 
 * first ones. The order is the order of discovery while going through the componenents of the JFrame. 
 * <p>
 * For UIParameters, on the other hand, two UIParameters are allowed to have the same hash ({ConfigurablePanel name}-{UIParameter name})
 * only if they have the same type. Should such case arise, all UIParameters but the first one to appear (in order
 * of registration of the ConfigurablePanel that owns it) are replaced in their owner ConfigurablePanel by the 
 * first UIParameter. There, UIParameters with same hash and type are made replaced by a single reference and are
 * shared by all the corresponding ConfigurationPanel. Note that if two UIParameters have same name but different
 * types, the second one to appear is ignored altogether.
 * <p>
 * The same idea applies to InternalProperties.
 * 
 * @author Joran Deschamps
 *
 */
public abstract class ConfigurableMainFrame extends JFrame implements ConfigurableFrame{

	private static final long serialVersionUID = -6852560487523093601L;

	private ArrayList<ConfigurablePanel> panels_;
	private SystemController controller_;
    private JMenu switch_plugin, switch_configuration;
	private HashMap<String, UIProperty> properties_;
	@SuppressWarnings("rawtypes")
	private HashMap<String, UIParameter> parameters_;
	
	/**
	 * Constructor, it sets up the JMenu, calls {@link #initComponents()} from the subclass, then links InternaProperties and
	 * gather UIPropertiers and UIParameters.
	 * 
	 * @param title Title of the frame
	 * @param controller EMU system controller
	 */
	public ConfigurableMainFrame(String title, SystemController controller){
		controller_ = controller;
				
        this.setTitle(title);
		
    	this.addWindowListener(new WindowAdapter() {
    	    @Override
    	    public void windowClosing(WindowEvent e) {
    	    	shutDownAllConfigurablePanels();
    	    }
    	});

        setUpMenu();
		initComponents();		
		
		GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
		this.setLocation(g.getCenterPoint().x-this.getWidth()/2,g.getCenterPoint().y-this.getHeight()/2);
		this.setVisible(true);
		
		panels_ = listConfigurablePanels(this.getContentPane().getComponents(), new ArrayList<ConfigurablePanel>());
		
		linkInternalProperties();
		retrieveUIPropertiesAndParameters();
	}

	private ArrayList<ConfigurablePanel> listConfigurablePanels(Component[] c, ArrayList<ConfigurablePanel> list) {
		if(list == null) {
			throw new NullPointerException();
		}
				
		for(int i=0;i<c.length;i++) {
			if(c[i] instanceof ConfigurablePanel) {
				list.add((ConfigurablePanel) c[i]);
			} else if(c[i] instanceof Container) {
				listConfigurablePanels(((Container) c[i]).getComponents(), list);
			}
		}
		
		return list;
	}

	/**
	 * Sets up the menu bar.
	 */
	protected void setUpMenu() {
		JMenuBar mb=new JMenuBar(); 
        
		JMenu menu = new JMenu("Menu");
		JMenuItem wiz = new JMenuItem(new AbstractAction("Settings Wizard") {
			private static final long serialVersionUID = -8992610502306964249L;

			public void actionPerformed(ActionEvent e) {
				boolean b = controller_.launchWizard();
				if (!b ) {
					showWizardRunning();
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

	protected void showWizardRunning() {
		JOptionPane.showMessageDialog(null,
				"Configuration wizard already running.",
				"Information", JOptionPane.INFORMATION_MESSAGE);
	}

	@SuppressWarnings("rawtypes")
	private void retrieveUIPropertiesAndParameters() {
		properties_ = new HashMap<String,UIProperty>();
		parameters_ = new HashMap<String,UIParameter>();
		
		Iterator<ConfigurablePanel> it = this.getConfigurablePanels().iterator();
		ConfigurablePanel pan;
		
		while (it.hasNext()) { // loops over the PropertyPanel contained in the MainFrame
			pan = it.next();
			
			// adds all the UIProperties, if there is collision the last one is kept. Thus, when writing
			// a plugin, one needs to be aware of this.
			properties_.putAll(pan.getUIProperties()); 
			 
			
			// adds all the UIParameters, in case of collision the first UIParameter has priority
			// and substituted to the second UIParameter in its owner PropertyPanel: "same name" = "same parameter"
			// Because UIParameters do not update their ConfigurablePanel owner by themselves, then by doing so
			// we obtain a shared Parameter.
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
	
	
	@SuppressWarnings("rawtypes")
	private void linkInternalProperties(){
		HashMap<String, InternalProperty> allinternalprops, panelinternalprops, tempinternalprops;
		allinternalprops = new HashMap<String, InternalProperty>();
		tempinternalprops = new HashMap<String, InternalProperty>();
		Iterator<ConfigurablePanel> panelsIt = panels_.iterator();
		
		while(panelsIt.hasNext()) { // iterate over panels
			tempinternalprops.clear();
			ConfigurablePanel pane = panelsIt.next();
			panelinternalprops = pane.getInternalProperties();
			
			Iterator<String> propsit = panelinternalprops.keySet().iterator();
			while(propsit.hasNext()) { // iterate over one panel's internal props
				String internalprop = propsit.next();
				if(allinternalprops.containsKey(internalprop)) { // if the internal property already exist
					// add to a temporary HashMap, and will take care of them later to avoid a concurrent modifications of panelinternalprops
					tempinternalprops.put(internalprop, panelinternalprops.get(internalprop));
				} else {
					allinternalprops.put(internalprop, panelinternalprops.get(internalprop));
				}
			}
			
			// Now substitute all the internal properties from the temporary HashMap with the internal
			// property already in allinternalprops. So far they have the same name, but could have
			// different type. In the following calls, if the properties have different type, then nothing
			// will happen. In this case, we just ignore it. Doing it here at the end avoids concurrent 
			// modification of the ConfigurablePanel hashmap.
			propsit = tempinternalprops.keySet().iterator();
			while(propsit.hasNext()) {
				allinternalprops.get(propsit.next()).registerListener(pane);
			}
		}

		
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
	 * Adds all listeners to the JComponents.
	 */
	public void addAllListeners() {
		Iterator<ConfigurablePanel> it = panels_.iterator();
		ConfigurablePanel pan;
		while(it.hasNext()){
			pan = it.next();

			pan.addComponentListeners();
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
	
	/**
	 * Updates the JMenu, called when loading a new Plugin or a new Configuration. 
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
	 * Sets-up the frame, in this method the subclasses should instantiate the ConfigurablePanels and add them
	 * to the ConfigurableMainFrame.
	 */
	protected abstract void initComponents();



}
