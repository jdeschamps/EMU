package main.embl.rieslab.htSMLM.ui;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
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

        focusPanel = new FocusPanel("Focus panel");
        qpdPanel = new QPDPanel("QPD");
        filterPanel = new FiltersPanel("Filter wheel");

        JPanel mainpane = new JPanel();
        mainpane.setLayout(new BoxLayout(mainpane, BoxLayout.PAGE_AXIS));
        
        mainpane.add(focusPanel);
        mainpane.add(qpdPanel);
        mainpane.add(filterPanel);
        this.add(mainpane);
 
        this.pack(); // avoid packing when one can
        this.setResizable(false);
 	    this.setVisible(true);        
    }

	@Override
	protected void registerPropertyPanels() {
        registerPropertyPanel(focusPanel);
        registerPropertyPanel(qpdPanel);
        registerPropertyPanel(filterPanel);
	}
}
