package main.java.embl.rieslab.emu.uiexamples.focuslock;

import javax.swing.BoxLayout;

import main.java.embl.rieslab.emu.controller.SystemController;
import main.java.embl.rieslab.emu.ui.ConfigurableMainFrame;

public class FocusLockMainFrame extends ConfigurableMainFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1629769507154360466L;

	private PIFocusPanel focusPanel;
	private QPDPanel qpdPanel;
	
	public FocusLockMainFrame(String title, SystemController controller) {
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
            java.util.logging.Logger.getLogger(FocusLockMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FocusLockMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FocusLockMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FocusLockMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        setupPanels();
 
        this.pack(); 
        this.setResizable(false);
 	    this.setVisible(true);        
    }

    private void setupPanels(){
		setLayout(new BoxLayout(getContentPane(),BoxLayout.PAGE_AXIS));
		
		focusPanel = new PIFocusPanel("PI focus lock");
		qpdPanel = new QPDPanel("QPD panel");
		this.add(focusPanel);
		this.add(qpdPanel);

       	registerPropertyPanel(focusPanel);
       	registerPropertyPanel(qpdPanel);
    }
   
}