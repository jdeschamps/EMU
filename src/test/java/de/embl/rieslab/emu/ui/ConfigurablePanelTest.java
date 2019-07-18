package de.embl.rieslab.emu.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.HashMap;

import org.junit.Test;

import de.embl.rieslab.emu.exceptions.AlreadyAssignedUIPropertyException;
import de.embl.rieslab.emu.exceptions.IncorrectInternalPropertyTypeException;
import de.embl.rieslab.emu.exceptions.IncorrectUIParameterTypeException;
import de.embl.rieslab.emu.exceptions.UnknownInternalPropertyException;
import de.embl.rieslab.emu.exceptions.UnknownUIParameterException;
import de.embl.rieslab.emu.exceptions.UnknownUIPropertyException;
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
import de.embl.rieslab.emu.ui.uiproperties.MultiStateUIProperty;
import de.embl.rieslab.emu.ui.uiproperties.PropertyPair;
import de.embl.rieslab.emu.ui.uiproperties.TwoStateUIProperty;
import de.embl.rieslab.emu.ui.uiproperties.UIProperty;
import de.embl.rieslab.emu.ui.uiproperties.UIPropertyType;
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
	public void testAddUIProperty() throws UnknownUIPropertyException {
		final String[] props = {"PROP1", "PROP2", "PROP3"};
		
		ConfigurableTestPanel cp = new ConfigurableTestPanel("My") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeProperties() {
				this.addUIProperty(new UIProperty(this, props[0], ""));
				this.addUIProperty(new TwoStateUIProperty(this, props[1], ""));
				this.addUIProperty(new MultiStateUIProperty(this, props[2], "", 1));
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

		assertEquals(UIPropertyType.UIPROPERTY, cp.getUIPropertyType(props[0]));
		assertEquals(UIPropertyType.TWOSTATE, cp.getUIPropertyType(props[1]));
		assertEquals(UIPropertyType.MULTISTATE, cp.getUIPropertyType(props[2]));
	}

	/**
	 * Tests adding a null UIProperty.
	 */
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

	/**
	 * Tests getting an unknown UIProperty
	 */
	@Test(expected = UnknownUIPropertyException.class)
	public void testGetWrongProperty() throws UnknownUIPropertyException {		
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
	/**
	 * Tests getting a null UIProperty.
	 */
	@Test(expected = NullPointerException.class)
	public void testGetNullProperty() throws UnknownUIPropertyException {		
		ConfigurableTestPanel cp = new ConfigurableTestPanel("My") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeProperties() {
				this.addUIProperty(new UIProperty(this,"MyProp", ""));
			}
		};

		cp.getUIProperty(null);
	}
	
	/**
	 * Tests getting the type a null UIProperty.
	 */
	@Test(expected = NullPointerException.class)
	public void testGetNullPropertyType() {		
		ConfigurableTestPanel cp = new ConfigurableTestPanel("My") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeProperties() {
				this.addUIProperty(new UIProperty(this,"MyProp", ""));
			}
		};

		cp.getUIPropertyType(null);
	}

	// get and set UIProperty value
	/**
	 * Tests setting and getting a UIProperty value.
	 */
	@Test 
	public void testGetSetPropertyValue() throws AlreadyAssignedUIPropertyException, UnknownUIPropertyException {		
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
	
	/**
	 * Tests getting the value of a null UIProperty.
	 */
	@Test (expected = NullPointerException.class)
	public void testGetNullPropertyValue() throws UnknownUIPropertyException {		
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
	
	/**
	 * Tests getting the value of an unknown UIProperty.
	 */
	@Test (expected = UnknownUIPropertyException.class)
	public void testGetWrongPropertyValue() throws UnknownUIPropertyException {		
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

	/**
	 * Test setting the value of a UIProperty to null. 
	 */
	@Test 
	public void testSetNullPropertyValue() throws AlreadyAssignedUIPropertyException, UnknownUIPropertyException {		
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
	/**
	 * Tests setting a UIProperty friendly name.
	 * @throws UnknownUIPropertyException
	 */
	@Test 
	public void testSetFriendlyName() throws UnknownUIPropertyException {		
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
	public void testAddUIParameter() throws UnknownUIParameterException {
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
	public void testGetNullUIParameter() throws UnknownUIParameterException {
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeParameters() {
				this.addUIParameter(new StringUIParameter(this, "MyProp", "", "Vive la France!"));
			}
		};

		cp.getUIParameter(null);
	}

	@Test(expected = NullPointerException.class)
	public void testGetNullUIParameterType() {
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeParameters() {
				this.addUIParameter(new StringUIParameter(this, "MyProp", "", "Vive la France!"));
			}
		};

		cp.getUIParameterType(null);
	}

	@Test
	public void testGetUnknownUIParameterType() {
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeParameters() {
				this.addUIParameter(new StringUIParameter(this, "MyProp", "", "Vive la France!"));
			}
		};

		assertEquals(UIParameter.UIParameterType.NONE, cp.getUIParameterType("Omelette"));
	}

	@Test(expected = UnknownUIParameterException.class)
	public void testGetWrongUIParameter() throws UnknownUIParameterException {
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
	public void testGetStringUIParameterValue() throws IncorrectUIParameterTypeException, UnknownUIParameterException {
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
	public void testGetStringUIParameterNullValue() throws IncorrectUIParameterTypeException, UnknownUIParameterException {
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
	
	@Test(expected = UnknownUIParameterException.class) 
	public void testGetStringUIParameterWrongValue() throws IncorrectUIParameterTypeException, UnknownUIParameterException {
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
	
	@Test(expected = IncorrectUIParameterTypeException.class) 
	public void testGetStringUIParameterWrongType() throws IncorrectUIParameterTypeException, UnknownUIParameterException {
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
	public void testGetBoolUIParameterValue() throws IncorrectUIParameterTypeException, UnknownUIParameterException {
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
	public void testGetBoolUIParameterNullValue() throws IncorrectUIParameterTypeException, UnknownUIParameterException {
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

	@Test(expected = UnknownUIParameterException.class) 
	public void testGetBoolUIParameterWrongValue() throws IncorrectUIParameterTypeException, UnknownUIParameterException {
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
	
	@Test(expected = IncorrectUIParameterTypeException.class) 
	public void testGetBoolUIParameterWrongType() throws IncorrectUIParameterTypeException, UnknownUIParameterException {
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
	public void testGetIntegerUIParameterValue() throws IncorrectUIParameterTypeException, UnknownUIParameterException {
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
	public void testGetIntegerUIParameterNullValue() throws IncorrectUIParameterTypeException, UnknownUIParameterException {
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

	@Test(expected = UnknownUIParameterException.class) 
	public void testGetIntegerUIParameterWrongValue() throws IncorrectUIParameterTypeException, UnknownUIParameterException {
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

	@Test(expected = IncorrectUIParameterTypeException.class) 
	public void testGetIntegerUIParameterWrongType() throws IncorrectUIParameterTypeException, UnknownUIParameterException {
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
	public void testGetDoubleUIParameterValue() throws IncorrectUIParameterTypeException, UnknownUIParameterException {
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
	public void testGetDoubleUIParameterNullValue() throws IncorrectUIParameterTypeException, UnknownUIParameterException {
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

	@Test(expected = UnknownUIParameterException.class) 
	public void testGetDoubleUIParameterWrongValue() throws IncorrectUIParameterTypeException, UnknownUIParameterException {
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

	@Test(expected = IncorrectUIParameterTypeException.class) 
	public void testGetDoubleUIParameterWrongType() throws IncorrectUIParameterTypeException, UnknownUIParameterException {
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
	public void testGetComboUIParameterValue() throws IncorrectUIParameterTypeException, UnknownUIParameterException {
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
	public void testGetComboUIParameterNullValue() throws IncorrectUIParameterTypeException, UnknownUIParameterException {
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

	@Test(expected = UnknownUIParameterException.class) 
	public void testGetComboUIParameterWrongValue() throws IncorrectUIParameterTypeException, UnknownUIParameterException {
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

	@Test(expected = IncorrectUIParameterTypeException.class) 
	public void testGetComboUIParameterWrongType() throws IncorrectUIParameterTypeException, UnknownUIParameterException {
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
	public void testGetColorUIParameterValue() throws IncorrectUIParameterTypeException, UnknownUIParameterException {
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
	public void testGetColorUIParameterNullValue() throws IncorrectUIParameterTypeException, UnknownUIParameterException {
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

	@Test(expected = UnknownUIParameterException.class) 
	public void testGetColorUIParameterWrongValue() throws IncorrectUIParameterTypeException, UnknownUIParameterException {
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

	@Test(expected = IncorrectUIParameterTypeException.class) 
	public void testGetColorUIParameterWrongType() throws IncorrectUIParameterTypeException, UnknownUIParameterException {
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
	public void testGetUIPropertyParameterValue() throws IncorrectUIParameterTypeException, UnknownUIParameterException {
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
	public void testGetUIPropertyParameterNullValue() throws IncorrectUIParameterTypeException, UnknownUIParameterException {
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

	@Test(expected = UnknownUIParameterException.class) 
	public void testGetUIPropertyParameterWrongValue() throws IncorrectUIParameterTypeException, UnknownUIParameterException {
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

	@Test(expected = IncorrectUIParameterTypeException.class) 
	public void testGetUIPropertyParameterWrongType() throws IncorrectUIParameterTypeException, UnknownUIParameterException {
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
	public void testUpdateAllParameters() throws UnknownUIParameterException {
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
					} catch (IncorrectUIParameterTypeException e) {
						e.printStackTrace();
					} catch (UnknownUIParameterException e) {
						e.printStackTrace();
					}
				} else if(parameterName.equals(params[1])) {
					try {
						val2 = this.getStringUIParameterValue(parameterName);
					} catch (IncorrectUIParameterTypeException e) {
						e.printStackTrace();
					} catch (UnknownUIParameterException e) {
						e.printStackTrace();
					}
				} else if(parameterName.equals(params[2])) {
					try {
						val3 = this.getStringUIParameterValue(parameterName);
					} catch (IncorrectUIParameterTypeException e) {
						e.printStackTrace();
					} catch (UnknownUIParameterException e) {
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

	// substitute UIParameter
	@Test
	public void testSubsituteUIParameter() throws IncorrectUIParameterTypeException, UnknownUIParameterException {
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

	@Test
	public void testSubsituteUIParameterOfDifferentType() throws IncorrectUIParameterTypeException, UnknownUIParameterException {
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
				this.addUIParameter(new IntegerUIParameter(this, param, "", 1));
			}
		};
  
		assertEquals(UIParameter.UIParameterType.STRING, cp1.getUIParameterType(param));
		assertEquals(UIParameter.UIParameterType.INTEGER, cp2.getUIParameterType(param));
		
		//substitute the UIParameter
		cp1.substituteParameter(cp2.getUIParameter(param));

		// check that the type of cp1's UIParameter has not changed
		assertEquals(UIParameter.UIParameterType.STRING, cp1.getUIParameterType(param));
	}
	
	@Test
	public void testSubsituteUIParameterOfDifferentHash() throws IncorrectUIParameterTypeException, UnknownUIParameterException {
		final String defval = "default";
		final String param = "Param";
		final String param2 = "Param2";
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
				this.addUIParameter(new StringUIParameter(this, param2, "", defval));
			}
		};
  
		assertEquals(UIParameter.UIParameterType.STRING, cp1.getUIParameterType(param));
		assertEquals(UIParameter.UIParameterType.STRING, cp2.getUIParameterType(param2));
		
		//substitute the UIParameter
		cp1.substituteParameter(cp2.getUIParameter(param2));

		// check that the type of cp1's UIParameter has not changed
		assertEquals(UIParameter.UIParameterType.STRING, cp1.getUIParameterType(param));
		
		// and that param2 is unknown
		assertEquals(UIParameter.UIParameterType.NONE, cp1.getUIParameterType(param2));
	}
	

	
	////////////////////////////////////////////
	//////////// InternalProperties ////////////
	////////////////////////////////////////////

	@Test
	public void testAddInternalProperty() throws UnknownInternalPropertyException {
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

		assertEquals(InternalProperty.InternalPropertyType.INTEGER, cp.getInternalPropertyType(intprop[0]));
		assertEquals(InternalProperty.InternalPropertyType.BOOLEAN, cp.getInternalPropertyType(intprop[1]));
		assertEquals(InternalProperty.InternalPropertyType.DOUBLE, cp.getInternalPropertyType(intprop[2]));
	}

	@Test(expected = NullPointerException.class) 
	public void testAddNullInternalProperty(){		
		new ConfigurableTestPanel("MyPanel") {
	
			private static final long serialVersionUID = 1L;
	
			@Override
			protected void initializeInternalProperties() {
				this.addInternalProperty(null);
			}
		};
	}
	
	@Test(expected = NullPointerException.class) 
	public void testGetNullInternalProperty() throws UnknownInternalPropertyException{		
		final int defval = 1;
		final String intprop = "InternalProp";
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeInternalProperties() {
				this.addInternalProperty(new IntegerInternalProperty(this, intprop, new Integer(defval)));
			}
		};
		
		cp.getInternalProperty(null);
	}
	
	@Test(expected = UnknownInternalPropertyException.class) 
	public void testGetUnknownInternalProperty() throws UnknownInternalPropertyException{		
		final int defval = 1;
		final String intprop = "InternalProp";
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeInternalProperties() {
				this.addInternalProperty(new IntegerInternalProperty(this, intprop, new Integer(defval)));
			}
		};
		
		cp.getInternalProperty("Alice");
	}
	
	@Test(expected = NullPointerException.class) 
	public void testGetNullInternalPropertyType(){		
		final int defval = 1;
		final String intprop = "InternalProp";
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeInternalProperties() {
				this.addInternalProperty(new IntegerInternalProperty(this, intprop, new Integer(defval)));
			}
		};
		
		cp.getInternalPropertyType(null);
	}

	// integer internalproperty
	@Test
	public void testSetIntegerInternalProperty() throws IncorrectInternalPropertyTypeException, UnknownInternalPropertyException {
		final int defval = 1;
		final String intprop = "InternalProp";
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeInternalProperties() {
				this.addInternalProperty(new IntegerInternalProperty(this, intprop, new Integer(defval)));
			}
		};
		
		assertEquals(defval, (int) cp.getInternalProperty(intprop).getInternalPropertyValue());
		
		int val = -5;
		cp.setInternalPropertyValue(intprop, val);
		
		assertEquals(val, (int) cp.getInternalProperty(intprop).getInternalPropertyValue());
	}

	@Test(expected = NullPointerException.class) 
	public void testSetIntegerNullInternalProperty() throws IncorrectInternalPropertyTypeException, UnknownInternalPropertyException {
		final int defval = 1;
		final String intprop = "InternalProp";
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeInternalProperties() {
				this.addInternalProperty(new IntegerInternalProperty(this, intprop, new Integer(defval)));
			}
		};
		
		int val = -5;
		cp.setInternalPropertyValue(null, val);
	}

	/**
	 * Sets the value of a DoubleInternalProperty calling setInternalProperty(String, int).
	 * @throws IncorrectInternalPropertyTypeException
	 * @throws UnknownInternalPropertyException 
	 */
	@Test(expected = IncorrectInternalPropertyTypeException.class) 
	public void testSetIntegerInternalPropertyWrongType() throws IncorrectInternalPropertyTypeException, UnknownInternalPropertyException {
		final double defval = 1.56;
		final String intprop = "InternalProp";
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeInternalProperties() {
				this.addInternalProperty(new DoubleInternalProperty(this, intprop, new Double(defval)));
			}
		};
		
		int val = -5;
		cp.setInternalPropertyValue(intprop, val);
	}

	@Test
	public void testGetIntegerInternalProperty() throws IncorrectInternalPropertyTypeException, UnknownInternalPropertyException {
		final int defval = 1;
		final String intprop = "InternalProp";
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeInternalProperties() {
				this.addInternalProperty(new IntegerInternalProperty(this, intprop, new Integer(defval)));
			}
		};
		
		int val = -5;
		cp.setInternalPropertyValue(intprop, val);

		assertEquals(val, cp.getIntegerInternalPropertyValue(intprop));
	}

	@Test(expected = NullPointerException.class) 
	public void testGetIntegerNullInternalProperty() throws IncorrectInternalPropertyTypeException {
		final int defval = 1;
		final String intprop = "InternalProp";
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeInternalProperties() {
				this.addInternalProperty(new IntegerInternalProperty(this, intprop, new Integer(defval)));
			}
		};

		cp.getIntegerInternalPropertyValue(null);
	}
	
	// double InternalProperty
	@Test
	public void testSetDoubleInternalProperty() throws IncorrectInternalPropertyTypeException, UnknownInternalPropertyException {
		final double defval = 1.59;
		final String intprop = "InternalProp";
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeInternalProperties() {
				this.addInternalProperty(new DoubleInternalProperty(this, intprop, new Double(defval)));
			}
		};
		
		assertEquals(defval, (double) cp.getInternalProperty(intprop).getInternalPropertyValue(), 1E-20);
		
		double val = -5.4945;
		cp.setInternalPropertyValue(intprop, val);
		
		assertEquals(val, (double) cp.getInternalProperty(intprop).getInternalPropertyValue(), 1E-20);
	}

	@Test(expected = NullPointerException.class) 
	public void testSetDoubleNullInternalProperty() throws IncorrectInternalPropertyTypeException {
		final double defval = 1.59;
		final String intprop = "InternalProp";
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeInternalProperties() {
				this.addInternalProperty(new DoubleInternalProperty(this, intprop, new Double(defval)));
			}
		};
		
		double val = -5.4945;
		cp.setInternalPropertyValue(null, val);
	}

	/**
	 * Sets the value of an IntegerInternalProperty using a call to setInternalProperty(String, double)
	 * @throws IncorrectInternalPropertyTypeException
	 */
	@Test(expected = IncorrectInternalPropertyTypeException.class) 
	public void testSetDoubleInternalPropertyWrongType() throws IncorrectInternalPropertyTypeException {
		final int defval = 1;
		final String intprop = "InternalProp";
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeInternalProperties() {
				this.addInternalProperty(new IntegerInternalProperty(this, intprop, new Integer(defval)));
			}
		};
		
		double val = -5.4945;
		cp.setInternalPropertyValue(intprop, val);
	}

	@Test
	public void testGetDoubleInternalProperty() throws IncorrectInternalPropertyTypeException {
		final double defval = 1.59;
		final String intprop = "InternalProp";
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeInternalProperties() {
				this.addInternalProperty(new DoubleInternalProperty(this, intprop, new Double(defval)));
			}
		};
		
		double val = -5.448;
		cp.setInternalPropertyValue(intprop, val);

		assertEquals(val, cp.getDoubleInternalPropertyValue(intprop), 1E-20);
	}

	@Test(expected = NullPointerException.class) 
	public void testGetDoubleNullInternalProperty() throws IncorrectInternalPropertyTypeException {
		final double defval = 1.59;
		final String intprop = "InternalProp";
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeInternalProperties() {
				this.addInternalProperty(new DoubleInternalProperty(this, intprop, new Double(defval)));
			}
		};

		cp.getDoubleInternalPropertyValue(null);
	}
	
	// BoolInternalProperty
	@Test
	public void testSetBoolInternalProperty() throws IncorrectInternalPropertyTypeException, UnknownInternalPropertyException {
		final boolean defval = false;
		final String intprop = "InternalProp";
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeInternalProperties() {
				this.addInternalProperty(new BoolInternalProperty(this, intprop, new Boolean(defval)));
			}
		};
		
		assertEquals(defval, (boolean) cp.getInternalProperty(intprop).getInternalPropertyValue());
		
		boolean val = true;
		cp.setInternalPropertyValue(intprop, val);
		
		assertEquals(val, (boolean) cp.getInternalProperty(intprop).getInternalPropertyValue());
	}

	@Test(expected = NullPointerException.class) 
	public void testSetBoolNullInternalProperty() throws IncorrectInternalPropertyTypeException {
		final boolean defval = false;
		final String intprop = "InternalProp";
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeInternalProperties() {
				this.addInternalProperty(new BoolInternalProperty(this, intprop, new Boolean(defval)));
			}
		};
		
		boolean val = true;
		cp.setInternalPropertyValue(null, val);
	}

	/**
	 * Sets the value of an IntegerInternalProperty using a call to setInternalProperty(String, boolean)
	 * @throws IncorrectInternalPropertyTypeException
	 */
	@Test(expected = IncorrectInternalPropertyTypeException.class) 
	public void testSetBoolInternalPropertyWrongType() throws IncorrectInternalPropertyTypeException {
		final int defval = 1;
		final String intprop = "InternalProp";
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeInternalProperties() {
				this.addInternalProperty(new IntegerInternalProperty(this, intprop, new Integer(defval)));
			}
		};
		
		boolean val = true;
		cp.setInternalPropertyValue(intprop, val);
	}

	@Test
	public void testGetBoolInternalProperty() throws IncorrectInternalPropertyTypeException {
		final boolean defval = false;
		final String intprop = "InternalProp";
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeInternalProperties() {
				this.addInternalProperty(new BoolInternalProperty(this, intprop, new Boolean(defval)));
			}
		};
		
		
		boolean val = true;
		cp.setInternalPropertyValue(intprop, val);

		assertEquals(val, cp.getBoolInternalPropertyValue(intprop));
	}

	@Test(expected = NullPointerException.class) 
	public void testGetBoolNullInternalProperty() throws IncorrectInternalPropertyTypeException {
		final boolean defval = false;
		final String intprop = "InternalProp";
		ConfigurableTestPanel cp = new ConfigurableTestPanel("MyPanel") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeInternalProperties() {
				this.addInternalProperty(new BoolInternalProperty(this, intprop, new Boolean(defval)));
			}
		};
		
		cp.getBoolInternalPropertyValue(null);
	}

	// substitute internalprop
	@Test
	public void testSubsituteInternalProperty() throws IncorrectInternalPropertyTypeException, UnknownInternalPropertyException {
		final int defval = 1;
		final String intprop = "InternalProp";
		ConfigurableTestPanel cp1 = new ConfigurableTestPanel("MyPanel") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeInternalProperties() {
				this.addInternalProperty(new IntegerInternalProperty(this, intprop, new Integer(defval)));
			}
		};

		ConfigurableTestPanel cp2 = new ConfigurableTestPanel("MyPanel") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeInternalProperties() {
				this.addInternalProperty(new IntegerInternalProperty(this, intprop, new Integer(defval)));
			}
		};

		assertEquals(defval, cp1.getIntegerInternalPropertyValue(intprop));
		assertEquals(defval, cp2.getIntegerInternalPropertyValue(intprop));
		
		// change cp2 property's value
		int val = -3;
		cp2.setInternalPropertyValue(intprop, val);
		
		// substitute property in cp1
		cp1.substituteInternalProperty(cp2.getInternalProperty(intprop));
		
		assertEquals(val, cp1.getIntegerInternalPropertyValue(intprop));
	}

	@Test
	public void testSubsituteInternalPropertyOfDifferentType() throws IncorrectInternalPropertyTypeException, UnknownInternalPropertyException {
		final int defval = 1;
		final String intprop = "InternalProp";
		ConfigurableTestPanel cp1 = new ConfigurableTestPanel("MyPanel") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeInternalProperties() {
				this.addInternalProperty(new IntegerInternalProperty(this, intprop, new Integer(defval)));
			}
		};

		ConfigurableTestPanel cp2 = new ConfigurableTestPanel("MyPanel") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeInternalProperties() {
				this.addInternalProperty(new DoubleInternalProperty(this, intprop, new Double(1.48)));
			}
		};

		assertEquals(InternalProperty.InternalPropertyType.INTEGER, cp1.getInternalPropertyType(intprop));
		assertEquals(InternalProperty.InternalPropertyType.DOUBLE, cp2.getInternalPropertyType(intprop));
		
		// substitute property in cp1
		cp1.substituteInternalProperty(cp2.getInternalProperty(intprop));
		
		// should not have worked
		assertEquals(InternalProperty.InternalPropertyType.INTEGER, cp1.getInternalPropertyType(intprop));
	}

	@Test
	public void testSubsituteInternalPropertyOfDifferentLabel() throws IncorrectInternalPropertyTypeException, UnknownInternalPropertyException {
		final int defval = 1;
		final String intprop = "InternalProp";
		final String intprop2 = "InternalProp2";
		ConfigurableTestPanel cp1 = new ConfigurableTestPanel("MyPanel") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeInternalProperties() {
				this.addInternalProperty(new IntegerInternalProperty(this, intprop, new Integer(defval)));
			}
		};

		ConfigurableTestPanel cp2 = new ConfigurableTestPanel("MyPanel") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeInternalProperties() {
				this.addInternalProperty(new IntegerInternalProperty(this, intprop2, new Integer(defval)));
			}
		};

		assertEquals(InternalProperty.InternalPropertyType.INTEGER, cp1.getInternalPropertyType(intprop));
		assertEquals(InternalProperty.InternalPropertyType.INTEGER, cp2.getInternalPropertyType(intprop2));
		
		// substitute property in cp1
		cp1.substituteInternalProperty(cp2.getInternalProperty(intprop2));
		
		// should not have worked
		assertEquals(InternalProperty.InternalPropertyType.NONE, cp1.getInternalPropertyType(intprop2));
	}
	

	// component trigger enabled and on/off
	// update all params and all props
	// test the triggering of hasChanged()
	// test the order of events
	
	
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
