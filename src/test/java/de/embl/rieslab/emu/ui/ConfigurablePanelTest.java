package de.embl.rieslab.emu.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Test;

import de.embl.rieslab.emu.micromanager.mmproperties.MMProperty;
import de.embl.rieslab.emu.ui.uiproperties.PropertyPair;
import de.embl.rieslab.emu.ui.uiproperties.UIProperty;

public class ConfigurablePanelTest {

	@Test
	public void testConfigurablePanel() {
		final String s = "MyPanel";
		ConfigurableTestPanel cp = new ConfigurableTestPanel(s);
		
		assertEquals(s, cp.getLabel());
	}

	@Test (expected = NullPointerException.class)
	public void testNullLabel() {
		new ConfigurableTestPanel(null);
	}

	@Test 
	public void testAddUIProperty() {
		final String[] props = {"PROP1", "PROP2", "PROP3"};
		
		ConfigurableTestPanel cp = new ConfigurableTestPanel("My") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeProperties() {
				this.addUIProperty(new UIProperty(this, props[0], ""));
				this.addUIProperty(new UIProperty(this, props[1], ""));
				this.addUIProperty(new UIProperty(this, props[2], ""));
			}
		};
		
		HashMap<String, UIProperty> list = cp.getUIProperties();
		assertEquals(3, list.size());
		
		for(int i=0;i<props.length;i++) {
			assertTrue(list.containsKey(props[i]));
			assertNotNull(list.get(props[i]));
			assertEquals(props[i], list.get(props[i]).getLabel());
		}

		for(int i=0;i<props.length;i++) {
			assertNotNull(cp.getUIProperty(props[i]));
			assertEquals(props[i], cp.getUIProperty(props[i]).getLabel());
		}
	}

	@Test (expected = NullPointerException.class)
	public void testAddNullUIProperty() {		
		new ConfigurableTestPanel("My") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeProperties() {
				this.addUIProperty(null);
			}
		};
	}

	@Test 
	public void testGetWrongProperty() {		
		ConfigurableTestPanel cp = new ConfigurableTestPanel("My") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeProperties() {
				this.addUIProperty(new UIProperty(this,"MyProp", ""));
			}
		};

		assertNull(cp.getUIProperty(null));
		assertNull(cp.getUIProperty(""));
		assertNull(cp.getUIProperty("Whatever"));
		assertNotNull(cp.getUIProperty("MyProp"));
	}

	@Test 
	public void testGetSetPropertyValue() {		
		final String uipropLabel = "MyProp";
		ConfigurableTestPanel cp = new ConfigurableTestPanel("My") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeProperties() {
				this.addUIProperty(new UIProperty(this,uipropLabel, ""));
			}
		};

		TestableMMProperty mmprop = new TestableMMProperty("MyMMProp");
		PropertyPair.pair(cp.getUIProperty(uipropLabel), mmprop);
		
		assertEquals(TestableMMProperty.DEFVAL, cp.getUIPropertyValue(uipropLabel));
		
		final String s = "New value";
		cp.setUIPropertyValue(uipropLabel, s);

		// waits to let the other thread finish
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		assertEquals(s, cp.getUIPropertyValue(uipropLabel));
	}

	@Test 
	public void testSetNullPropertyValue() {		
		final String uipropLabel = "MyProp";
		ConfigurableTestPanel cp = new ConfigurableTestPanel("My") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeProperties() {
				this.addUIProperty(new UIProperty(this,uipropLabel, ""));
			}
		};

		TestableMMProperty mmprop = new TestableMMProperty("MyMMProp");
		PropertyPair.pair(cp.getUIProperty(uipropLabel), mmprop);
		
		assertEquals(TestableMMProperty.DEFVAL, cp.getUIPropertyValue(uipropLabel));
		
		cp.setUIPropertyValue(uipropLabel, null); // should ignore in the MMProperty
	}
	
	@Test 
	public void testSetFriendlyName() {		
		final String uipropLabel = "MyProp";
		ConfigurableTestPanel cp = new ConfigurableTestPanel("My") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeProperties() {
				this.addUIProperty(new UIProperty(this,uipropLabel, ""));
			}
		};
		
		final String fname = "An explicit name";
		cp.setUIPropertyFriendlyName(uipropLabel, fname);
		assertEquals(fname, cp.getUIProperty(uipropLabel).getFriendlyName());
	}
	
	@Test (expected = NullPointerException.class)
	public void testSetNullFriendlyName() {		
		final String uipropLabel = "MyProp";
		ConfigurableTestPanel cp = new ConfigurableTestPanel("My") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeProperties() {
				this.addUIProperty(new UIProperty(this,uipropLabel, ""));
			}
		};
		
		cp.setUIPropertyFriendlyName(uipropLabel, null);
	}

	

	// test adding UIProperty
	// test adding UIParameter
	// test adding InternalProperty
	// getters for different types
	// component trigger enabled and on/off
	// get uiprop value
	//setuiproperty friendly name
	// update all params and all props
	// test the triggering of hasChanged()
	// test two internal properties same
	// test all the nulls
	// test the order of events
	// write the main frame tests to figure out what is missing
	
	
	
	private class ConfigurableTestPanel extends ConfigurablePanel{

		private static final long serialVersionUID = 1L;
	 	
		public ConfigurableTestPanel(String label) {
			super(label);
		}
		
		@Override
		protected void initializeProperties() {}
		
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
	
	public class TestableMMProperty extends MMProperty<String> {

		public static final String DEV = "MyDevice";
		public static final String DEFVAL = "default";
		
		public TestableMMProperty(String propname) {
			super(null, "String", DEV, propname, false);
			this.value = DEFVAL;
		}

		@Override
		protected String convertToValue(String s) {
			return s;
		}

		@Override
		protected String convertToValue(int i) {
			return String.valueOf(i);
		}

		@Override
		protected String convertToValue(double d) {
			return String.valueOf(d);
		}

		@Override
		protected String[] arrayFromStrings(String[] s) {
			return s;
		}

		@Override
		protected String convertToString(String val) {
			return val;
		}

		@Override
		protected boolean areEquals(String val1, String val2) {
			return val1.equals(val2);
		}

		@Override
		protected boolean isAllowed(String val) {
			return true;
		}

		@Override
		public String getValue() {
			return this.value;
		}

		@Override
		public String getStringValue() {
			return this.value;
		}

		@Override
		public boolean setValue(String stringval, UIProperty source) {
			value = stringval;
			notifyListeners(source, stringval);
			return true;
		}
	};
}
