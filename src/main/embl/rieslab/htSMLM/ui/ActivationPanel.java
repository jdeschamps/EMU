package main.embl.rieslab.htSMLM.ui;

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
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import main.embl.rieslab.htSMLM.ui.graph.TimeChart;

public class ActivationPanel extends PropertyPanel {

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
	private JButton buttongetcutoff_;
	private JButton buttonclear_;
	private JCheckBox checkboxnms_;
	private JCheckBox checkboxactivate_;
	private TimeChart graph_;
	
	//////// Properties
	private static String LASER_PULSE = "Laser pulse length";
	
	//////// Parameters
	private static String PARAM_IDLE = "Idle ime";
	private static String PARAM_NPOS = "Number of points";
	private static String PARAM_DEF_SD = "Default sd coeff";
	private static String PARAM_DEF_FB = "Default feedback";

	//////// Conveniance variables
	private boolean update_ = false;
	
	public ActivationPanel(String label) {
		super(label);
		// TODO Auto-generated constructor stub
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
						setProperty("Standard deviation", typed);
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
						setProperty("Standard deviation", typed);
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
						setProperty("UV feedback", typed);
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
						setProperty("UV feedback", typed);
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
						setProperty("Time average", typed);
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
						setProperty("Time average", typed);
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
            	setProperty("N0",val);
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
						setProperty("N0", typed);
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
						setProperty("N0", typed);
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
					setProperty("Activate","1");
				} else if(e.getStateChange()==ItemEvent.DESELECTED){
					setProperty("Activate","0");
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
					setProperty("Run activation","1");
				} else if(e.getStateChange()==ItemEvent.DESELECTED){
					setProperty("Run activation","0");
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
		graph_ = new TimeChart("Number of locs","time","N",800,350,250, true);		
		return graph_.getChart();
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
						setProperty("Cutoff", typed);
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
						setProperty("Cutoff", typed);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
        	}
        });
		c.gridx = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		pane.add(textfieldcutoff_,c);	

		buttongetcutoff_ = new JButton("Get Cutoff");
		buttongetcutoff_.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	textfieldcutoff_.setText(getProperty("Cutoff").getValue());
            }
        });
		c.gridx = 1;
		c.fill = GridBagConstraints.NONE;
		pane.add(buttongetcutoff_,c);

		togglebuttonautocutoff_ = new JToggleButton("Auto");
		togglebuttonautocutoff_.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED){
					setProperty("Monitor cutoff","1");
				} else if(e.getStateChange()==ItemEvent.DESELECTED){
					setProperty("Monitor cutoff","0");
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
					setProperty("Show NMS","1");
				} else if(e.getStateChange()==ItemEvent.DESELECTED){
					setProperty("Show NMS","0");
				}
			}
        });
		c.gridx = 4;
		pane.add(checkboxnms_,c);	
		

		return pane;
	}

	@Override
	public HashMap<String, Parameter> buildDefaultParameters() {		
		return new HashMap<String, Parameter>();
	}

	@Override
	public boolean checkParameters(HashMap<String, Parameter> param) {
		//TODO: default values for the parameters?
		return true;
	}
	
	@Override
	public void propertyChanged(String index) {
		if(index.equals("N")){
				int n = ((IntPropertyInterface) getProperty(index)).getCastValue();
				graph_.addPoint(n);
		} else if(index.equals("Cutoff")){
			if(togglebuttonautocutoff_.isSelected()){
				textfieldcutoff_.setText(getProperty(index).getValue());
			}
		}
	}	
	
	@Override
	protected void createProperties() {
		addProperty(new FloatPropertyInterface("Standard deviation" ,"Standard deviation", this)); 
		addProperty(new FloatPropertyInterface("UV feedback" ,"UV feedback", this));		
		addProperty(new FloatPropertyInterface("Time average" ,"Time average", this));				
		addProperty(new FloatPropertyInterface("N0" ,"Target number of events", this));					
		addProperty(new FloatPropertyInterface("N" ,"Number of events", this));							
		addProperty(new FloatPropertyInterface("Monitor cutoff" ,"Monitor cutoff", this));			
		addProperty(new FloatPropertyInterface("Cutoff" ,"Cutoff on the brightness", this));				
		addProperty(new FloatPropertyInterface("Activate" ,"Activate: use UV or not", this));						
		addProperty(new FloatPropertyInterface("Run activation" ,"Run activation", this));				
		addProperty(new FloatPropertyInterface("Show NMS" ,"Show NMS", this));							
	}

	@Override
	protected void initializeProperties() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initializeParameters() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void changeProperty(String name, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propertyhasChanged(String name, String newvalue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void parameterhasChanged(String label) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutDown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}