package main.java.embl.rieslab.emu.uiexamples.lasers;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import main.java.embl.rieslab.emu.controller.SystemController;
import main.java.embl.rieslab.emu.ui.ConfigurableMainFrame;

public class LasersMainFrame extends ConfigurableMainFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1629769507154360466L;
	
	private LaserControlPanel[] controlPanels;
	
	public LasersMainFrame(String title, SystemController controller) {
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
            java.util.logging.Logger.getLogger(LasersMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LasersMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LasersMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LasersMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        setupPanels();
 
        this.pack(); 
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
		this.add(lasers);
	
        for(int i=0;i<controlPanels.length;i++){
        	registerPropertyPanel(controlPanels[i]);
        }
    }
   
}
