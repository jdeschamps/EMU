package main.embl.rieslab.htSMLM.acquisitions.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import main.embl.rieslab.htSMLM.acquisitions.Acquisition;
import main.embl.rieslab.htSMLM.acquisitions.AcquisitionFactory;
import main.embl.rieslab.htSMLM.acquisitions.Experiment;
import main.embl.rieslab.htSMLM.configuration.SystemController;
import main.embl.rieslab.htSMLM.util.utils;

public class AcquisitionWizard {

	private AcquisitionUI owner_;
	private JFrame frame_;
	private ArrayList<AcquisitionTab> tabs_;
	private JTabbedPane tabbedpane_;
	private JTextField waitfield;
	private JTextField numposfield;
	private SystemController controller_;
	
	public AcquisitionWizard(SystemController controller, AcquisitionUI owner){
		owner_ = owner;
		controller_ = controller;
		tabs_ = new ArrayList<AcquisitionTab>();
	}
	
	public AcquisitionWizard(SystemController controller, AcquisitionUI owner, Experiment exp) {
		owner_ = owner;
		controller_ = controller;
		tabs_ = new ArrayList<AcquisitionTab>();

		startWizard(exp);
	}

	public void startWizard(){
		setUpFrame(3,0);
	}	
	
	public void startWizard(Experiment exp){
		setUpFrame(exp.getPauseTime(),exp.getNumberPositions());
		setAcquisitions(exp.getAcquisitionList());
	}
	
	private void setUpFrame(int waitingtime, int numpos) {
		frame_ = new JFrame("Acquisition wizard");
		JPanel contentpane = new JPanel();
		contentpane.setLayout(new BoxLayout(contentpane,BoxLayout.LINE_AXIS));

		contentpane.add(setUpLeftPanel(waitingtime, numpos));
		contentpane.add(setUpRightPanel());
		
		frame_.add(contentpane);
		
		frame_.pack();
		frame_.setVisible(true);
	}

	private JPanel setUpLeftPanel(int waitingtime, int numpos) {
		JPanel leftpane = new JPanel();

		JButton add = new JButton("Add");
		JButton remove = new JButton("Remove");
		JButton left = new JButton("<");
		JButton right = new JButton(">");
		JButton save = new JButton("Save");
		
		JLabel wait = new JLabel("Waiting (s)");
		waitfield = new JTextField(String.valueOf(waitingtime));
		
		JLabel pos = new JLabel("Pos number");
		numposfield = new JTextField(String.valueOf(numpos));
		
		add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	createNewTab();
            }
        });
		
		remove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	removeTab();
            }
        });
		
		save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	saveAcqList();
            }
        });
		
		right.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	moveTabRight();
            }
        });
		
		left.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	moveTabLeft();
            }
        });
		
	    /////////////////////////////////////////////////
	    /// Grid bag layout
		JPanel pane = new JPanel();
		pane.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,4,2,4);
		c.fill = GridBagConstraints.BOTH;

		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 3;
		pane.add(add, c);	

		c.gridx = 1;
		c.gridy = 1;
		pane.add(remove, c);	

		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 1;
		pane.add(left, c);
		
		c.gridx = 3;
		c.gridy = 3;
		pane.add(right, c);	

		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 1;
		pane.add(Box.createHorizontalStrut(10), c);	

		c.gridx = 1;
		c.gridy = 5;
		pane.add(wait, c);	
		
		c.gridx = 2;
		c.gridy = 5;
		c.gridwidth = 2;
		pane.add(waitfield, c);	

		c.gridx = 1;
		c.gridy = 6;
		pane.add(pos, c);	
		
		c.gridx = 2;
		c.gridy = 6;
		c.gridwidth = 2;
		pane.add(numposfield, c);	
		
		c.gridx = 1;
		c.gridy = 8;
		pane.add(save, c);	

		leftpane.add(pane);
		leftpane.add(new JPanel());
		
		return leftpane;
	}

	private JTabbedPane setUpRightPanel() {
		tabbedpane_ = new JTabbedPane();
		
		AcquisitionTab acqtab = new AcquisitionTab(this, new AcquisitionFactory(owner_, controller_));
		tabbedpane_.add(acqtab.getTypeName(), acqtab);
		tabs_.add(acqtab);
		
		return tabbedpane_;
	}
	
	protected void moveTabLeft() {
    	if(tabs_.size()>1){
    		int i = tabbedpane_.getSelectedIndex();

    		if(i>0){
        		AcquisitionTab tab = tabs_.get(i);

        		tabbedpane_.remove(i);
        		tabs_.remove(i);
        		
        		tabs_.add(i-1, tab);
        		tabbedpane_.add(tab, i-1);
        		tabbedpane_.setSelectedIndex(i-1);
    		}
    	} 		
	}

	protected void moveTabRight() {
    	if(tabs_.size()>1){
    		int i = tabbedpane_.getSelectedIndex();

    		if(i<tabs_.size()-1){
    			AcquisitionTab tab = tabs_.get(i);

    			tabbedpane_.remove(i);
        		tabs_.remove(i);
        		
        		tabs_.add(i+1, tab);
        		tabbedpane_.add(tab, i+1);
        		tabbedpane_.setSelectedIndex(i+1);
    		}
    	} 
	}

	protected void removeTab() {
    	if(tabs_.size()>1){
    		int i = tabbedpane_.getSelectedIndex();

    		tabbedpane_.remove(i);
    		tabs_.remove(i);
    	} 
	}

	protected void createNewTab() {
       	tabs_.add(new AcquisitionTab(this, new AcquisitionFactory(owner_, controller_)));
        tabbedpane_.add(tabs_.get(tabs_.size()-1), tabs_.size()-1);
        tabbedpane_.setSelectedIndex(tabs_.size()-1);	
	}
	
	public void setAcquisitions(ArrayList<Acquisition> acqlist) {
		tabbedpane_.removeAll();
		tabs_.clear();
		
		for(int i=0;i<acqlist.size();i++){
			tabs_.add(new AcquisitionTab(this, new AcquisitionFactory(owner_, controller_), acqlist.get(i)));
	        tabbedpane_.add(tabs_.get(i), i);
		}
		tabbedpane_.setSelectedIndex(0);	
	}
	
	public void changeName(AcquisitionTab acquisitionTab) {
		setNameTab(acquisitionTab.getTypeName());
	}
	
    private void setNameTab(String name){
       	int i = tabbedpane_.getSelectedIndex();
       	if(i>=0 && i<tabbedpane_.getTabCount()){
           	tabbedpane_.setTitleAt(i, name);
       	}
    }
    	
    protected void saveAcqList() {
		owner_.setExperiment(new Experiment(getWaitingTime(), getNumberPositions(),getAcquisitionList()));
		shutDown();		
	}
    
	private int getWaitingTime() {
		String s = waitfield.getText();
		if(utils.isInteger(s)){
			return Integer.parseInt(s); 
		}
		return 3000;
	}  
	
	private int getNumberPositions() {
		String s = numposfield.getText();
		if(utils.isInteger(s)){
			return Integer.parseInt(s); 
		}
		return 0;
	}
	
	private ArrayList<Acquisition> getAcquisitionList() {
		ArrayList<Acquisition> acqlist = new ArrayList<Acquisition>();
		
		for(int i=0;i<tabs_.size();i++){
			acqlist.add(tabs_.get(i).getAcquisition());
		}
		
		return acqlist;
	}

	public boolean isRunning(){
		return frame_.isActive();
	}
	
	public Experiment loadAcquisitionList(String path) {		
    	return (new AcquisitionFactory(owner_, controller_)).readAcquisitionList(path);
	}

	public boolean saveAcquisitionList(Experiment exp, String path) {
		return (new AcquisitionFactory(owner_, controller_)).writeAcquisitionList(exp, path);
	}
	
	public SystemController getController(){
		return controller_;
	}
	
	public void shutDown() {
		if(frame_ != null){
			frame_.dispose();
		}
	}


}
