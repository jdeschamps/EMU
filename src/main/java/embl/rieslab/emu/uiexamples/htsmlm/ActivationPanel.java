package main.java.embl.rieslab.emu.uiexamples.htsmlm;

import ij.ImagePlus;
import ij.process.ImageProcessor;
import ij.process.ShortProcessor;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import main.java.embl.rieslab.emu.ui.ConfigurablePanel;
import main.java.embl.rieslab.emu.ui.internalproperties.IntInternalProperty;
import main.java.embl.rieslab.emu.ui.uiparameters.DoubleUIParameter;
import main.java.embl.rieslab.emu.ui.uiparameters.IntUIParameter;
import main.java.embl.rieslab.emu.ui.uiproperties.UIProperty;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.graph.TimeChart;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.tasks.ActivationTask;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.tasks.Task;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.tasks.TaskHolder;
import main.java.embl.rieslab.emu.utils.utils;
import mmcorej.CMMCore;

public class ActivationPanel extends ConfigurablePanel implements TaskHolder<Double> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -517213311514918870L;

	//////// Task
	public final static String TASK_NAME = "Activation task";
	
	//////// Components
	private JLabel labelsdcoeff_;
	private JLabel labelfeeback_;
	private JLabel labeldT_;
	private JTextField textfieldcutoff_;
	private JTextField textfieldN0_;
	private JTextField textfieldsdcoeff_;
	private JTextField textfieldfeedback_;
	private JTextField textfielddT_;
	private JToggleButton togglebuttonrun_;
	private JToggleButton togglebuttonautocutoff_;
	private JButton buttongetN_;
	//private JButton buttongetcutoff_;
	private JButton buttonclear_;
	private JCheckBox checkboxnms_;
	private JCheckBox checkboxactivate_;
	private TimeChart graph_;
	private JPanel graphpane_;
	private ActivationTask task_;
	
	//////// Properties
	private final static String LASER_PULSE = "UV pulse duration (activation)";
	
	//////// Internal properties
	private final static String INTERNAL_MAXPULSE = LaserPulsingPanel.INTERNAL_MAXPULSE;
	
	//////// Parameters
	private final static String PARAM_IDLE = "Idle time (ms)";
	private final static String PARAM_NPOS = "Number of points";
	private final static String PARAM_DEF_SD = "Default sd coeff";
	private final static String PARAM_DEF_FB = "Default feedback";

	//////// Convenience variables
	private boolean activate_, shownms_, autocutoff_;
	private double sdcoeff_, feedback_, N0_ = 1, cutoff_ = 100;
	private int npos_, idletime_, maxpulse_;
	private double dT_;
	private ImagePlus im_;
	private ImageProcessor ip_;
	private int counternms_ = 0;
	private Double[] params;
	
	public ActivationPanel(String label, CMMCore core) {
		super(label);
		
		task_ = new ActivationTask(this, core, idletime_);
    	ip_ = new ShortProcessor(200,200);
		im_ = new ImagePlus("NMS result");
		
		params = new Double[ActivationTask.NUM_PARAMETERS];
	}
	
	@Override
	public void setupPanel() {
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 4;
		c.weightx = 0.2;
		c.weighty = 0.9;
		c.fill = GridBagConstraints.VERTICAL;
		this.add(getleftpanel(),c);  
		
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 3;
		c.gridheight = 3;
		c.weightx = 0.8;
		c.weighty = 0.8;
		c.fill = GridBagConstraints.BOTH;
		this.add(getgraphpanel(),c);
		
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 3;
		c.gridheight = 1;
		c.weighty = 0.03;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(getlowerpanel(),c);
		
	}
	
	public JPanel getleftpanel(){
		JPanel pane = new JPanel();
		pane.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		
		labelsdcoeff_ = new JLabel("Sd coeff:");
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(2,6,2,6);
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.BOTH;
		pane.add(labelsdcoeff_,c);
		
		textfieldsdcoeff_ = new JTextField(String.valueOf(sdcoeff_));
		textfieldsdcoeff_.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent arg0) {}
			@Override
			public void focusLost(FocusEvent arg0) {
				String typed = textfieldsdcoeff_.getText();
        	    if(!utils.isNumeric(typed)) {
        	        return;
        	    } 
				try {
					double val = Double.parseDouble(typed);
					if (val >= 0) {
						sdcoeff_ = val;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
        	}
         });
		textfieldsdcoeff_.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ex) {
				String typed = textfieldsdcoeff_.getText();
        	    if(!utils.isNumeric(typed)) {
        	        return;
        	    } 
				try {
					double val = Double.parseDouble(typed);
					if (val >= 0) {
						sdcoeff_ = val;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
        	}
        });
		c.gridy = 1;
		pane.add(textfieldsdcoeff_,c);	
		
		labelfeeback_ = new JLabel("Feedback:");
		c.gridy = 2;
		pane.add(labelfeeback_,c);
		
		textfieldfeedback_ = new JTextField(String.valueOf(feedback_));
		textfieldfeedback_.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent arg0) {}
			@Override
			public void focusLost(FocusEvent arg0) {
				String typed = textfieldfeedback_.getText();
        	    if(!utils.isNumeric(typed)) {
        	        return;
        	    } 
				try {
					double val = Double.parseDouble(typed);
					if (val >= 0) {
						feedback_ = val;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
        	}
         });
		textfieldfeedback_.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ex) {
				String typed = textfieldfeedback_.getText();
        	    if(!utils.isNumeric(typed)) {
        	        return;
        	    } 
				try {
					double val = Double.parseDouble(typed);
					if (val >= 0) {
						feedback_ = val;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
        	}
        });
		c.gridy = 3;
		pane.add(textfieldfeedback_,c);	
		
		labeldT_ = new JLabel("Average:");
		c.gridy = 4;
		pane.add(labeldT_,c);

		dT_ = 1.;
		textfielddT_ = new JTextField(String.valueOf(dT_));
		textfielddT_.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent arg0) {}
			@Override
			public void focusLost(FocusEvent arg0) {
				String typed = textfielddT_.getText();
        	    if(!utils.isNumeric(typed)) {
        	        return;
        	    } 
				try {
					double val = Double.parseDouble(typed);
					if (val > 1) {
						dT_ = val;
					} else {
						dT_ = 1;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
        	}
         });
		textfielddT_.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ex) {
				String typed = textfielddT_.getText();
        	    if(!utils.isNumeric(typed)) {
        	        return;
        	    } 
				try {
					double val = Double.parseDouble(typed);
					if (val > 1) {
						dT_ = val;
					} else {
						dT_ = 1;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
        	}
        });
		c.gridy = 5;
		pane.add(textfielddT_,c);	
		
		buttongetN_ = new JButton("Get N:");
		buttongetN_.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	String val = String.valueOf(graph_.getLastPoint());
            	textfieldN0_.setText(val);
            	N0_ = graph_.getLastPoint();
            }
        });
		c.gridy = 6;
		c.insets = new Insets(20,6,2,6);
		pane.add(buttongetN_,c);	
		
		N0_ = 1;
		textfieldN0_ = new JTextField(String.valueOf(N0_));
		textfieldN0_.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent arg0) {}
			@Override
			public void focusLost(FocusEvent arg0) {
				String typed = textfieldN0_.getText();
        	    if(!utils.isNumeric(typed)) {
        	        return;
        	    } 
				try {
					double val = Double.parseDouble(typed);
					if (val >= 0) {
						N0_ = val;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
        	}
         });
		textfieldN0_.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ex) {
				String typed = textfieldN0_.getText();
        	    if(!utils.isNumeric(typed)) {
        	        return;
        	    } 
				try {
					double val = Double.parseDouble(typed);
					if (val >= 0) {
						N0_ = val;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
        	}
        });
		c.gridy = 7;
		c.insets = new Insets(2,6,2,6);
		pane.add(textfieldN0_,c);
		
		checkboxactivate_ = new JCheckBox("Activate");
		checkboxactivate_.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED){
					activate_ = true;
				} else if(e.getStateChange()==ItemEvent.DESELECTED){
					activate_ = false;
				}
			}
        });
		c.gridy = 8;
		c.insets = new Insets(40,6,2,6);
		pane.add(checkboxactivate_,c);	
		
		togglebuttonrun_ = new JToggleButton("Run");
		togglebuttonrun_.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED){
					runActivation(true);
				} else if(e.getStateChange()==ItemEvent.DESELECTED){
					runActivation(false);
				}
			}
        });
		togglebuttonrun_.setPreferredSize(new Dimension(40,40));
		c.gridy = 9;
		c.gridheight = 2;
		c.insets = new Insets(2,6,2,6);
		pane.add(togglebuttonrun_,c);	
		
		return pane;	
	}
	
	public JPanel getgraphpanel(){
		graphpane_  = new JPanel();
		newGraph();
		graphpane_.add(graph_.getChart());
		return graphpane_;
	}
	
	public JPanel getlowerpanel(){
		JPanel pane = new JPanel();
		pane.setLayout(new GridBagLayout());
		
//		labelcutoff_ = new JLabel("Cut-off:");
//		c.gridx = 0;
//		pane.add(labelcutoff_,c);
		
		textfieldcutoff_ = new JTextField(String.valueOf(cutoff_));
		textfieldcutoff_.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent arg0) {}
			@Override
			public void focusLost(FocusEvent arg0) {
				String typed = textfieldcutoff_.getText();
        	    if(!utils.isNumeric(typed)) {
        	        return;
        	    } 
				try {
					double val = Double.parseDouble(typed);
					if (val >= 0) {
						cutoff_ = val;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
        	}
         });
		textfieldcutoff_.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ex) {
				String typed = textfieldcutoff_.getText();
        	    if(!utils.isNumeric(typed)) {
        	        return;
        	    } 
				try {
					double val = Double.parseDouble(typed);
					if (val >= 0) {
						cutoff_ = val;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
        	}
        });
		

		/*buttongetcutoff_ = new JButton("Get Cutoff");
		buttongetcutoff_.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	textfieldcutoff_.setText(getProperty("Cutoff").getValue());
            }
        });
		c.gridx = 1;
		c.fill = GridBagConstraints.NONE;
		pane.add(buttongetcutoff_,c);*/

		togglebuttonautocutoff_ = new JToggleButton("Auto");
		togglebuttonautocutoff_.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED){
					autocutoff_ = true;
				} else if(e.getStateChange()==ItemEvent.DESELECTED){
					autocutoff_ = false;
				}
			}
        });

		
		buttonclear_ = new JButton("Clear");
		buttonclear_.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	graph_.clearChart();
            }
        });	
		
		checkboxnms_ = new JCheckBox("NMS");
		checkboxnms_.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED){
					showNMS(true);
				} else if(e.getStateChange()==ItemEvent.DESELECTED){
					showNMS(false);
				}
			}
        });
		
		//////////////////////////////// grid bag setup
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,4,2,4);
		c.ipadx = 50;
		pane.add(textfieldcutoff_,c);	
		
		c.gridx = 1;
		c.ipadx = 0;
		pane.add(togglebuttonautocutoff_,c);	
		
		c.gridx = 2;
		pane.add(buttonclear_,c);	
		
		c.gridx = 3;
		pane.add(checkboxnms_,c);	

		return pane;
	}
	
	/////////////////////////////////////////////////////////////////////////////
	//////
	////// Convenience methods
	//////
	
	
	protected void runActivation(boolean b){
		if(b){
			task_.startTask();
		} else {
			task_.stopTask();
		}
	}
	
	protected void showNMS(boolean b){
		if(b){
			shownms_ = true;
			im_.setProcessor(ip_);
			im_.show();
		} else {
			shownms_ = false;
			im_.close();
		}
	}
	
	private void newGraph(){
		graph_ = new TimeChart("Number of locs","time","N",npos_,300,240, true);	
	}
	
	public boolean isActivationAtMax(){
		String val = getUIPropertyValue(LASER_PULSE);
		if(utils.isNumeric(val)){
			if(Double.parseDouble(val)>=maxpulse_){
				return true;
			}
		}
		return false;
	}

	/////////////////////////////////////////////////////////////////////////////
	//////
	////// PropertyPanel methods
	//////
	
	@Override
	protected void initializeProperties() {
		addUIProperty(new UIProperty(this, LASER_PULSE,"Pulse length property of the activation laser"));		
	}

	@Override
	protected void initializeParameters() {
		sdcoeff_ = 1.5;
		feedback_ = 0.4;
		idletime_ = 100;
		npos_ = 30; 
		
		addUIParameter(new DoubleUIParameter(this, PARAM_DEF_SD,"Default value of the cutoff coefficient.",sdcoeff_));
		addUIParameter(new DoubleUIParameter(this, PARAM_DEF_FB,"Default value of the activation feedback coefficient.",feedback_));
		addUIParameter(new IntUIParameter(this, PARAM_IDLE,"Idle time (ms) of the stage position monitoring.",idletime_)); // thread idle time
		addUIParameter(new IntUIParameter(this, PARAM_NPOS,"Number of stage positions displayed in the chart.",npos_)); // number of point in the graph
	}

	@Override
	protected void changeProperty(String name, String value) {
		if(name.equals(LASER_PULSE)){
			setUIPropertyValue(name,value);
		}
	}

	@Override
	public void propertyhasChanged(String name, String newvalue) {
		// do nothing
	}

	@Override
	public void parameterhasChanged(String label) {
		if(label.equals(PARAM_DEF_SD)){
			sdcoeff_ = ((DoubleUIParameter) getUIParameter(PARAM_DEF_SD)).getValue();
			textfieldsdcoeff_.setText(String.valueOf(sdcoeff_));
		} else if(label.equals(PARAM_DEF_FB)){
			feedback_ = ((DoubleUIParameter) getUIParameter(PARAM_DEF_FB)).getValue();
			textfieldfeedback_.setText(String.valueOf(feedback_));
		}else if(label.equals(PARAM_IDLE)){
			if(((IntUIParameter) getUIParameter(PARAM_IDLE)).getValue() != idletime_){
				idletime_ = ((IntUIParameter) getUIParameter(PARAM_IDLE)).getValue();
				task_.setIdleTime(idletime_);
			}
		}else if(label.equals(PARAM_NPOS)){
			if(((IntUIParameter) getUIParameter(PARAM_NPOS)).getValue() != npos_){
				npos_ = ((IntUIParameter) getUIParameter(PARAM_NPOS)).getValue();
				graphpane_.remove(graph_.getChart());
				newGraph();
				graphpane_.add(graph_.getChart());
				graphpane_.updateUI();
			}
		}
	}

	@Override
	public void shutDown() {
		task_.stopTask();
		showNMS(false);
	}

	@Override
	public String getDescription() {
		return "This panel allows automated activation of a SMLM experiment.";
	}

	@Override
	protected void initializeInternalProperties() {
		maxpulse_ = 10000;
		
		addInternalProperty(new IntInternalProperty(this, INTERNAL_MAXPULSE, maxpulse_));
	}

	@Override
	public void internalpropertyhasChanged(String label) {
		if(label.equals(INTERNAL_MAXPULSE)){
			maxpulse_ = ((IntInternalProperty) getInternalProperty(label)).getPropertyValue();
		}
	}

	@Override
	protected void changeInternalProperty(String name, String value) {
		// Do nothing
	}


	/////////////////////////////////////////////////////////////////////////////
	//////
	////// TaskHolder methods
	//////
	
	@Override
	public void update(final Double[] output) {
		graph_.addPoint(output[ActivationTask.OUTPUT_N]);
		
		if(autocutoff_){
			textfieldcutoff_.setText(String.valueOf(output[ActivationTask.OUTPUT_NEWCUTOFF]));
			cutoff_ = output[ActivationTask.OUTPUT_NEWCUTOFF];
		}
		
		if(shownms_ && counternms_ % 10 == 0){
			ImageProcessor imp = task_.getNMSResult();
			if(imp != null && imp.getPixels() != null){
				ip_ = task_.getNMSResult();
				im_.setProcessor(ip_);
				im_.updateAndRepaintWindow();
			}
		} else if(counternms_ == Integer.MAX_VALUE){
			counternms_ = 0;
		}
		
		if(activate_){
			
			changeProperty(LASER_PULSE,String.valueOf(output[ActivationTask.OUTPUT_NEWPULSE]));
		}
		
		counternms_ ++;
	}

	@Override
	public Double[] retrieveAllParameters() {
		params[ActivationTask.PARAM_ACTIVATE] = activate_ ? 1. : 0.; 
		params[ActivationTask.PARAM_AUTOCUTOFF] = autocutoff_ ? 1. : 0.; 
		params[ActivationTask.PARAM_CUTOFF] = cutoff_; 
		params[ActivationTask.PARAM_dT] = dT_; 
		params[ActivationTask.PARAM_FEEDBACK] = feedback_; 
		params[ActivationTask.PARAM_MAXPULSE] = (double) maxpulse_; // what to do for this? 
		params[ActivationTask.PARAM_N0] = N0_; 
		
		if(utils.isNumeric(getUIProperty(LASER_PULSE).getPropertyValue())){
			params[ActivationTask.PARAM_PULSE] = Double.parseDouble(getUIProperty(LASER_PULSE).getPropertyValue()); 
		} else {
			params[ActivationTask.PARAM_PULSE] = 0.;
		}
		
		params[ActivationTask.PARAM_SDCOEFF] = sdcoeff_; 
		
		return params;
	}

	/**
	 * Called from automated acquisition?
	 * 
	 */
	@Override
	public boolean startTask() {
		if(task_.isRunning()){ // if task is running 
			if(!activate_){ // but not changing the pulse
				 Runnable checkactivate = new Runnable() {
					 public void run() {
						 checkboxactivate_.setSelected(true);
					 }
				 };
				 if (SwingUtilities.isEventDispatchThread()) {
					 checkactivate.run();
				 } else {
					  EventQueue.invokeLater(checkactivate);
				 }
				 activate_ = true;
			}
		} else { // task not running
			runActivation(true); // then run
			if(!activate_){ // task not changing the pulse
				 Runnable checkactivate = new Runnable() {
					 public void run() {
						 togglebuttonrun_.setSelected(true);
						 checkboxactivate_.setSelected(true);
					 }
				 };
				 if (SwingUtilities.isEventDispatchThread()) {
					 checkactivate.run();
				 } else {
					  EventQueue.invokeLater(checkactivate);
				 }
				 activate_ = true;
			} else {
				 Runnable checkactivate = new Runnable() {
					 public void run() {
						 togglebuttonrun_.setSelected(true);
					 }
				 };
				 if (SwingUtilities.isEventDispatchThread()) {
					 checkactivate.run();
				 } else {
					  EventQueue.invokeLater(checkactivate);
				 }
			}
		}
		return true;
	}

	@Override
	public void stopTask() {
		// do nothing
	}

	@Override
	public boolean isPausable() {
		return true;
	}

	@Override
	public void pauseTask() {
		if(activate_){ 
			 Runnable checkactivate = new Runnable() {
				 public void run() {
					 checkboxactivate_.setSelected(false);
				 }
			 };
			 if (SwingUtilities.isEventDispatchThread()) {
				 checkactivate.run();
			 } else {
				  EventQueue.invokeLater(checkactivate);
			 }
			 activate_ = false;
		}		
	}

	@Override
	public void resumeTask() {
		startTask();	
	}

	@Override
	public boolean isTaskRunning() {
		return task_.isRunning();
	}

	@Override
	public String getTaskName() {
		return TASK_NAME;
	}

	@Override
	public boolean isCriterionReached() {
		return isActivationAtMax();
	}

	@Override
	public void initializeTask() {
		changeProperty(LASER_PULSE,"0");
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Task getTask() {
		return task_;
	}

	@Override
	public void taskDone() {
		// Do nothing
	}

}