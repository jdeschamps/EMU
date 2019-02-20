package main.java.embl.rieslab.emu.uiexamples.focuslock;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

import main.java.embl.rieslab.emu.ui.ConfigurablePanel;
import main.java.embl.rieslab.emu.ui.uiparameters.IntUIParameter;
import main.java.embl.rieslab.emu.ui.uiproperties.UIProperty;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.graph.Chart;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.updaters.ChartUpdater;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.updaters.JProgressBarUpdater;

public class QPDPanel extends ConfigurablePanel {

	//////// Thread
	private ChartUpdater chartupdater_;
	private JProgressBarUpdater progressbarupdater_;
	
	//////// Properties
	public final static String QPD_X = "QPD X";
	public final static String QPD_Y = "QPD Y";
	public final static String QPD_Z = "QPD Z";
	
	//////// Parameters
	public final static String PARAM_XYMAX = "XY max";
	public final static String PARAM_ZMAX = "Z max";
	public final static String PARAM_IDLE = "Idle time (ms)";
	
	//////// Default parameters
	private int idle_, xymax_, zmax_; 
	
	//////// Components
	private JProgressBar progressBar_;
	private JToggleButton togglebuttonMonitor_;
	private Chart graph_;
	private JPanel graphpanel_;
	
	public QPDPanel(String label) {
		super(label);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 7082332561417231746L;

	@Override
	public void setupPanel() {
		this.setLayout(new GridBagLayout());
		
		newGraph();
		chartupdater_ = new ChartUpdater(graph_,getUIProperty(QPD_X),getUIProperty(QPD_Y),idle_);
		graphpanel_ = new JPanel();
		graphpanel_.add(graph_.getChart());

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
//		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(2,2,2,2);
		c.gridwidth = 3;
		c.gridheight = 3;
		
		this.add(graphpanel_,c);
		
		c.gridx = 3;
		c.gridy = 0;
		c.fill = GridBagConstraints.VERTICAL;
		c.insets = new Insets(40,2,2,2);
		c.gridwidth = 1;
		c.gridheight = 2;

		progressBar_ = new javax.swing.JProgressBar();
		progressBar_.setOrientation(SwingConstants.VERTICAL);
		progressBar_.setMaximum(zmax_);
		progressBar_.setMinimum(0);
		progressbarupdater_ = new JProgressBarUpdater(progressBar_, getUIProperty(QPD_Z), idle_);
		this.add(progressBar_,c);
		
		c.gridx = 3;
		c.gridy = 2;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(2,10,2,10);
		c.gridwidth = 1;
		c.gridheight = 1;
		togglebuttonMonitor_ = new JToggleButton("Monitor");
		togglebuttonMonitor_.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED){
					monitorQPD(true);
				} else if(e.getStateChange()==ItemEvent.DESELECTED){
					monitorQPD(false);
				}
			}
        });
		this.add(togglebuttonMonitor_,c);

	}
	
	protected void monitorQPD(boolean b) {
		if(b){
			chartupdater_.startUpdater();
			progressbarupdater_.startUpdater();
		} else {
			chartupdater_.stopUpdater();
			progressbarupdater_.stopUpdater();
		}
	}

	private void newGraph(){
		graph_ = new Chart("QPD","X","Y",1,270,270, xymax_);
	}

	@Override
	protected void initializeProperties() {
		addUIProperty(new UIProperty(this, QPD_X,"Read-out property of the QPD X signal."));
		addUIProperty(new UIProperty(this, QPD_Y,"Read-out property of the QPD Y signal."));
		addUIProperty(new UIProperty(this, QPD_Z,"Read-out property of the QPD Z signal."));
	}

	@Override
	protected void initializeParameters() {
		xymax_ = 700;
		zmax_ = 700;
		idle_ = 100;
		
		addUIParameter(new IntUIParameter(this, PARAM_XYMAX,"Maximum X and Y signals value from the QPD to display.",xymax_));
		addUIParameter(new IntUIParameter(this, PARAM_ZMAX,"Maximum Z signal value from the QPD to display.",zmax_));
		addUIParameter(new IntUIParameter(this, PARAM_IDLE,"Idle time (ms) of the QPD signals monitoring.",idle_)); // thread idle time
	}

	@Override
	public void propertyhasChanged(String name, String newvalue) {
		// do nothing
	}

	@Override
	public void parameterhasChanged(String label) {
		if(label.equals(PARAM_XYMAX)){
			if(((IntUIParameter) getUIParameter(PARAM_XYMAX)).getValue() != xymax_){
				xymax_ = ((IntUIParameter) getUIParameter(PARAM_XYMAX)).getValue();
				graphpanel_.remove(graph_.getChart());
				newGraph();
				graphpanel_.add(graph_.getChart());
				graphpanel_.updateUI();
				chartupdater_.changeChart(graph_);
			}
		} else if(label.equals(PARAM_ZMAX)){
			if(((IntUIParameter) getUIParameter(PARAM_ZMAX)).getValue() != zmax_){
				zmax_ = ((IntUIParameter) getUIParameter(PARAM_ZMAX)).getValue();
				progressBar_.setMaximum(zmax_);
			}
		}else if(label.equals(PARAM_IDLE)){
			if(((IntUIParameter) getUIParameter(PARAM_IDLE)).getValue() != idle_){
				idle_ = ((IntUIParameter) getUIParameter(PARAM_IDLE)).getValue();
				chartupdater_.changeIdleTime(idle_);
				progressbarupdater_.changeIdleTime(idle_);
			}
		}
	}

	@Override
	public void shutDown() {
		chartupdater_.stopUpdater();
		progressbarupdater_.stopUpdater();
	}

	@Override
	public String getDescription() {
		return "The "+getLabel()+" panel follows the values of a QPD. Three signals are displayed: X and Y in a 2D chart and Z in a progress bar. The maximum values of each component can be set in the parameters.";
	}

	@Override
	protected void initializeInternalProperties() {
		// Do nothing
	}

	@Override
	public void internalpropertyhasChanged(String label) {
		// Do nothing
	}
}