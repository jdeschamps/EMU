package main.embl.rieslab.htSMLM.ui;

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

import main.embl.rieslab.htSMLM.ui.uiparameters.ColorUIParameter;
import main.embl.rieslab.htSMLM.ui.uiparameters.StringUIParameter;
import main.embl.rieslab.htSMLM.ui.uiproperties.TwoStateUIProperty;
import main.embl.rieslab.htSMLM.ui.uiproperties.UIProperty;
import main.embl.rieslab.htSMLM.util.utils;

public class LaserControlPanel extends PropertyPanel {

	private static final long serialVersionUID = -6553153910855055671L;
	
	//////// Components
	private JTextField textfieldUser_;
	private JToggleButton togglebutton100_;
	private JToggleButton togglebuttonUser_;
	private JToggleButton togglebutton50_;
	private JToggleButton togglebutton1_;
	private JToggleButton togglebuttonOnOff_;	
	private TitledBorder border_;

	//////// Properties
	public static String LASER_PERCENTAGE = "Laser power percentage";
	public static String LASER_OPERATION = "Laser on/off property";	
	
	//////// Parameters
	public static String PARAM_TITLE = "Title";
	public static String PARAM_COLOR = "Color";	
	private String title_;	
	private Color color_;
	
	public LaserControlPanel(String label) {
		super(label);
	}
	
	@Override
	public void setupPanel() {
		
		this.setLayout(new GridBagLayout());
		
		border_ = BorderFactory.createTitledBorder(null, title_, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, color_);
		this.setBorder(border_);
		border_.setTitleFont(border_.getTitleFont().deriveFont(Font.BOLD, 12));
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.weighty = 0.9;
		c.weightx = 0.7;
		c.insets = new Insets(2,15,2,15);
		
		///////////////////////////////////////////////////////////////////////// User input text field
		textfieldUser_ = new JTextField("20");
		c.fill = GridBagConstraints.BOTH;
		c.gridy = 0;
		this.add(textfieldUser_, c);

		textfieldUser_.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent arg0) {}
			@Override
			public void focusLost(FocusEvent arg0) {
				String typed = getUserInput();
        	    if(typed == null) {
        	        return;
        	    } 
				try {
					int val = Integer.parseInt(typed);
					if (val <= 100 && val >= 0) {
						togglebuttonUser_.setText(typed + "%");
						if (togglebuttonUser_.isSelected()) {
							changeProperty(LASER_PERCENTAGE,typed);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
        	}
         });
		textfieldUser_.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String typed = getUserInput();
        	    if(typed == null) {
        	        return;
        	    } 
				try {
					int val = Integer.parseInt(typed);
					if (val <= 100 && val >= 0) {
						togglebuttonUser_.setText(typed + "%");
						if (togglebuttonUser_.isSelected()) {
							changeProperty(LASER_PERCENTAGE,typed);
						}
					}
				} catch (Exception exc) {
					exc.printStackTrace();
				}
        	}
        });
        
		///////////////////////////////////////////////////////////////////////// Percentage buttons
		togglebutton100_ = new JToggleButton("100%");
		togglebutton100_.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED){
					changeProperty(LASER_PERCENTAGE,"100");
				}
			}
        });
		c.gridy = 1;
		this.add(togglebutton100_, c);

		togglebuttonUser_ = new JToggleButton("20%");
		togglebuttonUser_.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED){
					changeProperty(LASER_PERCENTAGE,getUserInput());
				}
			}
        });
		c.gridy = 2;
		this.add(togglebuttonUser_, c);
		
		togglebutton50_ = new JToggleButton("50%");
		togglebutton50_.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED){
					changeProperty(LASER_PERCENTAGE,"50");
				}
			}
        });
		c.gridy = 3;
		this.add(togglebutton50_, c);

		togglebutton1_ = new JToggleButton("1%");
		togglebutton1_.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED){
					changeProperty(LASER_PERCENTAGE,"1");
				}
			}
        });
		c.gridy = 4;
		this.add(togglebutton1_, c);

        ButtonGroup group=new ButtonGroup();
        group.add(togglebutton100_);
        group.add(togglebuttonUser_);
        group.add(togglebutton50_);
        group.add(togglebutton1_);
		
		///////////////////////////////////////////////////////////////////////// On/Off button
		togglebuttonOnOff_ = new JToggleButton();
		
		togglebuttonOnOff_.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED){
					changeProperty(LASER_OPERATION,TwoStateUIProperty.ON);
				} else if(e.getStateChange()==ItemEvent.DESELECTED){
					changeProperty(LASER_OPERATION,TwoStateUIProperty.OFF);
				}
			}
        });
		
		c.gridy = 5;
		togglebuttonOnOff_.setBorderPainted(false);
		togglebuttonOnOff_.setBorder(null);
		togglebuttonOnOff_.setFocusable(false);
		togglebuttonOnOff_.setContentAreaFilled(false);
		togglebuttonOnOff_.setIcon(new ImageIcon("off.png"));
		togglebuttonOnOff_.setSelectedIcon(new ImageIcon("on.png"));
		togglebuttonOnOff_.setDisabledIcon(new ImageIcon("off.png"));
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
		addUIProperty(new UIProperty(this, LASER_PERCENTAGE,"Power percentage of the laser."));
		addUIProperty(new TwoStateUIProperty(this, LASER_OPERATION,"Laser On/Off operation property."));
	}

	@Override
	protected void initializeParameters() {
		title_ = "Laser";	
		color_ = Color.black;
		
		addUIParameter(new StringUIParameter(this, PARAM_TITLE,"Panel title.",title_));
		addUIParameter(new ColorUIParameter(this, PARAM_COLOR,"Default value for large z stage step.",color_));
	}

	@Override
	protected void changeProperty(String name, String value) {
		if(name.equals(LASER_PERCENTAGE) || name.equals(LASER_OPERATION)){
			getUIProperty(name).setPropertyValue(value);
		}		
	}

	@Override
	public void propertyhasChanged(String name, String newvalue) {
		if(name.equals(LASER_PERCENTAGE)){
			if(utils.isNumeric(newvalue)){
				double val = Double.parseDouble(newvalue) ;
				if(val == 100){
					togglebutton100_.setSelected(true);
				} else if(val == 20){
					togglebutton50_.setSelected(true);
				} else if(val == 0){
					togglebutton1_.setSelected(true);
				} else {
					if(val>0 && val<100){
						togglebuttonUser_.setSelected(true);
						togglebuttonUser_.setText(String.valueOf(val)+"%");
						textfieldUser_.setText(String.valueOf(val));
					} 
				}
			}
		} else if(name.equals(LASER_OPERATION)){
			if(newvalue.equals(TwoStateUIProperty.ON)){
				togglebuttonOnOff_.setSelected(true);
			} else {
				togglebuttonOnOff_.setSelected(false);
			}
		}		
	}

	@Override
	public void parameterhasChanged(String label) {
		if(label.equals(PARAM_TITLE)){
			title_ = ((StringUIParameter) getUIParameter(PARAM_TITLE)).getValue();
			border_.setTitle(title_);
			this.repaint();
		} else if(label.equals(PARAM_COLOR)){
			color_ = ((ColorUIParameter) getUIParameter(PARAM_COLOR)).getValue();
			border_.setTitleColor(color_);
			this.repaint();
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
}
