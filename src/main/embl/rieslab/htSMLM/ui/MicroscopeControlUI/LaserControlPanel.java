package main.embl.rieslab.htSMLM.ui.MicroscopeControlUI;

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
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.TitledBorder;

import main.embl.rieslab.htSMLM.ui.PropertyPanel;
import main.embl.rieslab.htSMLM.ui.uiparameters.ColorUIParameter;
import main.embl.rieslab.htSMLM.ui.uiparameters.IntUIParameter;
import main.embl.rieslab.htSMLM.ui.uiparameters.StringUIParameter;
import main.embl.rieslab.htSMLM.ui.uiproperties.PropertyFlag;
import main.embl.rieslab.htSMLM.ui.uiproperties.TwoStateUIProperty;
import main.embl.rieslab.htSMLM.ui.uiproperties.UIProperty;
import main.embl.rieslab.htSMLM.util.utils;

public class LaserControlPanel extends PropertyPanel {

	private static final long serialVersionUID = -6553153910855055671L;
	
	//////// Components
	private JTextField textfieldUser_;
	private JToggleButton togglebutton100_;
	private JToggleButton togglebuttonUser_;
	private JToggleButton togglebutton20_;
	private JToggleButton togglebutton1_;
	private JToggleButton togglebuttonOnOff_;	
	private TitledBorder border_;

	//////// Properties
	public final static String LASER_PERCENTAGE = "power percentage";
	public final static String LASER_OPERATION = "on/off";	
	
	//////// Parameters
	public final static String PARAM_TITLE = "Name";
	public final static String PARAM_COLOR = "Color";	
	public final static String PARAM_SCALING = "Scaling max";	
	private String title_;	
	private Color color_;
	private int scaling_;
	
	/////// Convenience variables
	
	public LaserControlPanel(String label) {
		super(label);
	}
	
	@Override
	public void setupPanel() {
		
		this.setLayout(new GridBagLayout());
		
		border_ = BorderFactory.createTitledBorder(null, title_, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, color_);
		this.setBorder(border_);
		border_.setTitleFont(border_.getTitleFont().deriveFont(Font.BOLD, 12));
				
		///////////////////////////////////////////////////////////////////////// User input text field
		textfieldUser_ = new JTextField("50");

		textfieldUser_.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent arg0) {}
			@Override
			public void focusLost(FocusEvent arg0) {
				if(isPropertyChangeAllowed()){
	
					String typed = getUserInput();
	        	    if(typed == null) {
	        	        return;
	        	    } 
					try {
						int val = Integer.parseInt(typed);
						if (val <= 100 && val >= 0) {
							togglebuttonUser_.setText(typed + "%");
							if (togglebuttonUser_.isSelected()) {
				        	    int value = (int) (val*scaling_/100);
								changeProperty(LASER_PERCENTAGE,String.valueOf(value));
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
        	}
         });
		textfieldUser_.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(isPropertyChangeAllowed()){
					String typed = getUserInput();
	        	    if(typed == null) {
	        	        return;
	        	    } 
					try {
						int val = Integer.parseInt(typed);
						if (val <= 100 && val >= 0) {
							togglebuttonUser_.setText(typed + "%");
							if (togglebuttonUser_.isSelected()) {
				        	    int value = (int) (val*scaling_/100);
								changeProperty(LASER_PERCENTAGE,String.valueOf(value));
							}
						}
					} catch (Exception exc) {
						exc.printStackTrace();
					}
				}
        	}
        });
        
		///////////////////////////////////////////////////////////////////////// Percentage buttons
		togglebutton100_ = new JToggleButton("100%");
		togglebutton100_.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED){
					changeProperty(LASER_PERCENTAGE,String.valueOf(scaling_));
				}
			}
        });		

		togglebuttonUser_ = new JToggleButton("50%");
		togglebuttonUser_.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(isPropertyChangeAllowed()){
					if(e.getStateChange()==ItemEvent.SELECTED){
						String typed = getUserInput();
		        	    if(typed == null) {
		        	        return;
		        	    }
		        	    int val = (int) (Double.valueOf(typed)*scaling_/100);
						changeProperty(LASER_PERCENTAGE,String.valueOf(val));
					}
				}
			}
        });
		
		togglebutton20_ = new JToggleButton("20%");
		togglebutton20_.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED){
					int val = (int) (scaling_*0.2);
					changeProperty(LASER_PERCENTAGE,String.valueOf(val));
				}
			}
        });

		togglebutton1_ = new JToggleButton("1%");
		togglebutton1_.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED){
					int val = (int) (scaling_*0.01);
					changeProperty(LASER_PERCENTAGE,String.valueOf(val));
				}
			}
        });

        ButtonGroup group=new ButtonGroup();
        group.add(togglebutton100_);
        group.add(togglebuttonUser_);
        group.add(togglebutton20_);
        group.add(togglebutton1_);
        
        /*Font buttonfont = togglebutton100_.getFont();
        Font newfont = buttonfont.deriveFont((float) 10);
        togglebutton100_.setFont(newfont);
        togglebuttonUser_.setFont(newfont);
        togglebutton20_.setFont(newfont);
        togglebutton1_.setFont(newfont);*/

        togglebutton100_.setMargin(new Insets(2,8,2,8));
        togglebuttonUser_.setMargin(new Insets(2,8,2,8));
        togglebutton20_.setMargin(new Insets(2,8,2,8));
        togglebutton1_.setMargin(new Insets(2,8,2,8));
        
		///////////////////////////////////////////////////////////////////////// On/Off button
		togglebuttonOnOff_ = new JToggleButton();
		
		togglebuttonOnOff_.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED){
					changeProperty(LASER_OPERATION,TwoStateUIProperty.getOnStateName());
				} else if(e.getStateChange()==ItemEvent.DESELECTED){
					changeProperty(LASER_OPERATION,TwoStateUIProperty.getOffStateName());
				}
			}
        });

		togglebuttonOnOff_.setBorderPainted(false);
		togglebuttonOnOff_.setBorder(null);
		togglebuttonOnOff_.setFocusable(false);
		togglebuttonOnOff_.setContentAreaFilled(false);
		togglebuttonOnOff_.setIcon(new ImageIcon("ht-SMLM/off.png"));
		togglebuttonOnOff_.setSelectedIcon(new ImageIcon("ht-SMLM/on.png"));
		togglebuttonOnOff_.setDisabledIcon(new ImageIcon("ht-SMLM/off.png"));	
		
		
		////// grid bag layout
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.weighty = 0.9;
		c.weightx = 0.7;
		c.insets = new Insets(2,15,2,15);
		c.fill = GridBagConstraints.BOTH;
		c.gridy = 0;
		this.add(textfieldUser_, c);
		c.ipady = 8;
		c.gridy = 1;
		this.add(togglebuttonUser_, c);
		c.gridy = 2;
		this.add(togglebutton100_, c);
		c.gridy = 3;
		this.add(togglebutton20_, c);
		c.gridy = 4;
		this.add(togglebutton1_, c);
		c.ipady = 0;
		c.gridy = 5;
		this.add(togglebuttonOnOff_, c);
		
	}
	
	private String getUserInput(){
		String s = textfieldUser_.getText();
		if(utils.isInteger(s)){
			return s;
		}
		return null;
	}

	@Override
	protected void initializeProperties() {
		addUIProperty(new UIProperty(this, getLabel()+" "+LASER_PERCENTAGE,"Power percentage of the laser.", PropertyFlag.LASER));
		addUIProperty(new TwoStateUIProperty(this,getLabel()+" "+LASER_OPERATION,"Laser On/Off operation property.", PropertyFlag.LASER));
	}

	@Override
	protected void initializeParameters() {
		title_ = "Laser";	
		color_ = Color.black;
		scaling_ = 100;
		
		addUIParameter(new StringUIParameter(this, PARAM_TITLE,"Panel title.",title_));
		addUIParameter(new ColorUIParameter(this, PARAM_COLOR,"Laser color.",color_));
		addUIParameter(new IntUIParameter(this, PARAM_SCALING,"Maximum value of the laser percentage after scaling.",scaling_));
	}

	@Override
	protected void changeProperty(String name, String value) {
		if(name.equals(LASER_PERCENTAGE) || name.equals(LASER_OPERATION)){
			getUIProperty(getLabel()+" "+name).setPropertyValue(value);
		}		
	}

	@Override
	public void propertyhasChanged(String name, String newvalue) {
		turnOffPropertyChange();
		if(name.equals(getLabel()+" "+LASER_PERCENTAGE)){
			if(utils.isNumeric(newvalue)){
				int val = (int) Double.parseDouble(newvalue);
				
				// scale if necessary
				if(scaling_ != 100){
					val = (int) (val*scaling_/100);
				}
				
				if(val == 100){
					togglebutton100_.setSelected(true);
				} else if(val == 20){
					togglebutton20_.setSelected(true);
				} else if(val == 1){
					togglebutton1_.setSelected(true);
				} else {
					if(val>=0 && val<100){
						togglebuttonUser_.setSelected(true);
						togglebuttonUser_.setText(String.valueOf(val)+"%");
						textfieldUser_.setText(String.valueOf(val));
					} 
				}
			}
		} else if(name.equals(getLabel()+" "+LASER_OPERATION)){
			if(newvalue.equals(TwoStateUIProperty.getOnStateName())){
				togglebuttonOnOff_.setSelected(true);
			} else {  
				togglebuttonOnOff_.setSelected(false);
			}
		}		
		turnOnPropertyChange();
	}

	@Override
	public void parameterhasChanged(String label) {
		if(label.equals(PARAM_TITLE)){
			title_ = ((StringUIParameter) getUIParameter(PARAM_TITLE)).getValue();
			border_.setTitle(title_);
			this.repaint();
			getUIProperty(getLabel()+" "+LASER_PERCENTAGE).setFriendlyName(title_+" "+LASER_PERCENTAGE);
			getUIProperty(getLabel()+" "+LASER_OPERATION).setFriendlyName(title_+" "+LASER_OPERATION);
		} else if(label.equals(PARAM_COLOR)){
			color_ = ((ColorUIParameter) getUIParameter(PARAM_COLOR)).getValue();
			border_.setTitleColor(color_);
			this.repaint();
		}else if(label.equals(PARAM_SCALING)){
			scaling_ = ((IntUIParameter) getUIParameter(PARAM_SCALING)).getValue();
		}
	}

	@Override
	public void shutDown() {
		// nothing to do
	}

	@Override
	public String getDescription() {
		return "The "+getLabel()+" panel controls a single laser and allows for rapid on/off and power percentage changes.";
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
