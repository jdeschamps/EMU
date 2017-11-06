package main.embl.rieslab.htSMLM.ui;

import ij.ImagePlus;

import java.awt.Dimension;
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

import main.embl.rieslab.htSMLM.threads.ActivationTask;
import main.embl.rieslab.htSMLM.threads.TaskHolder;
import main.embl.rieslab.htSMLM.ui.graph.TimeChart;
import main.embl.rieslab.htSMLM.ui.uiparameters.DoubleUIParameter;
import main.embl.rieslab.htSMLM.ui.uiparameters.IntUIParameter;
import main.embl.rieslab.htSMLM.ui.uiproperties.UIProperty;
import main.embl.rieslab.htSMLM.util.utils;
import mmcorej.CMMCore;

public class ActivationPanel extends PropertyPanel implements TaskHolder {

	/**
	 * 
	 */
	private static final long serialVersionUID = -517213311514918870L;

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
	private static String LASER_PULSE = "Laser pulse length";
	
	//////// Parameters
	private static String PARAM_IDLE = "Idle time";
	private static String PARAM_NPOS = "Number of points";
	private static String PARAM_DEF_SD = "Default sd coeff";
	private static String PARAM_DEF_FB = "Default feedback";

	//////// Conveniance variables
	private boolean activate_, shownms_, autocutoff_;
	private double sdcoeff_, feedback_, dT_, N0_, cutoff_;
	private int npos_, idletime_;
	private ImagePlus im_;
	private int counternms_ = 0;
	
	public ActivationPanel(String label, CMMCore core) {
		super(label);
		
		task_ = new ActivationTask(this, core, idletime_);
		im_ = new ImagePlus();
	}
	
	@Override
	public void setupPanel() {
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 2;
		c.fill = GridBagConstraints.VERTICAL;
		this.add(getleftpanel(),c);  
		
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 2;
		c.gridheight = 2;
		c.fill = GridBagConstraints.NONE;
		this.add(getgraphpanel(),c);
		
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 3;
		c.gridheight = 1;
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
		
		textfieldsdcoeff_ = new JTextField();
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
		
		textfieldfeedback_ = new JTextField();
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
		
		textfielddT_ = new JTextField();
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
					if (val >= 0) {
						dT_ = val;
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
					if (val >= 0) {
						dT_ = val;
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
            	N0_ = Double.parseDouble(val);
            }
        });
		c.gridy = 6;
		c.insets = new Insets(20,6,2,6);
		pane.add(buttongetN_,c);	
		
		textfieldN0_ = new JTextField();
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

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(2,4,2,4);
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.NONE;
		
//		labelcutoff_ = new JLabel("Cut-off:");
//		c.gridx = 0;
//		pane.add(labelcutoff_,c);
		
		textfieldcutoff_ = new JTextField("40000.000");
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
		c.gridx = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		pane.add(textfieldcutoff_,c);	

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
		c.gridx = 2;
		pane.add(togglebuttonautocutoff_,c);	
		
		buttonclear_ = new JButton("Clear");
		buttonclear_.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	graph_.clearChart();
            }
        });
		c.gridx = 3;
		pane.add(buttonclear_,c);		
		
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
		c.gridx = 4;
		pane.add(checkboxnms_,c);	

		return pane;
	}

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
			im_.show();
		} else {
			shownms_ = false;
			im_.close();
		}
	}
	
	private void newGraph(){
		graph_ = new TimeChart("Number of locs","time","N",npos_,350,250, true);	
	}

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
		addUIParameter(new IntUIParameter(this, PARAM_IDLE,"Idle time of the stage position monitoring.",idletime_)); // thread idle time
		addUIParameter(new IntUIParameter(this, PARAM_NPOS,"Number of stage positions displayed in the chart.",npos_)); // number of point in the graph
	}

	@Override
	protected void changeProperty(String name, String value) {
		if(name.equals(LASER_PULSE)){
			getUIProperty(name).setPropertyValue(value);
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
	public void update(final Double[] output) {
		graph_.addPoint(output[ActivationTask.OUTPUT_N]);
		
		textfieldcutoff_.setText(String.valueOf(output[ActivationTask.OUTPUT_NEWCUTOFF]));
		
		if(shownms_ && counternms_ % 10 == 0){
			im_.setProcessor(task_.getNMSResult());
			im_.updateAndRepaintWindow();
			counternms_ ++;
		}
		
		// run on non-EDT, but does it really take long?
		Thread t = new Thread("Update pulse") {
            public void run() {
            	changeProperty(LASER_PULSE,String.valueOf(output[ActivationTask.OUTPUT_NEWPULSE]));
            }

        };
        t.start();
	}

	@Override
	public double[] retrieveAllParameters() {
		double[] params = new double[ActivationTask.NUM_PARAMETERS];

		params[ActivationTask.PARAM_ACTIVATE] = activate_ ? 1 : 0; 
		params[ActivationTask.PARAM_AUTOCUTOFF] = autocutoff_ ? 1 : 0; 
		params[ActivationTask.PARAM_CUTOFF] = cutoff_; 
		params[ActivationTask.PARAM_dT] = dT_; 
		params[ActivationTask.PARAM_FEEDBACK] = feedback_; 
		params[ActivationTask.PARAM_MAXPULSE] = 100000; // what to do for this? 
		params[ActivationTask.PARAM_N0] = N0_; 
		
		if(utils.isFloat(getUIProperty(LASER_PULSE).getPropertyValue())){
			params[ActivationTask.PARAM_PULSE] = Double.parseDouble(getUIProperty(LASER_PULSE).getPropertyValue()); 
		} else {
			params[ActivationTask.PARAM_PULSE] = 0;
		}
		
		params[ActivationTask.PARAM_SDCOEFF] = sdcoeff_; 
		
		return params;
	}

}