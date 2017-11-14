package main.embl.rieslab.htSMLM.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;

import org.micromanager.utils.MMScriptException;

import main.embl.rieslab.htSMLM.acquisitions.Acquisition;
import main.embl.rieslab.htSMLM.acquisitions.AcquisitionEngine;
import main.embl.rieslab.htSMLM.acquisitions.ui.AcquisitionWizard;
import main.embl.rieslab.htSMLM.controller.SystemController;
import main.embl.rieslab.htSMLM.threads.TaskHolder;
import main.embl.rieslab.htSMLM.util.StringSorting;

public class AcquisitionPanel extends PropertyPanel implements TaskHolder<Integer>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8529929695246469740L;
		
	private SystemController controller_;
	private ArrayList<Acquisition> acqlist_;
    private AcquisitionWizard wizard_;
    private MainFrame owner_;
    
	///// Task
	private static String TASK_NAME = "Unsupervised acquisitions";
	private AcquisitionEngine acqengine_;
	
    ///// Convenience variables
    private boolean ready_;
    private JFrame summaryframe_;

    private final static String TEXT_INIT = "No configured acquisition list.\n";
    private final static String TEXT_START = "Starting acquisition.\n";
    private final static String TEXT_NEW = "Stage position done: ";
    private final static String TEXT_FINISHED = "Acquisition finished.";
    private final static String TEXT_STOP = "Stopping acquisition. \n";
    private final static String TEXT_SUMMARY = "Acquisition summary: \n";
    private final static String TEXT_LOADED = "Acquisition list loaded.\n";
    
    ///// UI
    private JButton jButton_setpath;
    private JToggleButton jToggle_startstop,jButton_showSummary;
    private JButton jButton_load,jButton_save,jButton_configAcq;
    private JLabel jLabel_expname, jLabel_path, jLabel_progress;
    private JProgressBar jProgressBar_progress;
    private JTextField jTextField_expname;
    private JTextField jTextField_path;
    private JTextPane jTextPane_progress;
    	
	public AcquisitionPanel(SystemController controller, MainFrame owner){
		super("Acquisitions");
		controller_ = controller;
		ready_ = false;
		
		owner_ = owner;
		owner_.addComponentListener(new ComponentAdapter() {
            public void componentMoved(ComponentEvent evt) {
				Point newLoc = getSummaryButtonLocation();
				if (summaryframe_ != null) {
					summaryframe_.setLocation(newLoc);
					summaryframe_.toFront();
					summaryframe_.repaint();
				}

            }
          });
		
		owner_.addWindowListener(new WindowAdapter() {


            @Override
            public void windowDeactivated(WindowEvent e) {
				if (summaryframe_ != null) {
					summaryframe_.setAlwaysOnTop(false);
				}
            }


            @Override
            public void windowActivated(WindowEvent e) {
				if (summaryframe_ != null) {
					summaryframe_.setAlwaysOnTop(true);
				}
            }
            
            @Override
            public void windowIconified(WindowEvent e) {
            	System.out.println("FrameChange");
            	if ((owner_.getExtendedState() & Frame.ICONIFIED) != 0) {
                    System.out.println("Frame was minimized");
					if (summaryframe_ != null) {
						summaryframe_.setAlwaysOnTop(false);
						summaryframe_.toBack();
					}
                  }
             }

        });
       
		
	}
	
	private void initiPanel() {
	    jButton_setpath = new JButton("...");
	    jButton_setpath .addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	showSelectPath();
            }
        });

	    jToggle_startstop = new JToggleButton("Start");
	    jToggle_startstop.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED){
					startTask();
					jToggle_startstop.setText("Stop");
				} else if(e.getStateChange()==ItemEvent.DESELECTED){
					stopTask();
					jToggle_startstop.setText("Start");
				}
			}
        });
        
        jButton_save = new JButton("Save");
        jButton_save.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveAcquisitionList();
			}
        });
        
        jButton_load = new JButton("Load");
        jButton_load.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				loadAcquisitionList();
			}
        });
        
        jButton_configAcq = new JButton("Configure");
        jButton_configAcq.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showAcquisitionConfiguration();
			}
        });

        jButton_showSummary = new JToggleButton(">>");
        jButton_showSummary.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED){
					showSummary(true);
					jButton_showSummary.setText("<<");
				} else if(e.getStateChange()==ItemEvent.DESELECTED){
					showSummary(false);
					jButton_showSummary.setText(">>");
				}
			}
        });
        
        jLabel_path = new JLabel("Path");
        jLabel_expname = new JLabel("Experiment name");
        jLabel_progress = new JLabel("Progress");

	    jProgressBar_progress = new JProgressBar();
	    jProgressBar_progress.setMinimum(0);
        

	    jTextField_expname = new JTextField();
	    jTextField_path = new JTextField();
	    jTextPane_progress = new JTextPane();
	    
	    jTextPane_progress.setText(TEXT_INIT);
	    jTextPane_progress.setBackground(this.getBackground());
	    
	    JScrollPane scroll = new JScrollPane(jTextPane_progress);
	    scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    scroll.setBounds(50, 30, 300, 50);

	    
	    /////////////////////////////////////////////////
	    /// Grid bag layout
		this.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,4,2,4);
		c.fill = GridBagConstraints.HORIZONTAL;

		// upper part
		c.gridx = 0;
		c.gridy = 1;
		this.add(jLabel_path, c);	    

		c.gridx = 0;
		c.gridy = 2;
		this.add(jLabel_expname, c);

		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 3;
		this.add(jTextField_path, c);
		
		c.gridx = 1;
		c.gridy = 2;
		this.add(jTextField_expname, c);

		c.gridx = 3;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = getWeightX(3);
		this.add(jButton_setpath, c);	
		
		// progress 
		c.gridx = 0;
		c.gridy = 3;
		this.add(jLabel_progress, c);

		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 2;
		this.add(jProgressBar_progress, c);	
		
		c.gridx = 3;
		c.gridy = 3;
		c.gridwidth = 1;
		c.weightx = getWeightX(3);
		this.add(jButton_showSummary, c);	 

		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 4;
		c.gridheight = 3;
		c.weightx = 0.8;
		c.weighty = 0.8;
		c.fill = GridBagConstraints.BOTH;
		this.add(scroll, c);	 
		
		// lower part
		c.gridx = 0;
		c.gridy = 8;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;		
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(jToggle_startstop, c);	 
		
		c.gridx = 1;
		c.weightx = getWeightX(1);
		this.add(jButton_configAcq, c);	 

		c.gridx = 2;
		c.weightx = getWeightX(1);
		this.add(jButton_save, c);	 

		c.gridx = 3;
		c.weightx = getWeightX(3);
		this.add(jButton_load, c);	  
		
	}
	
	private double getWeightX(int col){
		switch(col){
		case 0:
			return 0.4;
		case 1:
			return 0.4;
		}
		return 0.1;
	}
	
	private Point getSummaryButtonLocation(){
		Point newLoc = jButton_showSummary.getLocation();

		newLoc.x += owner_.getAcquisitionPanelLocation().getX()+68;
		newLoc.y += owner_.getAcquisitionPanelLocation().getY()+48;
		
		return newLoc;
	}
	
	private void showSelectPath(){
    	JFileChooser fc = new JFileChooser();
    	fc.setCurrentDirectory(new java.io.File(".")); 
    	fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    	int returnVal = fc.showOpenDialog(this);
    	if(returnVal == JFileChooser.APPROVE_OPTION) {
    	    File folder = fc.getSelectedFile();
    	    jTextField_path.setText(folder.getAbsolutePath());  
    	}
	}
	
	private void showSummary(boolean b){
		if(b){
			summaryframe_ = new JFrame();
			DefaultMutableTreeNode top = new DefaultMutableTreeNode("List of experiments to be performed at each stage position:");
			createNodes(top);
			JTree tree = new JTree(top);
			JScrollPane treeView = new JScrollPane(tree);
			JPanel pane = new JPanel();
			pane.setBorder(BorderFactory.createTitledBorder(null, "Summary", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));
			pane.add(treeView);
			summaryframe_.setLocation(getSummaryButtonLocation());
			summaryframe_.setUndecorated(true);
			summaryframe_.setContentPane(pane);
			summaryframe_.pack();
			summaryframe_.setVisible(true);
			
		} else {
			if(summaryframe_ != null){
				summaryframe_.dispose();
			}
		}
	}
	
	private void createNodes(DefaultMutableTreeNode top) {
	    DefaultMutableTreeNode exp = null;
	    DefaultMutableTreeNode setting = null;
	    
	    exp = new DefaultMutableTreeNode("BFP");
        top.add(exp);

        //original Tutorial
        setting = new DefaultMutableTreeNode("sadasdsadsada");
        exp.add(setting);

        //Tutorial Continued
        setting = new DefaultMutableTreeNode("vsdfsdfdsfdsfsd");
        exp.add(setting);

	    exp = new DefaultMutableTreeNode("Localization");
        top.add(exp);
        
        //original Tutorial
        setting = new DefaultMutableTreeNode("sadasdsadsada");
        exp.add(setting);

        //Tutorial Continued
        setting = new DefaultMutableTreeNode("vsdfsdfdsfdsfsd");
        exp.add(setting);
	    
	    if(ready_){
	    	Acquisition acq;
	    	String s;
	    	String[] propval;
	    	for(int i=0;i<acqlist_.size();i++){
	    		acq = acqlist_.get(i);
	    	    exp = new DefaultMutableTreeNode(acq.getFriendlyName());
	    	    
	    	    propval = new String[acq.getPropertyValues().size()];
	    	    Iterator<String> it = acq.getPropertyValues().keySet().iterator();
	    	    int j=0;
	    	    while(it.hasNext()){
	    	    	s = it.next();
	    	    	propval[j] = s+": "+acq.getPropertyValues().get(s);
	    	    	j++;
	    	    }
	    	    
	    	    propval = StringSorting.sort(propval);
	    	    for(j=0;j<propval.length;j++){
	    	    	setting = new DefaultMutableTreeNode(propval[j]);
	    	    	exp.add(setting);
	    	    }
	    	}
	    }
	}
	
	private void showAcquisitionConfiguration(){
		wizard_ = new AcquisitionWizard();
	}
	
	public void getAcquisitionList(){
		acqlist_ = wizard_.getAcquisitionList();
		if(!acqlist_.isEmpty()){
			ready_ = true;
			setSummaryText();
		} else {
			ready_ = false;
		}
	}

	private void setStopText(){
		addText(TEXT_STOP);
	}

	private void setStartText(){
		addText(TEXT_START);
	}
	
	private void setFinishedText(){
		addText(TEXT_FINISHED);
	}
	
	private void saveAcquisitionList(){
		wizard_.saveAcquisitionList();
	}
	
	private void loadAcquisitionList(){
		wizard_.loadAcquisitionList();		
		addText(TEXT_LOADED);
	}
	
	private void setSummaryText(){
		if(ready_){
			addText(TEXT_SUMMARY);
			String s = "Experiments: ";
			for(int i=0;i<acqlist_.size()-1;i++){
				s = s+acqlist_.get(i).getFriendlyName()+", ";
			}
			s = s+acqlist_.get(acqlist_.size()-1).getFriendlyName()+".\n";
			addText(s);
		}
	}

	private void addText(String message){
		String s = jTextPane_progress.getText();
		s = s + message;
		jTextPane_progress.setText(s);
	}
	
	
	/////////////////////////////////////////////////////////////////////////////
	//////
	////// TaskHolder methods
	//////
	
	@Override
	public void update(Integer[] output) {
		jProgressBar_progress.setValue(output[0]);
		addText(TEXT_NEW+output[0]+"\n");
	}

	@Override
	public Integer[] retrieveAllParameters() {
		return null;
	}

	@Override
	public void startTask() {
		if(ready_){			
			// set path and experiment name in acquisition
			String path = jTextField_path.getText();
			String name = jTextField_expname.getText();
			for(int i=0;i<acqlist_.size();i++){
				acqlist_.get(i).setName(name);
				acqlist_.get(i).setPath(path);
			}
			
			acqengine_ = new AcquisitionEngine(this, controller_);
			acqengine_.setAcquisitionList(acqlist_);
			acqengine_.startTask();
			
			setStartText();
			
			try {
				int numpos = controller_.getScriptInterface().getPositionList().getNumberOfPositions();
				jProgressBar_progress.setMaximum(numpos);
			} catch (MMScriptException e) {
				// Do nothing
			}
		}
	}

	@Override
	public void stopTask() {
		if(acqengine_ != null){
			acqengine_.stopTask();
		}
		setStopText();
	}

	@Override
	public boolean isPausable() {
		return false;
	}

	@Override
	public void pauseTask() {
		// Do nothing		
	}

	@Override
	public void resumeTask() {
		// Do nothing
	}

	@Override
	public boolean isTaskRunning() {
		return acqengine_.isRunning();
	}

	@Override
	public String getTaskName() {
		return TASK_NAME;
	}

	@Override
	public boolean isCriterionReached() {
		return false;
	}

	@Override
	public void initializeTask() {
		// Do nothing
	}

	/////////////////////////////////////////////////////////////////////////////
	//////
	////// PropertyPanel methods
	//////


	@Override
	protected void initializeProperties() {
		// Do nothing
	}

	@Override
	protected void initializeInternalProperties() {
		// Do nothing
	}

	@Override
	protected void initializeParameters() {
		// Do nothing
	}

	@Override
	public void setupPanel() {
		initiPanel();
	}

	@Override
	protected void changeProperty(String name, String value) {
		// Do nothing
	}

	@Override
	protected void changeInternalProperty(String name, String value) {
		// Do nothing
	}

	@Override
	public void propertyhasChanged(String name, String newvalue) {
		// Do nothing
	}

	@Override
	public void parameterhasChanged(String label) {
		// Do nothing
	}
	
	@Override
	public void internalpropertyhasChanged(String label) {
		// Do nothing
	}

	@Override
	public void shutDown() {
		stopTask();
		if(summaryframe_ != null){
			summaryframe_.dispose();
		}
	}

	@Override
	public String getDescription() {
		return "";
	}
}
