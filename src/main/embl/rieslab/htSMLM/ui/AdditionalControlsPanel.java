package main.embl.rieslab.htSMLM.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;
import javax.swing.border.TitledBorder;

import main.embl.rieslab.htSMLM.controller.Configuration;
import main.embl.rieslab.htSMLM.ui.uiparameters.StringUIParameter;
import main.embl.rieslab.htSMLM.ui.uiproperties.TwoStateUIProperty;

public class AdditionalControlsPanel extends PropertyPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6559849050710175913L;
	
	//////// Components
	private JToggleButton[] togglebuttons_;

	//////// Properties
	public static String SERVO_1 = "Two-state servo 1";
	public static String SERVO_2 = "Two-state servo 2";
	public static String SERVO_3 = "Two-state servo 3";
	public static String SERVO_4 = "Two-state servo 4";
	
	//////// Parameters
	public static String PARAM_NAME1 = "Servo 1 name";
	public static String PARAM_NAME2 = "Servo 2 name";
	public static String PARAM_NAME3 = "Servo 3 name";
	public static String PARAM_NAME4 = "Servo 4 name";
	public static int PARAM_NPOS = 4;
	
	public AdditionalControlsPanel(String label) {
		super(label);
	}
	
	@Override
	public void setupPanel() {
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createTitledBorder(null, getLabel(), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, new Color(0,0,0)));
		((TitledBorder) this.getBorder()).setTitleFont(((TitledBorder) this.getBorder()).getTitleFont().deriveFont(Font.BOLD, 12));

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 0.9;
		c.weightx = 0.7;
		c.gridx = 0;
		c.insets = new Insets(2,0,2,0);
	
		ButtonGroup group=new ButtonGroup();
	
		togglebuttons_ = new JToggleButton[PARAM_NPOS];
		for(int i=0;i<togglebuttons_.length;i++){
			togglebuttons_[i] = new JToggleButton();
			
			c.gridy = i;
			this.add(togglebuttons_[i], c);
			
			group.add(togglebuttons_[i]);
			
			togglebuttons_[i].addItemListener(new ItemListener(){
				@Override
				public void itemStateChanged(ItemEvent e) {
					int pos = getButtonNumber((JToggleButton) e.getItem());
					if(e.getStateChange()==ItemEvent.SELECTED){
						switch(pos){
						case 0:
							changeProperty(SERVO_1, TwoStateUIProperty.getOnStateName());
							break;
						case 1:
							changeProperty(SERVO_2, TwoStateUIProperty.getOnStateName());
							break;
						case 2:
							changeProperty(SERVO_3, TwoStateUIProperty.getOnStateName());
							break;
						case 3:
							changeProperty(SERVO_4, TwoStateUIProperty.getOnStateName());
							break;
						}
					} else if (e.getStateChange()==ItemEvent.DESELECTED){
						switch(pos){
						case 0:
							changeProperty(SERVO_1, TwoStateUIProperty.getOffStateName());
							break;
						case 1:
							changeProperty(SERVO_2, TwoStateUIProperty.getOffStateName());
							break;
						case 2:
							changeProperty(SERVO_3, TwoStateUIProperty.getOffStateName());
							break;
						case 3:
							changeProperty(SERVO_4, TwoStateUIProperty.getOffStateName());
							break;
						}
					} 
				}
	       });
		}  
	}
	
	private int getButtonNumber(JToggleButton button){
		int pos = -1;
		for(int i=0; i<togglebuttons_.length;i++){
			if(togglebuttons_[i].equals(button)){
				pos = i;
			}
		}
		return pos;
	}
	
	@Override
	protected void initializeProperties() {
		addUIProperty(new TwoStateUIProperty(this, SERVO_1,"Position property of the first servo."));
		addUIProperty(new TwoStateUIProperty(this, SERVO_2,"Position property of the first servo."));
		addUIProperty(new TwoStateUIProperty(this, SERVO_3,"Position property of the first servo."));
		addUIProperty(new TwoStateUIProperty(this, SERVO_4,"Position property of the first servo."));
	}

	@Override
	protected void initializeParameters() {
		addUIParameter(new StringUIParameter(this, PARAM_NAME1,"Servo 1 name.","Servo 1"));
		addUIParameter(new StringUIParameter(this, PARAM_NAME2,"Servo 2 name.","Servo 2"));
		addUIParameter(new StringUIParameter(this, PARAM_NAME3,"Servo 3 name.","Servo 3"));
		addUIParameter(new StringUIParameter(this, PARAM_NAME4,"Servo 4 name.","Servo 4"));
	}

	@Override
	protected void changeProperty(String name, String value) {
		if(name.equals(SERVO_1) || name.equals(SERVO_2) || name.equals(SERVO_3) || name.equals(SERVO_4)){
			getUIProperty(name).setPropertyValue(value);
		}
	}

	@Override
	public void propertyhasChanged(String name, String newvalue) {
		if(name.equals(SERVO_1)){
			if(newvalue.equals(Configuration.KEY_UNALLOCATED)){
				togglebuttons_[0].setEnabled(false);
			} else {
				if(newvalue.equals(TwoStateUIProperty.getOnStateName())){
					togglebuttons_[0].setSelected(true);
				} else {
					togglebuttons_[0].setSelected(false);
				}
			}
		} else if(name.equals(SERVO_2)){
			if(newvalue.equals(Configuration.KEY_UNALLOCATED)){
				togglebuttons_[1].setEnabled(false);
			} else {
				if(newvalue.equals(TwoStateUIProperty.getOnStateName())){
					togglebuttons_[1].setSelected(true);
				} else {
					togglebuttons_[1].setSelected(false);
				}
			}
		} else if(name.equals(SERVO_3)){
			if(newvalue.equals(Configuration.KEY_UNALLOCATED)){
				togglebuttons_[2].setEnabled(false);
			} else {
				if(newvalue.equals(TwoStateUIProperty.getOnStateName())){
					togglebuttons_[2].setSelected(true);
				} else {
					togglebuttons_[2].setSelected(false);
				}
			}
		} else if(name.equals(SERVO_4)){
			if(newvalue.equals(Configuration.KEY_UNALLOCATED)){
				togglebuttons_[3].setEnabled(false);
			} else {
				if(newvalue.equals(TwoStateUIProperty.getOnStateName())){
					togglebuttons_[3].setSelected(true);
				} else {
					togglebuttons_[3].setSelected(false);
				}
			}
		}
	}

	@Override
	public void parameterhasChanged(String label) {
		if(label.equals(PARAM_NAME1)){
			String s = ((StringUIParameter) getUIParameter(PARAM_NAME1)).getValue();
			togglebuttons_[0].setText(s);
		} else if(label.equals(PARAM_NAME2)){
			String s = ((StringUIParameter) getUIParameter(PARAM_NAME2)).getValue();
			togglebuttons_[1].setText(s);
		} else if(label.equals(PARAM_NAME3)){
			String s = ((StringUIParameter) getUIParameter(PARAM_NAME3)).getValue();
			togglebuttons_[2].setText(s);
		} else if(label.equals(PARAM_NAME4)){
			String s = ((StringUIParameter) getUIParameter(PARAM_NAME4)).getValue();
			togglebuttons_[3].setText(s);
		}
	}

	@Override
	public void shutDown() {
		// do nothing
	}

	@Override
	public String getDescription() {
		return "The "+getLabel()+" panel makes use of two-state buttons to control servos.";
	}
}