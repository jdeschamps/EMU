package main.java.embl.rieslab.emu.uiexamples.htsmlm;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import main.java.embl.rieslab.emu.controller.SystemController;
import main.java.embl.rieslab.emu.ui.ConfigurableMainFrame;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.tasks.TaskHolder;

public class MainFrame extends ConfigurableMainFrame{
		/**
	 * 
	 */
	private static final long serialVersionUID = -7647811940748674013L;

	private AdditionalFiltersPanel addFiltersPanel;
	private FocusPanel focusPanel;
	private QPDPanel qpdPanel;
	private FocusLockPanel focuslockpanel;
	private FiltersPanel filterPanel;
	private LaserControlPanel[] controlPanels;
	private LaserPulsingPanel pulsePanel;
	private LaserTriggerPanel[] triggerPanels;
	private ActivationPanel activationPanel;
	private AdditionalControlsPanel addcontrolPanel;
	private AcquisitionPanel acqPanel;
	private JPanel lowerpanel;
	private JTabbedPane tab;
	@SuppressWarnings("rawtypes")
	private HashMap<String,TaskHolder> taskholders_;

	
	public MainFrame(String title, SystemController controller) {
		super(title, controller);
	}

	@Override
    protected void initComponents() {    	
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        setupPanels();
 
        this.pack(); 
        this.setResizable(false);
 	    this.setVisible(true);        
    }

    @SuppressWarnings("rawtypes")
	private void setupPanels(){
		JPanel lasers = new JPanel(); 
		controlPanels = new LaserControlPanel[4];
		lasers.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		//c.ipady = 30;
		c.gridy = 0;
		c.insets = new Insets(2,0,2,0);
		for(int i=0;i<controlPanels.length;i++){
			controlPanels[i] = new LaserControlPanel("Laser "+i);
			c.gridx = i;
			lasers.add(controlPanels[i], c);
		}

		setLayout(new BoxLayout(getContentPane(),BoxLayout.PAGE_AXIS));
		
		JPanel upperpane = new JPanel();
		upperpane.setLayout(new GridBagLayout());
		GridBagConstraints c2 = new GridBagConstraints();
		
		c2.gridx = 0;
		c2.gridy = 0;
		c2.gridwidth = 1;
		c2.gridheight = 3;
		c2.weightx = 0.2;
		c2.weighty = 0.8;
		pulsePanel = new LaserPulsingPanel("Laser 0");
		upperpane.add(pulsePanel,c2);

		c2.gridx = 1;
		c2.gridy = 0;
		c2.gridwidth = 3;
		c2.gridheight = 2;
		c2.weightx = 0.8;
		c2.weighty = 0.6;
		c2.fill = GridBagConstraints.VERTICAL;
		upperpane.add(lasers,c2);
		
		filterPanel = new FiltersPanel("Filters");
		c2.gridx = 1;
		c2.gridy = 2;
		c2.gridwidth = 3;
		c2.gridheight = 1;
		c2.weightx = 0.8;
		c2.weighty = 0.2;
		//c2.weighty = 0.1;
		c2.fill = GridBagConstraints.HORIZONTAL;
		upperpane.add(filterPanel,c2);
		
		this.add(upperpane);
		
		focusPanel = new FocusPanel("Focus");
	/*	c2.gridx = 0;
		c2.gridy = 3;
		c2.gridwidth = 4;
		c2.gridheight = 3;
		c2.weighty = 0.4;
		c2.fill = GridBagConstraints.HORIZONTAL;
		this.add(focusPanel,c2);
*/
		this.add(focusPanel);	
		
		///////////////////////////////////////////////////////////// lower panel
		lowerpanel = new JPanel();
		//GridBagConstraints c3 = new GridBagConstraints();
		lowerpanel.setLayout(new BoxLayout(lowerpanel,BoxLayout.LINE_AXIS));
		tab = new JTabbedPane();
		
		/////////// tab
		
		/// QPD tab
		qpdPanel = new QPDPanel("QPD");
		tab.add("QPD", qpdPanel);

		/// Focus-lock panel
		focuslockpanel = new FocusLockPanel("Focus-lock");
		tab.add("Focus-lock", focuslockpanel);
		
		// Activation
		activationPanel = new ActivationPanel("Activation", getCore());
		tab.add("Activation", activationPanel);
		
		/// laser trigger tab
		JPanel lasertrigg = new JPanel();
		lasertrigg.setLayout(new GridLayout(2,2));
		triggerPanels = new LaserTriggerPanel[4];
		for(int i=0;i<triggerPanels.length;i++){
			triggerPanels[i] = new LaserTriggerPanel("Laser "+i); // The "Laser #" is used in the AcquisitionPanel to discriminate lasers
			lasertrigg.add(triggerPanels[i]);
		}
		tab.add("Trigger", lasertrigg);
		
		/// Additional filters tab
		addFiltersPanel = new AdditionalFiltersPanel("Additional filters");
		tab.add("Additional filters", addFiltersPanel);
		
		/// Acquisition tab
		acqPanel = new AcquisitionPanel(getController(), this);
		tab.add("Acquisition", acqPanel);
		
		
		/*c3.gridx = 0;
		c3.gridy = 0;
		c3.gridwidth = 3;
		c3.gridheight = 3;
		c3.fill = GridBagConstraints.HORIZONTAL;*/
		lowerpanel.add(tab);

		////////// rest of the lower panel
		addcontrolPanel = new AdditionalControlsPanel("Controls");
	/*	c3.gridx = 3;
		c3.gridy = 0;
		c3.gridwidth = 1;
		c3.gridheight = 1;*/
		//c3.fill = GridBagConstraints.BOTH;
		lowerpanel.add(addcontrolPanel);
		
		/*c2.gridx = 0;
		c2.gridy = 6;
		c2.gridwidth = 4;
		c2.gridheight = 3;
		c2.weighty = 0.4;
		c2.fill = GridBagConstraints.NONE;

		this.add(lowerpanel,c2);*/
		this.add(lowerpanel);
		
		// Register property panels
        registerConfigurablePanel(focusPanel);
        registerConfigurablePanel(qpdPanel);
        registerConfigurablePanel(focuslockpanel);
        registerConfigurablePanel(filterPanel);
        registerConfigurablePanel(pulsePanel);
        for(int i=0;i<triggerPanels.length;i++){
        	registerConfigurablePanel(triggerPanels[i]);
        	registerConfigurablePanel(controlPanels[i]);
        }
        registerConfigurablePanel(addcontrolPanel);
        registerConfigurablePanel(activationPanel);
        registerConfigurablePanel(acqPanel);
        registerConfigurablePanel(addFiltersPanel);
        
        // tasks, ignore the acquisition task as it is not supposed to be called by another panel
        taskholders_ = new HashMap<String,TaskHolder>();
        taskholders_.put(activationPanel.getTaskName(), activationPanel);

    }
    
    public Point getAcquisitionPanelLocation(){
    	Point loc = this.getLocation();
    	loc.x += tab.getLocation().x+lowerpanel.getLocation().x+acqPanel.getLocation().x;
    	loc.y += tab.getLocation().y+lowerpanel.getLocation().y+acqPanel.getLocation().y;
    	
    	return loc;
    }
    
    @SuppressWarnings("rawtypes")
	public HashMap<String,TaskHolder> getTaskHolders(){
    	return taskholders_;
    }
}
