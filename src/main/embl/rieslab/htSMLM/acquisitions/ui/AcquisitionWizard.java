package main.embl.rieslab.htSMLM.acquisitions.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import main.embl.rieslab.htSMLM.acquisitions.Acquisition;
import main.embl.rieslab.htSMLM.acquisitions.AcquisitionFactory;
import main.embl.rieslab.htSMLM.configuration.SystemController;
import main.embl.rieslab.htSMLM.util.utils;

public class AcquisitionWizard {

	private AcquisitionUI owner_;
	private JFrame frame_;
	private ArrayList<AcquisitionTab> tabs_;
	private JTabbedPane tabbedpane_;
	private JTextField waitfield;
	private SystemController controller_;
	
	public AcquisitionWizard(SystemController controller, AcquisitionUI owner){
		owner_ = owner;
		controller_ = controller;
		tabs_ = new ArrayList<AcquisitionTab>();
		
		setUpFrame();
	}
	
	private void setUpFrame() {
		frame_ = new JFrame("Acquisition wizard");
		JPanel contentpane = new JPanel();
		contentpane.setLayout(new BoxLayout(contentpane,BoxLayout.LINE_AXIS));

		contentpane.add(setUpLeftPanel());
		contentpane.add(setUpRightPanel());
		
		frame_.add(contentpane);
		
		frame_.pack();
		frame_.setVisible(true);
	}

	private JPanel setUpLeftPanel() {
		JPanel leftpane = new JPanel();

		JButton add = new JButton("Add");
		JButton remove = new JButton("Remove");
		JButton left = new JButton("<");
		JButton right = new JButton(">");
		JButton save = new JButton("Save");
		
		JLabel wait = new JLabel("Waiting (s)");
		
		waitfield = new JTextField("5");
		
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
		leftpane.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,4,2,4);
		c.fill = GridBagConstraints.BOTH;

		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 3;
		leftpane.add(add, c);	

		c.gridx = 1;
		c.gridy = 1;
		leftpane.add(remove, c);	

		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 1;
		leftpane.add(left, c);
		
		c.gridx = 3;
		c.gridy = 3;
		leftpane.add(right, c);	

		c.gridx = 1;
		c.gridy = 5;
		leftpane.add(wait, c);	
		
		c.gridx = 2;
		c.gridy = 5;
		c.gridwidth = 2;
		leftpane.add(waitfield, c);	
		
		c.gridx = 1;
		c.gridy = 8;
		leftpane.add(save, c);	

		return leftpane;
	}

	private JTabbedPane setUpRightPanel() {
		tabbedpane_ = new JTabbedPane();
		
		AcquisitionTab acqtab = new AcquisitionTab(this, new AcquisitionFactory(owner_, controller_));
		tabbedpane_.add(acqtab.getTypeName(), acqtab);
		
		return tabbedpane_;
	}
	
	protected void moveTabLeft() {
    	if(tabs_.size()>1){
    		int i = tabbedpane_.getSelectedIndex()-1;

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
		owner_.setAcquisitionList(getAcquisitionList(), getWaitingTime());
		saveAcquisitionList();		
	}

	private int getWaitingTime() {
		String s = waitfield.getText();
		if(utils.isInteger(s)){
			return Integer.parseInt(s); 
		}
		return 5000;
	}
	private ArrayList<Acquisition> getAcquisitionList() {
		ArrayList<Acquisition> acqlist = new ArrayList<Acquisition>();
		
		for(int i=0;i<tabs_.size();i++){
			acqlist.add(tabs_.get(i).getAcquisition());
		}
		
		return acqlist;
	}

	public void loadAcquisitionList() {
		// TODO Auto-generated method stub
		
	}

	public void saveAcquisitionList() {
		// TODO Auto-generated method stub
		
	}
	
	public SystemController getController(){
		return controller_;
	}
	
	public void shutDown() {
		frame_.dispose();
	}

}
