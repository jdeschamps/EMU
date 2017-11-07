package main.embl.rieslab.htSMLM.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import main.embl.rieslab.htSMLM.controller.SystemController;

public class MainFrame extends PropertyMainFrame{
	
	public MainFrame(String title, SystemController controller) {
		super(title, controller);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -7647811940748674013L;

	public FocusPanel focusPanel;
	public QPDPanel qpdPanel;
	public FiltersPanel filterPanel;
	public LaserControlPanel[] controlPanels;
	public LaserPulsingPanel pulsePanel;
	public LaserTriggerPanel[] triggerPanels;
	public ActivationPanel activationPanel;
	public AdditionalControlsPanel addcontrolPanel;
    
    protected void initComponents() {
    	
    	System.out.println("Is the MainFrame setting up running on the EDT: "+SwingUtilities.isEventDispatchThread());
    	
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
 
        this.pack(); // avoid packing when one can
        this.setResizable(false);
 	    this.setVisible(true);        
    }

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
		pulsePanel = new LaserPulsingPanel("UV pulse");
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
		JPanel lowerpanel = new JPanel();
		//GridBagConstraints c3 = new GridBagConstraints();
		lowerpanel.setLayout(new BoxLayout(lowerpanel,BoxLayout.LINE_AXIS));
		JTabbedPane tab = new JTabbedPane();
		
		/////////// tab
		qpdPanel = new QPDPanel("QPD");
		tab.add("QPD", qpdPanel);

		activationPanel = new ActivationPanel("Activation", getCore());
		tab.add("Activation", activationPanel);
		
		/// laser trigger
		JPanel lasertrigg = new JPanel();
		lasertrigg.setLayout(new GridLayout(2,2));
		triggerPanels = new LaserTriggerPanel[4];
		for(int i=0;i<triggerPanels.length;i++){
			triggerPanels[i] = new LaserTriggerPanel("Laser "+i);
			lasertrigg.add(triggerPanels[i]);
		}
		tab.add("Trigger", lasertrigg);
		
		/*c3.gridx = 0;
		c3.gridy = 0;
		c3.gridwidth = 3;
		c3.gridheight = 3;
		c3.fill = GridBagConstraints.HORIZONTAL;*/
		lowerpanel.add(tab);

		////////// rest of the lower panel
		addcontrolPanel = new AdditionalControlsPanel("Servos");
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
    }
    
	@Override
	protected void registerPropertyPanels() {
        registerPropertyPanel(focusPanel);
        registerPropertyPanel(qpdPanel);
        registerPropertyPanel(filterPanel);
        registerPropertyPanel(pulsePanel);
        for(int i=0;i<triggerPanels.length;i++){
        	registerPropertyPanel(triggerPanels[i]);
        	registerPropertyPanel(controlPanels[i]);
        }
        registerPropertyPanel(addcontrolPanel);
        registerPropertyPanel(activationPanel);
	}
}
