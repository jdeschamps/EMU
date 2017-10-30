package main.embl.rieslab.htSMLM.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JSlider;
import javax.swing.JTextField;

import main.de.embl.MicroInterface.dataflow.IntPropertyInterface;
import main.de.embl.MicroInterface.dataflow.UIPanel;
import main.de.embl.MicroInterface.dataflow.Parameter;
import main.de.embl.MicroInterface.utils.Colors;
import main.de.embl.MicroInterface.utils.utils;
import main.embl.rieslab.htSMLM.ui.misc.LogarithmicJSlider;
import mmcorej.PropertyType;

public class LaserPulsingPanel extends UIPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
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
		
		///////////////////////////////////////////////////////////////////////// User value text field
		textfieldvalue_ = new JTextField();
		c.fill = GridBagConstraints.BOTH;
		c.gridy = 0;
		this.add(textfieldvalue_, c);
		
		textfieldvalue_.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent arg0) {}
			@Override
			public void focusLost(FocusEvent arg0) {
	       	    String typed = textfieldvalue_.getText();
        	    if(!utils.isInteger(typed)) {
        	        return;
        	    }  
        	    int val = Integer.parseInt(typed);
        	    if(val<=logslider_.getMaxWithin()){
        	    	logslider_.setValueWithin(val);
        	    	setProperty("Laser pulse",textfieldvalue_.getText());
        	    } else {
        	    	logslider_.setValueWithin(logslider_.getMaxWithin());
        	    	setProperty("Laser pulse",String.valueOf(logslider_.getMaxWithin()));
        	    }
        	}
         });
		textfieldvalue_.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
	       	    String typed = textfieldvalue_.getText();
        	    if(!utils.isInteger(typed)) {
        	        return;
        	    }  
        	    int val = Integer.parseInt(typed);
        	    if(val<=logslider_.getMaxWithin()){
        	    	logslider_.setValueWithin(val);
        	    	setProperty("Laser pulse",textfieldvalue_.getText());
        	    } else {
        	    	logslider_.setValueWithin(logslider_.getMaxWithin());
        	    	setProperty("Laser pulse",String.valueOf(logslider_.getMaxWithin()));
        	    }
        	}
        });
		

		///////////////////////////////////////////////////////////////////////// User max text field
		textfieldmax_ = new JTextField();
		c.fill = GridBagConstraints.BOTH;
		c.gridy = 1;
		this.add(textfieldmax_, c);
		
		textfieldmax_.addActionListener(new java.awt.event.ActionListener() {
	         public void actionPerformed(java.awt.event.ActionEvent evt) {	
	        	 String s = textfieldmax_.getText();
		    	 if(utils.isInteger(s)){
		    		 int max = Integer.parseInt(s);
		    		 if(max>1000*((IntPropertyInterface)getProperty("Camera exposure")).getCastValue()){
		    			 max = 1000*((IntPropertyInterface)getProperty("Camera exposure")).getCastValue();
		    		 }
		    		 logslider_.setMaxWithin(max);
		    		 if(logslider_.getValue()>logslider_.getMaxWithin()){
		    			 logslider_.setValueWithin(logslider_.getMaxWithin());
		    			 textfieldvalue_.setText(String.valueOf(logslider_.getValue()));
		    			 setProperty("Laser pulse",String.valueOf(logslider_.getValue()));
		    		 }
		    	 } 
	         }
	    });
		textfieldmax_.addFocusListener(new FocusListener() {
          @Override
          public void focusGained(FocusEvent ex) {
          }

          @Override
          public void focusLost(FocusEvent ex) {
	        	 String s = textfieldmax_.getText();
		    	 if(utils.isInteger(s)){
		    		 int max = Integer.parseInt(s);
		    		 if(max>1000*((IntPropertyInterface)getProperty("Camera exposure")).getCastValue()){
		    			 max = 1000*((IntPropertyInterface)getProperty("Camera exposure")).getCastValue();
		    		 }
		    		 logslider_.setMaxWithin(max);
		    		 if(logslider_.getValue()>logslider_.getMaxWithin()){
		    			 logslider_.setValueWithin(logslider_.getMaxWithin());
		    			 textfieldvalue_.setText(String.valueOf(logslider_.getValue()));
		    			 setProperty("Laser pulse",String.valueOf(logslider_.getValue()));
		    		 }
		    	 } 
	         }
	    });
		
		///////////////////////////////////////////////////////////////////////// Log JSlider
		logslider_ = new LogarithmicJSlider(JSlider.VERTICAL,1, 10000, 10);
		
		logslider_.setPaintTicks(true);
		logslider_.setPaintTrack(true);
		logslider_.setPaintLabels(true);
		logslider_.setMajorTickSpacing(10);
		logslider_.setMinorTickSpacing(10);  
		logslider_.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {				
				  int val = logslider_.getValue();
				  logslider_.setValueWithin(val);
				  try{
					  textfieldvalue_.setText(String.valueOf(logslider_.getValue()));
					  setProperty("Laser pulse",String.valueOf(logslider_.getValue()));
				  } catch(Exception ex){
					  ex.printStackTrace();
				  }  
			}});
		
		c.gridy = 2;
		c.gridheight = 8;
		this.add(logslider_, c);
	}

	
	//////// Components
	private JTextField textfieldmax_;
	private JTextField textfieldvalue_;
	private LogarithmicJSlider logslider_;

	@Override
	public HashMap<String,Parameter> buildDefaultParameters() {
		HashMap<String,Parameter> defparam = new HashMap<String,Parameter>();
		
		String[] defaultlabelval = {"Laser pulsing"};
		defparam.put("Label",new Parameter("Label",defaultlabelval,PropertyType.String,true));
		String[] defaultcolorval = {"black"};
		defparam.put("Color",new Parameter("Color",defaultcolorval,PropertyType.String,true));
		
		return defparam;
	}

	@Override
	public void propertyChanged(String ID) {
		if(ID.equals("Laser pulse")){
			int val = ((IntPropertyInterface) getProperty(ID)).getCastValue();
			int max = logslider_.getMaxWithin();
			if(val>max){
				logslider_.setMaximum(val);
			}
			logslider_.setValue(val);
			textfieldvalue_.setText(((IntPropertyInterface) getProperty(ID)).getValue());
		} else if (ID.equals("Camera exposure")){
			int val = ((IntPropertyInterface) getProperty(ID)).getCastValue();
			logslider_.setMaximum(1000*val);
		}	
	}

	@Override
	protected void createProperties() {
		addProperty(new IntPropertyInterface("Laser pulse","Laser pulse length",this));
		addProperty(new IntPropertyInterface("Camera exposure","Camera exposure",this));		
	}

	@Override
	public boolean checkParameters(HashMap<String, Parameter> param) {
		if(param.size() == 2){	
			return Parameter.testProperty(param, getParameter("Label"), "Label", 1) && Parameter.testProperty(param, getParameter("Color"), "Color", 1);
		}
		return false;
	}
}
