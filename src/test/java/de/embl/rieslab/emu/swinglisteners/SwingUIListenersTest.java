package de.embl.rieslab.emu.swinglisteners;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

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
	public void testJComboboxActionListenerOnIndexWithArray() {
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
				SwingUIListeners.addActionListenerOnSelectedIndex(this, prop, combo, vals); 
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
	public void testJTextFieldActionListenerOnIntegerValue() throws AWTException {
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
				SwingUIListeners.addActionListenerOnIntegerValue(this, prop, textfield);
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
	}

	@Test
	public void testJTextFieldActionListenerOnStringValue() throws AWTException {
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
				SwingUIListeners.addActionListenerOnStringValue(this, prop, textfield);
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
		final String value = "£$£!14,:{";
		textfield.setText(value); 
		
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
		assertEquals(value, mmprop.getStringValue());	
	}
	
	@Test
	public void testJTextFieldActionListenerOnIntegerValueWithFeedbackToSlider() throws AWTException {
		final String prop = "My Prop";
		final JTextField textfield = new JTextField();
		final JSlider slider = new JSlider(); // 0 - 100
		
		final ComponentTestPanel cp = new ComponentTestPanel("My panel") {
			@Override
			public void setUpComponent() {
				this.add(textfield);
				this.add(slider);
			}
			@Override
			protected void initializeProperties() {
				this.property = new UIProperty(this, prop, "", new NoFlag());
				this.addUIProperty(this.property);
			}
			@Override
			protected void addComponentListeners() {
				SwingUIListeners.addActionListenerOnIntegerValue(this, prop, textfield, slider);
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
		assertEquals(value, slider.getValue());	
		
		// changes the text of the JtextField, this does not trigger the action listeners
		int value2 = 110;
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
		/// Test 3: value of the MMProperty has not changed
		assertEquals(String.valueOf(value), mmprop.getStringValue());
		assertEquals(value, slider.getValue());	
	}
	
	@Test
	public void testJTextFieldActionListenerOnBoundedIntegerValue() throws AWTException {
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
				SwingUIListeners.addActionListenerOnIntegerValue(this, prop, textfield, min, max); 
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
	public void testJTextFieldActionListenerOnDoubleValue() throws AWTException {
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
				SwingUIListeners.addActionListenerOnDoubleValue(this, prop, textfield); 
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
	public void testJTextFieldActionListenerOnBoundedDoubleValue() throws AWTException {
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
				SwingUIListeners.addActionListenerOnDoubleValue(this, prop, textfield, min, max); 
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

	@Test
	public void testJTextFieldActionListenerToDoubleTrigger() throws AWTException {
		final String prop = "My Prop";
		final JTextField textfield = new JTextField();
		
		final double defmyValue = 1.45;
		
		final ComponentTestPanel cp = new ComponentTestPanel("My panel") {
			
			@Override
			public void setUpComponent() {
				this.add(textfield);
			}
			@Override
			protected void initializeProperties() {}
			
			@Override
			protected void addComponentListeners() {
				SwingUIListeners.addActionListenerToDoubleTrigger(d -> myDoubleValue = d, textfield); 
			}
		};
		final TestableConfigurableMainFrame cmf = new TestableConfigurableMainFrame() { // need a ConfigurableMainFrame to call functions in the ConfigurablePanel
			@Override
			protected void initComponents() {
				this.add(cp);
			}
		};

		cp.myDoubleValue = defmyValue;

		cmf.pack();
		cmf.addAllListeners(); // this calls "addComponentListeners()", which otherwise is called when loading a configuration
		
		////////////////////////////////////////////
		/// Test 1: default value
		assertEquals(defmyValue, cp.myDoubleValue, 1E-20);
		
		// changes the text of the JtextField, this does not trigger the action listeners
		double value = 12.68;
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
		
		///////////////////////////////////////////////////
		/// Test 2: value has been updated
		assertEquals(value, cp.myDoubleValue, 1E-20);	
	}

	@Test
	public void testJToggleButtonActionListenerToBoolTrigger() {
		final String prop = "My Prop";
		final JToggleButton btn = new JToggleButton();
				
		final ComponentTestPanel cp = new ComponentTestPanel("My panel") {
			
			@Override
			public void setUpComponent() {
				this.add(btn);
			}
			@Override
			protected void initializeProperties() {}
			
			@Override
			protected void addComponentListeners() {
				SwingUIListeners.addActionListenerToBooleanTrigger(d -> myBoolValue = d, btn); 
			}
		};
		final TestableConfigurableMainFrame cmf = new TestableConfigurableMainFrame() { // need a ConfigurableMainFrame to call functions in the ConfigurablePanel
			@Override
			protected void initComponents() {
				this.add(cp);
			}
		};

		cp.myBoolValue = false;

		cmf.pack();
		cmf.addAllListeners(); // this calls "addComponentListeners()", which otherwise is called when loading a configuration
		
		////////////////////////////////////////////
		/// Test 1: default value
		assertEquals(false, cp.myBoolValue);
		
		// click on the button
		btn.doClick();
		
		// waits to let the other thread finish
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		///////////////////////////////////////////////////
		/// Test 2: value has been updated
		assertEquals(true, cp.myBoolValue);	
		
		// click on the button
		btn.doClick();
		
		// waits to let the other thread finish
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		///////////////////////////////////////////////////
		/// Test 3: value has been updated
		assertEquals(false, cp.myBoolValue);	
	}
	
	@Test
	public void testJTextFieldActionListenerToDoubleTriggerBounded() throws AWTException {
		final String prop = "My Prop";
		final JTextField textfield = new JTextField();
		
		final double defmyValue = 1.45;
		final double min = -1.46;
		final double max = 21.456;
		
		final ComponentTestPanel cp = new ComponentTestPanel("My panel") {
			
			@Override
			public void setUpComponent() {
				this.add(textfield);
			}
			@Override
			protected void initializeProperties() {}
			
			@Override
			protected void addComponentListeners() {
				SwingUIListeners.addActionListenerToDoubleTrigger(d -> myDoubleValue = d, textfield, min, max); 
			}
		};
		final TestableConfigurableMainFrame cmf = new TestableConfigurableMainFrame() { // need a ConfigurableMainFrame to call functions in the ConfigurablePanel
			@Override
			protected void initComponents() {
				this.add(cp);
			}
		};

		cp.myDoubleValue = defmyValue;

		cmf.pack();
		cmf.addAllListeners(); // this calls "addComponentListeners()", which otherwise is called when loading a configuration
		
		////////////////////////////////////////////
		/// Test 1: default value
		assertEquals(defmyValue, cp.myDoubleValue, 1E-20);
		
		// changes the text of the JtextField, this does not trigger the action listeners
		double value = 21.457;
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
		/// Test 3: value has not been updated
		assertEquals(defmyValue, cp.myDoubleValue, 1E-20);
		
		// changes the text of the JtextField, this does not trigger the action listeners
		value = -1.477;
		textfield.setText(String.valueOf(value)); 
		
		// triggers an "Enter2 key
		r.keyPress(KeyEvent.VK_ENTER);
		
		// waits to let the other thread finish
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		///////////////////////////////////////////////////
		/// Test 4: value has not been updated
		assertEquals(defmyValue, cp.myDoubleValue, 1E-20);

		
		// changes the text of the JtextField, this does not trigger the action listeners
		value = 20;
		textfield.setText(String.valueOf(value)); 
		
		// triggers an "Enter2 key
		r.keyPress(KeyEvent.VK_ENTER);
		
		// waits to let the other thread finish
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		///////////////////////////////////////////////////
		/// Test 5: value has been updated
		assertEquals(value, cp.myDoubleValue, 1E-20);
	}
	
	@Test
	public void testJSliderActionListenerOnIntegerValue() throws AWTException {
		final String prop = "My Prop";
		int value = 21;
		final JSlider slider = new JSlider(0,100,value);
		
		final ComponentTestPanel cp = new ComponentTestPanel("My panel") {
			@Override
			public void setUpComponent() {
				this.add(slider);
			}
			@Override
			protected void initializeProperties() {
				this.property = new UIProperty(this, prop, "", new NoFlag());
				this.addUIProperty(this.property);
			}
			@Override
			protected void addComponentListeners() {
				SwingUIListeners.addActionListenerOnIntegerValue(this, prop, slider);
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
		
		Point pt = new Point(slider.getLocation()); 
		SwingUtilities.convertPointToScreen(pt, slider); 
		int w = slider.getSize().width;
		
		// click once in the middle of the JSlider to advance one value
		Robot r = new Robot();
		r.mouseMove(pt.x+(int) w/2, pt.y);
		r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		
		// waits to let the other thread finish
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		////////////////////////////////////////////////
		/// Test 2: value of the MMProperty has changed
		assertEquals(String.valueOf(value+1), mmprop.getStringValue());	
	}
	
	@Test
	public void testJSliderActionListenerOnIntegerValueWithFeedbackToTextField() throws AWTException {
		final String prop = "My Prop";
		int value = 21;
		final JSlider slider = new JSlider(0,100,value);
		final JTextField textfield = new JTextField(prop);
		
		final ComponentTestPanel cp = new ComponentTestPanel("My panel") {
			@Override
			public void setUpComponent() {
				this.add(slider);
				this.add(textfield);
			}
			@Override
			protected void initializeProperties() {
				this.property = new UIProperty(this, prop, "", new NoFlag());
				this.addUIProperty(this.property);
			}
			@Override
			protected void addComponentListeners() {
				SwingUIListeners.addActionListenerOnIntegerValue(this, prop, slider, textfield);
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
		
		Point pt = new Point(slider.getLocation()); 
		SwingUtilities.convertPointToScreen(pt, slider); 
		int w = slider.getSize().width;
		
		// click once in the middle of the JSlider to advance one value
		Robot r = new Robot();
		r.mouseMove(pt.x+(int) w/2, pt.y);
		r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		
		// waits to let the other thread finish
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		////////////////////////////////////////////////
		/// Test 2: value of the MMProperty has changed
		assertEquals(String.valueOf(value+1), mmprop.getStringValue());	
		assertEquals(String.valueOf(value+1), textfield.getText());	
	}		
	
	@Test
	public void testJSliderActionListenerOnIntegerValueWithFeedbackToLabel() throws AWTException {
		final String prop = "My Prop";
		int value = 21;
		final JSlider slider = new JSlider(0,100,value);
		final JLabel label = new JLabel(prop);
		
		final ComponentTestPanel cp = new ComponentTestPanel("My panel") {
			@Override
			public void setUpComponent() {
				this.add(slider);
				this.add(label);
			}
			@Override
			protected void initializeProperties() {
				this.property = new UIProperty(this, prop, "", new NoFlag());
				this.addUIProperty(this.property);
			}
			@Override
			protected void addComponentListeners() {
				SwingUIListeners.addActionListenerOnIntegerValue(this, prop, slider, label);
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
		
		Point pt = new Point(slider.getLocation()); 
		SwingUtilities.convertPointToScreen(pt, slider); 
		int w = slider.getSize().width;
		
		// click once in the middle of the JSlider to advance one value
		Robot r = new Robot();
		r.mouseMove(pt.x+(int) w/2, pt.y);
		r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		
		// waits to let the other thread finish
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		////////////////////////////////////////////////
		/// Test 2: value of the MMProperty has changed
		assertEquals(String.valueOf(value+1), mmprop.getStringValue());	
		assertEquals(String.valueOf(value+1), label.getText());	
	}	
	
	@Test
	public void testJSliderActionListenerOnIntegerValueWithFeedbackPreSuffixes() throws AWTException {
		final String prop = "My Prop";
		int value = 21;
		final JSlider slider = new JSlider(0,100,value);
		final JLabel label = new JLabel(prop);
		final String prefix = "My slider: ";
		final String suffix = "%";
		
		
		final ComponentTestPanel cp = new ComponentTestPanel("My panel") {
			@Override
			public void setUpComponent() {
				this.add(slider);
				this.add(label);
			}
			@Override
			protected void initializeProperties() {
				this.property = new UIProperty(this, prop, "", new NoFlag());
				this.addUIProperty(this.property);
			}
			@Override
			protected void addComponentListeners() {
				SwingUIListeners.addActionListenerOnIntegerValue(this, prop, slider, label, prefix, suffix);
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
		
		Point pt = new Point(slider.getLocation()); 
		SwingUtilities.convertPointToScreen(pt, slider); 
		int w = slider.getSize().width;
		
		// click once in the middle of the JSlider to advance one value
		Robot r = new Robot();
		r.mouseMove(pt.x+(int) w/2, pt.y);
		r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		
		// waits to let the other thread finish
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		////////////////////////////////////////////////
		/// Test 2: value of the MMProperty has changed
		assertEquals(String.valueOf(value+1), mmprop.getStringValue());	
		assertEquals(prefix+String.valueOf(value+1)+suffix, label.getText());	
	}	

	@Test
	public void testButtonGroupActionListenerOnSelectedIndex() {
		final String prop = "My Prop";
		final JToggleButton button1 = new JToggleButton();
		final JToggleButton button2 = new JToggleButton();
		final JToggleButton button3 = new JToggleButton();
		final ButtonGroup bg = new ButtonGroup();
		bg.add(button1);
		bg.add(button2);
		bg.add(button3);
		
		final ComponentTestPanel cp = new ComponentTestPanel("My panel") {
			@Override
			public void setUpComponent() {
				this.add(button1);
				this.add(button2);
				this.add(button3);
			}
			@Override
			protected void initializeProperties() {
				this.property = new UIProperty(this, prop, "", new NoFlag());
				this.addUIProperty(this.property);
			}
			@Override
			protected void addComponentListeners() {
				SwingUIListeners.addActionListenerOnSelectedIndex(this, prop, bg);
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
		
		// click on button2
		button2.doClick();
		
		// waits to let the other thread finish
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		////////////////////////////////////////////////
		/// Test 2: value of the MMProperty has changed
		assertEquals("1", mmprop.getStringValue());		
	}

	@Test
	public void testButtonGroupActionListenerOnSelectedIndexWithArray() {
		final String prop = "My Prop";
		final JToggleButton button1 = new JToggleButton();
		final JToggleButton button2 = new JToggleButton();
		final JToggleButton button3 = new JToggleButton();
		final ButtonGroup bg = new ButtonGroup();
		bg.add(button1);
		bg.add(button2);
		bg.add(button3);
		
		final String[] vals = {"One", "Two", "Three"}; 
		
		final ComponentTestPanel cp = new ComponentTestPanel("My panel") {
			@Override
			public void setUpComponent() {
				this.add(button1);
				this.add(button2);
				this.add(button3);
			}
			@Override
			protected void initializeProperties() {
				this.property = new UIProperty(this, prop, "", new NoFlag());
				this.addUIProperty(this.property);
			}
			@Override
			protected void addComponentListeners() {
				SwingUIListeners.addActionListenerOnSelectedIndex(this, prop, bg, vals);
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
		
		// click on button2
		button2.doClick();
		
		// waits to let the other thread finish
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		////////////////////////////////////////////////
		/// Test 2: value of the MMProperty has changed
		assertEquals(vals[1], mmprop.getStringValue());		
	}

	@Test
	public void testAbstractButtonActionListenerToSingleState() {
		final String prop = "My Prop";
		final JButton btn = new JButton();
		
		final ComponentTestPanel cp = new ComponentTestPanel("My panel") {
			@Override
			public void setUpComponent() {
				this.add(btn);
			}
			@Override
			protected void initializeProperties() {
				this.property = new SingleStateUIProperty(this, prop, "", new NoFlag());
				this.addUIProperty(this.property);
			}
			@Override
			protected void addComponentListeners() {
				SwingUIListeners.addActionListenerToSingleState(this, prop, btn);;
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
		
		// sets single state
		String myValue = "My value";
		((SingleStateUIProperty) cp.property).setStateValue(myValue);
		
		////////////////////////////////////////////
		/// Test 1: default value of the MMproperty
		assertEquals(TestableMMProperty.DEFVAL, mmprop.getStringValue());
		
		// click on button
		btn.doClick();
		
		// waits to let the other thread finish
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		////////////////////////////////////////////////
		/// Test 2: value of the MMProperty has changed
		assertEquals(myValue, mmprop.getStringValue());		
	}
	
	private abstract class ComponentTestPanel extends ConfigurablePanel{

		public UIProperty property;
		public double myDoubleValue;
		public int myIntValue;
		public boolean myBoolValue;
		
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
