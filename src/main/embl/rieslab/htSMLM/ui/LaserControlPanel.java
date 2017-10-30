package main.embl.rieslab.htSMLM.ui;

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

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import main.de.embl.MicroInterface.dataflow.IntPropertyInterface;
import main.de.embl.MicroInterface.dataflow.UIPanel;
import main.de.embl.MicroInterface.dataflow.StringPropertyInterface;
import main.de.embl.MicroInterface.dataflow.Parameter;
import main.de.embl.MicroInterface.utils.Colors;
import main.de.embl.MicroInterface.utils.utils;
import mmcorej.PropertyType;

public class LaserControlPanel extends UIPanel {

	private static final long serialVersionUID = -6553153910855055671L;
	
	@Override
	public void initComponents() {
		
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createTitledBorder(null, (getParameter("Label").getValues())[0], javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, 
				null, Colors.getColor(getParameter("Color").getValues()[0])));
		
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
							setProperty("Laser Power %", typed);
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
							setProperty("Laser Power %", typed);
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
					setProperty("Laser Power %","100");
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
					setProperty("Laser Power %",getUserInput());
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
					setProperty("Laser Power %","50");
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
					setProperty("Laser Power %","1");
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
					setProperty("Laser On/Off",getParameter("On value").getValues()[0]);
				} else if(e.getStateChange()==ItemEvent.DESELECTED){
					setProperty("Laser On/Off",getParameter("Off value").getValues()[0]);
				}
			}
        });
		
		c.gridy = 5;
		togglebuttonOnOff_.setBorderPainted(false);
		togglebuttonOnOff_.setBorder(null);
		togglebuttonOnOff_.setFocusable(false);
		togglebuttonOnOff_.setContentAreaFilled(false);
		togglebuttonOnOff_.setIcon(new ImageIcon("images/off.png"));
		togglebuttonOnOff_.setSelectedIcon(new ImageIcon("images/on.png"));
		togglebuttonOnOff_.setDisabledIcon(new ImageIcon("images/off.png"));
		this.add(togglebuttonOnOff_, c);
		
	}
	
	private String getUserInput(){
		String s = textfieldUser_.getText();
		if(utils.isInteger(s)){
			return s;
		}
		return null;
	}
	
	//////// Components
	private JTextField textfieldUser_;
	private JToggleButton togglebutton100_;
	private JToggleButton togglebuttonUser_;
	private JToggleButton togglebutton50_;
	private JToggleButton togglebutton1_;
	private JToggleButton togglebuttonOnOff_;


	@Override
	public HashMap<String, Parameter> buildDefaultParameters() {
		HashMap<String, Parameter> defparam = new HashMap<String, Parameter>();
		
		String[] defaultlabelval = {"405"};
		defparam.put("Label",new Parameter("Label",defaultlabelval,PropertyType.String,true));
		String[] defaultcolorval = {"blue"};
		defparam.put("Color",new Parameter("Color of the label",defaultcolorval,PropertyType.String,true));
		String[] defaultonval = {"1"};
		defparam.put("On value",new Parameter("Value of the On property",defaultonval,PropertyType.String,true));
		String[] defaultoffval = {"0"};
		defparam.put("Off value",new Parameter("Value of the On property",defaultoffval,PropertyType.String,true));
		
		return defparam;
	}


	@Override
	public void propertyChanged(String ID) {
		if(ID.equals("Laser Power %")){
			int val = ((IntPropertyInterface) getProperty(ID)).getCastValue();
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
		} else if(ID.equals("Laser On/Off")){
			String val = getProperty(ID).getValue();
			if(val.equals(getParameter("Off value").getValues()[0])){
				togglebuttonOnOff_.setSelected(false);
			} else if (val.equals(getParameter("On value").getValues()[0])){
				togglebuttonOnOff_.setSelected(true);
			} 
		}
	}

	@Override
	protected void createProperties() {
		addProperty(new IntPropertyInterface("Laser Power %","Laser Power percentage",this));
		addProperty(new StringPropertyInterface("Laser On/Off","Laser On/Off",this));
	}

	@Override
	public boolean checkParameters(HashMap<String, Parameter> param) {
		if(param.size() == 4){	
			return Parameter.testProperty(param, getParameter("Label"), "Label", 1) && Parameter.testProperty(param, getParameter("Color"), "Color", 1)
					&& Parameter.testProperty(param, getParameter("On value"), "On value", 1) && Parameter.testProperty(param, getParameter("Off value"), "Off value", 1);
		}
		return false;
	}
}
