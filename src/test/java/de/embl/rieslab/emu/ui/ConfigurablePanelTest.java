package de.embl.rieslab.emu.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.HashMap;

import org.junit.Test;

import de.embl.rieslab.emu.exceptions.AlreadyAssignedUIPropertyException;
import de.embl.rieslab.emu.exceptions.IncorrectParameterTypeException;
import de.embl.rieslab.emu.micromanager.mmproperties.MMProperty;
import de.embl.rieslab.emu.ui.internalproperties.BoolInternalProperty;
import de.embl.rieslab.emu.ui.internalproperties.DoubleInternalProperty;
import de.embl.rieslab.emu.ui.internalproperties.IntegerInternalProperty;
import de.embl.rieslab.emu.ui.internalproperties.InternalProperty;
import de.embl.rieslab.emu.ui.uiparameters.BoolUIParameter;
import de.embl.rieslab.emu.ui.uiparameters.ColorUIParameter;
import de.embl.rieslab.emu.ui.uiparameters.ComboUIParameter;
import de.embl.rieslab.emu.ui.uiparameters.DoubleUIParameter;
import de.embl.rieslab.emu.ui.uiparameters.IntegerUIParameter;
import de.embl.rieslab.emu.ui.uiparameters.StringUIParameter;
import de.embl.rieslab.emu.ui.uiparameters.UIParameter;
import de.embl.rieslab.emu.ui.uiparameters.UIParameter.UIParameterType;
import de.embl.rieslab.emu.ui.uiparameters.UIPropertyParameter;
import de.embl.rieslab.emu.ui.uiproperties.PropertyPair;
import de.embl.rieslab.emu.ui.uiproperties.UIProperty;
import de.embl.rieslab.emu.ui.uiproperties.flag.NoFlag;
import de.embl.rieslab.emu.utils.ColorRepository;

public class ConfigurablePanelTest {

	/**
	 * Tests that the ConfigurablePanel has the right label.
	 */
	@Test
	public void testConfigurablePanel() {
		final String s = "MyPanel";
		ConfigurableTestPanel cp = new ConfigurableTestPanel(s);
		
		assertEquals(s, cp.getLabel());
	}

	/**
	 * Tests that a NullPointerException is thrown when instantiating a ConfigurablePanel with a null label.
	 */
	@Test (expected = NullPointerException.class)
	public void testNullLabel() {
		new ConfigurableTestPanel(null);
	}

	//////////////////////////////////////
	//////////// UIProperties ////////////
	//////////////////////////////////////

	// add UIProperty
	/**
	 * Tests adding UIProperties.
	 */
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

	@Test(expected = IllegalArgumentException.class)
	public void testGetWrongProperty() {		
		ConfigurableTestPanel cp = new ConfigurableTestPanel("My") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeProperties() {
				this.addUIProperty(new UIProperty(this,"MyProp", ""));
			}
		};

		cp.getUIProperty("Definitely not a property");
	}

	// get UIProperty
	@Test(expected = NullPointerException.class)
	public void testGetNullProperty() {		
		ConfigurableTestPanel cp = new ConfigurableTestPanel("My") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeProperties() {
				this.addUIProperty(new UIProperty(this,"MyProp", ""));
			}
		};

		cp.getUIProperty(null);
	}

	// get and set UIProperty value
	@Test 
	public void testGetSetPropertyValue() throws AlreadyAssignedUIPropertyException {		
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
	
	@Test (expected = NullPointerException.class)
	public void testGetNullPropertyValue() {		
		final String uipropLabel = "MyProp";
		ConfigurableTestPanel cp = new ConfigurableTestPanel("My") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeProperties() {
				this.addUIProperty(new UIProperty(this,uipropLabel, ""));
			}
		};

		cp.getUIPropertyValue(null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testGetWrongPropertyValue() {		
		final String uipropLabel = "MyProp";
		ConfigurableTestPanel cp = new ConfigurableTestPanel("My") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeProperties() {
				this.addUIProperty(new UIProperty(this,uipropLabel, ""));
			}
		};

		cp.getUIPropertyValue("I am not quite dead");
	}

	@Test 
	public void testSetNullPropertyValue() throws AlreadyAssignedUIPropertyException {		
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
		assertEquals(TestableMMProperty.DEFVAL, mmprop.getStringValue());
		
		cp.setUIPropertyValue(uipropLabel, null); // should ignore in the MMProperty

		assertEquals(TestableMMProperty.DEFVAL, cp.getUIPropertyValue(uipropLabel));
		assertEquals(TestableMMProperty.DEFVAL, mmprop.getStringValue());
	}
	
	// UIProperty friendly name
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
	
	//////////////////////////////////////
	//////////// UIParameters ////////////
	//////////////////////////////////////

	// add UIParameter
	@Test 
	public void testAddUIParameter() {
		final String defval = "default";
		final String[] params = {"PARAM1", "PARAM2", "PARAM3"};
		ConfigurableTestPanel cp = new ConfigurableTestPanel("My") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeParameters() {
				this.addUIParameter(new StringUIParameter(this, params[0], "", defval));
				this.addUIParameter(new IntegerUIParameter(this, params[1], "", 1));
				this.addUIParameter(new BoolUIParameter(this, params[2], "", true));
			}
		};
		
		@SuppressWarnings("rawtypes")
		HashMap<String, UIParameter> list = cp.getUIParameters();
		assertEquals(3, list.size());
		
		for(int i=0;i<params.length;i++) {
			assertTrue(list.containsKey(UIParameter.getHash(cp, params[i])));
			assertNotNull(list.get(UIParameter.getHash(cp, params[i])));
			assertEquals(params[i], list.get(UIParameter.getHash(cp, params[i])).getLabel());
			assertEquals(UIParameter.getHash(cp, params[i]), list.get(UIParameter.getHash(cp, params[i])).getHash());
		}

		for(int i=0;i<params.length;i++) {
			assertNotNull(cp.getUIParameter(params[i]));
			assertEquals(params[i], cp.getUIParameter(params[i]).getLabel());
		}

		assertEquals(UIParameterType.STRING, cp.getUIParameterType(params[0]));
		assertEquals(UIParameterType.INTEGER, cp.getUIParameterType(params[1]));
		assertEquals(UIParameterType.BOOL, cp.getUIParameterType(params[2]));
	}

	
	@Test (expected = NullPointerException.class) 
	public void testAddNullUIParameter() {
		new ConfigurableTestPanel("My") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeParameters() {
				this.addUIParameter(null);
			}
		};
	}

	@Test(expected = NullPointerException.class)
	public void testGetNullUIParameter() {
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeParameters() {
				this.addUIParameter(new StringUIParameter(this, "MyProp", "", "Vive la France!"));
			}
		};

		cp.getUIParameter(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetWrongUIParameter() {
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeParameters() {
				this.addUIParameter(new StringUIParameter(this, "MyProp", "", "Vive la France!"));
			}
		};

		cp.getUIParameter("God save Queens!");
	}

	// get StringUIParameter value
	@Test 
	public void testGetStringUIParameterValue() throws IncorrectParameterTypeException {
		final String defval = "default";
		final String param = "Param";
		
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeParameters() {
				this.addUIParameter(new StringUIParameter(this, param, "", defval));
			}
		};

		assertEquals(defval, cp.getStringUIParameterValue(param));

		final String nval = "Gumbys";
		cp.getUIParameter(param).setStringValue(nval);
		
		assertEquals(nval, cp.getStringUIParameterValue(param));
	}
	
	@Test(expected = NullPointerException.class) 
	public void testGetStringUIParameterNullValue() throws IncorrectParameterTypeException {
		final String defval = "default";
		final String param = "Param";
		
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeParameters() {
				this.addUIParameter(new StringUIParameter(this, param, "", defval));
			}
		};

		cp.getStringUIParameterValue(null);
	}
	
	@Test(expected = IllegalArgumentException.class) 
	public void testGetStringUIParameterWrongValue() throws IncorrectParameterTypeException {
		final String defval = "default";
		final String param = "Param";
		
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeParameters() {
				this.addUIParameter(new StringUIParameter(this, param, "", defval));
			}
		};

		cp.getStringUIParameterValue("Rosebud");
	}
	
	@Test(expected = IncorrectParameterTypeException.class) 
	public void testGetStringUIParameterWrongType() throws IncorrectParameterTypeException {
		final String param = "Param";
		
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeParameters() {
				this.addUIParameter(new BoolUIParameter(this, param, "", true));
			}
		};

		cp.getStringUIParameterValue(param);
	}

	// get BoolUIParameter value
	@Test 
	public void testGetBoolUIParameterValue() throws IncorrectParameterTypeException {
		final boolean defval = false;
		final String param = "Param";
		
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeParameters() {
				this.addUIParameter(new BoolUIParameter(this, param, "", defval));
			}
		};

		assertEquals(defval, cp.getBoolUIParameterValue(param));
		
		final String nval = "true";
		cp.getUIParameter(param).setStringValue(nval);
		
		assertEquals(nval, String.valueOf(cp.getBoolUIParameterValue(param)));
	}
	
	
	@Test(expected = NullPointerException.class) 
	public void testGetBoolUIParameterNullValue() throws IncorrectParameterTypeException {
		final boolean defval = false;
		final String param = "Param";
		
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeParameters() {
				this.addUIParameter(new BoolUIParameter(this, param, "", defval));
			}
		};

		cp.getBoolUIParameterValue(null);
	}

	@Test(expected = IllegalArgumentException.class) 
	public void testGetBoolUIParameterWrongValue() throws IncorrectParameterTypeException {
		final boolean defval = false;
		final String param = "Param";
		
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeParameters() {
				this.addUIParameter(new BoolUIParameter(this, param, "", defval));
			}
		};

		cp.getBoolUIParameterValue("Rosebud");
	}
	
	@Test(expected = IncorrectParameterTypeException.class) 
	public void testGetBoolUIParameterWrongType() throws IncorrectParameterTypeException {
		final String param = "Param";
		
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeParameters() {
				this.addUIParameter(new StringUIParameter(this, param, "", "default"));
			}
		};

		cp.getBoolUIParameterValue(param);
	}


	// get IntegerUIParameter value
	@Test 
	public void testGetIntegerUIParameterValue() throws IncorrectParameterTypeException {
		final int defval = 42;
		final String param = "Param";
		
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeParameters() {
				this.addUIParameter(new IntegerUIParameter(this, param, "", defval));
			}
		};

		assertEquals(defval, cp.getIntegerUIParameterValue(param));
		
		final String nval = "81";
		cp.getUIParameter(param).setStringValue(nval);
		
		assertEquals(nval, String.valueOf(cp.getIntegerUIParameterValue(param)));
	}
	
	
	@Test(expected = NullPointerException.class) 
	public void testGetIntegerUIParameterNullValue() throws IncorrectParameterTypeException {
		final int defval = 42;
		final String param = "Param";
		
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeParameters() {
				this.addUIParameter(new IntegerUIParameter(this, param, "", defval));
			}
		};

		cp.getIntegerUIParameterValue(null);
	}

	@Test(expected = IllegalArgumentException.class) 
	public void testGetIntegerUIParameterWrongValue() throws IncorrectParameterTypeException {
		final int defval = 42;
		final String param = "Param";
		
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeParameters() {
				this.addUIParameter(new IntegerUIParameter(this, param, "", defval));
			}
		};

		cp.getIntegerUIParameterValue("Rosebud");
	}

	@Test(expected = IncorrectParameterTypeException.class) 
	public void testGetIntegerUIParameterWrongType() throws IncorrectParameterTypeException {
		final boolean defval = false;
		final String param = "Param";
		
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeParameters() {
				this.addUIParameter(new BoolUIParameter(this, param, "", defval));
			}
		};

		cp.getIntegerUIParameterValue(param);
	}
	
	// get DoubleUIParameter value
	@Test 
	public void testGetDoubleUIParameterValue() throws IncorrectParameterTypeException {
		final double defval = 42.195;
		final String param = "Param";
		
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeParameters() {
				this.addUIParameter(new DoubleUIParameter(this, param, "", defval));
			}
		};

		assertEquals(defval, cp.getDoubleUIParameterValue(param), 1E-20);
		
		final String nval = "81.7841";
		cp.getUIParameter(param).setStringValue(nval);
		
		assertEquals(nval, String.valueOf(cp.getDoubleUIParameterValue(param)));
	}
	
	
	@Test(expected = NullPointerException.class) 
	public void testGetDoubleUIParameterNullValue() throws IncorrectParameterTypeException {
		final double defval = 42.195;
		final String param = "Param";
		
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeParameters() {
				this.addUIParameter(new DoubleUIParameter(this, param, "", defval));
			}
		};

		cp.getDoubleUIParameterValue(null);
	}

	@Test(expected = IllegalArgumentException.class) 
	public void testGetDoubleUIParameterWrongValue() throws IncorrectParameterTypeException {
		final double defval = 42.195;
		final String param = "Param";
		
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeParameters() {
				this.addUIParameter(new DoubleUIParameter(this, param, "", defval));
			}
		};

		cp.getDoubleUIParameterValue("Rosebud");
	}

	@Test(expected = IncorrectParameterTypeException.class) 
	public void testGetDoubleUIParameterWrongType() throws IncorrectParameterTypeException {
		final boolean defval = false;
		final String param = "Param";
		
		ConfigurableTestPanel cp = new ConfigurableTestPanel("My") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeParameters() {
				this.addUIParameter(new BoolUIParameter(this, param, "", defval));
			}
		};

		cp.getDoubleUIParameterValue(param);
	}
	
	// get ComboUIParameter value
	@Test 
	public void testGetComboUIParameterValue() throws IncorrectParameterTypeException {
		final int defval = 2;
		final String[] vals = {"SuperVal", "MediocreVal", "UnitTesting is tough", "SomeVal"};
		final String param = "Param";
		
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeParameters() {
				this.addUIParameter(new ComboUIParameter(this, param, "", vals, defval));
			}
		};

		assertEquals(vals[defval], cp.getComboUIParameterValue(param));
		
		cp.getUIParameter(param).setStringValue(vals[0]);
		
		assertEquals(vals[0], String.valueOf(cp.getComboUIParameterValue(param)));
	}
	
	
	@Test(expected = NullPointerException.class) 
	public void testGetComboUIParameterNullValue() throws IncorrectParameterTypeException {
		final int defval = 2;
		final String[] vals = {"SuperVal", "MediocreVal", "UnitTesting is tough", "SomeVal"};
		final String param = "Param";
		
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeParameters() {
				this.addUIParameter(new ComboUIParameter(this, param, "", vals, defval));
			}
		};

		cp.getComboUIParameterValue(null);
	}

	@Test(expected = IllegalArgumentException.class) 
	public void testGetComboUIParameterWrongValue() throws IncorrectParameterTypeException {
		final int defval = 2;
		final String[] vals = {"SuperVal", "MediocreVal", "UnitTesting is tough", "SomeVal"};
		final String param = "Param";
		
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeParameters() {
				this.addUIParameter(new ComboUIParameter(this, param, "", vals, defval));
			}
		};

		cp.getComboUIParameterValue("Rosebud");
	}

	@Test(expected = IncorrectParameterTypeException.class) 
	public void testGetComboUIParameterWrongType() throws IncorrectParameterTypeException {
		final boolean defval = false;
		final String param = "Param";
		
		ConfigurableTestPanel cp = new ConfigurableTestPanel("My") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeParameters() {
				this.addUIParameter(new BoolUIParameter(this, param, "", defval));
			}
		};

		cp.getComboUIParameterValue(param);
	}
	
	
	// get ColorUIParameter value
	@Test 
	public void testGetColorUIParameterValue() throws IncorrectParameterTypeException {
		final String param = "Param";
		
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeParameters() {
				this.addUIParameter(new ColorUIParameter(this, param, "", Color.BLACK));
			}
		};

		assertEquals(Color.BLACK, cp.getColorUIParameterValue(param));
		
		cp.getUIParameter(param).setStringValue(ColorRepository.strblue);
		
		assertEquals(Color.BLUE, cp.getColorUIParameterValue(param));
	}
	
	
	@Test(expected = NullPointerException.class) 
	public void testGetColorUIParameterNullValue() throws IncorrectParameterTypeException {
		final String param = "Param";
		
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeParameters() {
				this.addUIParameter(new ColorUIParameter(this, param, "", Color.BLACK));
			}
		};

		cp.getColorUIParameterValue(null);
	}

	@Test(expected = IllegalArgumentException.class) 
	public void testGetColorUIParameterWrongValue() throws IncorrectParameterTypeException {
		final String param = "Param";
		
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeParameters() {
				this.addUIParameter(new ColorUIParameter(this, param, "", Color.BLACK));
			}
		};

		cp.getColorUIParameterValue("Rosebud");
	}

	@Test(expected = IncorrectParameterTypeException.class) 
	public void testGetColorUIParameterWrongType() throws IncorrectParameterTypeException {
		final boolean defval = false;
		final String param = "Param";
		
		ConfigurableTestPanel cp = new ConfigurableTestPanel("My") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeParameters() {
				this.addUIParameter(new BoolUIParameter(this, param, "", defval));
			}
		};

		cp.getColorUIParameterValue(param);
	}
	
	
	// get UIPropertyParameter value
	@Test 
	public void testGetUIPropertyParameterValue() throws IncorrectParameterTypeException {
		final String param = "Param";
		
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeParameters() {
				this.addUIParameter(new UIPropertyParameter(this, param, "", new NoFlag()));
			}
		};

		assertEquals(NoFlag.NONE_FLAG, cp.getUIPropertyParameterValue(param));
		
		final String val = "A legitimate property";
		cp.getUIParameter(param).setStringValue(val);
		
		assertEquals(val, cp.getUIPropertyParameterValue(param));
	}
	
	
	@Test(expected = NullPointerException.class) 
	public void testGetUIPropertyParameterNullValue() throws IncorrectParameterTypeException {
		final String param = "Param";
		
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeParameters() {
				this.addUIParameter(new UIPropertyParameter(this, param, "", new NoFlag()));
			}
		};
		
		cp.getUIPropertyParameterValue(null);
	}

	@Test(expected = IllegalArgumentException.class) 
	public void testGetUIPropertyParameterWrongValue() throws IncorrectParameterTypeException {
		final String param = "Param";
		
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeParameters() {
				this.addUIParameter(new UIPropertyParameter(this, param, "", new NoFlag()));
			}
		};

		cp.getUIPropertyParameterValue("Rosebud");
	}

	@Test(expected = IncorrectParameterTypeException.class) 
	public void testGetUIPropertyParameterWrongType() throws IncorrectParameterTypeException {
		final boolean defval = false;
		final String param = "Param";
		
		ConfigurableTestPanel cp = new ConfigurableTestPanel("My") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeParameters() {
				this.addUIParameter(new BoolUIParameter(this, param, "", defval));
			}
		};

		cp.getUIPropertyParameterValue(param);
	}
	
	// update all parameters
	@Test 
	public void testUpdateAllParameters() {
		final String defval = "default";
		final String[] params = {"PARAM1", "PARAM2", "PARAM3"};
		
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeParameters() {
				this.addUIParameter(new StringUIParameter(this, params[0], "", defval));
				this.addUIParameter(new StringUIParameter(this, params[1], "", defval));
				this.addUIParameter(new StringUIParameter(this, params[2], "", defval));
			}

			@Override
			public void parameterhasChanged(String parameterName) {
				if(parameterName.equals(params[0])) {
					try {
						val1 = this.getStringUIParameterValue(parameterName);
					} catch (IncorrectParameterTypeException e) {
						e.printStackTrace();
					}
				} else if(parameterName.equals(params[1])) {
					try {
						val2 = this.getStringUIParameterValue(parameterName);
					} catch (IncorrectParameterTypeException e) {
						e.printStackTrace();
					}
				} else if(parameterName.equals(params[2])) {
					try {
						val3 = this.getStringUIParameterValue(parameterName);
					} catch (IncorrectParameterTypeException e) {
						e.printStackTrace();
					}
				}
			}
		};

		assertEquals("", cp.val1);
		assertEquals("", cp.val2);
		assertEquals("", cp.val3);
		
		final String nval1 = "Nobody", nval2 = "Expects", nval3 = "The Spanish Inquisition";
		cp.getUIParameter(params[0]).setStringValue(nval1);
		cp.getUIParameter(params[1]).setStringValue(nval2);
		cp.getUIParameter(params[2]).setStringValue(nval3);
		
		cp.updateAllParameters();
		
		assertEquals(nval1, cp.getUIParameter(params[0]).getStringValue());
	}

	// substitute param
	@Test
	public void testSubsituteUIParameter() throws IncorrectParameterTypeException {
		final String defval = "default";
		final String param = "Param";
		final String panel = "MyPanel";

		ConfigurableTestPanel cp1 = new ConfigurableTestPanel(panel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeParameters() {
				this.addUIParameter(new StringUIParameter(this, param, "", defval));
			}
		};
		
		ConfigurableTestPanel cp2 = new ConfigurableTestPanel(panel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeParameters() {
				this.addUIParameter(new StringUIParameter(this, param, "", defval));
			}
		};
  
		assertEquals(defval, cp1.getStringUIParameterValue(param));
		assertEquals(defval, cp2.getStringUIParameterValue(param));
		
		// change cp2's UIParameter value
		final String nval = "Rock the Casbah";
		cp2.getUIParameter(param).setStringValue(nval);
		assertEquals(defval, cp1.getStringUIParameterValue(param));
		assertEquals(nval, cp2.getStringUIParameterValue(param));
		
		//substitute the UIParameter
		cp1.substituteParameter(cp2.getUIParameter(param));

		// check that the value has changed in cp1's UIParameter
		assertEquals(nval, cp1.getStringUIParameterValue(param));
	}
	

	
	////////////////////////////////////////////
	//////////// InternalProperties ////////////
	////////////////////////////////////////////

	@Test
	public void testAddInternalProperty() {
		final int defval = 1;
		final String[] intprop = {"InternalProp1", "InternalProp2", "InternalProp3"};
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeInternalProperties() {
				this.addInternalProperty(new IntegerInternalProperty(this, intprop[0], new Integer(defval)));
				this.addInternalProperty(new BoolInternalProperty(this, intprop[1], new Boolean(false)));
				this.addInternalProperty(new DoubleInternalProperty(this, intprop[2], new Double(2.1)));
			}
		};
		
		@SuppressWarnings("rawtypes")
		HashMap<String, InternalProperty> list = cp.getInternalProperties();
		assertEquals(3, list.size());
		
		for(int i=0;i<intprop.length;i++) {
			assertTrue(list.containsKey(intprop[i]));
			assertNotNull(list.get(intprop[i]));
			assertEquals(intprop[i], list.get(intprop[i]).getLabel());
		}

		for(int i=0;i<intprop.length;i++) {
			assertNotNull(cp.getInternalProperty(intprop[i]));
			assertEquals(intprop[i], cp.getInternalProperty(intprop[i]).getLabel());
		}

		assertEquals(InternalProperty.InternalPropertyType.INTEGER, cp.getInternalProperty(intprop[0]).getType());
		assertEquals(InternalProperty.InternalPropertyType.BOOLEAN, cp.getInternalProperty(intprop[1]).getType());
		assertEquals(InternalProperty.InternalPropertyType.DOUBLE, cp.getInternalProperty(intprop[2]).getType());
	}
	
	
	// subsitute internalprop
	// test adding InternalProperty
	// getters for different types
	// component trigger enabled and on/off
	// update all params and all props
	// test the triggering of hasChanged()
	// test two internal properties same
	// test all the nulls
	// test the order of events
	// write the main frame tests to figure out what is missing
	
	
	private class ConfigurableTestPanel extends ConfigurablePanel{

		private static final long serialVersionUID = 1L;
		public String val1 = "", val2 = "", val3 = "";
	 	
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
