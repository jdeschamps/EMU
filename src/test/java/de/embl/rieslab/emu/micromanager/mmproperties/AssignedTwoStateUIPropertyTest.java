package de.embl.rieslab.emu.micromanager.mmproperties;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.embl.rieslab.emu.ui.ConfigurablePanel;
import de.embl.rieslab.emu.ui.uiproperties.PropertyPair;
import de.embl.rieslab.emu.ui.uiproperties.TwoStateUIProperty;
import de.embl.rieslab.emu.ui.uiproperties.UIProperty;

public class AssignedTwoStateUIPropertyTest {
	
	@Test
	public void testSettingOnOffValues() {
		TwoStateUIPropertyTestPanel cp = new TwoStateUIPropertyTestPanel("MyPanel");

		final IntegerMMProperty mmprop = new IntegerMMProperty(null, "", "", false) {
			@Override
			public Integer getValue() { // avoids NullPointerException
				return 0;
			}
		};
		
		PropertyPair.pair(cp.property, mmprop);

		final String offval = "4";
		final String onval = "8";
		
		cp.property.setOffStateValue(offval);
		assertEquals(offval, cp.property.getOffStateValue());
		
		cp.property.setOnStateValue(onval);	
		assertEquals(onval, cp.property.getOnStateValue());
	}
	
	@Test
	public void testSettingValue() {
		TwoStateUIPropertyTestPanel cp = new TwoStateUIPropertyTestPanel("MyPanel");

		final IntegerMMProperty mmprop = new IntegerMMProperty(null, "", "", false) {
			@Override
			public Integer getValue() { // avoids NullPointerException
				return 0;
			}
			
			@Override
			public String getStringValue() {
				return convertToString(value);
			}
			
			@Override
			public void setValue(String stringval, UIProperty source){
				value = convertToValue(stringval);
			}
		};
		
		PropertyPair.pair(cp.property, mmprop);

		final String offval = "4";
		final String onval = "8";
		cp.property.setOffStateValue(offval);
		cp.property.setOnStateValue(onval);	

		cp.property.setPropertyValue(onval);
		assertEquals(onval, cp.property.getPropertyValue());

		cp.property.setPropertyValue(offval);
		assertEquals(offval, cp.property.getPropertyValue());

		cp.property.setPropertyValue(TwoStateUIProperty.getOnStateName());
		assertEquals(onval, cp.property.getPropertyValue());

		cp.property.setPropertyValue(TwoStateUIProperty.getOffStateName());
		assertEquals(offval, cp.property.getPropertyValue());

		cp.property.setPropertyValue("1");
		assertEquals(onval, cp.property.getPropertyValue());

		cp.property.setPropertyValue("0");
		assertEquals(offval, cp.property.getPropertyValue());

		cp.property.setPropertyValue("true");
		assertEquals(onval, cp.property.getPropertyValue());

		cp.property.setPropertyValue("false");
		assertEquals(offval, cp.property.getPropertyValue());

		cp.property.setPropertyValue(null);
		assertEquals(offval, cp.property.getPropertyValue());
		
		cp.property.setPropertyValue("");
		assertEquals(offval, cp.property.getPropertyValue());
		
		cp.property.setPropertyValue("fdsfeg");
		assertEquals(offval, cp.property.getPropertyValue());
		
		cp.property.setPropertyValue("2.48");
		assertEquals(offval, cp.property.getPropertyValue());
	}
	

	@Test
	public void testAssigningOnAndOff01Values() {
		TwoStateUIPropertyTestPanel cp = new TwoStateUIPropertyTestPanel("MyPanel");

		final IntegerMMProperty mmprop = new IntegerMMProperty(null, "", "", false) {
			@Override
			public Integer getValue() { // avoids NullPointerException
				return 0;
			}
			
			@Override
			public String getStringValue() {
				return convertToString(value);
			}
			
			@Override
			public void setValue(String stringval, UIProperty source){
				value = convertToValue(stringval);
			}
		};
		
		PropertyPair.pair(cp.property, mmprop);

		// set state values opposite to accepted value to test priority given to the value
		final String offval = "1";
		final String onval = "0";
		cp.property.setOffStateValue(offval);
		cp.property.setOnStateValue(onval);	

		cp.property.setPropertyValue("0");
		assertEquals(onval, cp.property.getPropertyValue());

		cp.property.setPropertyValue("1");
		assertEquals(offval, cp.property.getPropertyValue());
	}
	
	@Test
	public void testAssigningOnAndOffBooleanValues() {
		TwoStateUIPropertyTestPanel cp = new TwoStateUIPropertyTestPanel("MyPanel");

		final StringMMProperty mmprop = new StringMMProperty(null, MMProperty.TYPE_STRING, "", "", new String[] {"true", "false"}) {
			@Override
			public String getValue() { // avoids NullPointerException
				return "";
			}
			
			@Override
			public String getStringValue() {
				return convertToString(value);
			}
			
			@Override
			public void setValue(String stringval, UIProperty source){
				value = convertToValue(stringval);
			}
		};
		
		PropertyPair.pair(cp.property, mmprop);

		// set state values opposite to accepted value to test priority given to the value
		final String offval = "true";
		final String onval = "false";
		cp.property.setOffStateValue(offval);
		cp.property.setOnStateValue(onval);	

		cp.property.setPropertyValue("false");
		assertEquals(onval, cp.property.getPropertyValue());

		cp.property.setPropertyValue("true");
		assertEquals(offval, cp.property.getPropertyValue());
		
		cp.property.setPropertyValue("1");
		assertEquals(onval, cp.property.getPropertyValue());

		cp.property.setPropertyValue("0");
		assertEquals(offval, cp.property.getPropertyValue());
	}
	
	private class TwoStateUIPropertyTestPanel extends ConfigurablePanel{

		private static final long serialVersionUID = 1L;
		public TwoStateUIProperty property;
		public final String PROP = "MyProp"; 
		public final String DESC = "MyDescription";
	 	
		public TwoStateUIPropertyTestPanel(String label) {
			super(label);
		}
		
		@Override
		protected void initializeProperties() {
			property = new TwoStateUIProperty(this, PROP, DESC);
		}
		
		@Override
		protected void initializeParameters() {}
		
		@Override
		protected void parameterhasChanged(String parameterName) {}
		
		@Override
		protected void initializeInternalProperties() {}

		@Override
		public void internalpropertyhasChanged(String propertyName) {}

		@Override
		protected void propertyhasChanged(String propertyName, String newvalue) {}

		@Override
		public void shutDown() {}

		@Override
		protected void addComponentListeners() {}
		
		@Override
		public String getDescription() {return "";}
	}
}
