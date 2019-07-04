package test.java.de.embl.rieslab.emu.dummyclasses;

import main.java.de.embl.rieslab.emu.ui.ConfigurablePanel;
import main.java.de.embl.rieslab.emu.ui.internalproperties.DoubleInternalProperty;
import main.java.de.embl.rieslab.emu.ui.uiparameters.IntegerUIParameter;
import main.java.de.embl.rieslab.emu.ui.uiparameters.UIParameter;
import main.java.de.embl.rieslab.emu.ui.uiproperties.UIProperty;

public class TestConfigurablePanel extends ConfigurablePanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1060147883464015210L;
	public final String PROP = "UIProp";	
	public final String INTPROP = "IntProp";
	public final String PARAM = "UIParam";	
	
	public TestConfigurablePanel(String label) {
		super(label);
	}
	public String propValue = "";
	public double internalPropValue = 0.;
	public int paramValue = 4;
	
	@Override
	protected void initializeProperties() {
		this.addUIProperty(new UIProperty(this, PROP, "description"));
	}

	@Override
	protected void initializeInternalProperties() {
		this.addInternalProperty(new DoubleInternalProperty(this, INTPROP, 1.));
	}

	@Override
	protected void initializeParameters() {
		this.addUIParameter(new IntegerUIParameter(this, PARAM, "description", paramValue));	
	}

	@Override
	protected void addComponentListeners() {
		// Do nothing
	}

	@Override
	public void internalpropertyhasChanged(String propertyName) {
		if(propertyName.equals(INTPROP)) {
			internalPropValue = getDoubleInternalPropertyValue(propertyName);
		}
	}

	@Override
	protected void propertyhasChanged(String propertyName, String newvalue) {
		if(propertyName.equals(PROP)) {
			propValue = newvalue;
		}
	}

	@Override
	protected void parameterhasChanged(String parameterName) {
		if(parameterName.equals(PARAM)) {
			paramValue = this.getIntegerUIParameterValue(PARAM);
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
};