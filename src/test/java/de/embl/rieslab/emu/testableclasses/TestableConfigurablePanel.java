package test.java.de.embl.rieslab.emu.testableclasses;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import main.java.de.embl.rieslab.emu.swinglisteners.SwingUIListeners;
import main.java.de.embl.rieslab.emu.ui.ConfigurablePanel;
import main.java.de.embl.rieslab.emu.ui.internalproperties.BoolInternalProperty;
import main.java.de.embl.rieslab.emu.ui.internalproperties.DoubleInternalProperty;
import main.java.de.embl.rieslab.emu.ui.internalproperties.IntegerInternalProperty;
import main.java.de.embl.rieslab.emu.ui.uiparameters.BoolUIParameter;
import main.java.de.embl.rieslab.emu.ui.uiparameters.ColorUIParameter;
import main.java.de.embl.rieslab.emu.ui.uiparameters.ComboUIParameter;
import main.java.de.embl.rieslab.emu.ui.uiparameters.DoubleUIParameter;
import main.java.de.embl.rieslab.emu.ui.uiparameters.IntegerUIParameter;
import main.java.de.embl.rieslab.emu.ui.uiparameters.StringUIParameter;
import main.java.de.embl.rieslab.emu.ui.uiparameters.UIParameter;
import main.java.de.embl.rieslab.emu.ui.uiparameters.UIPropertyParameter;
import main.java.de.embl.rieslab.emu.ui.uiproperties.MultiStateUIProperty;
import main.java.de.embl.rieslab.emu.ui.uiproperties.SingleStateUIProperty;
import main.java.de.embl.rieslab.emu.ui.uiproperties.TwoStateUIProperty;
import main.java.de.embl.rieslab.emu.ui.uiproperties.UIProperty;
import main.java.de.embl.rieslab.emu.ui.uiproperties.flag.NoFlag;
import main.java.de.embl.rieslab.emu.utils.ColorRepository;

public class TestableConfigurablePanel extends ConfigurablePanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1060147883464015210L;
	
	// properties
	public final String UIPROP = "UIProp";	
	public final String UIPROP2 = "UIProp2";	
	public final String MULTPROP = "MultiProp";	
	public final String SINGPROP = "SingleProp";	
	public final String TWOSTPROP = "TwoStateProp";	

	public String uiPropValue = "";
	public String uiPropValue2 = "";
	public String multiPropValue = "";
	public String singlePropValue = "";
	public String twoPropValue = "";
	
	public final int sizeMultiProp = 3;

	// internal properties
	public final String BOOLPROP = "BoolProp";
	public final String DOUBLEPROP = "DoubleProp";
	public final String INTPROP = "IntProp";

	public boolean boolInternalPropValue = false;
	public double doubleInternalPropValue = 0.;
	public int intInternalPropValue = 0;
	
	// parameters
	public final String BOOLPARAM = "BoolParam";	
	public final String COLORPARAM = "ColorParam";	
	public final String COMBOPARAM = "ComboParam";	
	public final String DOUBLEPARAM = "DoubleParam";	
	public final String INTPARAM = "IntParam";		
	public final String STRINGPARAM = "StringParam";		
	public final String PROPPARAM = "PropParam";	

	public boolean boolParamVal;
	public Color colorParamVal;
	public String comboParamVal;
	public double doubleParamVal;
	public int intParamVal;
	public String stringParamVal;
	public String propParamVal;
	
	public final String STATE3 = "State3";
	
	public String[] combovalues;
	
	// components
	public JTextField textfield1, textfield2;
	public JSlider slider;
	public JToggleButton toggle;
	public JButton button;
	public JComboBox<String> combobox;
	
	public TestableConfigurablePanel(String label) {
		super(label);

		textfield1 = new JTextField(); // to link to the uiprop
		textfield2 = new JTextField(); // to use with slider
		slider = new JSlider(); // to use with textfield
		toggle = new JToggleButton(); // to link to a two state prop
		button = new JButton(); // to link to a single state prop
		
		combovalues = new String[] {comboParamVal, "State2", STATE3};
		combobox = new JComboBox<String>(combovalues); 
	}
	
	@Override
	protected void addComponentListeners() {
		SwingUIListeners.addDoubleValueAction(this, UIPROP, textfield1);
		SwingUIListeners.addBooleanValueAction(this, TWOSTPROP, toggle);
		SwingUIListeners.addSingleValueAction(this, SINGPROP, button);
		SwingUIListeners.addIndexValueAction(this, MULTPROP, combobox);
		SwingUIListeners.addIntegerValueAction(this, UIPROP2, slider, textfield2, 0, 100);
	}

	
	@Override
	protected void initializeProperties() {
		this.addUIProperty(new UIProperty(this, UIPROP, "description"));
		this.addUIProperty(new UIProperty(this, UIPROP2, "description"));
		this.addUIProperty(new MultiStateUIProperty(this, MULTPROP, "description", sizeMultiProp));
		this.addUIProperty(new SingleStateUIProperty(this, SINGPROP, "description"));
		this.addUIProperty(new TwoStateUIProperty(this, TWOSTPROP, "description"));
	}

	@Override
	protected void initializeInternalProperties() {
		this.addInternalProperty(new BoolInternalProperty(this, BOOLPROP, boolInternalPropValue));
		this.addInternalProperty(new DoubleInternalProperty(this, DOUBLEPROP, doubleInternalPropValue));
		this.addInternalProperty(new IntegerInternalProperty(this, INTPROP, intInternalPropValue));
	}

	@Override
	protected void initializeParameters() {
		boolParamVal = false;
		colorParamVal = ColorRepository.getColor(ColorRepository.strblack);
		comboParamVal = "State1";
		String[] combovalues = new String[] {comboParamVal, "State2", STATE3};
		doubleParamVal = 2.;
		intParamVal = 4;
		stringParamVal = "Title";
		propParamVal = UIPropertyParameter.NO_PROPERTY;
		
		this.addUIParameter(new BoolUIParameter(this, BOOLPARAM, "description", boolParamVal));	
		this.addUIParameter(new ColorUIParameter(this, COLORPARAM, "description", colorParamVal));	
		this.addUIParameter(new ComboUIParameter(this, COMBOPARAM, "description", combovalues, 0));	
		this.addUIParameter(new DoubleUIParameter(this, DOUBLEPARAM, "description", doubleParamVal));	
		this.addUIParameter(new IntegerUIParameter(this, INTPARAM, "description", intParamVal));
		this.addUIParameter(new StringUIParameter(this, STRINGPARAM, "description", stringParamVal));	
		this.addUIParameter(new UIPropertyParameter(this, PROPPARAM, "description", new NoFlag()));	
	}

	
	@Override
	public void internalpropertyhasChanged(String propertyName) {
		if(propertyName.equals(INTPROP)) {
			intInternalPropValue = getIntegerInternalPropertyValue(propertyName);
		} else if(propertyName.equals(DOUBLEPROP)) {
			doubleInternalPropValue = getDoubleInternalPropertyValue(propertyName);
		} else if(propertyName.equals(BOOLPROP)) {
			boolInternalPropValue = getBoolInternalPropertyValue(propertyName);
		}
	}

	@Override
	protected void propertyhasChanged(String propertyName, String newvalue) {
		if(propertyName.equals(UIPROP)) {
			uiPropValue = newvalue;
		} else if(propertyName.equals(UIPROP2)) {
			uiPropValue2 = newvalue;
		} else if(propertyName.equals(MULTPROP)) {
			multiPropValue = newvalue;
		} else if(propertyName.equals(SINGPROP)) {
			singlePropValue = newvalue;
		} else if(propertyName.equals(TWOSTPROP)) {
			twoPropValue = newvalue;
		}
	}

	@Override
	protected void parameterhasChanged(String parameterName) {
		if(parameterName.equals(BOOLPARAM)) {
			boolParamVal = this.getBoolUIParameterValue(BOOLPARAM);
		} else if(parameterName.equals(COLORPARAM)) {
			colorParamVal = this.getColorUIParameterValue(COLORPARAM);
		}else if(parameterName.equals(COMBOPARAM)) {
			comboParamVal = this.getComboUIParameterValue(COMBOPARAM);
		}else if(parameterName.equals(DOUBLEPARAM)) {
			doubleParamVal = this.getDoubleUIParameterValue(DOUBLEPARAM);
		}else if(parameterName.equals(INTPARAM)) {
			intParamVal = this.getIntegerUIParameterValue(INTPARAM);
		}else if(parameterName.equals(STRINGPARAM)) {
			stringParamVal = this.getStringUIParameterValue(STRINGPARAM);
		}else if(parameterName.equals(PROPPARAM)) {
			propParamVal = this.getUIPropertyParameterValue(PROPPARAM);
		}
	}

	@Override
	public void shutDown() {
		// Do nothing
	}

	@Override
	public String getDescription() {
		return "";
	}
	
	//////////////////////////////////////////////////////////////////////////////
	// specific methods to get access to non-visible methods (a bit hacky...)
	
	public UIParameter getPublicUIParameter(String s) {
		return this.getUIParameter(s);
	}
	
	public UIProperty getPublicUIProperty(String s) {
		return this.getUIProperty(s);
	}
	
	public void setPublicInternalProperty(String propertyName, double newValue) {
		this.setInternalPropertyValue(propertyName, newValue);
	}
	
	public void setPublicInternalProperty(String propertyName, int newValue) {
		this.setInternalPropertyValue(propertyName, newValue);
	}
	
	public void setPublicInternalProperty(String propertyName, boolean newValue) {
		this.setInternalPropertyValue(propertyName, newValue);
	}
};