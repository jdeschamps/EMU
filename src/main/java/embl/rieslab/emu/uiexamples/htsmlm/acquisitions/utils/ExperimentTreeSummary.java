package main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.utils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;

import main.java.embl.rieslab.emu.controller.SystemController;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.acquisitiontypes.Acquisition;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.wrappers.Experiment;

public class ExperimentTreeSummary {

	public static JPanel getExperiment(SystemController controller, Experiment exp){
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("List of experiments to be performed at each stage position:");

	    DefaultMutableTreeNode expnode = null;
	    DefaultMutableTreeNode setting = null;

	    if(exp.getAcquisitionList().size() > 0){
	    	Acquisition acq;
	    	String s;

    	    expnode = new DefaultMutableTreeNode("Pause between acquisitions (s): "+exp.getPauseTime());
            top.add(expnode);
            
            if(exp.getNumberPositions() > 0){
            	expnode = new DefaultMutableTreeNode("Number of positions: "+exp.getNumberPositions());
            	top.add(expnode);
            } else {
            	expnode = new DefaultMutableTreeNode("Number of positions: all");
            	top.add(expnode);
            }
	    	
	    	ArrayList<Acquisition> acqlist = exp.getAcquisitionList();
	    	for(int i=0;i<acqlist.size();i++){
	    		acq = acqlist.get(i);
	    	    expnode = new DefaultMutableTreeNode((i+1)+": "+acq.getType());
	            top.add(expnode);

	            ///////// settings specific to each acq
	            String[] specificsettings = acq.getSpecialSettings();
	    	    for(int j=0;j<specificsettings.length;j++){
	   	    		setting = new DefaultMutableTreeNode(specificsettings[j]);
	   	    		expnode.add(setting);
	    	    }
	            
	    	    //////// MM configuration groups
	    	    String[] confgroup = new String[acq.getParameters().getMMConfigurationGroupValues().size()];
	    	    Iterator<String> it = acq.getParameters().getMMConfigurationGroupValues().keySet().iterator();
	    	    int j=0;
	    	    while(it.hasNext()){
	    	    	s = it.next();  
	    	    	confgroup[j] = s+": "+acq.getParameters().getMMConfigurationGroupValues().get(s);
	    	    	j++;
	    	    }
	    	    Arrays.sort(confgroup);
	    	    for(j=0;j<confgroup.length;j++){
	   	    		setting = new DefaultMutableTreeNode(confgroup[j]);
	   	    		expnode.add(setting);
	    	    }
	    	    
	    	    //////// UIProperties values
	    	    String[] propval = new String[acq.getParameters().getPropertyValues().size()];
	    	    it = acq.getParameters().getPropertyValues().keySet().iterator();
	    	    j=0;
	    	    while(it.hasNext()){
	    	    	s = it.next();  
	    	    	propval[j] = controller.getProperty(s).getFriendlyName()+": "+acq.getParameters().getPropertyValues().get(s);
	    	    	j++;
	    	    }
	    	    Arrays.sort(propval);
	    	    for(j=0;j<propval.length;j++){
	   	    		setting = new DefaultMutableTreeNode(propval[j]);
	   	    		expnode.add(setting);
	    	    }
	    	}
	    } else {
    	    expnode = new DefaultMutableTreeNode("No acquisition defined");
            top.add(expnode);
	    }
		
		JTree tree = new JTree(top);
		JScrollPane treeView = new JScrollPane(tree);
		JPanel pane = new JPanel();
		pane.setBorder(BorderFactory.createTitledBorder(null, "Summary", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));
		pane.add(treeView);
		return pane;
	}
}
