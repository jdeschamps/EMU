package main.embl.rieslab.htSMLM.ui.MicroscopeControlUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import main.embl.rieslab.htSMLM.configuration.SystemConstants;
import main.embl.rieslab.htSMLM.ui.PropertyPanel;
import main.embl.rieslab.htSMLM.ui.uiparameters.ColorUIParameter;
import main.embl.rieslab.htSMLM.ui.uiparameters.ComboUIParameter;
import main.embl.rieslab.htSMLM.ui.uiparameters.StringUIParameter;
import main.embl.rieslab.htSMLM.ui.uiproperties.FixedStateUIProperty;
import main.embl.rieslab.htSMLM.ui.uiproperties.PropertyFlag;
import main.embl.rieslab.htSMLM.ui.uiproperties.UIProperty;
import main.embl.rieslab.htSMLM.util.BinaryConverter;
import main.embl.rieslab.htSMLM.util.ColorRepository;
import main.embl.rieslab.htSMLM.util.utils;


public class LaserTriggerPanel extends PropertyPanel {

	private static final long serialVersionUID = -6553153910855055671L;
	
	//////// Components
	private JLabel labelbehaviour_;
	private JLabel labelpulselength_;
	private JLabel labelsequence_;
	private JComboBox combobehaviour_;	
	//private JCheckBox usesequence_;
	private JTextField textfieldpulselength_;
	private JTextField textfieldsequence_;
	private JSlider sliderpulse_;
	private TitledBorder border_;

	//////// Properties
	private final static String TRIGGER_BEHAVIOUR = "mode";
	private final static String TRIGGER_SEQUENCE = "sequence";
	private final static String PULSE_LENGTH = "pulse length (trig)";
	
	//////// Parameters
	private final static String PARAM_TITLE = "Name";
	private final static String PARAM_COLOR = "Color";
	private final static String PARAM_DEF_BEHAVIOUR = "Default trigger";
	private final static String PARAM_DEF_SEQUENCE = "Default sequence";
	private String title_, behaviour_, sequence_;
	private Color color_;
	
	public LaserTriggerPanel(String label) {
		super(label);
	}

	@Override
	public void setupPanel() {
		this.setLayout(new GridBagLayout());
		
		border_ = BorderFactory.createTitledBorder(null, title_, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,  null, color_);
		this.setBorder(border_);
		border_.setTitleFont(border_.getTitleFont().deriveFont(Font.BOLD, 12));
	
		
		///////////////////////////////////////////////////////////////////////// Instantiate components
		
		/////////////////////////////////////////////////////// labels
		labelbehaviour_ = new JLabel("Trigger mode:");
		labelpulselength_ = new JLabel("Pulse length (us):");
		labelsequence_ = new JLabel("Trigger sequence:");

		/////////////////////////////////////////////////////// behaviour combobox
		combobehaviour_ = new JComboBox(SystemConstants.FPGA_BEHAVIOURS);
		combobehaviour_.setMinimumSize(new Dimension(40,10));
		combobehaviour_.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		String val= String.valueOf(combobehaviour_.getSelectedIndex());
	    		changeProperty(TRIGGER_BEHAVIOUR,val);
	    	}
        });

		/////////////////////////////////////////////////////// use sequence checkbox
		/*usesequence_ = new JCheckBox();
		usesequence_.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED){
					useseq_ = true;
				} else if(e.getStateChange()==ItemEvent.DESELECTED){
					useseq_ = false;
				}
			}
        });*/

		/////////////////////////////////////////////////////// pulse length
		textfieldpulselength_ = new JTextField();
		textfieldpulselength_.setPreferredSize(new Dimension(60,20));
		textfieldpulselength_.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {	
				String s = textfieldpulselength_.getText();
				if (utils.isInteger(s)) {
					if(Integer.parseInt(s)<=SystemConstants.FPGA_MAX_PULSE){
						sliderpulse_.setValue(Integer.parseInt(s));
						changeProperty(PULSE_LENGTH,s);
					} else {
						sliderpulse_.setValue(SystemConstants.FPGA_MAX_PULSE);
						textfieldpulselength_.setText(String.valueOf(SystemConstants.FPGA_MAX_PULSE));
						changeProperty(PULSE_LENGTH,String.valueOf(SystemConstants.FPGA_MAX_PULSE));
					}
				}
	         }
	    });
		textfieldpulselength_.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent ex) {}

			@Override
			public void focusLost(FocusEvent ex) {
				String s = textfieldpulselength_.getText();
				if (utils.isInteger(s)) {
					if (Integer.parseInt(s) <= SystemConstants.FPGA_MAX_PULSE) {
						sliderpulse_.setValue(Integer.parseInt(s));
						changeProperty(PULSE_LENGTH, s);
					} else {
						sliderpulse_.setValue(SystemConstants.FPGA_MAX_PULSE);
						textfieldpulselength_.setText(String.valueOf(SystemConstants.FPGA_MAX_PULSE));
						changeProperty(PULSE_LENGTH,String.valueOf(SystemConstants.FPGA_MAX_PULSE));
					}
				}
			}
		});
		
		sliderpulse_ = new JSlider(JSlider.HORIZONTAL, 0, SystemConstants.FPGA_MAX_PULSE, 0);
		sliderpulse_.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {				
				  textfieldpulselength_.setText(String.valueOf(sliderpulse_.getValue()));
			      changeProperty(PULSE_LENGTH,String.valueOf(sliderpulse_.getValue()));
			}});
		

		/////////////////////////////////////////////////////// sequence
		textfieldsequence_ = new JTextField();
		textfieldsequence_.setPreferredSize(new Dimension(105,20));
		textfieldsequence_.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {	
				String s = textfieldsequence_.getText();
				if (BinaryConverter.is16bits(s)) {
					textfieldsequence_.setForeground(ColorRepository.getColor("black"));
					String str = String.valueOf(BinaryConverter.getDecimal16bits(s));
					changeProperty(TRIGGER_SEQUENCE,str);
				} else if(BinaryConverter.isBits(s)) {
					textfieldsequence_.setForeground(ColorRepository.getColor("blue"));
				} else {
					textfieldsequence_.setForeground(ColorRepository.getColor("red"));
				}
	         }
	    });
		textfieldsequence_.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent ex) {}

			@Override
			public void focusLost(FocusEvent ex) {
				String s = textfieldsequence_.getText();
				if (BinaryConverter.is16bits(s)) {
					textfieldsequence_.setForeground(ColorRepository.getColor("black"));
					String str = String.valueOf(BinaryConverter.getDecimal16bits(s));
					changeProperty(TRIGGER_SEQUENCE,str);
				
				} else if(BinaryConverter.isBits(s)) {
					textfieldsequence_.setForeground(ColorRepository.getColor("blue"));
				} else {
					textfieldsequence_.setForeground(ColorRepository.getColor("red"));
				}
	         }
		});
		

		/////////////////////////////////////////////////////// place components
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.insets = new Insets(4,0,4,0);
		c.fill = GridBagConstraints.NONE;		
		//c.insets = new Insets(2,5,2,5);
		
		// 0,0
		c.gridwidth = 3;
		this.add(labelbehaviour_,c);

		c.gridy = 1;
		this.add(labelpulselength_,c);
		
		c.gridy = 2;
		c.gridwidth = 6;		
		c.fill = GridBagConstraints.HORIZONTAL;		
		this.add(sliderpulse_,c);
		
		c.gridy = 3;
		c.gridwidth = 3;
		c.fill = GridBagConstraints.NONE;		
		this.add(labelsequence_,c);

		c.gridx = 3;
		c.gridy = 0;
		this.add(combobehaviour_,c);

		c.gridy = 1;
		this.add(textfieldpulselength_,c);

		c.gridy = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(textfieldsequence_,c);

	}

	@Override
	protected void initializeProperties() {	
		LinkedHashMap<String,String> map = new LinkedHashMap<String,String>();
		for(int i=0;i<SystemConstants.FPGA_BEHAVIOURS.length;i++){
			map.put(SystemConstants.FPGA_BEHAVIOURS[i], String.valueOf(i));
		}
		
		addUIProperty(new FixedStateUIProperty(this, getLabel()+" "+TRIGGER_BEHAVIOUR,"Property dictating the behaviour of the laser trigger, from camera to pulsing.", PropertyFlag.LASER,map));
		addUIProperty(new UIProperty(this, getLabel()+" "+TRIGGER_SEQUENCE,"Trigger sequence property, following a 16 bits pattern of 0 (not triggered) and 1 (triggered).", PropertyFlag.LASER));
		addUIProperty(new UIProperty(this, getLabel()+" "+PULSE_LENGTH,"Pulse length of the laser.", PropertyFlag.LASER));
	}

	@Override
	protected void initializeParameters() {
		title_="Laser";
		sequence_=BinaryConverter.getBinary16bits(SystemConstants.FPGA_MAX_SEQUENCE);
		color_=Color.black;		
		
		addUIParameter(new StringUIParameter(this, PARAM_TITLE,"Name of the laser.",title_));
		addUIParameter(new ComboUIParameter(this, PARAM_DEF_BEHAVIOUR,"Default trigger behaviour of the laser.",SystemConstants.FPGA_BEHAVIOURS,4));
		addUIParameter(new StringUIParameter(this, PARAM_DEF_SEQUENCE,"Default triggering sequence of the laser.",sequence_));
		addUIParameter(new ColorUIParameter(this, PARAM_COLOR,"Color of the laser.",color_)); 
	}

	@Override
	protected void changeProperty(String name, String value) {
		if(name.equals(TRIGGER_BEHAVIOUR) || name.equals(PULSE_LENGTH) || name.equals(TRIGGER_SEQUENCE)){
			getUIProperty(getLabel()+" "+name).setPropertyValue(value);
		} 
	}

	@Override
	public void propertyhasChanged(String name, String newvalue) {
		if(name.equals(getLabel()+" "+TRIGGER_BEHAVIOUR)){
			combobehaviour_.setSelectedItem(newvalue);
		} else if(name.equals(getLabel()+" "+TRIGGER_SEQUENCE)){
			if(utils.isInteger(newvalue)){
				textfieldsequence_.setText(BinaryConverter.getBinary16bits(Integer.parseInt(newvalue)));
			} else {
				textfieldsequence_.setText(BinaryConverter.getBinary16bits(SystemConstants.FPGA_MAX_SEQUENCE));
			}
		} else if(name.equals(getLabel()+" "+PULSE_LENGTH)){
			textfieldpulselength_.setText(newvalue);
			if(utils.isInteger(newvalue)){
				sliderpulse_.setValue(Integer.parseInt(newvalue));
			}
		}
	}

	@Override
	public void parameterhasChanged(String label) {
		if(label.equals(PARAM_TITLE)){
			title_ = ((StringUIParameter) getUIParameter(PARAM_TITLE)).getValue();
			border_.setTitle(title_);
			this.repaint();
			getUIProperty(getLabel()+" "+TRIGGER_BEHAVIOUR).setFriendlyName(title_+" "+TRIGGER_BEHAVIOUR);
			getUIProperty(getLabel()+" "+TRIGGER_SEQUENCE).setFriendlyName(title_+" "+TRIGGER_SEQUENCE);
			getUIProperty(getLabel()+" "+PULSE_LENGTH).setFriendlyName(title_+" "+PULSE_LENGTH);
		} else if(label.equals(PARAM_COLOR)){
			color_ = ((ColorUIParameter) getUIParameter(PARAM_COLOR)).getValue();
			border_.setTitleColor(color_);
			this.repaint();
		} else if(label.equals(PARAM_DEF_BEHAVIOUR)){
			behaviour_ = ((ComboUIParameter) getUIParameter(PARAM_DEF_BEHAVIOUR)).getValue();
			combobehaviour_.setSelectedItem(behaviour_); // triggers the action listener
		} else if(label.equals(PARAM_DEF_SEQUENCE)){
			if(BinaryConverter.is16bits(((StringUIParameter) getUIParameter(PARAM_DEF_SEQUENCE)).getValue())){
				sequence_ = ((StringUIParameter) getUIParameter(PARAM_DEF_SEQUENCE)).getValue();
				textfieldsequence_.setText(sequence_);
				changeProperty(TRIGGER_SEQUENCE,String.valueOf(BinaryConverter.getDecimal16bits(sequence_))); // setting text on JTextField does not trigger the action listeners in this case
			}
		}
	}

	@Override
	public void shutDown() {
		// do nothing
	}

	@Override
	public String getDescription() {
		return "The "+getLabel()+" panel controls the triggering of laser thanks to the MicroMojo FPGA system. The triggering behaviour are either on/off, "
				+ "or pulsing on rising/falling edge or simply by following the camera trigger. The pulse length can be set through a text area or a slider. "
				+ "Finally, the laser can be triggered following a sequence of 0 (off) and 1 (triggered). The sequence is 16 bits long. If the sequence set"
				+ "in the text area is made of 0 and 1, albeit with the wrong size, the text is colored in blue. When wrong characters are entered, the text"
				+ "turns red.";
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
