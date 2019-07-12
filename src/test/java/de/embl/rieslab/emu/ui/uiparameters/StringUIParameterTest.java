package de.embl.rieslab.emu.ui.uiparameters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.embl.rieslab.emu.ui.ConfigurablePanel;

public class StringUIParameterTest {
	@Test
	public void testStringUIParameterCreation() {
		StringParamTestPanel cp = new StringParamTestPanel("My panel");

		assertEquals(cp.def_val, cp.parameter.getStringValue());		
		assertEquals(cp.def_val, cp.parameter.getValue());
		
		assertEquals(UIParameter.UIParameterType.STRING, cp.parameter.getType());
		assertEquals(UIParameter.getHash(cp, cp.PARAM), cp.parameter.getHash());
		assertEquals(cp.PARAM, cp.parameter.getLabel());
		assertEquals(cp.DESC, cp.parameter.getDescription());
	}

	@Test
	public void testAreValuesSuitable() {
		StringParamTestPanel cp = new StringParamTestPanel("My panel");

		// test if suitable
		assertTrue(cp.parameter.isSuitable("false"));
		assertTrue(cp.parameter.isSuitable("fdesggiojo"));
		assertTrue(cp.parameter.isSuitable("()9[]54@#'#~"));
		assertTrue(cp.parameter.isSuitable("448766"));
		assertTrue(cp.parameter.isSuitable(""));
		
		assertFalse(cp.parameter.isSuitable(null));
	}
	
	@Test
	public void testChangeValue() {
		StringParamTestPanel cp = new StringParamTestPanel("My panel");

		String s = "2fsdj*745+$£%$6(*&) {}~'";
		cp.parameter.setStringValue(s);
		assertEquals(s, cp.parameter.getStringValue());	
		assertEquals(s, cp.parameter.getValue());	

		s = "£$sdjdsn";
		cp.parameter.setValue(s);
		assertEquals(s, cp.parameter.getStringValue());	
		assertEquals(s, cp.parameter.getValue());	
	}
	
	private class StringParamTestPanel extends ConfigurablePanel{

		private static final long serialVersionUID = -3307917280544843397L;
		public StringUIParameter parameter;
		public final String PARAM = "MyParam"; 
		public final String DESC = "MyDescription"; 
		public String def_val;
		
		public StringParamTestPanel(String label) {
			super(label);
		}

		@Override
		protected void initializeParameters() {
			def_val = "MyDefault";
			
			parameter = new StringUIParameter(this, PARAM, DESC, def_val);
		}
		
		@Override
		protected void parameterhasChanged(String parameterName) {}
		
		@Override
		protected void initializeInternalProperties() {}

		@Override
		protected void initializeProperties() {}

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