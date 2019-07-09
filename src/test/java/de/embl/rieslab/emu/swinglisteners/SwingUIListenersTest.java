package de.embl.rieslab.emu.swinglisteners;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.junit.Test;

import de.embl.rieslab.emu.ui.ConfigurablePanel;
import de.embl.rieslab.emu.ui.uiproperties.MultiStateUIProperty;
import de.embl.rieslab.emu.ui.uiproperties.PropertyPair;
import de.embl.rieslab.emu.ui.uiproperties.SingleStateUIProperty;
import de.embl.rieslab.emu.ui.uiproperties.TwoStateUIProperty;
import de.embl.rieslab.emu.ui.uiproperties.UIProperty;
import de.embl.rieslab.emu.ui.uiproperties.flag.NoFlag;
import de.embl.rieslab.emu.testableclasses.TestableConfigurableMainFrame;
import de.embl.rieslab.emu.testableclasses.TestableConfigurablePanel;
import de.embl.rieslab.emu.testableclasses.TestableMMProperty;

public class SwingUIListenersTest {

	
	@Test
	public void testJComboboxActionListenerOnString() {
		final String prop = "My Prop";
		final String[] vals = {"MyValue1", "MyValue2", "MyValue3"};  
		final JComboBox<String> combo = new JComboBox<String>(vals);
		
		final ComponentTestPanel cp = new ComponentTestPanel("My panel") {
			@Override
			public void setUpComponent() {
				this.add(combo);
			}
			@Override
			protected void initializeProperties() {
				this.property = new UIProperty(this, prop, "", new NoFlag());
				this.addUIProperty(this.property);
			}
			@Override
			protected void addComponentListeners() {
				SwingUIListeners.addActionListenerOnStringValue(this, prop, combo);
			}
		};
		final TestableConfigurableMainFrame cmf = new TestableConfigurableMainFrame() { // need a ConfigurableMainFrame to call functions in the ConfigurablePanel
			@Override
			protected void initComponents() {
				this.add(cp);
			}
		};
		cmf.addAllListeners(); // this calls "addComponentListeners()", which otherwise is called when loading a configuration
		
		// creates a dummy MMProperty
		TestableMMProperty mmprop = new TestableMMProperty("Prop1");
		
		// pairs the two
		PropertyPair.pair(cp.property, mmprop);
		
		////////////////////////////////////////////
		/// Test 1: default value of the MMproperty
		assertEquals(TestableMMProperty.DEFVAL, mmprop.getStringValue());
		
		// change the selected item of the JComboBox to trigger the action listener
		int selectedIndex = 1;
		combo.setSelectedIndex(selectedIndex); 
		
		// waits to let the other thread finish
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		////////////////////////////////////////////////
		/// Test 2: value of the MMProperty has changed
		assertEquals(vals[selectedIndex], mmprop.getStringValue());	
	}
	
	@Test
	public void testJComboboxActionListenerOnIndex() {
		final String prop = "My Prop";
		final String[] vals = {"MyValue1", "MyValue2", "MyValue3"};  
		final JComboBox<String> combo = new JComboBox<String>(vals);
		
		final ComponentTestPanel cp = new ComponentTestPanel("My panel") {
			@Override
			public void setUpComponent() {
				this.add(combo);
			}
			@Override
			protected void initializeProperties() {
				this.property = new UIProperty(this, prop, "", new NoFlag());
				this.addUIProperty(this.property);
			}
			@Override
			protected void addComponentListeners() {
				SwingUIListeners.addActionListenerOnSelectedIndex(this, prop, combo);
			}
		};
		final TestableConfigurableMainFrame cmf = new TestableConfigurableMainFrame() { // need a ConfigurableMainFrame to call functions in the ConfigurablePanel
			@Override
			protected void initComponents() {
				this.add(cp);
			}
		};
		cmf.addAllListeners(); // this calls "addComponentListeners()", which otherwise is called when loading a configuration
		
		// creates a dummy MMProperty
		TestableMMProperty mmprop = new TestableMMProperty("Prop1");
		
		// pairs the two
		PropertyPair.pair(cp.property, mmprop);
		
		////////////////////////////////////////////
		/// Test 1: default value of the MMproperty
		assertEquals(TestableMMProperty.DEFVAL, mmprop.getStringValue());
		
		// change the selected item of the JComboBox to trigger the action listener
		int selectedIndex = 1;
		combo.setSelectedIndex(selectedIndex); 
		
		// waits to let the other thread finish
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		////////////////////////////////////////////////
		/// Test 2: value of the MMProperty has changed
		assertEquals(String.valueOf(selectedIndex), mmprop.getStringValue());	
	}
	
	
	@Test
	public void testJComboboxActionListenerOnIndexwithArray() {
		final String prop = "My Prop";
		final String[] combovals = {"MyValue1", "MyValue2", "MyValue3"}; // values shown to the user (maybe set by a UIParameter) 
		final String[] vals = {"CoolName1", "CoolName2", "CoolName3"}; // friendly names used to trigger the UIProperty 
		final JComboBox<String> combo = new JComboBox<String>(combovals);
		
		final ComponentTestPanel cp = new ComponentTestPanel("My panel") {
			@Override
			public void setUpComponent() {
				this.add(combo);
			}
			@Override
			protected void initializeProperties() {
				this.property = new UIProperty(this, prop, "", new NoFlag());
				this.addUIProperty(this.property);
			}
			@Override
			protected void addComponentListeners() {
				SwingUIListeners.addActionListenerOnSelectedIndex(this, prop, combo, vals); // uses names for the values sent to the UIProperty
			}
		};
		final TestableConfigurableMainFrame cmf = new TestableConfigurableMainFrame() { // need a ConfigurableMainFrame to call functions in the ConfigurablePanel
			@Override
			protected void initComponents() {
				this.add(cp);
			}
		};
		cmf.addAllListeners(); // this calls "addComponentListeners()", which otherwise is called when loading a configuration
		
		// creates a dummy MMProperty
		TestableMMProperty mmprop = new TestableMMProperty("Prop1");
		
		// pairs the two
		PropertyPair.pair(cp.property, mmprop);
		
		////////////////////////////////////////////
		/// Test 1: default value of the MMproperty
		assertEquals(TestableMMProperty.DEFVAL, mmprop.getStringValue());
		
		// change the selected item of the JComboBox to trigger the action listener
		int selectedIndex = 1;
		combo.setSelectedIndex(selectedIndex); 
		
		// waits to let the other thread finish
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		////////////////////////////////////////////////
		/// Test 2: value of the MMProperty has changed
		assertEquals(vals[selectedIndex], mmprop.getStringValue());	
	}
	
	@Test
	public void testJTextFieldActionListenerOnIntegerOutput() throws AWTException {
		final String prop = "My Prop";
		final JTextField textfield = new JTextField();
		
		final ComponentTestPanel cp = new ComponentTestPanel("My panel") {
			@Override
			public void setUpComponent() {
				this.add(textfield);
			}
			@Override
			protected void initializeProperties() {
				this.property = new UIProperty(this, prop, "", new NoFlag());
				this.addUIProperty(this.property);
			}
			@Override
			protected void addComponentListeners() {
				SwingUIListeners.addActionListenerOnIntegerValue(this, prop, textfield); // uses names for the values sent to the UIProperty
			}
		};
		final TestableConfigurableMainFrame cmf = new TestableConfigurableMainFrame() { // need a ConfigurableMainFrame to call functions in the ConfigurablePanel
			@Override
			protected void initComponents() {
				this.add(cp);
			}
		};
		cmf.pack();
		cmf.addAllListeners(); // this calls "addComponentListeners()", which otherwise is called when loading a configuration
		
		// creates a dummy MMProperty
		TestableMMProperty mmprop = new TestableMMProperty("Prop1");
		
		// pairs the two
		PropertyPair.pair(cp.property, mmprop);
		
		////////////////////////////////////////////
		/// Test 1: default value of the MMproperty
		assertEquals(TestableMMProperty.DEFVAL, mmprop.getStringValue());
		
		// changes the text of the JtextField, this does not trigger the action listeners
		int value = 21;
		textfield.setText(String.valueOf(value)); 
		
		// triggers an "Enter2 key
		Robot r = new Robot();
		r.keyPress(KeyEvent.VK_ENTER);
		
		// waits to let the other thread finish
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		////////////////////////////////////////////////
		/// Test 2: value of the MMProperty has changed
		assertEquals(String.valueOf(value), mmprop.getStringValue());	
	}
	
	@Test
	public void testJTextFieldActionListenerOnBoundedIntegerOutput() throws AWTException {
		final String prop = "My Prop";
		final JTextField textfield = new JTextField();
		final int min = -4;
		final int max = 12;
		
		final ComponentTestPanel cp = new ComponentTestPanel("My panel") {
			@Override
			public void setUpComponent() {
				this.add(textfield);
			}
			@Override
			protected void initializeProperties() {
				this.property = new UIProperty(this, prop, "", new NoFlag());
				this.addUIProperty(this.property);
			}
			@Override
			protected void addComponentListeners() {
				SwingUIListeners.addActionListenerOnIntegerValue(this, prop, textfield, min, max); // uses names for the values sent to the UIProperty
			}
		};
		final TestableConfigurableMainFrame cmf = new TestableConfigurableMainFrame() { // need a ConfigurableMainFrame to call functions in the ConfigurablePanel
			@Override
			protected void initComponents() {
				this.add(cp);
			}
		};
		cmf.pack();
		cmf.addAllListeners(); // this calls "addComponentListeners()", which otherwise is called when loading a configuration
		
		// creates a dummy MMProperty
		TestableMMProperty mmprop = new TestableMMProperty("Prop1");
		
		// pairs the two
		PropertyPair.pair(cp.property, mmprop);
		
		////////////////////////////////////////////
		/// Test 1: default value of the MMproperty
		assertEquals(TestableMMProperty.DEFVAL, mmprop.getStringValue());
		
		// changes the text of the JtextField, this does not trigger the action listeners
		int value = 21;
		textfield.setText(String.valueOf(value)); 
		
		// triggers an "Enter2 key
		Robot r = new Robot();
		r.keyPress(KeyEvent.VK_ENTER);
		
		// waits to let the other thread finish
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		///////////////////////////////////////////////////
		/// Test 2: value of the MMProperty has not changed
		assertEquals(TestableMMProperty.DEFVAL, mmprop.getStringValue());	
		
		// changes the text of the JtextField, this does not trigger the action listeners
		value = -1;
		textfield.setText(String.valueOf(value)); 
		
		// triggers an "Enter2 key
		r.keyPress(KeyEvent.VK_ENTER);
		
		// waits to let the other thread finish
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		////////////////////////////////////////////////
		/// Test 3: value of the MMProperty has changed
		assertEquals(String.valueOf(value), mmprop.getStringValue());
				
		// changes the text of the JtextField, this does not trigger the action listeners
		int value2 = -8;
		textfield.setText(String.valueOf(value2)); 
		
		// triggers an "Enter2 key
		r.keyPress(KeyEvent.VK_ENTER);
		
		// waits to let the other thread finish
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		///////////////////////////////////////////////////
		/// Test 4: value of the MMProperty has not changed
		assertEquals(String.valueOf(value), mmprop.getStringValue());
	}
	
	@Test
	public void testJTextFieldActionListenerOnDoubleOutput() throws AWTException {
		final String prop = "My Prop";
		final JTextField textfield = new JTextField();
		
		final ComponentTestPanel cp = new ComponentTestPanel("My panel") {
			@Override
			public void setUpComponent() {
				this.add(textfield);
			}
			@Override
			protected void initializeProperties() {
				this.property = new UIProperty(this, prop, "", new NoFlag());
				this.addUIProperty(this.property);
			}
			@Override
			protected void addComponentListeners() {
				SwingUIListeners.addActionListenerOnDoubleValue(this, prop, textfield); // uses names for the values sent to the UIProperty
			}
		};
		final TestableConfigurableMainFrame cmf = new TestableConfigurableMainFrame() { // need a ConfigurableMainFrame to call functions in the ConfigurablePanel
			@Override
			protected void initComponents() {
				this.add(cp);
			}
		};
		cmf.pack();
		cmf.addAllListeners(); // this calls "addComponentListeners()", which otherwise is called when loading a configuration
		
		// creates a dummy MMProperty
		TestableMMProperty mmprop = new TestableMMProperty("Prop1");
		
		// pairs the two
		PropertyPair.pair(cp.property, mmprop);
		
		////////////////////////////////////////////
		/// Test 1: default value of the MMproperty
		assertEquals(TestableMMProperty.DEFVAL, mmprop.getStringValue());
		
		// changes the text of the JtextField, this does not trigger the action listeners
		double value = 21.045;
		textfield.setText(String.valueOf(value)); 
		
		// triggers an "Enter" key
		Robot r = new Robot();
		r.keyPress(KeyEvent.VK_ENTER);
		
		// waits to let the other thread finish
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		////////////////////////////////////////////////
		/// Test 2: value of the MMProperty has changed
		assertEquals(String.valueOf(value), mmprop.getStringValue());	
		
		// changes the text of the JtextField, this does not trigger the action listeners
		textfield.setText("Not a value"); 
		
		// triggers an "Enter" key
		r.keyPress(KeyEvent.VK_ENTER);
		
		// waits to let the other thread finish
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		///////////////////////////////////////////////////
		/// Test 3: value of the MMProperty has not changed
		assertEquals(String.valueOf(value), mmprop.getStringValue());	
	}
	
	@Test
	public void testJTextFieldActionListenerOnBoundedDoubleOutput() throws AWTException {
		final String prop = "My Prop";
		final JTextField textfield = new JTextField();
		final double min = -4.022;
		final double max = 12.586;
		
		final ComponentTestPanel cp = new ComponentTestPanel("My panel") {
			@Override
			public void setUpComponent() {
				this.add(textfield);
			}
			@Override
			protected void initializeProperties() {
				this.property = new UIProperty(this, prop, "", new NoFlag());
				this.addUIProperty(this.property);
			}
			@Override
			protected void addComponentListeners() {
				SwingUIListeners.addActionListenerOnDoubleValue(this, prop, textfield, min, max); // uses names for the values sent to the UIProperty
			}
		};
		final TestableConfigurableMainFrame cmf = new TestableConfigurableMainFrame() { // need a ConfigurableMainFrame to call functions in the ConfigurablePanel
			@Override
			protected void initComponents() {
				this.add(cp);
			}
		};
		cmf.pack();
		cmf.addAllListeners(); // this calls "addComponentListeners()", which otherwise is called when loading a configuration
		
		// creates a dummy MMProperty
		TestableMMProperty mmprop = new TestableMMProperty("Prop1");
		
		// pairs the two
		PropertyPair.pair(cp.property, mmprop);
		
		////////////////////////////////////////////
		/// Test 1: default value of the MMproperty
		assertEquals(TestableMMProperty.DEFVAL, mmprop.getStringValue());
		
		// changes the text of the JtextField, this does not trigger the action listeners
		double value = 12.68;
		textfield.setText(String.valueOf(value)); 
		
		// triggers an "Enter2 key
		Robot r = new Robot();
		r.keyPress(KeyEvent.VK_ENTER);
		
		// waits to let the other thread finish
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		///////////////////////////////////////////////////
		/// Test 2: value of the MMProperty has not changed
		assertEquals(TestableMMProperty.DEFVAL, mmprop.getStringValue());	
		
		// changes the text of the JtextField, this does not trigger the action listeners
		value = -4.005;
		textfield.setText(String.valueOf(value)); 
		
		// triggers an "Enter2 key
		r.keyPress(KeyEvent.VK_ENTER);
		
		// waits to let the other thread finish
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		////////////////////////////////////////////////
		/// Test 3: value of the MMProperty has changed
		assertEquals(String.valueOf(value), mmprop.getStringValue());
				
		// changes the text of the JtextField, this does not trigger the action listeners
		double value2 = -8.456;
		textfield.setText(String.valueOf(value2)); 
		
		// triggers an "Enter2 key
		r.keyPress(KeyEvent.VK_ENTER);
		
		// waits to let the other thread finish
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		///////////////////////////////////////////////////
		/// Test 4: value of the MMProperty has not changed
		assertEquals(String.valueOf(value), mmprop.getStringValue());
	}
	
	
	
	
	public void testActionOnComponents() {
		// creates a testing configurable panel
		TestableConfigurablePanel cp = new TestableConfigurablePanel("myPanel");
		
		// creates dummy MMProperties
		TestableMMProperty mmprop1 = new TestableMMProperty("Prop1");
		TestableMMProperty mmprop2 = new TestableMMProperty("Prop2");
		TestableMMProperty mmprop3 = new TestableMMProperty("Prop3");
		TestableMMProperty mmprop4 = new TestableMMProperty("Prop4");
		TestableMMProperty mmprop5 = new TestableMMProperty("Prop5");
		
		// pairs UIProperty and MMProperty and set states
		PropertyPair.pair(cp.getPublicUIProperty(cp.UIPROP), mmprop1);
		PropertyPair.pair(cp.getPublicUIProperty(cp.UIPROP2), mmprop2);
		
		PropertyPair.pair(cp.getPublicUIProperty(cp.MULTPROP), mmprop3);
		MultiStateUIProperty multi = (MultiStateUIProperty) cp.getPublicUIProperty(cp.MULTPROP);
		int n = multi.getNumberOfStates();
		assertEquals(cp.sizeMultiProp,n);
		String[] vals = new String[n];
		for(int i=0;i<n;i++) {
			vals[i] = "MyState "+i;
		}
		multi.setStateValues(vals);
		
		PropertyPair.pair(cp.getPublicUIProperty(cp.SINGPROP), mmprop4);
		SingleStateUIProperty sing = (SingleStateUIProperty) cp.getPublicUIProperty(cp.SINGPROP);
		String singleVal = "My Single value";
		sing.setStateValue(singleVal);
		
		PropertyPair.pair(cp.getPublicUIProperty(cp.TWOSTPROP), mmprop5);
		TwoStateUIProperty twost = (TwoStateUIProperty) cp.getPublicUIProperty(cp.TWOSTPROP);
		String offst = "My off state";
		String onst = "My on state";
		twost.setOnStateValue(onst);
		twost.setOffStateValue(offst);
		
		// creates a configurablemainframe, in order to call the configureAllListeners method
		TestableConfigurableMainFrame cmf = new TestableConfigurableMainFrame() {

			@Override
			protected void initComponents() {
				this.add(cp);
			}
		};
		cmf.addAllListeners(); // method usually automatically when loading a configuration
		
		// check that the singlestateUIproperty state is singleVal
		assertEquals(singleVal, sing.getStateValue());
		assertEquals(offst, twost.getOffStateValue());
		assertEquals(onst, twost.getOnStateValue());
		
		// test that the default value of mmprop4 is not singleVal
		assertNotEquals(singleVal, mmprop4.getValue());
		assertNotEquals(onst, mmprop5.getValue());
		
		// click on the JButton
		cp.button.doClick();
		cp.toggle.doClick();
		int indexcombo = 2;
		cp.combobox.setSelectedIndex(indexcombo);

		// waits to let the other thread finish
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// test that the new values have been changed
		assertEquals(singleVal, mmprop4.getValue());
		assertEquals(onst, mmprop5.getValue());
		assertEquals(vals[indexcombo], mmprop3.getValue());
		
		// click off the togglebutton
		cp.toggle.doClick();

		// waits to let the other thread finish
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// test the off value
		assertEquals(offst, mmprop5.getValue());
	}
	
	private abstract class ComponentTestPanel extends ConfigurablePanel{

		public UIProperty property;
		
		public ComponentTestPanel(String label) {
			super(label);
			
			setUpComponent();
		}
		
		public abstract void setUpComponent();

		@Override
		protected void initializeInternalProperties() {}

		@Override
		protected void initializeParameters() {}

		@Override
		public void internalpropertyhasChanged(String propertyName) {}

		@Override
		protected void propertyhasChanged(String propertyName, String newvalue) {}

		@Override
		protected void parameterhasChanged(String parameterName) {}

		@Override
		public void shutDown() {}

		@Override
		public String getDescription() {return "";}
	}
}
