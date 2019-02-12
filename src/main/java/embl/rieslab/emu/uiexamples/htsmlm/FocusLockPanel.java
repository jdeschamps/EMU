package main.java.embl.rieslab.emu.uiexamples.htsmlm;

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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.TitledBorder;

import main.java.embl.rieslab.emu.ui.ConfigurablePanel;
import main.java.embl.rieslab.emu.ui.uiproperties.TwoStateUIProperty;
import main.java.embl.rieslab.emu.ui.uiproperties.UIProperty;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.flags.FocusLockFlag;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.others.TogglePower;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.others.ToggleSlider;
import main.java.embl.rieslab.emu.utils.ColorRepository;
import main.java.embl.rieslab.emu.utils.utils;

public class FocusLockPanel extends ConfigurablePanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6136665461633874956L;

	//////// Components
	private JTextField textfieldUserPower_;
	private JSlider sliderPower_;
	private JSlider sliderFinea_;
	private JSlider sliderFineb_;
	private JToggleButton togglebuttonLaser_;
	private ToggleSlider togglesliderenableFine_;
	private JLabel fineaperc_;
	private JLabel finebperc_;

	//////// Properties
	public final static String LASER_OPERATION = "operation";
	public final static String LASER_ENABLEFINE = "enable fine";	
	public final static String LASER_POWER = "laser power";	
	public final static String LASER_PERCFINEA = "fine a (%)";	
	public final static String LASER_PERCFINEB = "fine b (%)";	
	public final static String LASER_MAXPOWER = "max power";	
	
	/////// Convenience variables
	private int max_power;
	
	public FocusLockPanel(String label) {
		super(label);
	}

	@Override
	public void setupPanel() {

		///////////////////////////////////////////////////////////////////////////// set-up components
		// Power text field
		textfieldUserPower_ = new JTextField(String.valueOf(max_power));
		textfieldUserPower_.setPreferredSize(new Dimension(35,20));
		textfieldUserPower_.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent arg0) {}
			@Override
			public void focusLost(FocusEvent arg0) {
				if(isPropertyChangeAllowed()){
					String typed = textfieldUserPower_.getText();
					if(!utils.isNumeric(typed)){
						return;
					}
	
					try {
						double val = Double.parseDouble(typed);
						if (val <= max_power && val >= 0) {
							changeProperty(LASER_POWER,typed);
							sliderPower_.setValue((int) val);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
        	}
        });
		
		textfieldUserPower_.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				if(isPropertyChangeAllowed()){
					String typed = textfieldUserPower_.getText();
					if(!utils.isNumeric(typed)){
						return;
					}
	
					try {
						double val = Double.parseDouble(typed);
						if (val <= max_power && val >= 0) {
							changeProperty(LASER_POWER,typed);
							sliderPower_.setValue((int) val);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
        	}
        });
		
		// slider channel 1
		sliderPower_ = new JSlider(JSlider.HORIZONTAL, 0, (int) max_power, 0);
		sliderPower_.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {				
				textfieldUserPower_.setText(String.valueOf(sliderPower_.getValue()));
				changeProperty(LASER_POWER,String.valueOf(sliderPower_.getValue()));
			}});
		
		

		// slider fina a
		sliderFinea_ = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
		sliderFinea_.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				if(sliderFinea_.getValue() < 100){
					fineaperc_.setText(String.valueOf(sliderFinea_.getValue())+" %");				
				} else {
					fineaperc_.setText(String.valueOf(sliderFinea_.getValue())+" %");		
				}
				changeProperty(LASER_PERCFINEA,String.valueOf(sliderFinea_.getValue()));
			}});

		// slider fina b
		sliderFineb_ = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
		sliderFineb_.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {	
				if(sliderFineb_.getValue() < 100){
					finebperc_.setText(String.valueOf(sliderFineb_.getValue())+" %");				
				} else {
					finebperc_.setText(String.valueOf(sliderFineb_.getValue())+" %");		
				}				
				changeProperty(LASER_PERCFINEB,String.valueOf(sliderFineb_.getValue()));
			}});
		
		
		// laser enable
		/*togglebuttonLaser_ = new JToggleButton();
		togglebuttonLaser_.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED){
					changeProperty(LASER_OPERATION,TwoStateUIProperty.getOnStateName());
				} else if(e.getStateChange()==ItemEvent.DESELECTED){
					changeProperty(LASER_OPERATION,TwoStateUIProperty.getOffStateName());
				}
			}
        });
		togglebuttonLaser_.setBorderPainted(false);
		togglebuttonLaser_.setBorder(null);
		togglebuttonLaser_.setFocusable(false);
		togglebuttonLaser_.setContentAreaFilled(false);

		togglebuttonLaser_.setIcon(new ImageIcon(getClass().getResource("/images/off.png")));
		togglebuttonLaser_.setSelectedIcon(new ImageIcon(getClass().getResource("/images/on.png")));
		togglebuttonLaser_.setDisabledIcon(new ImageIcon(getClass().getResource("/images/off.png")));	*/
		togglebuttonLaser_ = new TogglePower();
		togglebuttonLaser_.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED){
					changeProperty(LASER_OPERATION,TwoStateUIProperty.getOnStateName());
				} else if(e.getStateChange()==ItemEvent.DESELECTED){
					changeProperty(LASER_OPERATION,TwoStateUIProperty.getOffStateName());
				}
			}
        });
		
		// Fine enable
		togglesliderenableFine_ = new ToggleSlider();
		togglesliderenableFine_.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED){
					changeProperty(LASER_ENABLEFINE,TwoStateUIProperty.getOnStateName());
				} else if(e.getStateChange()==ItemEvent.DESELECTED){
					changeProperty(LASER_ENABLEFINE,TwoStateUIProperty.getOffStateName());
				}
			}
        });

		fineaperc_ = new JLabel("100 %");
		finebperc_ = new JLabel("100 %");

		// others
		JLabel fineAperc = new JLabel("a");
		JLabel finebperc = new JLabel("b");
		JLabel power = new JLabel("Power (mW):");
		
		///////////////////////////////////////////////////////////////////////////// Channel 1
		
		JPanel panel2 = new JPanel();
		panel2.setLayout(new GridBagLayout());
		TitledBorder border2 = BorderFactory.createTitledBorder(null, "Power", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, 
				null, ColorRepository.getColor(ColorRepository.strblack));
		panel2.setBorder(border2);

		GridBagConstraints c2 = new GridBagConstraints();
		c2.gridx = 1;
		c2.gridy = 0;
		c2.ipadx = 5;
		c2.ipady = 5;
		c2.weightx = 0.2;
		c2.weighty = 0.3;
		c2.fill = GridBagConstraints.BOTH;
		panel2.add(power, c2);

		c2.gridx = 2;
		panel2.add(textfieldUserPower_, c2);
		

		c2.gridx = 3;
		panel2.add(togglebuttonLaser_, c2);

		c2.gridx = 0;
		c2.gridy = 4;
		c2.gridwidth = 4;
		c2.weightx = 0.9;
		c2.weighty = 0.5;
		panel2.add(sliderPower_, c2);


		///////////////////////////////////////////////////////////////////////////// fine
		JPanel panelfine = new JPanel();
		panelfine.setLayout(new GridBagLayout());
		TitledBorder borderfine = BorderFactory.createTitledBorder(null, "Fine", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, 
				null, ColorRepository.getColor(ColorRepository.strblack));
		panelfine.setBorder(borderfine);
		
		// gridbad layout
		GridBagConstraints cfine = new GridBagConstraints();
		cfine.fill = GridBagConstraints.HORIZONTAL;
		cfine.ipadx = 35;
		cfine.ipady = 2;
		cfine.gridx = 0;
		cfine.gridy = 0;
		panelfine.add(togglesliderenableFine_, cfine);

		cfine.gridx = 1;
		cfine.gridy = 1;
		cfine.ipadx = 5;
		panelfine.add(fineAperc, cfine);
		
		cfine.gridy = 2;
		panelfine.add(finebperc, cfine);
		
		cfine.gridx = 2;
		cfine.gridy = 1;
		cfine.ipadx = 4;
		cfine.gridwidth = 3;
		panelfine.add(sliderFinea_, cfine);
		
		cfine.gridy = 2;
		panelfine.add(sliderFineb_, cfine);
		
		cfine.gridx = 5;
		cfine.gridy = 1;
		cfine.ipadx = 5;
		cfine.gridwidth = 1;
		cfine.insets = new Insets(2,35,2,2);
		panelfine.add(fineaperc_, cfine);
		
		cfine.gridy = 2;
		cfine.insets = new Insets(2,35,2,2);
		panelfine.add(finebperc_, cfine);

		
		//////////////////////////////////////////////////////////////////////////// Main panel
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.4;
		c.weighty = 0.2;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.gridheight = 1;
		this.add(panel2,c);
		
		c.gridx = 0;
		c.gridy = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(panelfine,c);
		
		c.gridx = 0;
		c.gridy = 2;
		c.weighty = 0.8;
		c.fill = GridBagConstraints.BOTH;
		this.add(new JPanel(),c);
	}
	
	@Override
	protected void initializeProperties() {
		max_power = 200;
		
		addUIProperty(new UIProperty(this, getLabel()+" "+LASER_POWER,"iBeamSmart Power (mW).", new FocusLockFlag()));
		addUIProperty(new UIProperty(this, getLabel()+" "+LASER_PERCFINEA,"iBeamSmart Power percentage of fine a.", new FocusLockFlag()));
		addUIProperty(new UIProperty(this, getLabel()+" "+LASER_PERCFINEB,"iBeamSmart Power percentage of fine b.", new FocusLockFlag()));
		addUIProperty(new UIProperty(this, getLabel()+" "+LASER_MAXPOWER,"iBeamSmart Maximum power (mW).", new FocusLockFlag()));

		addUIProperty(new TwoStateUIProperty(this,getLabel()+" "+LASER_OPERATION,"iBeamSmart On/Off operation property.", new FocusLockFlag()));
		addUIProperty(new TwoStateUIProperty(this,getLabel()+" "+LASER_ENABLEFINE,"iBeamSmart Enable property of fine.", new FocusLockFlag()));
	}


	@Override
	protected void changeProperty(String name, String value) {
		if(name.equals(LASER_OPERATION) || name.equals(LASER_ENABLEFINE) || name.equals(LASER_POWER) || name.equals(LASER_PERCFINEA)
				 || name.equals(LASER_PERCFINEB)){
			setUIPropertyValue(name,value);
		}	
	}

	@Override
	public void propertyhasChanged(String name, String newvalue) {
		turnOffPropertyChange();
		if(name.equals(getLabel()+" "+LASER_POWER)){
			if(utils.isNumeric(newvalue)){
				double val = Double.parseDouble(newvalue);
				if(val>=0 && val<=max_power){
					textfieldUserPower_.setText(String.valueOf(val));
					sliderPower_.setValue((int) val);	
				}
			}
		} else if(name.equals(getLabel()+" "+LASER_PERCFINEA)){
			if(utils.isNumeric(newvalue)){
				double val = Double.parseDouble(newvalue);
				if(val>=0 && val<=100){
					if(val < 100){
						fineaperc_.setText("  "+String.valueOf(val)+" %");				
					} else {
						fineaperc_.setText(String.valueOf(val)+" %");		
					}	
					sliderFinea_.setValue((int) val);	
				}
			}
		} else if(name.equals(getLabel()+" "+LASER_PERCFINEB)){
			if(utils.isNumeric(newvalue)){
				double val = Double.parseDouble(newvalue);
				if(val>=0 && val<=100){	
					if(val < 100){
						finebperc_.setText("  "+String.valueOf(val)+" %");				
					} else {
						finebperc_.setText(String.valueOf(val)+" %");		
					}	
					sliderFineb_.setValue((int) val);	
				}
			}
		} else if(name.equals(getLabel()+" "+LASER_OPERATION)){
			if(newvalue.equals(TwoStateUIProperty.getOnStateName())){
				togglebuttonLaser_.setSelected(true);
			} else {  
				togglebuttonLaser_.setSelected(false);
			}
		} else if(name.equals(getLabel()+" "+LASER_ENABLEFINE)){
			if(newvalue.equals(TwoStateUIProperty.getOnStateName())){
				togglesliderenableFine_.setSelected(true);
			} else {  
				togglesliderenableFine_.setSelected(false);
			}
		} else if(name.equals(getLabel()+" "+LASER_MAXPOWER)){
			if(utils.isNumeric(newvalue)){
				double val = Double.parseDouble(newvalue);
				max_power = (int) val;
				if(sliderPower_ != null){
					sliderPower_.setMaximum(max_power);
				}
			}
		}
		turnOnPropertyChange();
	}


	@Override
	public void shutDown() {
		// Do nothing
	}

	@Override
	public String getDescription() {
		return "This panel controls the focus-lock laser fron Toptica, iBeam-smart.";
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
	
	@Override
	protected void initializeParameters() {		
		// Do nothing
	}

	@Override
	public void parameterhasChanged(String label) {
		// Do nothing		
	}
}
