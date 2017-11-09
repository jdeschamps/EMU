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

import main.embl.rieslab.htSMLM.ui.uiparameters.BoolUIParameter;
import main.embl.rieslab.htSMLM.ui.uiparameters.StringUIParameter;
import main.embl.rieslab.htSMLM.ui.uiproperties.TwoStateUIProperty;

public class AdditionalControlsPanel extends PropertyPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6559849050710175913L;
	
	//////// Components
	private JToggleButton[] togglebuttons_;
	private TitledBorder border_;

	//////// Properties
	public static String DEVICE_1 = "Two-state device 1";
	public static String DEVICE_2 = "Two-state device 2";
	public static String DEVICE_3 = "Two-state device 3";
	public static String DEVICE_4 = "Two-state device 4";
	
	//////// Parameters
	public static String PARAM_TITLE = "Title";
	public static String PARAM_NAME1 = "Two-state device 1 name";
	public static String PARAM_NAME2 = "Two-state device 2 name";
	public static String PARAM_NAME3 = "Two-state device 3 name";
	public static String PARAM_NAME4 = "Two-state device 4 name";
	public static String PARAM_ENABLE1 = "Enable two-state device 1";
	public static String PARAM_ENABLE2 = "Enable two-state device 2";
	public static String PARAM_ENABLE3 = "Enable two-state device 3";
	public static String PARAM_ENABLE4 = "Enable two-state device 4";
	public static int PARAM_NPOS = 4;
	
	public AdditionalControlsPanel(String label) {
		super(label);
	}
	
	@Override
	public void setupPanel() {
		this.setLayout(new GridBagLayout());
		border_ = BorderFactory.createTitledBorder(null, getLabel(), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, new Color(0,0,0));
		this.setBorder(border_);
		border_.setTitleFont(((TitledBorder) this.getBorder()).getTitleFont().deriveFont(Font.BOLD, 12));
	
		ButtonGroup group=new ButtonGroup();
		togglebuttons_ = new JToggleButton[PARAM_NPOS];
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.insets = new Insets(2,2,2,2);
		c.ipady = 10;
		c.weightx = 0.2;
		
		for(int i=0;i<togglebuttons_.length;i++){
			togglebuttons_[i] = new JToggleButton();
			
			if(i==togglebuttons_.length-1){
				c.insets = new Insets(2,2,30,2);	
			}
			
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
							changeProperty(DEVICE_1, TwoStateUIProperty.getOnStateName());
							break;
						case 1:
							changeProperty(DEVICE_2, TwoStateUIProperty.getOnStateName());
							break;
						case 2:
							changeProperty(DEVICE_3, TwoStateUIProperty.getOnStateName());
							break;
						case 3:
							changeProperty(DEVICE_4, TwoStateUIProperty.getOnStateName());
							break;
						}
					} else if (e.getStateChange()==ItemEvent.DESELECTED){
						switch(pos){
						case 0:
							changeProperty(DEVICE_1, TwoStateUIProperty.getOffStateName());
							break;
						case 1:
							changeProperty(DEVICE_2, TwoStateUIProperty.getOffStateName());
							break;
						case 2:
							changeProperty(DEVICE_3, TwoStateUIProperty.getOffStateName());
							break;
						case 3:
							changeProperty(DEVICE_4, TwoStateUIProperty.getOffStateName());
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
		addUIProperty(new TwoStateUIProperty(this, DEVICE_1,"Position property of the first two-state device."));
		addUIProperty(new TwoStateUIProperty(this, DEVICE_2,"Position property of the second two-state device."));
		addUIProperty(new TwoStateUIProperty(this, DEVICE_3,"Position property of the third two-state device."));
		addUIProperty(new TwoStateUIProperty(this, DEVICE_4,"Position property of the fourth two-state device."));
	}

	@Override
	protected void initializeParameters() {
		addUIParameter(new StringUIParameter(this, PARAM_TITLE,"Title of the section.","Controls"));
		addUIParameter(new StringUIParameter(this, PARAM_NAME1,"Two-state device 1 name, as dislayed in the UI.","BFP"));
		addUIParameter(new StringUIParameter(this, PARAM_NAME2,"Two-state device 2 name, as dislayed in the UI.","3DA"));
		addUIParameter(new StringUIParameter(this, PARAM_NAME3,"Two-state device 3 name, as dislayed in the UI.","Servo 3"));
		addUIParameter(new StringUIParameter(this, PARAM_NAME4,"Two-state device 4 name, as dislayed in the UI.","Servo 4"));
		addUIParameter(new BoolUIParameter(this, PARAM_ENABLE1,"Enable the button corresponding to the first two-state device.", true));
		addUIParameter(new BoolUIParameter(this, PARAM_ENABLE2,"Enable the button corresponding to the second two-state device.",true));
		addUIParameter(new BoolUIParameter(this, PARAM_ENABLE3,"Enable the button corresponding to the third two-state device.",false));
		addUIParameter(new BoolUIParameter(this, PARAM_ENABLE4,"Enable the button corresponding to the fourth two-state device.",false));
	}

	@Override
	protected void changeProperty(String name, String value) {
		if(name.equals(DEVICE_1) || name.equals(DEVICE_2) || name.equals(DEVICE_3) || name.equals(DEVICE_4)){
			getUIProperty(name).setPropertyValue(value);
		}
	}

	@Override
	public void propertyhasChanged(String name, String newvalue) {
		if(name.equals(DEVICE_1)){
			if(newvalue.equals(TwoStateUIProperty.getOnStateName())){
				togglebuttons_[0].setSelected(true);
			} else {
				togglebuttons_[0].setSelected(false);
			}
		} else if(name.equals(DEVICE_2)){
			if(newvalue.equals(TwoStateUIProperty.getOnStateName())){
				togglebuttons_[1].setSelected(true);
			} else {
				togglebuttons_[1].setSelected(false);
			}
		} else if(name.equals(DEVICE_3)){
			if(newvalue.equals(TwoStateUIProperty.getOnStateName())){
				togglebuttons_[2].setSelected(true);
			} else {
				togglebuttons_[2].setSelected(false);
			}
		} else if(name.equals(DEVICE_4)){
			if(newvalue.equals(TwoStateUIProperty.getOnStateName())){
				togglebuttons_[3].setSelected(true);
			} else {
				togglebuttons_[3].setSelected(false);
			}
		}
	}

	@Override
	public void parameterhasChanged(String label) {
		if(label.equals(PARAM_NAME1)){
			String s = ((StringUIParameter) getUIParameter(PARAM_NAME1)).getValue();
			togglebuttons_[0].setText(s);
			getUIProperty(DEVICE_1).setFriendlyName(s);
		} else if(label.equals(PARAM_NAME2)){
			String s = ((StringUIParameter) getUIParameter(PARAM_NAME2)).getValue();
			togglebuttons_[1].setText(s);
			getUIProperty(DEVICE_2).setFriendlyName(s);
		} else if(label.equals(PARAM_NAME3)){
			String s = ((StringUIParameter) getUIParameter(PARAM_NAME3)).getValue();
			togglebuttons_[2].setText(s);
			getUIProperty(DEVICE_3).setFriendlyName(s);
		} else if(label.equals(PARAM_NAME4)){
			String s = ((StringUIParameter) getUIParameter(PARAM_NAME4)).getValue();
			togglebuttons_[3].setText(s);
			getUIProperty(DEVICE_4).setFriendlyName(s);
		} else if(label.equals(PARAM_ENABLE1)){
			boolean b = ((BoolUIParameter) getUIParameter(PARAM_ENABLE1)).getValue();
			togglebuttons_[0].setEnabled(b);
		} else if(label.equals(PARAM_ENABLE2)){
			boolean b = ((BoolUIParameter) getUIParameter(PARAM_ENABLE2)).getValue();
			togglebuttons_[1].setEnabled(b);
		} else if(label.equals(PARAM_ENABLE3)){
			boolean b = ((BoolUIParameter) getUIParameter(PARAM_ENABLE3)).getValue();
			togglebuttons_[2].setEnabled(b);
		} else if(label.equals(PARAM_ENABLE4)){
			boolean b = ((BoolUIParameter) getUIParameter(PARAM_ENABLE4)).getValue();
			togglebuttons_[3].setEnabled(b);
		} else if(label.equals(PARAM_TITLE)){
			border_.setTitle(getUIParameter(PARAM_TITLE).getStringValue());
			this.repaint();
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