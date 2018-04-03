package main.embl.rieslab.htSMLM.ui.MicroscopeControlUI;

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
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.TitledBorder;

import main.embl.rieslab.htSMLM.ui.PropertyPanel;
import main.embl.rieslab.htSMLM.ui.components.ToggleSlider;
import main.embl.rieslab.htSMLM.ui.uiproperties.PropertyFlag;
import main.embl.rieslab.htSMLM.ui.uiproperties.TwoStateUIProperty;
import main.embl.rieslab.htSMLM.ui.uiproperties.UIProperty;
import main.embl.rieslab.htSMLM.util.ColorRepository;
import main.embl.rieslab.htSMLM.util.utils;

public class FocusLockPanel  extends PropertyPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6136665461633874956L;

	//////// Components
	private JTextField textfieldUserch1_;
	private JTextField textfieldUserch2_;
	private JSlider sliderCh1_;
	private JSlider sliderCh2_;
	private JSlider sliderFinea_;
	private JSlider sliderFineb_;
	private JToggleButton togglebuttonCh1_;
	private JToggleButton togglebuttonCh2_;
	private JToggleButton togglebuttonLaser_;
	private ToggleSlider togglesliderenableFine_;
	private ToggleSlider togglesliderenableExt_;
	private JLabel fineaperc_;
	private JLabel finebperc_;

	//////// Properties
	public final static String LASER_OPERATION = "focus-lock laser operation";
	public final static String LASER_ENABLECH1 = "enable channel1";	
	public final static String LASER_ENABLECH2 = "enable channel2";
	public final static String LASER_ENABLEEXT = "enable ext trigger";	
	public final static String LASER_ENABLEFINE = "enable fine";	
	public final static String LASER_POWERCH1 = "power channel1";	
	public final static String LASER_POWERCH2 = "power channel2";	
	public final static String LASER_PERCFINEA = "percentage fine a";	
	public final static String LASER_PERCFINEB = "percentage fine b";	
	public final static String LASER_MAXPOWER = "ibeamSmart maximum power";	
	
	/////// Convenience variables
	private int max_power;
	
	public FocusLockPanel(String label) {
		super(label);
	}

	@Override
	public void setupPanel() {

		///////////////////////////////////////////////////////////////////////////// set-up components
		// channel 1 text field
		textfieldUserch1_ = new JTextField(String.valueOf(max_power));
		textfieldUserch1_.setPreferredSize(new Dimension(35,20));
		textfieldUserch1_.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent arg0) {}
			@Override
			public void focusLost(FocusEvent arg0) {
				if(isPropertyChangeAllowed()){
					String typed = textfieldUserch1_.getText();
					if(!utils.isNumeric(typed)){
						return;
					}
	
					try {
						double val = Double.parseDouble(typed);
						if (val <= max_power && val >= 0) {
							changeProperty(LASER_POWERCH1,typed);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
        	}
        });
		
		textfieldUserch1_.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				if(isPropertyChangeAllowed()){
					String typed = textfieldUserch1_.getText();
					if(!utils.isNumeric(typed)){
						return;
					}
	
					try {
						double val = Double.parseDouble(typed);
						if (val <= max_power && val >= 0) {
							changeProperty(LASER_POWERCH1,typed);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
        	}
        });

		// channel 2 text field
		textfieldUserch2_ = new JTextField(String.valueOf(max_power));
		textfieldUserch2_.setPreferredSize(new Dimension(35,20));
		textfieldUserch2_.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent arg0) {}
			@Override
			public void focusLost(FocusEvent arg0) {
				if(isPropertyChangeAllowed()){
					String typed = textfieldUserch2_.getText();
					if(!utils.isNumeric(typed)){
						return;
					}
	
					try {
						double val = Double.parseDouble(typed);
						if (val <= max_power && val >= 0) {
							changeProperty(LASER_POWERCH2,typed);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
        	}
        });
		
		textfieldUserch2_.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				if(isPropertyChangeAllowed()){
					String typed = textfieldUserch2_.getText();
					if(!utils.isNumeric(typed)){
						return;
					}
	
					try {
						double val = Double.parseDouble(typed);
						if (val <= max_power && val >= 0) {
							changeProperty(LASER_POWERCH2,typed);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
        	}
        });

		// slider channel 1
		sliderCh1_ = new JSlider(JSlider.HORIZONTAL, 0, (int) max_power, 0);
		sliderCh1_.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {				
				textfieldUserch1_.setText(String.valueOf(sliderCh1_.getValue()));
				changeProperty(LASER_POWERCH1,String.valueOf(sliderCh1_.getValue()));
			}});
		
		// slider channel 1
		sliderCh2_ = new JSlider(JSlider.HORIZONTAL, 0, (int) max_power, 0);
		sliderCh2_.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {				
				textfieldUserch2_.setText(String.valueOf(sliderCh2_.getValue()));
				changeProperty(LASER_POWERCH2,String.valueOf(sliderCh2_.getValue()));
			}});
		

		// slider fina a
		sliderFinea_ = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
		sliderFinea_.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				if(sliderFinea_.getValue() < 100){
					fineaperc_.setText("  "+String.valueOf(sliderFinea_.getValue())+" %");				
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
					finebperc_.setText("  "+String.valueOf(sliderFineb_.getValue())+" %");				
				} else {
					finebperc_.setText(String.valueOf(sliderFineb_.getValue())+" %");		
				}				
				changeProperty(LASER_PERCFINEB,String.valueOf(sliderFineb_.getValue()));
			}});
		
		// enable channel 1
		togglebuttonCh1_ = new JToggleButton();
		togglebuttonCh1_.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED){
					changeProperty(LASER_ENABLECH1,TwoStateUIProperty.getOnStateName());
				} else if(e.getStateChange()==ItemEvent.DESELECTED){
					changeProperty(LASER_ENABLECH1,TwoStateUIProperty.getOffStateName());
				}
			}
        });		
		togglebuttonCh1_.setBorderPainted(false);
		togglebuttonCh1_.setBorder(null);
		togglebuttonCh1_.setFocusable(false);
		togglebuttonCh1_.setContentAreaFilled(false);
		togglebuttonCh1_.setIcon(new ImageIcon("ht-SMLM/off.png"));
		togglebuttonCh1_.setSelectedIcon(new ImageIcon("ht-SMLM/on.png"));
		togglebuttonCh1_.setDisabledIcon(new ImageIcon("ht-SMLM/off.png"));	

		
		// enable channel 2
		togglebuttonCh2_ = new JToggleButton();
		togglebuttonCh2_.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED){
					changeProperty(LASER_ENABLECH2,TwoStateUIProperty.getOnStateName());
				} else if(e.getStateChange()==ItemEvent.DESELECTED){
					changeProperty(LASER_ENABLECH2,TwoStateUIProperty.getOffStateName());
				}
			}
        });
		togglebuttonCh2_.setBorderPainted(false);
		togglebuttonCh2_.setBorder(null);
		togglebuttonCh2_.setFocusable(false);
		togglebuttonCh2_.setContentAreaFilled(false);
		togglebuttonCh2_.setIcon(new ImageIcon("ht-SMLM/off.png"));
		togglebuttonCh2_.setSelectedIcon(new ImageIcon("ht-SMLM/on.png"));
		togglebuttonCh2_.setDisabledIcon(new ImageIcon("ht-SMLM/off.png"));	
		
		// laser enable
		togglebuttonLaser_ = new JToggleButton();
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
		togglebuttonLaser_.setIcon(new ImageIcon("ht-SMLM/off.png"));
		togglebuttonLaser_.setSelectedIcon(new ImageIcon("ht-SMLM/on.png"));
		togglebuttonLaser_.setDisabledIcon(new ImageIcon("ht-SMLM/off.png"));	

		// laser enable
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

		// laser enable
		togglesliderenableExt_ = new ToggleSlider();
		togglesliderenableExt_.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED){
					changeProperty(LASER_ENABLEEXT,TwoStateUIProperty.getOnStateName());
				} else if(e.getStateChange()==ItemEvent.DESELECTED){
					changeProperty(LASER_ENABLEEXT,TwoStateUIProperty.getOffStateName());
				}
			}
        });

		fineaperc_ = new JLabel("100 %");
		finebperc_ = new JLabel("100 %");

		// others
		JLabel enableext = new JLabel("Enable ext");
		JLabel enablelaser = new JLabel("Enable laser");
		JLabel enablefine = new JLabel("Enable fine");
		JLabel fineAperc = new JLabel("a");
		JLabel finebperc = new JLabel("b");
		
		///////////////////////////////////////////////////////////////////////////// Channel 1
		JPanel panelch1 = new JPanel();
		panelch1.setLayout(new GridBagLayout());
		TitledBorder border = BorderFactory.createTitledBorder(null, "Channel 1", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, 
				null, ColorRepository.getColor(ColorRepository.strblack));
		panelch1.setBorder(border);
		
		// gridbad layout
		GridBagConstraints c1 = new GridBagConstraints();
		c1.gridx = 0;
		c1.fill = GridBagConstraints.HORIZONTAL;
		c1.gridy = 0;
		panelch1.add(textfieldUserch1_, c1);

		c1.gridx = 4;
		panelch1.add(togglebuttonCh1_, c1);
		
		c1.gridx = 0;
		c1.gridy = 1;
		c1.gridwidth = 5;
		panelch1.add(sliderCh1_, c1);
		

		///////////////////////////////////////////////////////////////////////////// Channel 2
		JPanel panelch2 = new JPanel();
		panelch2.setLayout(new GridBagLayout());
		TitledBorder border2 = BorderFactory.createTitledBorder(null, "Channel 2", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, 
				null, ColorRepository.getColor(ColorRepository.strblack));
		panelch2.setBorder(border2);
		
		// gridbad layout
		GridBagConstraints c2 = new GridBagConstraints();
		c2.gridx = 0;
		c2.fill = GridBagConstraints.HORIZONTAL;
		c2.gridy = 0;
		panelch2.add(textfieldUserch2_, c2);
		
		c2.gridx = 4;
		panelch2.add(togglebuttonCh2_, c2);
		
		c2.gridx = 0;
		c2.gridy = 1;
		c2.gridwidth = 5;
		panelch2.add(sliderCh2_, c2);
		
		
		///////////////////////////////////////////////////////////////////////////// global panel
		JPanel panelglob = new JPanel();
		panelglob.setLayout(new GridBagLayout());
		TitledBorder borderglob = BorderFactory.createTitledBorder(null, "Global", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, 
				null, ColorRepository.getColor(ColorRepository.strblack));
		panelglob.setBorder(borderglob);
		
		// gridbad layout
		GridBagConstraints cglob = new GridBagConstraints();
		cglob.gridx = 0;
		cglob.insets = new Insets(2,15,2,15);
		cglob.fill = GridBagConstraints.VERTICAL;
		cglob.gridy = 0;
		panelglob.add(enableext, cglob);
		
		cglob.gridy = 1;
		panelglob.add(togglesliderenableExt_, cglob);

		cglob.gridy = 2;
		panelglob.add(enablelaser, cglob);
		
		cglob.gridy = 3;
		panelglob.add(togglebuttonLaser_, cglob);

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
		panelfine.add(enablefine, cfine);
		
		cfine.gridy = 1;
		panelfine.add(togglesliderenableFine_, cfine);

		cfine.gridx = 1;
		cfine.gridy = 0;
		cfine.ipadx = 5;
		panelfine.add(fineAperc, cfine);
		
		cfine.gridy = 1;
		panelfine.add(finebperc, cfine);
		
		cfine.gridx = 2;
		cfine.gridy = 0;
		cfine.ipadx = 4;
		panelfine.add(sliderFinea_, cfine);
		
		cfine.gridy = 1;
		panelfine.add(sliderFineb_, cfine);
		
		cfine.gridx = 3;
		cfine.gridy = 0;
		cfine.ipadx = 5;
		cglob.insets = new Insets(2,35,2,2);
		panelfine.add(fineaperc_, cfine);
		
		cfine.gridy = 1;
		cglob.insets = new Insets(2,35,2,2);
		panelfine.add(finebperc_, cfine);

		
		//////////////////////////////////////////////////////////////////////////// Main panel
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = 0;
		c.gridwidth = 2;
		c.gridy = 0;
		this.add(panelch1,c);
		
		c.gridy = 1;
		this.add(panelch2,c);

		c.gridx = 2;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 2;
		c.fill = GridBagConstraints.VERTICAL;
		this.add(panelglob,c);
		
		c.gridx = 0;
		c.gridy = 2;
		c.gridheight = 1;
		c.gridwidth = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(panelfine,c);
	}
	
	@Override
	protected void initializeProperties() {
		max_power = 200;
		
		addUIProperty(new UIProperty(this, getLabel()+" "+LASER_POWERCH1,"iBeamSmart Power (mW) of channel 1 (bias).", PropertyFlag.FOCUSLOCK));
		addUIProperty(new UIProperty(this, getLabel()+" "+LASER_POWERCH2,"iBeamSmart Power (mW) of channel 2 (high-level).", PropertyFlag.FOCUSLOCK));
		addUIProperty(new UIProperty(this, getLabel()+" "+LASER_PERCFINEA,"iBeamSmart Power percentage of fine a.", PropertyFlag.FOCUSLOCK));
		addUIProperty(new UIProperty(this, getLabel()+" "+LASER_PERCFINEB,"iBeamSmart Power percentage of fine b.", PropertyFlag.FOCUSLOCK));
		addUIProperty(new UIProperty(this, getLabel()+" "+LASER_MAXPOWER,"iBeamSmart Maximum power (mW).", PropertyFlag.FOCUSLOCK));

		addUIProperty(new TwoStateUIProperty(this,getLabel()+" "+LASER_OPERATION,"iBeamSmart On/Off operation property.", PropertyFlag.FOCUSLOCK));
		addUIProperty(new TwoStateUIProperty(this,getLabel()+" "+LASER_ENABLECH1,"iBeamSmart Enable property of channel 1 (bias).", PropertyFlag.FOCUSLOCK));
		addUIProperty(new TwoStateUIProperty(this,getLabel()+" "+LASER_ENABLECH2,"iBeamSmart Enable property of channel 2 (high-level).", PropertyFlag.FOCUSLOCK));
		addUIProperty(new TwoStateUIProperty(this,getLabel()+" "+LASER_ENABLEEXT,"iBeamSmart Enable property of external trigger.", PropertyFlag.FOCUSLOCK));
		addUIProperty(new TwoStateUIProperty(this,getLabel()+" "+LASER_ENABLEFINE,"iBeamSmart Enable property of fine.", PropertyFlag.FOCUSLOCK));
	}


	@Override
	protected void changeProperty(String name, String value) {
		if(name.equals(LASER_OPERATION) || name.equals(LASER_ENABLECH1) || name.equals(LASER_ENABLECH2) || name.equals(LASER_ENABLEEXT)
				 || name.equals(LASER_ENABLEFINE) || name.equals(LASER_POWERCH1) || name.equals(LASER_POWERCH2) || name.equals(LASER_PERCFINEA)
				 || name.equals(LASER_PERCFINEB)){
			getUIProperty(getLabel()+" "+name).setPropertyValue(value);
		}	
	}

	@Override
	public void propertyhasChanged(String name, String newvalue) {
		turnOffPropertyChange();
		if(name.equals(getLabel()+" "+LASER_POWERCH1)){
			if(utils.isNumeric(newvalue)){
				double val = Double.parseDouble(newvalue);
				if(val>=0 && val<=max_power){
					textfieldUserch1_.setText(String.valueOf(val));
					sliderCh1_.setValue((int) val);	
				}
			}
		} else if(name.equals(getLabel()+" "+LASER_POWERCH2)){
			if(utils.isNumeric(newvalue)){
				double val = Double.parseDouble(newvalue);
				if(val>=0 && val<=max_power){
					textfieldUserch2_.setText(String.valueOf(val));
					sliderCh2_.setValue((int) val);	
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
		} else if(name.equals(getLabel()+" "+LASER_ENABLECH1)){
			if(newvalue.equals(TwoStateUIProperty.getOnStateName())){
				togglebuttonCh1_.setSelected(true);
			} else {  
				togglebuttonCh1_.setSelected(false);
			}
		} else if(name.equals(getLabel()+" "+LASER_ENABLECH2)){
			if(newvalue.equals(TwoStateUIProperty.getOnStateName())){
				togglebuttonCh2_.setSelected(true);
			} else {  
				togglebuttonCh2_.setSelected(false);
			}
		} else if(name.equals(getLabel()+" "+LASER_ENABLEEXT)){
			if(newvalue.equals(TwoStateUIProperty.getOnStateName())){
				togglesliderenableExt_.setSelected(true);
			} else {  
				togglesliderenableExt_.setSelected(false);
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
				if(sliderCh1_ != null){
					sliderCh1_.setMaximum(max_power);
				}
				if(sliderCh2_ != null){
					sliderCh2_.setMaximum(max_power);
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
