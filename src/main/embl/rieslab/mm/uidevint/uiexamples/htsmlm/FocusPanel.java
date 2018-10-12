package main.embl.rieslab.mm.uidevint.uiexamples.htsmlm;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.TitledBorder;

import main.embl.rieslab.mm.uidevint.ui.PropertyPanel;
import main.embl.rieslab.mm.uidevint.ui.graph.TimeChart;
import main.embl.rieslab.mm.uidevint.ui.uiparameters.DoubleUIParameter;
import main.embl.rieslab.mm.uidevint.ui.uiparameters.IntUIParameter;
import main.embl.rieslab.mm.uidevint.ui.uiproperties.TwoStateUIProperty;
import main.embl.rieslab.mm.uidevint.ui.uiproperties.UIProperty;
import main.embl.rieslab.mm.uidevint.uiexamples.htsmlm.flags.FocusStabFlag;
import main.embl.rieslab.mm.uidevint.updaters.TimeChartUpdater;
import main.embl.rieslab.mm.uidevint.utils.utils;

/**
 *
 * @author Joran Deschamps
 */
public class FocusPanel extends PropertyPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6190484716500047549L;

	//////// Components
	private JLabel poslabel_;
	private JLabel smallsteplabel_;
	private JLabel largesteplabel_;
	private JTextField textfieldPosition_;
	private JToggleButton togglebuttonMonitor_;
	private JToggleButton togglebuttonLock_;
	private JButton buttonLargeStepsUp_;
	private JButton buttonSmallStepsUp_;
	private JButton buttonLargeStepsDown_;
	private JButton buttonSmallStepsDown_;
	private JTextField textfieldLargeStep_;
	private JTextField textfieldSmallStep_;
	private JPanel panelLeftControl_;
	private JPanel panelRightControl_;
	private JPanel panelGraph_;
	private TimeChart graph_;
	
	//////// Thread
	private TimeChartUpdater updater_;

	//////// Properties
	public final static String FOCUS_POSITION = "Z stage position";
	public final static String FOCUS_STABILIZATION = "Z stage focus locking";
	
	//////// Parameters
	public final static String PARAM_LARGESTEP = "Large step";
	public final static String PARAM_SMALLSTEP = "Small step";
	public final static String PARAM_IDLE = "Idle time (ms)";
	public final static String PARAM_NPOS = "Number of points";
	
	//////// Default parameters
	private double smallstep_, largestep_;
	private int idle_, npos_; 
	private boolean initialised = false; // used only for initial textfield value
	
	public FocusPanel(String label) {
		super(label);
	}
	
	public void setupPanel() {
		newGraph();
		updater_ = new TimeChartUpdater(graph_,getUIProperty(FOCUS_POSITION),idle_);
		
		this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		this.setBorder(BorderFactory.createTitledBorder(null, getLabel(), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, new Color(0,0,0)));
		((TitledBorder) this.getBorder()).setTitleFont(((TitledBorder) this.getBorder()).getTitleFont().deriveFont(Font.BOLD, 12));
		
		panelLeftControl_ = new JPanel();
		panelGraph_ = new JPanel();
		panelRightControl_ = new JPanel();
		
		initLeftPanel();
		initCentralPanel();
		initRightPanel();
		
/*
		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.05;
		c.gridwidth = 1;
		c.gridheight = 1;
		this.add(panelLeftControl_,c);

		c.gridx = 1;
		c.gridwidth = 3;
		c.weightx = 1.8;
		c.gridheight = 1;
		c.fill = GridBagConstraints.BOTH;
		this.add(panelGraph_,c);

		c.gridx = 4;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0.05;
		c.fill = GridBagConstraints.NONE;
		this.add(panelRightControl_,c);
		*/

		this.add(panelLeftControl_);
		this.add(panelGraph_);
		this.add(panelRightControl_);
		
	}

	private void initLeftPanel(){
		panelLeftControl_.setLayout(new GridBagLayout());
		
		poslabel_ = new JLabel("Position:");

		
		textfieldPosition_ = new JTextField();
		textfieldPosition_.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent arg0) {}
			@Override
			public void focusLost(FocusEvent arg0) {
				String typed = getUserInput();
        	    if(typed == null) {
        	        return;
        	    } 
				try {
					double val = Double.parseDouble(typed);
					if (val >= 0) {
						if (!togglebuttonLock_.isSelected()) {
							changeProperty(FOCUS_POSITION, typed);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
        	}
         });
		textfieldPosition_.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String typed = getUserInput();
        	    if(typed == null) {
        	        return;
        	    } 
				try {
					double val = Double.parseDouble(typed);
					if (val >= 0) {
						if (!togglebuttonLock_.isSelected()) {
							changeProperty(FOCUS_POSITION, typed);
						}
					}
				} catch (Exception exc) {
					exc.printStackTrace();
				}
        	}
        });

		togglebuttonMonitor_ = new JToggleButton("Monitor");
		togglebuttonMonitor_.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED){
					monitorPosition(true);
				} else if(e.getStateChange()==ItemEvent.DESELECTED){
					monitorPosition(false);
				}
			}
        });

		togglebuttonLock_ = new JToggleButton("Lock");
		togglebuttonLock_.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED){
					lockPosition(true);
				} else if(e.getStateChange()==ItemEvent.DESELECTED){
					lockPosition(false);
				}
			}
        });
		
		///// grid bag 
		GridBagConstraints c = new GridBagConstraints();
	
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(2,15,2,15);
		c.gridwidth = 1;
		panelLeftControl_.add(poslabel_, c);

		c.gridy = 1;
		c.insets = new Insets(2,15,40,15);
		panelLeftControl_.add(textfieldPosition_, c);
		
		c.gridy = 4;
		c.insets = new Insets(2,15,2,15);
		panelLeftControl_.add(togglebuttonMonitor_, c);
		
		c.gridy = 5;
		panelLeftControl_.add(togglebuttonLock_, c);
		
	}

	private void initCentralPanel(){
		 panelGraph_.add(graph_.getChart());
	}
	
	private void initRightPanel(){
		panelRightControl_.setLayout(new GridBagLayout());
		
		buttonLargeStepsUp_ = new JButton("^^");
		buttonLargeStepsUp_.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	moveRelativePosition(largestep_);
            }
        });

		buttonSmallStepsUp_ = new JButton("^");
		buttonSmallStepsUp_.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	moveRelativePosition(smallstep_);
            }
        });

		largesteplabel_ = new JLabel(">>");
		textfieldLargeStep_ = new JTextField(String.valueOf(largestep_));
		textfieldLargeStep_.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent arg0) {}
			@Override
			public void focusLost(FocusEvent arg0) {
        	    if(!utils.isNumeric(textfieldLargeStep_.getText())) {
        	        return;
        	    } 
				try {
					largestep_ = Double.parseDouble(textfieldLargeStep_.getText());
				} catch (Exception e) {
					e.printStackTrace();
				}
        	}
         });
		textfieldPosition_.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
        	    if(!utils.isNumeric(textfieldLargeStep_.getText())) {
        	        return;
        	    } 
				try {
					largestep_ = Double.parseDouble(textfieldLargeStep_.getText());
				} catch (Exception ex) {
					ex.printStackTrace();
				}
        	}
        });
		smallsteplabel_ = new JLabel(">");
		textfieldSmallStep_ = new JTextField(String.valueOf(smallstep_));
		textfieldSmallStep_.addFocusListener(new FocusListener() {  
			@Override
			public void focusGained(FocusEvent arg0) {}
			@Override
			public void focusLost(FocusEvent arg0) {
        	    if(!utils.isNumeric(textfieldSmallStep_.getText())) {
        	        return;
        	    } 
				try {
					smallstep_ = Double.parseDouble(textfieldSmallStep_.getText());
				} catch (Exception e) {
					e.printStackTrace();
				}
        	}
         });
		textfieldSmallStep_.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
        	    if(!utils.isNumeric(textfieldSmallStep_.getText())) {
        	        return;
        	    } 
				try {
					smallstep_ = Double.parseDouble(textfieldSmallStep_.getText());
				} catch (Exception ex) {
					ex.printStackTrace();
				}
        	}
        });
		buttonSmallStepsDown_ = new JButton("v");
		buttonSmallStepsDown_.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	moveRelativePosition(-smallstep_);
            }
        });
		buttonLargeStepsDown_ = new JButton("vv");
		buttonLargeStepsDown_.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	moveRelativePosition(-largestep_);
            }
        });
		
		
		// grid bag constraints
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(2,10,2,10);
		c.gridwidth = 2;		
		panelRightControl_.add(buttonLargeStepsUp_, c);
		
		c.gridy = 1;		
		panelRightControl_.add(buttonSmallStepsUp_, c);

		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.insets = new Insets(2,1,2,1);
		panelRightControl_.add(largesteplabel_, c);

		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 2;
		c.insets = new Insets(2,10,2,10);
		panelRightControl_.add(textfieldLargeStep_, c);
		
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		c.insets = new Insets(2,1,2,1);
		panelRightControl_.add(smallsteplabel_, c);

		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 2;
		c.insets = new Insets(2,10,2,10);
		panelRightControl_.add(textfieldSmallStep_, c);
		
		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 2;
		c.insets = new Insets(2,10,2,10);
		panelRightControl_.add(buttonSmallStepsDown_, c);

		c.gridx = 1;
		c.gridy = 5;
		c.gridwidth = 2;
		panelRightControl_.add(buttonLargeStepsDown_, c);
	}
	
	protected void moveRelativePosition(double step) {
    	String s = getUIPropertyValue(FOCUS_POSITION);
    	
    	if (utils.isNumeric(s)) {
    		double val = Double.parseDouble(s)+step;
    		changeProperty(FOCUS_POSITION,String.valueOf(val));
    	}		
	}

	protected void lockPosition(boolean b) {
		if(b){
			changeProperty(FOCUS_STABILIZATION,TwoStateUIProperty.getOnStateName());
		} else {
			changeProperty(FOCUS_STABILIZATION,TwoStateUIProperty.getOffStateName());
		}
	}

	protected void monitorPosition(boolean b) {
		if(b){
			updater_.startUpdater();
		} else {
			updater_.stopUpdater();
		}
	}
	
	public boolean isMonitoring(){
		return togglebuttonMonitor_.isSelected();
	}
	
	private String getUserInput(){
		String s = textfieldPosition_.getText();
		if(utils.isNumeric(s)){
			return s;
		}
		return null;
	}
	
	private void newGraph(){
		graph_ = new TimeChart("position","time","position",npos_,310,150,false);
	}

	@Override
	protected void initializeProperties() {
		addUIProperty(new UIProperty(this, FOCUS_POSITION,"Position of the stage, used to move the stage and monitor its position."));
		addUIProperty(new TwoStateUIProperty(this, FOCUS_STABILIZATION,"Property used for focus stabilization.", new FocusStabFlag()));
	}
	
	@Override
	protected void initializeParameters() {
		smallstep_ = 0.2;
		largestep_ = 2;
		idle_ = 100;
		npos_ = 30; 
		
		addUIParameter(new DoubleUIParameter(this, PARAM_LARGESTEP,"Default value for large z stage step.",largestep_));
		addUIParameter(new DoubleUIParameter(this, PARAM_SMALLSTEP,"Default value for small z stage step.",smallstep_));
		addUIParameter(new IntUIParameter(this, PARAM_IDLE,"Idle time in ms of the stage position monitoring.",idle_)); // thread idle time
		addUIParameter(new IntUIParameter(this, PARAM_NPOS,"Number of stage positions displayed in the chart.",npos_)); // number of point in the graph
	}
	
	@Override
	protected void changeProperty(String name, String value) {
		if(name.equals(FOCUS_POSITION) || name.equals(FOCUS_STABILIZATION)){
			getUIProperty(name).setPropertyValue(value);
		}
	}

	@Override
	public void propertyhasChanged(String name, String newvalue) {
		if(name.equals(FOCUS_POSITION)){
			if(utils.isNumeric(newvalue)){
				if(!initialised){
					initialised = true;
					textfieldPosition_.setText(newvalue);
				}
			}
		} else if(name.equals(FOCUS_STABILIZATION)){
			if(newvalue.equals(TwoStateUIProperty.getOnStateName())){
				togglebuttonLock_.setSelected(true);
			} else {
				togglebuttonLock_.setSelected(false);
			}
		}
	}

	

	@Override
	public void parameterhasChanged(String label) {
		if(label.equals(PARAM_LARGESTEP)){
			largestep_ = ((DoubleUIParameter) getUIParameter(PARAM_LARGESTEP)).getValue();
			textfieldLargeStep_.setText(String.valueOf(largestep_));
		} else if(label.equals(PARAM_SMALLSTEP)){
			smallstep_ = ((DoubleUIParameter) getUIParameter(PARAM_SMALLSTEP)).getValue();
			textfieldSmallStep_.setText(String.valueOf(smallstep_));
		}else if(label.equals(PARAM_IDLE)){
			if(((IntUIParameter) getUIParameter(PARAM_IDLE)).getValue() != idle_){
				idle_ = ((IntUIParameter) getUIParameter(PARAM_IDLE)).getValue();
				updater_.changeIdleTime(idle_);
			}
		}else if(label.equals(PARAM_NPOS)){
			if(((IntUIParameter) getUIParameter(PARAM_NPOS)).getValue() != npos_){
				npos_ = ((IntUIParameter) getUIParameter(PARAM_NPOS)).getValue();
				panelGraph_.remove(graph_.getChart());
				newGraph();
				panelGraph_.add(graph_.getChart());
				panelGraph_.updateUI();
				updater_.changeChart(graph_);
			}
		}
	}

	@Override
	public void shutDown() {
		updater_.stopUpdater();
	}

	@Override
	public String getDescription() {
		return "The "+getLabel()+" panel controls the Z stage of the microscope. It allows monitoring of the stage position. In addition, small and large steps buttons can move the stage up and down. "
				+ "The locking property corresponds to focus stabilization and is very specific to certain stages.";
	}

	@Override
	protected void initializeInternalProperties() {
		// Do nothing
	}

	@Override
	protected void changeInternalProperty(String name, String value) {
		// Do nothing
	}

	@Override
	public void internalpropertyhasChanged(String label) {
		// Do nothing
	}
}
