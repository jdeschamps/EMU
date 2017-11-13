package main.embl.rieslab.htSMLM.ui;

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

import javax.swing.BorderFactory;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import main.embl.rieslab.htSMLM.ui.internalproperty.IntInternalProperty;
import main.embl.rieslab.htSMLM.ui.misc.LogarithmicJSlider;
import main.embl.rieslab.htSMLM.ui.uiparameters.ColorUIParameter;
import main.embl.rieslab.htSMLM.ui.uiparameters.StringUIParameter;
import main.embl.rieslab.htSMLM.ui.uiproperties.UIProperty;
import main.embl.rieslab.htSMLM.util.utils;

public class LaserPulsingPanel extends PropertyPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1379090644780233443L;

	//////// Components
	private JTextField textfieldmax_;
	private JTextField textfieldvalue_;
	private LogarithmicJSlider logslider_;
	private TitledBorder border_;

	//////// Properties
	public static String CAMERA_EXPOSURE = "Camera exposure";
	public static String LASER_PULSE = "Activation pulse length (UI)";	
	
	//////// Parameters
	public static String PARAM_TITLE = "Title";
	public static String PARAM_COLOR = "Color";	
	private String title_;	
	private Color color_;
	
	//////// Internal property
	public static String INTERNAL_MAXPULSE = "Maximum pulse";
	
	//////// Convenience variables
	private int maxpulse_;
	
	public LaserPulsingPanel(String label) {
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
		c.gridheight = 1;
		c.gridwidth = 1;
		c.insets = new Insets(1,15,1,15);
		
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
        	    	changeProperty(LASER_PULSE,typed);
        	    } else {
        	    	logslider_.setValueWithin(logslider_.getMaxWithin());
        	    	changeProperty(LASER_PULSE,String.valueOf(logslider_.getMaxWithin()));
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
        	    	changeProperty(LASER_PULSE,typed);
        	    } else {
        	    	logslider_.setValueWithin(logslider_.getMaxWithin());
        	    	changeProperty(LASER_PULSE,String.valueOf(logslider_.getMaxWithin()));
        	    }
        	}
        });
		

		///////////////////////////////////////////////////////////////////////// User max text field
		textfieldmax_ = new JTextField("10000");
		textfieldmax_.setPreferredSize(new Dimension(30,15));
		c.fill = GridBagConstraints.BOTH;
		c.gridy = 1;
		c.ipady = 7;
		this.add(textfieldmax_, c);
		
		textfieldmax_.addActionListener(new java.awt.event.ActionListener() {
	         public void actionPerformed(java.awt.event.ActionEvent evt) {	
	        	 String s = textfieldmax_.getText();
					if (utils.isInteger(s)) {
						int max = Integer.parseInt(s);
						String str = getUIProperty(CAMERA_EXPOSURE).getPropertyValue();
						
						if (utils.isNumeric(str)) {
							double exp = Double.parseDouble(str);

							if (max > 1000 * exp) {
								max = (int) Math.round(1000 * exp);
							}
						}

						logslider_.setMaxWithin(max);
						if (logslider_.getValue() > logslider_.getMaxWithin()) {
							logslider_.setValueWithin(logslider_.getMaxWithin());
							textfieldvalue_.setText(String.valueOf(logslider_.getValue()));
							changeProperty(LASER_PULSE,String.valueOf(logslider_.getValue()));
						}
						
						changeMaxPulseProperty(max);
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
				if (utils.isInteger(s)) {
					int max = Integer.parseInt(s);
					String str = getUIProperty(CAMERA_EXPOSURE).getPropertyValue();
					
					if (utils.isNumeric(str)) {
						double exp = Double.parseDouble(str);

						if (max > 1000 * exp) {
							max = (int) Math.round(1000 * exp);
						}
					}

					logslider_.setMaxWithin(max);
					if (logslider_.getValue() > logslider_.getMaxWithin()) {
						logslider_.setValueWithin(logslider_.getMaxWithin());
						textfieldvalue_.setText(String.valueOf(logslider_.getValue()));
						changeProperty(LASER_PULSE,String.valueOf(logslider_.getValue()));
					}
					
					changeMaxPulseProperty(max);
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
					  changeProperty(LASER_PULSE,String.valueOf(logslider_.getValue()));
				  } catch(Exception ex){
					  ex.printStackTrace();
				  }  
			}});
		

		c.gridy = 2;
		c.ipady = 0;
		this.add(logslider_, c);
	}
	

	@Override
	protected void initializeProperties() {
		addUIProperty(new UIProperty(this, CAMERA_EXPOSURE,"Camera exposure in ms."));
		addUIProperty(new UIProperty(this, LASER_PULSE,"Pulse length of the activation laser."));
	}

	@Override
	protected void initializeParameters() {
		title_ = "UV";	
		color_ = Color.black;
		
		addUIParameter(new StringUIParameter(this, PARAM_TITLE,"Panel title.",title_));
		addUIParameter(new ColorUIParameter(this, PARAM_COLOR,"Default value for large z stage step.",color_));
	}


	@Override
	protected void changeProperty(String name, String value) {
		if(name.equals(LASER_PULSE)){
			getUIProperty(name).setPropertyValue(value);
		}
	}

	@Override
	public void propertyhasChanged(String name, String newvalue) {
		if(name.equals(LASER_PULSE)){
			System.out.println("Property pulse has changed");
			if(utils.isInteger(newvalue)){
				int val = Integer.parseInt(newvalue);
				
				if(val>logslider_.getMaxWithin()){
					logslider_.setValueWithin(logslider_.getMaxWithin());
					textfieldvalue_.setText(String.valueOf(logslider_.getMaxWithin()));
					changeProperty(LASER_PULSE,String.valueOf(logslider_.getMaxWithin()));
				} else {
					logslider_.setValueWithin(val);
					textfieldvalue_.setText(newvalue);
				}
			} else {
				System.out.println("Not integer");
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
		// do nothing
	}

	@Override
	public String getDescription() {
		return "The "+getLabel()+" panel is meant to control the pulse length of the activation laser. "
				+ "The user can set a maximum to the slider by entering a value in the second text area. The pulse length can be set by entering a value in the first text area or by moving the slider.";
	}

	@Override
	protected void initializeInternalProperties() {
		maxpulse_  = 10000;
		
		addInternalProperty(new IntInternalProperty(this, INTERNAL_MAXPULSE, maxpulse_));
	}

	@Override
	public void internalpropertyhasChanged(String label) {
		if(label.equals(INTERNAL_MAXPULSE)){
			maxpulse_ = ((IntInternalProperty) getInternalProperty(label)).getPropertyValue();
			logslider_.setMaxWithin(maxpulse_);
			if (logslider_.getValue() > logslider_.getMaxWithin()) {
				logslider_.setValueWithin(logslider_.getMaxWithin());
				textfieldvalue_.setText(String.valueOf(logslider_.getValue()));
				changeProperty(LASER_PULSE,String.valueOf(logslider_.getValue()));
			}
			textfieldmax_.setText(String.valueOf(maxpulse_));
		}
	}

	private void changeMaxPulseProperty(int val){
		System.out.println("Change internal property from panel "+getLabel());
		((IntInternalProperty) getInternalProperty(INTERNAL_MAXPULSE)).setPropertyValue(val);
	}
	
	// chose not to use this in order to avoid parsing...
	@Override
	protected void changeInternalProperty(String name, String value) {
		if(name.equals(INTERNAL_MAXPULSE) && utils.isNumeric(value)){
			((IntInternalProperty) getInternalProperty(name)).setPropertyValue(Integer.parseInt(value));
		}
	}
}
